package com.cchtrip.stop.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{GetMapping, RequestMapping, ResponseBody}

/**
  * Created by <yuemenglong@126.com> on 2017/11/21.
  */
@Controller
@RequestMapping(value = Array("/course"), produces = Array("application/json"))
@ResponseBody
class CourseCtlr {

  @GetMapping(Array(""))
  def index: String = {
    "Course Index"
  }


}
