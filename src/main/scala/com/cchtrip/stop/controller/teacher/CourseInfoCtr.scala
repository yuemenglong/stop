package com.cchtrip.stop.controller.teacher

import com.cchtrip.stop.bean.Dao
import com.cchtrip.stop.entity.{Course, Courseware, Question, Video}
import io.github.yuemenglong.json.JSON
import io.github.yuemenglong.orm.Orm
import io.github.yuemenglong.orm.lang.types.Types._
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation._

/**
  * Created by <yuemenglong@126.com> on 2017/11/21.
  */
@RestController
@RequestMapping(value = Array("/teacher/course/{cid}"), produces = Array("application/json"))
class CourseInfoCtr {

  @Autowired
  var dao: Dao = _

  @PostMapping(Array("/courseware"))
  def postCourseware(@PathVariable cid: Long, @RequestBody body: String): String = dao.beginTransaction(session => {
    val c = JSON.parse(body, classOf[Courseware])
    //    c.courseId = cid
    c.crTime = new Date()
    session.execute(Orm.insert(c))
    JSON.stringify(c)
  })

  @PutMapping(Array("/courseware/{wid}"))
  def putCourseware(@PathVariable wid: Long, @RequestBody body: String): String = dao.beginTransaction(session => {
    val c = JSON.parse(body, classOf[Courseware])
    c.id = wid
    session.execute(Orm.update(c))
    JSON.stringify(c)
  })

  @DeleteMapping(Array("/courseware/{id}"))
  def deleteCourseware(@PathVariable id: Long): String = dao.beginTransaction(session => {
    val c = Orm.create(classOf[Courseware])
    c.id = id
    session.execute(Orm.delete(c))
    "{}"
  })

  @PostMapping(Array("/video"))
  def postVideo(@PathVariable cid: Long, @RequestBody body: String): String = dao.beginTransaction(session => {
    val c = JSON.parse(body, classOf[Video])
    //    c.courseId = cid
    c.crTime = new Date()
    session.execute(Orm.insert(c))
    JSON.stringify(c)
  })

  @PutMapping(Array("/video/{vid}"))
  def PutVideo(@PathVariable vid: Long, @RequestBody body: String): String = dao.beginTransaction(session => {
    val v = JSON.parse(body, classOf[Video])
    v.id = vid
    session.execute(Orm.update(v))
    JSON.stringify(v)
  })

  @DeleteMapping(Array("/video/{id}"))
  def deleteVideo(@PathVariable id: Long): String = dao.beginTransaction(session => {
    val c = Orm.create(classOf[Video])
    c.id = id
    session.execute(Orm.delete(c))
    "{}"
  })


  @PostMapping(Array("/question"))
  def postQuestion(@PathVariable cid: Long, @RequestBody body: String): String = dao.beginTransaction(session => {
    val q = JSON.parse(body, classOf[Question])
    //    q.courseId = cid
    q.crTime = new Date()
    val ex = Orm.insert(q)
    if (q.sc != null) {
      q.sc.crTime = q.crTime
      ex.insert("sc")
    }
    session.execute(ex)
    JSON.stringify(q)
  })

  @GetMapping(Array("/question/{qid}"))
  def GetQuestion(@PathVariable cid: Long,
                  @PathVariable qid: Long): String = dao.beginTransaction(session => {
    val root = Orm.root(classOf[Question])
    root.select("sc")
    val q = session.first(Orm.selectFrom(root).where(root.get("id").eql(qid)))
    JSON.stringify(q)
  })

  @PutMapping(Array("/question/{qid}"))
  def putQuestion(@PathVariable cid: Long,
                  @PathVariable qid: Long,
                  @RequestBody body: String): String = dao.beginTransaction(session => {
    val q = JSON.parse(body, classOf[Question])
    //    q.courseId = cid
    q.id = qid
    val ex = Orm.update(q)
    ex.update("sc")
    session.execute(ex)
    JSON.stringify(q)
  })

  @DeleteMapping(Array("/question/{id}"))
  def deleteQuestion(@PathVariable id: Long): String = dao.beginTransaction(session => {
    val root = Orm.root(classOf[Question])
    val ex = Orm.delete(root, root.leftJoin("sc"))
      .from(root).where(root.get("id").eql(id))
    session.execute(ex)
    "{}"
  })
}
