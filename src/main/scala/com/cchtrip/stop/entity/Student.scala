package com.cchtrip.stop.entity

import io.github.yuemenglong.orm.lang.anno._
import io.github.yuemenglong.orm.lang.types.Types._

/**
  * Created by <yuemenglong@126.com> on 2017/11/21.
  */
@Entity
class Student extends EntityBase {
  @Column(length = 32)
  var loginName: String = _

  @Column(length = 32)
  var userName: String = _

  @Column(length = 16)
  var mobile: String = _

  @Column(length = 32)
  var password: String = _

  @Column(length = 32)
  var email: String = _

  @Column(length = 80)
  var avatar: String = _

  @Pointer
  var clazz: Clazz = _
  var clazzId: Long = _

  @OneToOne
  var team: TeamApply = _

  @OneToMany
  var jobs: Array[StudentStudyJob] = Array()
}
