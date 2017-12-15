package com.cchtrip.stop.controller.teacher

import javax.servlet.http.HttpServletRequest

import com.cchtrip.stop.bean.Dao
import com.cchtrip.stop.util.NamedException
import io.github.yuemenglong.json.JSON
import io.github.yuemenglong.orm.lang.types.Types._
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.{AuthenticationManager, UsernamePasswordAuthenticationToken}
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetails
import org.springframework.web.bind.annotation._

/**
  * Created by <yuemenglong@126.com> on 2017/11/21.
  */
private class LoginUser {
  var username: String = _
  var password: String = _
}

@RestController
@RequestMapping(value = Array("/teacher"), produces = Array("application/json"))
class TeacherAuthCtr {

  @Autowired var authMgr: AuthenticationManager = _
  @Autowired var dao: Dao = _

  @PostMapping(Array("/login"))
  def login(@RequestBody body: String, request: HttpServletRequest): String = dao.beginTransaction(session => {
    try {
      val user = JSON.parse(body, classOf[LoginUser])
      val token = new UsernamePasswordAuthenticationToken(user.username, user.password)
      token.setDetails(new WebAuthenticationDetails(request))

      val auth = authMgr.authenticate(token)
      val context = SecurityContextHolder.getContext
      context.setAuthentication(auth)

      auth.isAuthenticated match {
        case true => "{}"
        case false => throw NamedException(NamedException.LOGIN_FAIL, "登录失败")
      }
    } catch {
      case _: Throwable => throw NamedException(NamedException.LOGIN_FAIL, "登录失败")
    }
  })

  @GetMapping(Array("logout"))
  def logout(request: HttpServletRequest): String = {
    request.getSession().invalidate()
    "{}"
  }
}
