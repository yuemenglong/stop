package com.cchtrip.stop.entity

import io.github.yuemenglong.orm.lang.anno._
import io.github.yuemenglong.orm.lang.types.Types._

/**
  * Created by <yuemenglong@126.com> on 2017/11/21.
  */
@Entity
class User extends EntityBase {
  @Column(length = 32)
  var username: String = _

  @Column(length = 32)
  var password: String = _

  @Column(length = 32)
  var role: String = _

  @Enum(Array("teacher", "student"))
  @Column(nullable = false)
  var ty: String = _
}