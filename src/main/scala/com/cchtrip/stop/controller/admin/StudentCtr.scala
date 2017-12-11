package com.cchtrip.stop.controller.admin

import com.cchtrip.stop.bean.Dao
import com.cchtrip.stop.entity.Student
import io.github.yuemenglong.json.JSON
import io.github.yuemenglong.orm.Orm
import org.springframework.web.bind.annotation._
import io.github.yuemenglong.orm.lang.types.Types._
import io.github.yuemenglong.orm.tool.OrmTool
import org.springframework.beans.factory.annotation.Autowired

/**
  * Created by <yuemenglong@126.com> on 2017/11/21.
  */
@RestController
@RequestMapping(value = Array("/student"), produces = Array("application/json"))
class StudentCtr {

  @Autowired
  var dao: Dao = _

  @PostMapping(Array(""))
  def postStudent(@RequestBody body: String): String = dao.beginTransaction(session => {
    val student = JSON.parse(body, classOf[Student])
    student.crTime = new Date
    student.clazzId = 0L
    session.execute(Orm.insert(student))
    JSON.stringify(student)
  })

  @DeleteMapping(Array("/{id}"))
  def deleteStudent(@PathVariable id: Long): String = dao.beginTransaction(session => {
    val student = Orm.create(classOf[Student])
    student.id = id
    session.execute(Orm.delete(student))
    "{}"
  })

  @GetMapping(Array("/{id}"))
  def getStudent(@PathVariable id: Long): String = dao.beginTransaction(session => {
    val res = OrmTool.selectById(classOf[Student], id, session)
    JSON.stringify(res)
  })

  @PutMapping(Array("/{id}"))
  def putStudent(@PathVariable id: Long, @RequestBody body: String): String = dao.beginTransaction(session => {
    val student = JSON.parse(body, classOf[Student])
    student.id = id
    session.execute(Orm.update(student))
    JSON.stringify(student)
  })

  @GetMapping(Array("/list"))
  def GetStudentsList(@RequestParam(defaultValue = "20") limit: Long,
                      @RequestParam(defaultValue = "0") offset: Long
                     ): String = dao.beginTransaction(session => {
    val root = Orm.root(classOf[Student]).ignore("password")
    val query = Orm.selectFrom(root).limit(limit).offset(offset)
    val res = session.query(query)
    JSON.stringify(res)
  })

  @GetMapping(Array("/count"))
  def GetStudentsCount(): String = dao.beginTransaction(session => {
    val root = Orm.root(classOf[Student])
    val query = Orm.select(root.count()).from(root)
    val res = session.first(query)
    JSON.stringify(res)
  })
}
