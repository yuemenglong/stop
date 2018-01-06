package com.cchtrip.stop.controller.admin

import com.cchtrip.stop.bean.{Dao, IdGenerator}
import com.cchtrip.stop.entity._
import com.cchtrip.stop.entity.res.Question
import io.github.yuemenglong.json.JSON
import io.github.yuemenglong.orm.Orm
import io.github.yuemenglong.orm.lang.types.Types._
import io.github.yuemenglong.orm.operate.traits.core.Root
import io.github.yuemenglong.orm.tool.OrmTool
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation._

/**
  * Created by <yuemenglong@126.com> on 2017/11/21.
  */
@Controller
@RequestMapping(value = Array("/admin/question"), produces = Array("application/json"))
@ResponseBody
class QuestionCtr {

  @Autowired
  var dao: Dao = _

  @PostMapping(Array(""))
  def post(@RequestBody body: String): String = dao.beginTransaction(session => {
    val obj = JSON.parse(body, classOf[Question])
    obj.id = IdGenerator.generateId
    obj.crTime = new Date
    if (obj.sc != null) {
      obj.sc.id = IdGenerator.generateId
      obj.sc.crTime = new Date
    }
    val ex = Orm.insert(obj)
    ex.insert("sc")
    session.execute(ex)
    JSON.stringify(obj)
  })

  @PutMapping(Array("/{id}"))
  def put(@PathVariable id: Long, @RequestBody body: String): String = dao.beginTransaction(session => {
    val obj = JSON.parse(body, classOf[Question])
    obj.id = id
    val ex = Orm.update(obj)
    ex.update("sc")
    session.execute(ex)
    JSON.stringify(obj)
  })

  @GetMapping(Array("/list"))
  def list(@RequestParam(defaultValue = "20") limit: Long,
           @RequestParam(defaultValue = "0") offset: Long,
           cate0Id: Long,
           cate1Id: Long,
          ): String = dao.beginTransaction(session => {
    val root = Orm.root(classOf[Question])
    root.select("sc")
    root.select("cate0")
    root.select("cate1")
    var cond = Orm.cond()
    if (cate0Id != null) {
      cond = cond.and(root.get("cate0Id").eql(cate0Id))
    }
    if (cate1Id != null) {
      cond = cond.and(root.get("cate1Id").eql(cate1Id))
    }
    val query = Orm.selectFrom(root).where(cond).limit(limit).offset(offset)
    val res = session.query(query)
    JSON.stringify(res)
  })

  @GetMapping(Array("/count"))
  def count(): String = dao.beginTransaction(session => {
    val root = Orm.root(classOf[Question])
    val query = Orm.select(root.count()).from(root)
    val res = session.first(query)
    JSON.stringify(res)
  })

  @GetMapping(Array("/cate-count"))
  def countByCate(): String = dao.beginTransaction(session => {
    // 默认二级
    val root = Orm.root(classOf[Question])
    val query = Orm.select(root.get("cate1Id").as(classOf[Long]), root.count("id"))
      .from(root).where(root.get("cate1Id").notNull()).groupBy("cate1Id")
    val res = session.query(query).map(p => (p._1.toString, p._2)).toMap
    JSON.stringify(res)
  })

  @GetMapping(Array("/{id}"))
  def get(@PathVariable id: Long): String = dao.beginTransaction(session => {
    val res = OrmTool.selectById(classOf[Question], id, session, (root: Root[Question]) => {
      root.select("sc")
    })
    JSON.stringify(res)
  })

  @DeleteMapping(Array("/{id}"))
  def delete(@PathVariable id: Long): String = dao.beginTransaction(fn = session => {
    OrmTool.deleteById(classOf[Question], id, session, (root: Root[Question]) => {
      Array(root.leftJoin("sc"))
    })
    "{}"
  })

}
