package com.cchtrip.stop.controller

import com.cchtrip.stop.bean.Dao
import com.cchtrip.stop.entity.{Course, CourseCategory}
import com.cchtrip.stop.util.NamedException
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
@RequestMapping(value = Array("/course-category"), produces = Array("application/json"))
@ResponseBody
class CourseCategoryCtr {

  @Autowired
  var dao: Dao = _

  @PostMapping(Array(""))
  def newCategory(@RequestBody body: String): String = dao.beginTransaction(session => {
    val cate = JSON.parse(body, classOf[CourseCategory])
    cate.crTime = new Date
    if (cate.level == null || cate.parentId == null) {
      cate.level = 0
      cate.parentId = 0L
    }
    session.execute(Orm.insert(cate))
    JSON.stringify(cate)
  })

  //包括更新父子关系
  @PutMapping(Array("/{id}"))
  def putCategory(@PathVariable id: Long, @RequestBody body: String): String = dao.beginTransaction(session => {
    val cate = JSON.parse(body, classOf[CourseCategory])
    cate.id = id
    session.execute(Orm.update(cate))
    JSON.stringify(cate)
  })

  @GetMapping(Array(""))
  def getCategory: String = {
    throw new NamedException("Name", "Test")
  }
}
