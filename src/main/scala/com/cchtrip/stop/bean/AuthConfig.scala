package com.cchtrip.stop.bean

/**
  * Created by <yuemenglong@126.com> on 2017/12/14.
  */

import java.util

import com.cchtrip.stop.entity.Student
import io.github.yuemenglong.orm.Orm
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration._
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.{User, UserDetails, UserDetailsService, UsernameNotFoundException}

@EnableWebSecurity
class AuthConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  var dao: Dao = _

  @Autowired
  def configureGlobal(auth: AuthenticationManagerBuilder): Unit = {
    //    auth.inMemoryAuthentication
    //      .withUser("admin").password("admin").roles("ADMIN")
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
      .antMatchers("/user/**").authenticated()
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
        new User(user.username, user.password, grantedAuthorities)
      case false => throw new UsernameNotFoundException("用户名不存在")
    }
  }
}
