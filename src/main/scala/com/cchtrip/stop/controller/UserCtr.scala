package com.cchtrip.stop.controller

import com.cchtrip.stop.bean.Dao
import com.cchtrip.stop.entity._
import io.github.yuemenglong.json.JSON
import io.github.yuemenglong.orm.Orm
import io.github.yuemenglong.orm.lang.types.Types._
import io.github.yuemenglong.orm.tool.OrmTool
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation._

/**
  * Created by <yuemenglong@126.com> on 2017/11/21.
  */
@RestController
@RequestMapping(value = Array("/user/{uid}"), produces = Array("application/json"))
class UserCtr {

  @Autowired
  var dao: Dao = _

  @GetMapping(Array(""))
  def getUser(@PathVariable uid: Long): String = dao.beginTransaction(session => {
    val res = OrmTool.selectById(classOf[Student], uid, session)
    JSON.stringify(res)
  })


  @PutMapping(Array(""))
  def putUser(@PathVariable uid: Long, @RequestBody body: String): String = dao.beginTransaction(session => {
    val student = JSON.parse(body, classOf[Student])
    student.id = uid
    session.execute(Orm.update(student))
    JSON.stringify(student)
  })

  @GetMapping(Array("/study-job-item/{id}"))
  def getStudyJobItem(@PathVariable id: Long): String = dao.beginTransaction(session => {
    val root = Orm.root(classOf[StudentStudyJobItem])
    val query = Orm.selectFrom(root).where(root.get("id").eql(id))
    val job = session.first(query)
    // 更新为已完成
    if (Array("courseware", "video").contains(job.ty)) {
      val root = Orm.root(classOf[StudentStudyJobItem])
      session.execute(Orm.update(root).set(root.get("status").assign("succ"))
        .where(root.get("id").eql(id)))
    }
    JSON.stringify(job)
  })

  @GetMapping(Array("/question/{id}"))
  def getQuestion(@PathVariable id: Long): String = dao.beginTransaction(session => {
    val root = Orm.root(classOf[Question])
    root.select("sc")
    root.select("tf")
    val query = Orm.selectFrom(root).where(root.get("id").eql(id))
    val question = session.first(query)
    JSON.stringify(question)
  })

  @GetMapping(Array("/courseware/{id}"))
  def getCourseware(@PathVariable id: Long): String = dao.beginTransaction(session => {
    val root = Orm.root(classOf[Courseware])
    val query = Orm.selectFrom(root).where(root.get("id").eql(id))
    val item = session.first(query)
    JSON.stringify(item)
  })

  @GetMapping(Array("/video/{id}"))
  def getVideo(@PathVariable id: Long): String = dao.beginTransaction(session => {
    val root = Orm.root(classOf[Video])
    val query = Orm.selectFrom(root).where(root.get("id").eql(id))
    val item = session.first(query)
    JSON.stringify(item)
  })
}
