package com.cchtrip.stop.controller.teacher

import com.cchtrip.stop.bean.{Dao, IdGenerator}
import com.cchtrip.stop.entity._
import io.github.yuemenglong.json.JSON
import io.github.yuemenglong.orm.Orm
import io.github.yuemenglong.orm.lang.types.Types._
import io.github.yuemenglong.orm.tool.OrmTool
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation._

/**
  * Created by <yuemenglong@126.com> on 2017/11/21.
  */
@Controller
@RequestMapping(value = Array("/teacher/course"), produces = Array("application/json"))
@ResponseBody
class CourseCtr {

  @Autowired
  var dao: Dao = _

  @GetMapping(Array("/list"))
  def listCourse(@RequestParam(defaultValue = "20") limit: Long,
                 @RequestParam(defaultValue = "0") offset: Long,
                 categoryId: Long,
                ): String = dao.beginTransaction(session => {
    val root = Orm.root(classOf[Course])
    var cond = Orm.cond()
    if (categoryId != null) {
      cond = cond.and(root.get("categoryId").eql(categoryId))
    }
    val query = Orm.selectFrom(root).where(cond).limit(limit).offset(offset)
    val res = session.query(query)
    val ids = res.map(_.id)

    def countFn(clazz: Class[_]): Map[Long, Long] = {
      val root = Orm.root(clazz)
      session.query(Orm.select(root.get("courseId").as(classOf[Long]),
        root.count("courseId"))
        .from(root).where(root.get("courseId").in(ids))
        .groupBy("courseId")).toMap
    }

    val cmap = countFn(classOf[CourseCourseware])
    val vmap = countFn(classOf[CourseVideo])
    val qmap = countFn(classOf[CourseQuestion])

    JSON.stringify(res.map(c => {
      c.coursewareCount = cmap(c.id)
      c.videoCount = vmap(c.id)
      c.questionCount = qmap(c.id)
      c
    }))
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
    course.id = IdGenerator.generateId
    course.crTime = new Date
    session.execute(Orm.insert(course))
    JSON.stringify(course)
  })

  @GetMapping(Array("/{id}"))
  def getCourse(@PathVariable id: Long): String = dao.beginTransaction(session => {
    val root = Orm.root(classOf[Course])
    //    root.select("coursewares")
    //    root.select("questions").select("sc")
    //    root.select("videos")
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

  @GetMapping(Array("/{id}/courseware"))
  def getCourseware(@PathVariable id: Long): String = dao.beginTransaction(session => {
    val root = Orm.root(classOf[CourseCourseware])
    root.select("courseware")
    val res = session.query(Orm.selectFrom(root).where(root.get("courseId").eql(id)))
    JSON.stringify(res)
  })

  @GetMapping(Array("/{id}/video"))
  def getVideo(@PathVariable id: Long): String = dao.beginTransaction(session => {
    val root = Orm.root(classOf[CourseVideo])
    root.select("video")
    val res = session.query(Orm.selectFrom(root).where(root.get("courseId").eql(id)))
    JSON.stringify(res)
  })

  @GetMapping(Array("/{id}/question"))
  def getQuestion(@PathVariable id: Long): String = dao.beginTransaction(session => {
    val root = Orm.root(classOf[CourseQuestion])
    root.select("question")
    val res = session.query(Orm.selectFrom(root).where(root.get("courseId").eql(id)))
    JSON.stringify(res)
  })

  @PutMapping(Array("/{id}/courseware"))
  def putCourseware(@PathVariable id: Long, @RequestBody body: String): String = dao.beginTransaction(session => {
    val item = JSON.parse(body, classOf[CourseCourseware])
    item.id = IdGenerator.generateId
    item.crTime = new Date
    session.execute(Orm.insert(item))
    JSON.stringify(item)
  })

  @DeleteMapping(Array("/{id}/courseware/{oid}"))
  def deleteCourseware(@PathVariable oid: Long): String = dao.beginTransaction(session => {
    OrmTool.deleteById(classOf[CourseCourseware], oid, session)
    "{}"
  })

  @PutMapping(Array("/{id}/video"))
  def putVideo(@PathVariable id: Long, @RequestBody body: String): String = dao.beginTransaction(session => {
    val item = JSON.parse(body, classOf[CourseVideo])
    item.id = IdGenerator.generateId
    item.crTime = new Date
    session.execute(Orm.insert(item))
    JSON.stringify(item)
  })

  @DeleteMapping(Array("/{id}/video/{oid}"))
  def deleteVideo(@PathVariable oid: Long): String = dao.beginTransaction(session => {
    OrmTool.deleteById(classOf[Video], oid, session)
    "{}"
  })

  @PutMapping(Array("/{id}/question"))
  def putQuestion(@PathVariable id: Long, @RequestBody body: String): String = dao.beginTransaction(session => {
    val item = JSON.parse(body, classOf[CourseQuestion])
    item.id = IdGenerator.generateId
    item.crTime = new Date
    session.execute(Orm.insert(item))
    JSON.stringify(item)
  })

  @DeleteMapping(Array("/{id}/question/{oid}"))
  def deleteQuestion(@PathVariable oid: Long): String = dao.beginTransaction(session => {
    OrmTool.deleteById(classOf[CourseQuestion], oid, session)
    "{}"
  })

}
