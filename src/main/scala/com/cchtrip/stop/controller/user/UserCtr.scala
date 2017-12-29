package com.cchtrip.stop.controller.user

import com.cchtrip.stop.bean.Dao
import com.cchtrip.stop.entity._
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
@RequestMapping(value = Array("/user/{uid}"), produces = Array("application/json"))
class UserCtr {

  @Autowired
  var dao: Dao = _

  @GetMapping(Array(""))
  def getUser(@PathVariable uid: Long): String = dao.beginTransaction(session => {
    val res = OrmTool.selectById(classOf[Student], uid, session, (root: Root[Student]) => {
      root.select("clazz")
      root.select("team")
    })
    JSON.stringify(res)
  })


  @PutMapping(Array(""))
  def putUser(@PathVariable uid: Long, @RequestBody body: String): String = dao.beginTransaction(session => {
    val student = JSON.parse(body, classOf[Student])
    student.id = uid
    session.execute(Orm.update(student))
    JSON.stringify(student)
  })

}
