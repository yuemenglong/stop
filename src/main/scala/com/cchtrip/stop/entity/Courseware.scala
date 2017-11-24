package com.cchtrip.stop.entity

import io.github.yuemenglong.orm.lang.anno.{Column, Entity, Id, OneToMany}
import io.github.yuemenglong.orm.lang.types.Types._

/**
  * Created by <yuemenglong@126.com> on 2017/11/21.
  */
@Entity
class Courseware extends EntityBase {

  @Column(length = 64)
  var name: String = _

  var courseId: Long = _

  @Column(length = 64)
  var fileId: String = _

  @Column(length = 64)
  var fineName: String = _

  var size: Integer = _

  @Column(length = 16)
  var ext: String = _
}
