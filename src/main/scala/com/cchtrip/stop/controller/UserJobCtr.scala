package com.cchtrip.stop.controller

import com.cchtrip.stop.bean.Dao
import com.cchtrip.stop.entity.{Question, Student, StudentStudyJob, StudentStudyJobItem}
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

}
