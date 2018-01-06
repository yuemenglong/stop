package com.cchtrip.stop.controller.teacher

import com.cchtrip.stop.bean.{Dao, IdGenerator}
import com.cchtrip.stop.entity._
import io.github.yuemenglong.json.JSON
import io.github.yuemenglong.orm.Orm
import io.github.yuemenglong.orm.lang.interfaces.Entity
import io.github.yuemenglong.orm.lang.types.Types._
import io.github.yuemenglong.orm.operate.traits.core.Root
import io.github.yuemenglong.orm.tool.OrmTool
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation._

/**
  * Created by <yuemenglong@126.com> on 2017/11/21.
  */
@RestController
@RequestMapping(value = Array("/teacher/quiz"), produces = Array("application/json"))
class QuizCtr {

  @Autowired
  var dao: Dao = _

  @PostMapping(Array(""))
  def postQuiz(@RequestBody body: String): String = dao.beginTransaction(session => {
    val quiz = JSON.parse(body, classOf[Quiz])
    quiz.id = IdGenerator.generateId
    quiz.crTime = new Date
    quiz.questions.foreach(q => {
      q.id = IdGenerator.generateId
      q.crTime = new Date
    })
    val ex = Orm.insert(quiz)
    ex.insert("questions")
    session.execute(ex)
    val jobs = {
      val root = Orm.root(classOf[Student])
      val jobs = session.query(Orm.select(root.get("id").as(classOf[Long]))
        .from(root).where(root.get("clazzId").eql(quiz.clazzId)))
        .map(id => {
          val job = new QuizJob
          job.id = IdGenerator.generateId
          job.crTime = new Date
          job.studentId = id
          job.quizId = quiz.id
          job.status = "waiting"
          Orm.convert(job)
        })
      session.execute(Orm.insert(jobs))
      jobs
    }
    val items = jobs.flatMap(job => {
      quiz.questions.map(q => {
        val item = new QuizJobItem
        item.id = IdGenerator.generateId
        item.crTime = new Date
        item.questionId = q.questionId
        item.jobId = job.id
        item.status = "waiting"
        Orm.convert(item)
      })
    })
    session.execute(Orm.insert(items))
    JSON.stringifyJs(quiz)
  })

  @DeleteMapping(Array("/{id}"))
  def deleteQuiz(@PathVariable id: Long): String = dao.beginTransaction(session => {
    {
      val root = Orm.root(classOf[Quiz])
      session.execute(Orm.delete(root, root.leftJoin("questions")).from(root).where(root.get("id").eql(id)))
    }
    {
      val root = Orm.root(classOf[QuizJob])
      session.execute(Orm.delete(root, root.leftJoin("items")).from(root).where(root.get("quizId").eql(id)))
    }
    "{}"
  })

  @GetMapping(Array("/list"))
  def getQuizList(@RequestParam(defaultValue = "20") limit: Long,
                  @RequestParam(defaultValue = "0") offset: Long
                 ): String = dao.beginTransaction(session => {
    val root = Orm.root(classOf[Quiz])
    root.select("clazz")
    val query = Orm.selectFrom(root).limit(limit).offset(offset)
    val res = session.query(query)
    JSON.stringifyJs(res)
  })

  @GetMapping(Array("/count"))
  def getQuizCount: String = dao.beginTransaction(session => {
    val root = Orm.root(classOf[Quiz])
    val query = Orm.select(root.count()).from(root)
    val res = session.first(query)
    JSON.stringifyJs(res)
  })

  @GetMapping(Array("/{id}"))
  def getQuiz(@PathVariable id: Long): String = dao.beginTransaction(session => {
    val root = Orm.root(classOf[Quiz])
    root.select("questions")
    val query = Orm.selectFrom(root).where(root.get("id").eql(id))
    val res = session.first(query)
    dao.resTransaction(session => {
      OrmTool.attach(res.questions, "question", session)
    })
    JSON.stringifyJs(res)
  })

  @GetMapping(Array("/{id}/questions"))
  def getQuizQuestion(@PathVariable id: Long): String = dao.beginTransaction(session => {
    val root = Orm.root(classOf[QuizQuestion])
    //    root.select("question")
    val query = Orm.selectFrom(root).where(root.get("quizId").eql(id))
    val res = session.query(query)
    dao.resTransaction(session => {
      OrmTool.attach(res, "question", session)
    })
    JSON.stringifyJs(res)
  })

  @GetMapping(Array("/{id}/jobs"))
  def getQuizJobs(@PathVariable id: Long): String = dao.beginTransaction(session => {
    val root = Orm.root(classOf[QuizJob])
    root.select("student")
    val query = Orm.selectFrom(root).where(root.get("quizId").eql(id))
    val res = session.query(query)
    JSON.stringifyJs(res)
  })

  @GetMapping(Array("/{id}/jobs/{jid}"))
  def getQuizJobDetail(@PathVariable jid: Long): String = dao.beginTransaction(session => {
    val root = Orm.root(classOf[QuizJob])
    root.select("items")
    val query = Orm.selectFrom(root).where(root.get("id").eql(jid))
    val res = session.query(query)
    JSON.stringifyJs(res)
  })
}
