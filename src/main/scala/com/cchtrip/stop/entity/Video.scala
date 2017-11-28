package com.cchtrip.stop.entity

import io.github.yuemenglong.orm.lang.anno.{Column, Entity}
import io.github.yuemenglong.orm.lang.types.Types._

/**
  * Created by <yuemenglong@126.com> on 2017/11/21.
  */
@Entity
class Video extends EntityBase {

  @Column(length = 64)
  var name: String = _

  var courseId: Long = _

  @Column(length = 80)
  var fileId: String = _

  @Column(length = 64)
  var fileName: String = _

  var size: Integer = _

  @Column(length = 16)
  var ext: String = _
}
