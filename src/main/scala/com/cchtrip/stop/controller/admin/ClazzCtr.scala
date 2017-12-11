package com.cchtrip.stop.controller.admin

import com.cchtrip.stop.bean.Dao
import com.cchtrip.stop.entity.{Clazz, Student}
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
@RequestMapping(value = Array("/clazz"), produces = Array("application/json"))
class ClazzCtr {

  @Autowired
  var dao: Dao = _

  @PostMapping(Array(""))
  def postClazz(@RequestBody body: String): String = dao.beginTransaction(session => {
    val clazz = JSON.parse(body, classOf[Clazz])
    clazz.crTime = new Date
    clazz.studentCount = 0
    session.execute(Orm.insert(clazz))
    JSON.stringify(clazz)
  })

  @DeleteMapping(Array("/{id}"))
  def deleteClazz(@PathVariable id: Long): String = dao.beginTransaction(session => {
    val clazz = Orm.create(classOf[Clazz])
    clazz.id = id
    session.execute(Orm.delete(clazz))
    val root = Orm.root(classOf[Student])
    session.execute(Orm.update(root).set(root.get("clazzId").assign(0)))
    "{}"
  })

  @GetMapping(Array("/{id}"))
  def getClazz(@PathVariable id: Long): String = dao.beginTransaction(session => {
    val res = OrmTool.selectById(classOf[Clazz], id, session)
    JSON.stringify(res)
  })

  @GetMapping(Array("/{id}/students"))
  def getClazzStudents(@PathVariable id: Long,
                       clazzId: Long): String = dao.beginTransaction(session => {
    val root = Orm.root(classOf[Student])
    val query = if (clazzId != null) {
      Orm.selectFrom(root).where(root.get("clazzId").eql(clazzId))
    } else {
      Orm.selectFrom(root).where(root.get("clazzId").eql(id))
    }
    val res = session.query(query)
    JSON.stringify(res)
  })

  @PutMapping(Array("/{id}"))
  def putClazz(@PathVariable id: Long, @RequestBody body: String): String = dao.beginTransaction(session => {
    val clazz = JSON.parse(body, classOf[Clazz])
    clazz.id = id
    OrmTool.clearField(clazz, "studentCount")
    session.execute(Orm.update(clazz))
    JSON.stringify(clazz)
  })

  @PostMapping(Array("/{id}/students"))
  def postClazzStudents(@PathVariable id: Long, @RequestBody body: String): String = dao.beginTransaction(session => {
    val ids = JSON.parse(body, classOf[Array[Long]])

    {
      val root = Orm.root(classOf[Student])
      val ex = Orm.update(root).set(root.get("clazzId").assign(id)).where(root.get("id").in(ids))
      session.execute(ex)
    }
    {
      val root = Orm.root(classOf[Clazz])
      session.execute(Orm.update(root).set(root.get("studentCount").assignAdd(ids.length)))
    }
    "[]"
  })

  @GetMapping(Array("/list"))
  def GetClazzsList(@RequestParam(defaultValue = "20") limit: Long,
                    @RequestParam(defaultValue = "0") offset: Long
                   ): String = dao.beginTransaction(session => {
    val root = Orm.root(classOf[Clazz])
    val query = Orm.selectFrom(root).limit(limit).offset(offset)
    val res = session.query(query)
    JSON.stringify(res)
  })

  @GetMapping(Array("/count"))
  def GetClazzsCount(): String = dao.beginTransaction(session => {
    val root = Orm.root(classOf[Clazz])
    val query = Orm.select(root.count()).from(root)
    val res = session.first(query)
    JSON.stringify(res)
  })

  @DeleteMapping(Array("/{id}/student/{sid}"))
  def deleteClazzStudent(@PathVariable id: Long,
                         @PathVariable sid: Long): String = dao.beginTransaction(session => {
    val clazz = Orm.create(classOf[Clazz])
    clazz.id = id
    session.execute(Orm.delete(clazz))
    val root = Orm.root(classOf[Clazz])
    session.execute(Orm.update(root).set(root.get("studentCount").assignSub(1)))
    "{}"
  })
}
