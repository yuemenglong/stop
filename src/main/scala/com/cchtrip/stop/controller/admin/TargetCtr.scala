package com.cchtrip.stop.controller.admin

import java.io.File
import java.nio.file.Paths

import com.cchtrip.stop.bean.Dao
import com.cchtrip.stop.entity._
import com.cchtrip.stop.util.NamedException
import io.github.yuemenglong.json.JSON
import io.github.yuemenglong.orm.Orm
import io.github.yuemenglong.orm.lang.types.Types._
import io.github.yuemenglong.orm.operate.traits.core.Root
import io.github.yuemenglong.orm.tool.OrmTool
import org.springframework.beans.factory.annotation.{Autowired, Value}
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation._

/**
  * Created by <yuemenglong@126.com> on 2017/11/21.
  */
@Controller
@RequestMapping(value = Array("/admin/target"), produces = Array("application/json"))
@ResponseBody
class TargetCtr {

  @Autowired
  var dao: Dao = _
  @Value("${app.targetDir}")
  var targetDir: String = _

  @PostMapping(Array(""))
  def post(@RequestBody body: String): String = dao.beginTransaction(session => {
    val obj = JSON.parse(body, classOf[Target])
    obj.crTime = new Date
    require(obj.file != null)
    obj.file.crTime = new Date
    obj.file.tag = "target"
    require(obj.baseDir != null)
    if (!Paths.get(targetDir, obj.baseDir, "index.html").toFile.exists()) {
      throw new NamedException(NamedException.INVALID_PARAM, "路径下没有index文件")
    }

    val ex = Orm.insert(obj)
    ex.insert("file")
    session.execute(ex)
    JSON.stringify(obj)
  })

  @PutMapping(Array("/{id}"))
  def put(@PathVariable id: Long, @RequestBody body: String): String = dao.beginTransaction(session => {
    val obj = JSON.parse(body, classOf[Target])
    obj.id = id
    require(obj.baseDir != null)
    if (!Paths.get(targetDir, obj.baseDir, "index.html").toFile.exists()) {
      throw new NamedException(NamedException.INVALID_PARAM, "路径下没有index文件")
    }
    val ex = Orm.update(obj)
    ex.update("file")
    session.execute(ex)
    JSON.stringify(obj)
  })

  @GetMapping(Array("/list"))
  def list(@RequestParam(defaultValue = "20") limit: Long,
           @RequestParam(defaultValue = "0") offset: Long,
           cate0Id: Long,
           cate1Id: Long,
          ): String = dao.beginTransaction(session => {
    val root = Orm.root(classOf[Target])
    root.select("file")
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
  def count(cate0Id: Long,
            cate1Id: Long,
           ): String = dao.beginTransaction(session => {
    val root = Orm.root(classOf[Target])
    var cond = Orm.cond()
    if (cate0Id != null) {
      cond = cond.and(root.get("cate0Id").eql(cate0Id))
    }
    if (cate1Id != null) {
      cond = cond.and(root.get("cate1Id").eql(cate1Id))
    }
    val query = Orm.select(root.count()).from(root).where(cond)
    val res = session.first(query)
    JSON.stringify(res)
  })

  @GetMapping(Array("/{id}"))
  def get(@PathVariable id: Long): String = dao.beginTransaction(session => {
    val res = OrmTool.selectById(classOf[Target], id, session, (root: Root[Target]) => {
      root.select("file")
    })
    JSON.stringify(res)
  })

  @DeleteMapping(Array("/{id}"))
  def delete(@PathVariable id: Long): String = dao.beginTransaction(fn = session => {
    OrmTool.deleteById(classOf[Target], id, session)
    "{}"
  })
}
