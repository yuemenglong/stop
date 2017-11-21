package com.cchtrip.stop.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{GetMapping, RequestMapping, ResponseBody}

/**
  * Created by <yuemenglong@126.com> on 2017/11/21.
  */
@Controller
@RequestMapping(value = Array("/student"), produces = Array("application/json"))
@ResponseBody
class StudentCtlr {

  @GetMapping(Array(""))
  def index: String = {
    "Student Index"
  }
}
