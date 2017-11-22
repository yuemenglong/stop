package com.cchtrip.stop.entity

import io.github.yuemenglong.orm.lang.anno.{Column, Entity, Id, OneToMany}
import io.github.yuemenglong.orm.lang.types.Types._

/**
  * Created by <yuemenglong@126.com> on 2017/11/21.
  */
@Entity
class Courseware extends EntityBase {

  @Column(length = 32)
  var name: String = _

  var courseId: Long = _
}
