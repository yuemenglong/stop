package com.cchtrip.stop.bean

/**
  * Created by <yuemenglong@126.com> on 2017/12/14.
  */

import java.util

import com.cchtrip.stop.entity.Student
import com.cchtrip.stop.util.NamedException
import io.github.yuemenglong.orm.Orm
import io.github.yuemenglong.orm.lang.types.Types.String
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.{AuthenticationManager, UsernamePasswordAuthenticationToken}
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration._
import org.springframework.security.core.{Authentication, GrantedAuthority}
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.{User, UserDetails, UserDetailsService, UsernameNotFoundException}
import org.springframework.security.web.authentication.WebAuthenticationDetails
import org.springframework.stereotype.Component

class LoginUser {
  var username: String = _
  var password: String = _
}

@EnableWebSecurity
class AuthConfig extends WebSecurityConfigurerAdapter {
  @Autowired
  var dao: Dao = _

  @Autowired
  def configureGlobal(auth: AuthenticationManagerBuilder): Unit = {
    auth.inMemoryAuthentication
      .withUser("admin").password("admin").roles("ADMIN")
    auth.inMemoryAuthentication
      .withUser("teacher").password("teacher").roles("TEACHER")
    auth.userDetailsService(new AuthService())
    dao.beginTransaction(session => {
      val root = Orm.root(classOf[com.cchtrip.stop.entity.User])
      session.query(Orm.selectFrom(root)).foreach(AuthService.regist)
    })
  }

  override protected def configure(http: HttpSecurity): Unit = {
    http.csrf().disable()
      .authorizeRequests()
      .antMatchers("/user/login").permitAll()
      .antMatchers("/user/logout").permitAll()
      .antMatchers("/teacher/login").permitAll()
      .antMatchers("/teacher/logout").permitAll()
      .antMatchers("/admin/login").permitAll()
      .antMatchers("/admin/logout").permitAll()
      .antMatchers(HttpMethod.GET, "/admin/category").authenticated()
      .antMatchers(HttpMethod.GET, "/admin/courseware/**").authenticated()
      .antMatchers(HttpMethod.GET, "/admin/video/**").authenticated()
      .antMatchers(HttpMethod.GET, "/admin/question/**").authenticated()
      .antMatchers("/admin/**").hasRole("ADMIN")
      .antMatchers("/teacher/**").hasRole("TEACHER")
      .antMatchers("/user/**").hasRole("USER")
      .antMatchers("/**").permitAll()
      .anyRequest().authenticated()
      .and().asInstanceOf[HttpSecurity]
  }
}

object AuthService {
  private var cache: Map[String, com.cchtrip.stop.entity.User] = Map()

  def regist(user: com.cchtrip.stop.entity.User): Unit = {
    cache += (user.username -> user)
  }

  def drop(username: String): Unit = {
    cache -= username
  }
}

class AuthService extends UserDetailsService {
  @throws[UsernameNotFoundException]
  override def loadUserByUsername(username: String): UserDetails = {
    val cache = AuthService.cache
    cache.contains(username) match {
      case true =>
        val user = cache(username)
        val grantedAuthorities = new util.ArrayList[GrantedAuthority]()
        grantedAuthorities.add(new SimpleGrantedAuthority(s"ROLE_${user.role}"))
        new User(user.username, user.password, grantedAuthorities)
      case false => throw new UsernameNotFoundException("用户名不存在")
    }
  }
}

@Component
class AuthBean {
  @Autowired protected var authMgr: AuthenticationManager = _

  def auth(user: LoginUser, role: String = null): Authentication = {
    try {
      //      val grantedAuthorities = new util.ArrayList[GrantedAuthority]()
      //      if (role != null) {
      //        grantedAuthorities.add(new SimpleGrantedAuthority(role))
      //      }
      //    grantedAuthorities.add(new SimpleGrantedAuthority("ADMIN"))
      //      val token = new UsernamePasswordAuthenticationToken(user.username, user.password, grantedAuthorities)
      //    token.setDetails(new WebAuthenticationDetails(request))
      val token = new UsernamePasswordAuthenticationToken(user.username, user.password)

      val auth = authMgr.authenticate(token)
      var find = false
      role match {
        case null => find = true
        case _ => auth.getAuthorities.forEach(a => {
          if (a.getAuthority == s"ROLE_$role" || a.getAuthority == role) {
            find = true
          }
        })
      }
      if (!find) {
        throw new NamedException(NamedException.AUTH_FAIL, "角色认证失败")
      }

      val context = SecurityContextHolder.getContext
      context.setAuthentication(auth)
      auth
    } catch {
      case ne: NamedException => throw ne
      case _: Throwable => throw new NamedException(NamedException.AUTH_FAIL, "认证失败")
    }
  }
}

