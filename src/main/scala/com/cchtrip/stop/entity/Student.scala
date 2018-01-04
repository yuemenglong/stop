package com.cchtrip.stop.entity

import io.github.yuemenglong.orm.lang.anno._
import io.github.yuemenglong.orm.lang.types.Types._

/**
  * Created by <yuemenglong@126.com> on 2017/11/21.
  */
@Entity
class Student extends EntityBase {
  @Pointer
  var user: User = _

  @Column(length = 32)
  var name: String = _

  @Column(length = 16)
  var mobile: String = _

  @Column(length = 32)
  var email: String = _

  @Pointer
  var avatar: FileInfo = _

  @Pointer
  var clazz: Clazz = _
  var clazzId: Long = _

  @OneToOne
  var team: TeamApply = _

  @OneToMany
  var jobs: Array[StudentStudyJob] = Array()

  @OneToMany
  var finished: Array[FinishedTarget] = Array()
}

@Entity
class FinishedTarget extends EntityBase {
  var studentId: Long = _
  var targetId: Long = _
}