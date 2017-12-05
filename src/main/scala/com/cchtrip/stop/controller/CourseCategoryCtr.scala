package com.cchtrip.stop.controller

import com.cchtrip.stop.bean.Dao
import com.cchtrip.stop.entity.{Course, CourseCategory}
import com.cchtrip.stop.util.NamedException
import io.github.yuemenglong.json.JSON
import io.github.yuemenglong.orm.Orm
import io.github.yuemenglong.orm.lang.types.Types._
import io.github.yuemenglong.orm.tool.OrmTool
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
  def getCategoryAll: String = dao.beginTransaction(session => {
    val root = Orm.root(classOf[CourseCategory])
    val res = session.query(Orm.selectFrom(root))
    val map: Map[Long, CourseCategory] = res.map(c => (c.id, c))(collection.breakOut)
    res.foreach(c => {
      c.children = Array()
      if (map.contains(c.parentId)) {
        map(c.parentId).children ++= Array(c)
      }
    })
    val ret = map.values.filter(_.level == 0).toArray
    JSON.stringify(ret)
  })

  @DeleteMapping(Array("/{id}"))
  def deleteCategory(@PathVariable id: Long): String = dao.beginTransaction(session => {
    //1. 没有子节点才能删
    //2. 没有课程相关联才能删
    {
      val root = Orm.root(classOf[CourseCategory])
      val count = session.first(Orm.select(root.count()).from(root).where(root.get("parentId").eql(id)))
      if (count > 0) {
        throw NamedException("DEL_CATE_FAIL", "子类型不为空")
      }
    }
    {
      val root = Orm.root(classOf[Course])
      val count = session.first(Orm.select(root.count()).from(root).where(root.get("categoryId").eql(id)))
      if (count > 0) {
        throw NamedException("DEL_CATE_FAIL", "还有课程相关联")
      }
    }
    OrmTool.deleteById(classOf[CourseCategory], id, session)
    "{}"
  })

}
