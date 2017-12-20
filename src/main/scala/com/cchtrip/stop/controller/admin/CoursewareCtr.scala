package com.cchtrip.stop.controller.admin

import com.cchtrip.stop.bean.Dao
import com.cchtrip.stop.entity._
import com.cchtrip.stop.util.NamedException
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
@RequestMapping(value = Array("/admin/courseware"), produces = Array("application/json"))
@ResponseBody
class CoursewareCtr {

  @Autowired
  var dao: Dao = _

  @PostMapping(Array(""))
  def post(@RequestBody body: String): String = dao.beginTransaction(session => {
    val obj = JSON.parse(body, classOf[Courseware])
    obj.crTime = new Date
    session.execute(Orm.insert(obj))
    JSON.stringify(obj)
  })

  @PutMapping(Array("/{id}"))
  def put(@PathVariable id: Long, @RequestBody body: String): String = dao.beginTransaction(session => {
    val obj = JSON.parse(body, classOf[Courseware])
    obj.id = id
    session.execute(Orm.update(obj))
    JSON.stringify(obj)
  })

  @GetMapping(Array("/list"))
  def list(@RequestParam(defaultValue = "20") limit: Long,
           @RequestParam(defaultValue = "0") offset: Long
          ): String = dao.beginTransaction(session => {
    val root = Orm.root(classOf[Courseware])
    val query = Orm.selectFrom(root).limit(limit).offset(offset)
    val res = session.query(query)
    JSON.stringify(res)
  })

  @GetMapping(Array("/count"))
  def count(): String = dao.beginTransaction(session => {
    val root = Orm.root(classOf[Courseware])
    val query = Orm.select(root.count()).from(root)
    val res = session.first(query)
    JSON.stringify(res)
  })

  @GetMapping(Array("/{id}"))
  def get(@PathVariable id: Long): String = dao.beginTransaction(session => {
    val res = OrmTool.selectById(classOf[Courseware], id, session)
    JSON.stringify(res)
  })

  @DeleteMapping(Array("/{id}"))
  def delete(@PathVariable id: Long): String = dao.beginTransaction(fn = session => {
    OrmTool.deleteById(classOf[Courseware], id, session)
    "{}"
  })

}
