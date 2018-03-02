package com.cchtrip.stop.controller.admin

import javax.servlet.http.HttpServletRequest

import com.cchtrip.stop.bean.{AuthBean, Dao, LoginUser}
import com.cchtrip.stop.util.NamedException
import io.github.yuemenglong.json.JSON
import io.github.yuemenglong.orm.lang.types.Types._
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation._

/**
  * Created by <yuemenglong@126.com> on 2017/11/21.
  */

@RestController
@RequestMapping(value = Array("/admin"), produces = Array("application/json"))
class AdminAuthCtr {

  @Autowired var authBean: AuthBean = _
  @Autowired var dao: Dao = _

  @PostMapping(Array("/login"))
  def login(@RequestBody body: String, request: HttpServletRequest): String = dao.beginTransaction(session => {
    val user = JSON.parse(body, classOf[LoginUser])
    authBean.auth(user).isAuthenticated match {
      case true => "{}"
      case false => throw NamedException(NamedException.AUTH_FAIL, "登录失败")
    }
  })

  @GetMapping(Array("logout"))
  def logout(request: HttpServletRequest): String = {
    request.getSession().invalidate()
    "{}"
  }
}
