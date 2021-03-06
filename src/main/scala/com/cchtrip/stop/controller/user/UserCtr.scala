package com.cchtrip.stop.controller.user

import com.cchtrip.stop.bean.{Dao, IdGenerator}
import com.cchtrip.stop.entity._
import io.github.yuemenglong.json.JSON
import io.github.yuemenglong.orm.Orm
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
      root.select("avatar")
      root.select("clazz")
      root.select("team")
    })
    JSON.stringifyJs(res)
  })


  @PutMapping(Array(""))
  def putUser(@PathVariable uid: Long, @RequestBody body: String): String = dao.beginTransaction(session => {
    val student = JSON.parse(body, classOf[Student])
    student.id = uid
    val ex = Orm.update(student)
    if (student.avatar != null && student.avatar.id == null) {
      student.avatar.id = IdGenerator.generateId
      student.avatar.crTime = new Date
      ex.insert("avatar")
    } else {
      ex.update("avatar")
    }
    session.execute(ex)
    JSON.stringifyJs(student)
  })

}
