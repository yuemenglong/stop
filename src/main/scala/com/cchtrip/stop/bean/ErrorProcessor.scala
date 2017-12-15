package com.cchtrip.stop.bean

/**
  * Created by <yuemenglong@126.com> on 2017/12/5.
  */

import javax.servlet.http.HttpServletRequest

import com.cchtrip.stop.util.{Kit, NamedException}
import io.github.yuemenglong.json.JSON
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.web.DefaultErrorAttributes
import org.springframework.context.annotation.{Bean, Configuration}
import org.springframework.web.context.request.RequestAttributes
import org.springframework.boot.autoconfigure.web.DefaultErrorAttributes
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.{ControllerAdvice, ExceptionHandler, ResponseBody, ResponseStatus}
import org.springframework.web.context.request.RequestAttributes

@ControllerAdvice
class ErrorProcessor {
  private val logger = LoggerFactory.getLogger(classOf[ErrorProcessor])

  @ExceptionHandler(value = Array(classOf[Exception]))
  @ResponseBody
  @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
  def handle(request: HttpServletRequest, e: Exception): String = {
    Kit.logError(e)
    val name = e match {
      case ne: NamedException => ne.name
      case _ => e.getClass.getName
    }
    val map = Map("name" -> name, "message" -> e.getMessage)
    val ret = JSON.stringify(map)
    ret
  }
}
