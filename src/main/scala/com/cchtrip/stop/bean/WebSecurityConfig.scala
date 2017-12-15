package com.cchtrip.stop.bean

/**
  * Created by <yuemenglong@126.com> on 2017/12/14.
  */

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration._

@EnableWebSecurity
class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  def configureGlobal(auth: AuthenticationManagerBuilder): Unit = {
    auth.inMemoryAuthentication
      .withUser("admin").password("admin").roles("ADMIN")
  }

  override protected def configure(http: HttpSecurity): Unit = {
    http.csrf().disable()
      .authorizeRequests()
      .antMatchers("/user/login").permitAll()
      .antMatchers("/teacher/login").permitAll()
      .antMatchers("/**").permitAll()
      .anyRequest().authenticated()
      .and().asInstanceOf[HttpSecurity]
  }
}
