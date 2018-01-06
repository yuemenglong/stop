package com.cchtrip.stop.controller.user

import com.cchtrip.stop.bean.Dao
import com.cchtrip.stop.entity._
import com.cchtrip.stop.entity.res.Question
import com.cchtrip.stop.util.NamedException
import io.github.yuemenglong.json.JSON
import io.github.yuemenglong.orm.Orm
import io.github.yuemenglong.orm.Session.Session
import io.github.yuemenglong.orm.lang.types.Types._
import io.github.yuemenglong.orm.operate.traits.core.Root
import io.github.yuemenglong.orm.tool.OrmTool
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation._

/**
  * Created by <yuemenglong@126.com> on 2017/11/21.
  */
@RestController
@RequestMapping(value = Array("/user/{uid}/quiz-job"), produces = Array("application/json"))
class UserQuizJobCtr {

  @Autowired
  var dao: Dao = _

  @GetMapping(Array("/list"))
  def getList(@RequestParam(defaultValue = "20") limit: Long,
              @RequestParam(defaultValue = "0") offset: Long,
              status: String,
              @PathVariable uid: Long): String = dao.beginTransaction(session => {
    val root = Orm.root(classOf[QuizJob])
    root.select("quiz")
    var cond = Orm.cond()
    if (status != null) {
      cond = cond.and(root.get("status").eql(status))
    }
    val query = Orm.selectFrom(root).where(root.get("studentId").eql(uid)
      .and(cond)).limit(limit).offset(offset)
    val res = session.query(query)
    val ids = res.map(_.id)
    val itemCountMap = {
      val root = Orm.root(classOf[QuizJobItem])
      session.query(Orm.select(root.get("jobId").as(classOf[Long]),
        root.count("id")).from(root)
        .where(root.get("jobId").in(ids))).toMap
    }
    val scoreMap = {
      val root = Orm.root(classOf[QuizJobItem])
      session.query(Orm.select(root.get("jobId").as(classOf[Long]),
        root.sum(root.leftJoin("question").get("score"))).from(root)
        .where(root.get("jobId").in(ids))).toMap
    }
    val finishCountMap = {
      val root = Orm.root(classOf[QuizJobItem])
      session.query(Orm.select(root.get("jobId").as(classOf[Long]),
        root.count("id")).from(root)
        .where(root.get("jobId").in(ids).and(root.get("status").eql("succ")))
        .groupBy("jobId")).toMap
    }
    res.foreach(o => {
      o.itemCount = itemCountMap(o.id)
      o.finishCount = finishCountMap(o.id)
      o.totalScore = scoreMap(o.id)
    })
    JSON.stringify(res)
  })

  @GetMapping(Array("/count"))
  def getCount(status: String,
               @PathVariable uid: Long): String = dao.beginTransaction(session => {
    val root = Orm.root(classOf[QuizJob])
    var cond = Orm.cond()
    if (status != null) {
      cond = cond.and(root.get("status").eql(status))
    }
    val query = Orm.select(root.count()).from(root).where(root.get("studentId").eql(uid).and(cond))
    val res = session.first(query)
    JSON.stringify(res)
  })

  @GetMapping(Array("/{id}"))
  def getJobDetail(@PathVariable id: Long): String = dao.beginTransaction(session => {
    val root = Orm.root(classOf[QuizJob])
    root.select("items").select("question").select("sc")
    root.select("quiz")
    val query = Orm.selectFrom(root).where(root.get("id").eql(id))
    val res = session.first(query)
    JSON.stringify(res)
  })

  def updateStatus(session: Session, item: QuizJobItem): Unit = {
    // 判断job完成情况
    val count = {
      val root = Orm.root(classOf[QuizJobItem])
      val query = Orm.select(root.count()).from(root)
        .where(root.get("jobId").eql(item.jobId)
          .and(root.get("status").eql("waiting")))
      session.first(query)
    }
    // 都完成就更新job状态
    if (count == 0) {
      val root = Orm.root(classOf[QuizJob])
      val ex = Orm.update(root).set(root.get("status").assign("succ")).where(root.get("id").eql(item.jobId))
      session.execute(ex)
    }
  }

  @PutMapping(Array("/{jid}/items/{id}"))
  def putAnswer(@PathVariable jid: Long,
                @PathVariable id: Long,
                @RequestBody body: String
               ): String = dao.beginTransaction(session => {
    val question = {
      val root = Orm.root(classOf[QuizJobItem])
      val query = Orm.select(root.join("question").as(classOf[Question]))
        .from(root).where(root.get("id").eql(id))
      session.first(query)
    }
    val answer = JSON.parse(body).asObj().getStr("answer")
    // 对比题目正确性
    // 设置item完成情况
    val item = Orm.empty(classOf[QuizJobItem])
    item.id = id
    item.jobId = jid
    item.answer = answer
    item.status = "succ"
    item.correct = question.answer == answer
    item.score = if (item.correct.booleanValue()) {
      question.score
    } else {
      0.0
    }
    session.execute(Orm.update(item))
    //    updateStatus(session, item)
    item.correct = null
    JSON.stringify(item)
  })

  @PutMapping(Array("/{jid}"))
  def submitQuiz(@PathVariable jid: Long,
                 @RequestBody body: String
                ): String = dao.beginTransaction(session => {
    val job = OrmTool.selectById(classOf[QuizJob], jid, session)
    job.status match {
      case "succ" => throw new NamedException(NamedException.INVALID_PARAM, "已经提交过试卷")
      case _ =>
        val items = {
          val root = Orm.root(classOf[QuizJobItem])
          session.query(Orm.selectFrom(root).where(root.get("jobId").eql(jid)))
        }

        val score = items.filter(_.score != null).map(_.score.doubleValue()).sum
        val jobRet = Orm.empty(classOf[QuizJob])
        jobRet.id = jid
        jobRet.status = "succ"
        jobRet.score = score
        session.execute(Orm.update(jobRet))
        jobRet.items = items
        val ret = JSON.stringify(jobRet)

        // 测试整个quiz是否完成
        val _ = {
          val root = Orm.root(classOf[QuizJob])
          val notSucc = session.first(Orm.select(root.count).from(root).where(root.get("quizId").eql(job.quizId)
            .and(root.get("status").eql("waiting"))))
          if (notSucc == 0) {
            val root = Orm.root(classOf[Quiz])
            session.execute(Orm.update(root).set(root.get("status").assign("succ")).where(root.get("id").eql(job.quizId)))
          }
        }

        ret
    }
  }
  )

}
