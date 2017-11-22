package com.cchtrip.stop.controller

import com.cchtrip.stop.bean.Dao
import com.cchtrip.stop.entity.{Course, Courseware}
import io.github.yuemenglong.json.JSON
import io.github.yuemenglong.orm.Orm
import io.github.yuemenglong.orm.lang.types.Types._
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation._

/**
  * Created by <yuemenglong@126.com> on 2017/11/21.
  */
@Controller
@RequestMapping(value = Array("/course"), produces = Array("application/json"))
@ResponseBody
class CourseCtlr {

  @Autowired
  var dao: Dao = _

  @GetMapping(Array("/list"))
  def listCourse(@RequestParam(defaultValue = "20") limit: Long,
                 @RequestParam(defaultValue = "0") offset: Long
                ): String = dao.beginTransaction(session => {
    val root = Orm.root(classOf[Course])
    val query = Orm.selectFrom(root).limit(limit).offset(offset)
    val res = session.query(query)
    JSON.stringify(res)
  })

  @GetMapping(Array("/count"))
  def listCourseCount: String = dao.beginTransaction(session => {
    val root = Orm.root(classOf[Course])
    val query = Orm.select(root.count()).from(root)
    val count = session.first(query)
    count.toString
  })

  @PostMapping(Array(""))
  def newCourse(@RequestBody body: String): String = dao.beginTransaction(session => {
    val course = JSON.parse(body, classOf[Course])
    course.crTime = new Date
    session.execute(Orm.insert(course))
    JSON.stringify(course)
  })

  @GetMapping(Array("/{id}"))
  def getCourse(@PathVariable id: Long): String = dao.beginTransaction(session => {
    val root = Orm.root(classOf[Course])
    root.select("coursewares")
    root.select("questions")
    root.select("videos")
    val course = session.first(Orm.selectFrom(root).where(root.get("id").eql(id)))
    JSON.stringify(course)
  })

  @PutMapping(Array("/{id}"))
  def putCourse(@RequestBody body: String): String = dao.beginTransaction(session => {
    val course = JSON.parse(body, classOf[Course])
    session.execute(Orm.update(course))
    JSON.stringify(course)
  })

  @DeleteMapping(Array("/{id}"))
  def deleteCourse(@PathVariable id: Long): String = dao.beginTransaction(session => {
    val root = Orm.root(classOf[Course])
    val ex = Orm.delete(root,
      root.leftJoin("coursewares"),
      root.leftJoin("questions"),
      root.leftJoin("videos"),
    ).from(root).where(root.get("id").eql(id))
    session.execute(ex).toString
  })

  @PostMapping(Array("/{id}/courseware"))
  def postCourseware(@PathVariable cid: Long, @RequestBody body: String): String = dao.beginTransaction(session => {
    val courseware = JSON.parse(body, classOf[Courseware])
    courseware.courseId = cid
    session.execute(Orm.insert(courseware))
    JSON.stringify(courseware)
  })
}
