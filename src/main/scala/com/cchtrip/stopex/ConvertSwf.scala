/**
  * Created by <yuemenglong@126.com> on 2017/9/29.
  */
package com.cchtrip.stopex

import java.io.{BufferedInputStream, FileInputStream}
import java.nio.file.Paths
import javax.servlet.http.{HttpServletRequest, HttpServletResponse}

import com.cchtrip.stop.util.Kit
import io.github.yuemenglong.template.HTML.<
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot._
import org.springframework.boot.autoconfigure._
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.{EnableWebSecurity, WebSecurityConfigurerAdapter}
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation._
import org.springframework.web.multipart.MultipartFile


object ConvertSwf {
  def main(args: Array[String]): Unit = {
    SpringApplication.run(classOf[ConvertSwf])
  }
}

@Configuration // 声明为配置类
@EnableWebSecurity // 启用 Spring Security web 安全的功能
class WebSecurityConfig extends WebSecurityConfigurerAdapter {
  override def configure(http: HttpSecurity) {
    http.csrf().disable()
      .authorizeRequests()
      .anyRequest().permitAll() // 允许所有请求通过
  }
}

@SpringBootApplication
@Controller("")
class ConvertSwf {

  @Value("${app.convertDir}")
  var convertDir: String = _

  @GetMapping(value = Array(""), produces = Array("text/html"))
  @ResponseBody
  def index(): String = {
    <.html.>(
      <.head.>,
      <.body.>(
        <.form(action = "/", method = "POST", attrs = Map("enctype" -> "multipart/form-data")).>(
          <.input(ty = "file", name = "file").>,
          <.input(ty = "submit", value = "submit").>,
        ),
      ),
    ).toString()
  }

  @PostMapping(Array("/"))
  def convert(file: MultipartFile,
              request: HttpServletRequest,
              response: HttpServletResponse
             ): Unit = {
    require(!file.isEmpty)
    val saveFile = Paths.get(convertDir, file.getOriginalFilename).toFile
    file.transferTo(saveFile)

    val outputFile = saveFile

    response.setContentType("application/force-download")
    response.addHeader("Content-Disposition",
      "attachment;fileName=" + saveFile.getName); // 设置文件名
    val is = new BufferedInputStream(new FileInputStream(outputFile))
    val os = response.getOutputStream
    Kit.pipe(is, os)
    is.close()
    os.flush()
  }
}
