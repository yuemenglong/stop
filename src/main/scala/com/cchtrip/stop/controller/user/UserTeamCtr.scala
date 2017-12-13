package com.cchtrip.stop.controller.user

import com.cchtrip.stop.bean.Dao
import com.cchtrip.stop.entity._
import com.cchtrip.stop.util.NamedException
import io.github.yuemenglong.json.JSON
import io.github.yuemenglong.orm.Orm
import io.github.yuemenglong.orm.lang.types.Types._
import io.github.yuemenglong.orm.operate.traits.core.Root
import io.github.yuemenglong.orm.tool.OrmTool
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation._

/**
  * Created by <yuemenglong@126.com> on 2017/11/21.
  */
@RestController
@RequestMapping(value = Array("/user/{uid}/team"), produces = Array("application/json"))
class UserTeamCtr {

  @Autowired
  var dao: Dao = _

  // 加入Team
  @PutMapping(Array(""))
  def putTeam(@PathVariable uid: Long, @RequestBody body: String): String = dao.beginTransaction(session => {
    val teamId = JSON.parse(body).asObj().getLong("id")
    if (teamId == null) {
      throw new NamedException(NamedException.INVALID_PARAM, "队伍id为空")
    }
    // 删掉原有的apply
    {
      val root = Orm.root(classOf[TeamApply])
      val ex = Orm.deleteFrom(root).where(root.get("studentId").eql(uid))
      session.execute(ex)
    }
    val apply = new TeamApply
    apply.crTime = new Date()
    apply.studentId = uid
    apply.teamId = teamId
    apply.status = "waiting"
    session.execute(Orm.insert(Orm.convert(apply)))
    "{}"
  })

  // 删除Team
  @DeleteMapping(Array(""))
  def deleteTeam(@PathVariable uid: Long): String = dao.beginTransaction(session => {
    val createrId = {
      val root = Orm.root(classOf[Student])
      val query = Orm.select(root.join("team").join("team").get("createrId").as(classOf[Long]))
        .from(root).where(root.get("id").eql(uid))
      session.first(query)
    }
    if (createrId == uid) {
      // 解散队伍，队伍删除，更新中间表
      {
        val root = Orm.root(classOf[TeamApply])
        val ex = Orm.update(root).set(root.get("status").assign("delete"))
          .where(root.join("team").get("createrId").eql(createrId))
        session.execute(ex)
      }
      {
        val root = Orm.root(classOf[Team])
        val ex = Orm.deleteFrom(root).where(root.get("createrId").eql(createrId))
        session.execute(ex)
      }
    } else {
      // 删除自己的中间表
      val root = Orm.root(classOf[TeamApply])
      val ex = Orm.deleteFrom(root).where(root.get("studentId").eql(uid))
      session.execute(ex)
    }
    "{}"
  })


  @GetMapping(Array(""))
  def getTeam(@PathVariable uid: Long): String = dao.beginTransaction(session => {
    val res = OrmTool.selectById(classOf[Student], uid, session, (root: Root[Student]) => {
      root.select("team").select("team").select("students").select("student")
    })
    JSON.stringify(res.team)
  })

  @PutMapping(Array("/students/{aid}"))
  def putApplyStatus(@PathVariable uid: Long,
                     @PathVariable aid: Long,
                     @RequestBody body: String): String = dao.beginTransaction(session => {
    val status = JSON.parse(body).asObj().getStr("status")
    OrmTool.updateById(classOf[TeamApply], aid, session, ("status", status))
    "{}"
  })
}
