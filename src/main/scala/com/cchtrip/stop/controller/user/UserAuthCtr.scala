package com.cchtrip.stop.controller.user

import java.util
import javax.servlet.http.HttpServletRequest

import com.cchtrip.stop.bean.Dao
import com.cchtrip.stop.entity.Student
import com.cchtrip.stop.util.{Kit, NamedException}
import io.github.yuemenglong.json.JSON
import io.github.yuemenglong.orm.Orm
import io.github.yuemenglong.orm.lang.types.Types._
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.{AuthenticationManager, UsernamePasswordAuthenticationToken}
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
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
@RequestMapping(value = Array("/user"), produces = Array("application/json"))
class UserAuthCtr {

  @Autowired var authMgr: AuthenticationManager = _
  @Autowired var dao: Dao = _

  @PostMapping(Array("/login"))
  def login(@RequestBody body: String, request: HttpServletRequest): String = dao.beginTransaction(session => {
    try {
      val user = JSON.parse(body, classOf[LoginUser])

      val grantedAuthorities = new util.ArrayList[GrantedAuthority]()
      grantedAuthorities.add(new SimpleGrantedAuthority("USER"))
      val token = new UsernamePasswordAuthenticationToken(user.username, user.password, grantedAuthorities)
      token.setDetails(new WebAuthenticationDetails(request))

      val auth = authMgr.authenticate(token)
      val context = SecurityContextHolder.getContext
      context.setAuthentication(auth)

      val root = Orm.root(classOf[Student])
      val student = session.first(Orm.selectFrom(root).where(root.join("user").get("username").eql(user.username)
        .and(root.join("user").get("password").eql(user.password))))
      if (student == null) {
        throw NamedException(NamedException.AUTH_FAIL, "登录失败,用户名或密码不正确")
      }
      //      student.password = null
      auth.isAuthenticated match {
        case true => JSON.stringify(student)
        case false => throw NamedException(NamedException.AUTH_FAIL, "登录失败")
      }
    } catch {
      case ne: NamedException => throw ne
      case e: Throwable =>
        Kit.logError(e)
        throw NamedException(NamedException.AUTH_FAIL, "登录失败")
    }
  })

  @GetMapping(Array("logout"))
  def logout(request: HttpServletRequest): String = {
    request.getSession().invalidate()
    "{}"
  }
}
