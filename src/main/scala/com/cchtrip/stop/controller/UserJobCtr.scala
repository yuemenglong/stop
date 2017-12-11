package com.cchtrip.stop.controller

import com.cchtrip.stop.bean.Dao
import com.cchtrip.stop.entity.{Question, Student, StudentStudyJob, StudentStudyJobItem}
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
@RequestMapping(value = Array("/user/{uid}/study-job"), produces = Array("application/json"))
class UserJobCtr {

  @Autowired
  var dao: Dao = _

  @GetMapping(Array("/list"))
  def getUserJobList(@RequestParam(defaultValue = "20") limit: Long,
                     @RequestParam(defaultValue = "0") offset: Long,
                     status: String,
                     @PathVariable uid: Long): String = dao.beginTransaction(session => {
    val root = Orm.root(classOf[StudentStudyJob])
    root.select("job").select("course")
    var cond = Orm.cond()
    if (status != null) {
      cond = cond.and(root.get("status").eql(status))
    }
    val query = Orm.selectFrom(root).where(root.get("studentId").eql(uid)
      .and(cond)).limit(limit).offset(offset)
    val res = session.query(query)
    JSON.stringify(res)
  })

  @GetMapping(Array("/count"))
  def getUserJobCount(status: String,
                      @PathVariable uid: Long): String = dao.beginTransaction(session => {
    val root = Orm.root(classOf[StudentStudyJob])
    var cond = Orm.cond()
    if (status != null) {
      cond = cond.and(root.get("status").eql(status))
    }
    val query = Orm.select(root.count()).from(root).where(root.get("studentId").eql(uid).and(cond))
    val res = session.first(query)
    JSON.stringify(res)
  })

  @GetMapping(Array("/{id}"))
  def getUserJob(@PathVariable id: Long): String = dao.beginTransaction(session => {
    val root = Orm.root(classOf[StudentStudyJob])
    root.select("items")
    root.select("job")
    val query = Orm.selectFrom(root).where(root.get("id").eql(id))
    val res = session.first(query)
    JSON.stringify(res)
  })


  def updateStudyJobStatus(session: Session, item: StudentStudyJobItem): Unit = {
    // 判断job完成情况
    val count = {
      val root = Orm.root(classOf[StudentStudyJobItem])
      val query = Orm.select(root.count()).from(root)
        .where(root.get("studentStudyJobId")
          .eql(item.studentStudyJobId).and(root.get("status").eql("succ")))
      session.first(query)
    }
    // 都完成就更新job状态
    if (count == 0) {
      val root = Orm.root(classOf[StudentStudyJob])
      val ex = Orm.update(root).set(root.get("status").assign("succ"))
      session.execute(ex)
    }
  }

  @PostMapping(Array("/{sid}/item/{id}"))
  def postStudyJobItem(@PathVariable sid: Long,
                       @PathVariable id: Long,
                       @RequestBody body: String
                      ): String = dao.beginTransaction(session => {
    val item = OrmTool.selectById(classOf[StudentStudyJobItem], id, session)
    if (item.ty == "question") {
      val question = OrmTool.selectById(classOf[Question], item.targetId, session, (root: Root[Question]) => {
        root.select("sc")
      })
      val answer = JSON.parse(body).asObj().getStr("answer")
      // 对比题目正确性
      // 设置item完成情况
      item.status = "succ"
      item.correct = question.answer == answer
      item.score = if (item.correct.booleanValue()) {
        question.score
      } else {
        0.0
      }
      session.execute(Orm.update(item))
      updateStudyJobStatus(session, item)
      if (item.correct) {
        "true"
      } else {
        "false"
      }
    } else {
      "{}"
    }
  })
}
