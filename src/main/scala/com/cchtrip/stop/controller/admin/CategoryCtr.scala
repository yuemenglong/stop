package com.cchtrip.stop.controller.admin

import com.cchtrip.stop.bean.Dao
import com.cchtrip.stop.entity._
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
@RequestMapping(value = Array(
  "/admin/category",
  "/admin/course-category",
  "/admin/question-category",
  "/admin/courseware-category",
  "/admin/video-category",
  "/admin/target-category",
), produces = Array("application/json"))
@ResponseBody
class CategoryCtr {

  @Autowired
  var dao: Dao = _

  @PostMapping(Array(""))
  def newCategory(@RequestBody body: String): String = dao.beginTransaction(session => {
    val cate = JSON.parse(body, classOf[Category])
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
    val cate = JSON.parse(body, classOf[Category])
    cate.id = id
    session.execute(Orm.update(cate))
    JSON.stringify(cate)
  })

  @GetMapping(Array(""))
  def getCategoryAll(level: Integer,
                     @RequestParam(required = true) ty: String,
                    ): String = dao.beginTransaction(session => {
    val root = Orm.root(classOf[Category])
    if (level == null) {
      val res = session.query(Orm.selectFrom(root).where(root.get("ty").eql(ty)))
      val map: Map[Long, Category] = res.map(c => (c.id, c))(collection.breakOut)
      res.foreach(c => {
        c.children = Array()
        if (map.contains(c.parentId)) {
          map(c.parentId).children ++= Array(c)
        }
      })
      val ret = map.values.filter(_.level == 0).toArray
      JSON.stringify(ret)
    } else {
      val res = session.query(Orm.selectFrom(root)
        .where(root.get("level").eql(level).and(root.get("ty").eql(ty))))
      JSON.stringify(res)
    }
  })

  @DeleteMapping(Array("/{id}"))
  def deleteCategory(@PathVariable id: Long): String = dao.beginTransaction(fn = session => {
    //1. 没有子节点才能删
    //2. 没有课程相关联才能删
    val cate = OrmTool.selectById(classOf[Category], id, session)

    {
      val root = Orm.root(classOf[Category])
      val count = session.first(Orm.select(root.count()).from(root).where(root.get("parentId").eql(id)))
      if (count > 0) {
        throw NamedException(NamedException.DEL_CATE_FAIL, "子类型不为空")
      }
    }

    def relationCountFn = (clazz: Class[_]) => {
      val root = Orm.root(clazz)
      session.first(Orm.select(root.count()).from(root).where(root.get("cate0Id").eql(id).or(root.get("cate1Id").eql(id))))
    }

    cate.ty match {
      case "course" =>
        if (relationCountFn(classOf[Course]) > 0) {
          throw NamedException(NamedException.DEL_CATE_FAIL, "还有课程相关联")
        }
      case "courseware" =>
        if (relationCountFn(classOf[Courseware]) > 0) {
          throw NamedException(NamedException.DEL_CATE_FAIL, "还有课件相关联")
        }
      case "video" =>
        if (relationCountFn(classOf[Video]) > 0) {
          throw NamedException(NamedException.DEL_CATE_FAIL, "还有视频相关联")
        }
      case "question" =>
        if (relationCountFn(classOf[Question]) > 0) {
          throw NamedException(NamedException.DEL_CATE_FAIL, "还有题目相关联")
        }
    }
    OrmTool.deleteById(classOf[Category], id, session)
    "{}"
  })

}
