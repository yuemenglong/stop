package com.cchtrip.stop.controller.admin

import com.cchtrip.stop.bean.Dao
import com.cchtrip.stop.entity._
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
@RequestMapping(value = Array("/admin/video"), produces = Array("application/json"))
@ResponseBody
class VideoCtr {

  @Autowired
  var dao: Dao = _

  @PostMapping(Array(""))
  def post(@RequestBody body: String): String = dao.beginTransaction(session => {
    val obj = JSON.parse(body, classOf[Video])
    obj.crTime = new Date
    require(obj.file != null)
    obj.file.crTime = new Date
    obj.file.tag = "video"
    session.execute(Orm.insert(obj))
    JSON.stringify(obj)
  })

  @PutMapping(Array("/{id}"))
  def put(@PathVariable id: Long, @RequestBody body: String): String = dao.beginTransaction(session => {
    val obj = JSON.parse(body, classOf[Video])
    obj.id = id
    session.execute(Orm.update(obj))
    JSON.stringify(obj)
  })

  @GetMapping(Array("/list"))
  def list(@RequestParam(defaultValue = "20") limit: Long,
           @RequestParam(defaultValue = "0") offset: Long,
           cate0Id: Long,
           cate1Id: Long,
          ): String = dao.beginTransaction(session => {
    val root = Orm.root(classOf[Video])
    root.select("file")
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
    val root = Orm.root(classOf[Video])
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
    val res = OrmTool.selectById(classOf[Video], id, session, (root: Root[Video]) => {
      root.select("file")
    })
    JSON.stringify(res)
  })

  @DeleteMapping(Array("/{id}"))
  def delete(@PathVariable id: Long): String = dao.beginTransaction(fn = session => {
    OrmTool.deleteById(classOf[Video], id, session)
    "{}"
  })

}
