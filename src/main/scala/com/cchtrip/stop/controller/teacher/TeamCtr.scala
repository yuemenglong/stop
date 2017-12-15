package com.cchtrip.stop.controller.teacher

import com.cchtrip.stop.bean.Dao
import com.cchtrip.stop.entity.{Team, TeamApply}
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
@RequestMapping(value = Array("//team"), produces = Array("application/json"))
class TeamCtr {

  @Autowired
  var dao: Dao = _

  @DeleteMapping(Array("/{id}"))
  def deleteTeam(@PathVariable id: Long): String = dao.beginTransaction(session => {
    OrmTool.deleteById(classOf[Team], id, session)
    val root = Orm.root(classOf[TeamApply])
    session.execute(Orm.update(root).set(root.get("status").assign("delete"))
      .where(root.join("team").get("id").eql(id)))
    "{}"
  })

  @GetMapping(Array("/{id}"))
  def getTeam(@PathVariable id: Long): String = dao.beginTransaction(session => {
    val res = OrmTool.selectById(classOf[Team], id, session, (root: Root[Team]) => {
      root.select("students").select("student")
    })
    JSON.stringify(res)
  })

  @GetMapping(Array("/list"))
  def GetTeamList(@RequestParam(defaultValue = "20") limit: Long,
                  @RequestParam(defaultValue = "0") offset: Long
                 ): String = dao.beginTransaction(session => {
    val res = {
      val root = Orm.root(classOf[Team])
      root.select("creater")
      val query = Orm.select(root, root.count("id")).from(root)
        .where(root.join("students").get("status").eql("succ"))
        .groupBy("id").limit(limit).offset(offset)
      session.query(query)
    }
    val teams = res.map { case (team, count) =>
      team.studentCount = count
      team
    }
    JSON.stringify(teams)
  })

  @GetMapping(Array("/count"))
  def GetTeamsCount(): String = dao.beginTransaction(session => {
    val root = Orm.root(classOf[Team])
    val query = Orm.select(root.count()).from(root)
    val res = session.first(query)
    JSON.stringify(res)
  })
}
