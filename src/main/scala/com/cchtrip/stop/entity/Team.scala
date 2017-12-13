package com.cchtrip.stop.entity

import io.github.yuemenglong.orm.lang.anno._
import io.github.yuemenglong.orm.lang.types.Types._

/**
  * Created by <yuemenglong@126.com> on 2017/11/21.
  */
@Entity
class Team extends EntityBase {
  @Column(length = 32)
  var name: String = _

  @Pointer
  var creater: Student = _
  var createrId: Long = _

  @OneToMany
  var students: Array[TeamApply] = Array()

  @Ignore
  var studentCount: Long = _
}

@Entity
class TeamApply extends EntityBase {
  @Pointer
  var student: Student = _
  var studentId: Long = _

  @Pointer
  var team: Team = _
  var teamId: Long = _

  @Enum(Array("waiting", "succ", "fail", "delete"))
  var status: String = _
}
