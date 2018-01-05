package com.cchtrip.stop.entity

import io.github.yuemenglong.orm.lang.anno.{Column, DateTime, Id}
import io.github.yuemenglong.orm.lang.types.Types._

/**
  * Created by <yuemenglong@126.com> on 2017/11/21.
  */
class EntityBase {
  @Id
  var id: Long = _

  @DateTime
  @Column(nullable = false)
  var crTime: Date = _
}
