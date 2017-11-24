package com.cchtrip.stop.controller

import com.cchtrip.stop.bean.Dao
import com.cchtrip.stop.entity.{Course, Courseware}
import io.github.yuemenglong.json.JSON
import io.github.yuemenglong.orm.Orm
import io.github.yuemenglong.orm.lang.types.Types._
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation._

/**
  * Created by <yuemenglong@126.com> on 2017/11/21.
  */
@Controller
@RequestMapping(value = Array("/course/{cid}"), produces = Array("application/json"))
@ResponseBody
class CoursewareCtr {

  @Autowired
  var dao: Dao = _

  @PostMapping(Array("/courseware"))
  def postCourseware(@RequestBody body: String): String = dao.beginTransaction(session => {
    val c = JSON.parse(body, classOf[Courseware])
    session.execute(Orm.insert(c))
    JSON.stringify(c)
  })
}
