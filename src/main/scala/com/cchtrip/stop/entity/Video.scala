package com.cchtrip.stop.entity

import io.github.yuemenglong.orm.lang.anno.{Column, Entity, Pointer}
import io.github.yuemenglong.orm.lang.types.Types._

/**
  * Created by <yuemenglong@126.com> on 2017/11/21.
  */
@Entity
class Video extends EntityBase {

  @Column(length = 64)
  var name: String = _

  @Column(length = 80, nullable = false)
  var fileId: String = _

  @Column(length = 64)
  var fileName: String = _

  var size: Integer = _

  @Column(length = 16)
  var ext: String = _

  var categoryId: Long = _
  @Pointer
  var category: Category = _
}
