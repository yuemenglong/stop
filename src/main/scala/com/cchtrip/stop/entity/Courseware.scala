package com.cchtrip.stop.entity

import io.github.yuemenglong.orm.lang.anno._
import io.github.yuemenglong.orm.lang.types.Types._

/**
  * Created by <yuemenglong@126.com> on 2017/11/21.
  */
@Entity
class Courseware extends EntityBase {
  @Column(length = 64)
  var name: String = _
  @Column(length = 80, nullable = false)
  var fileId: String = _
  @Column(length = 64)
  var fileName: String = _
  var size: Integer = _
  @Column(length = 16)
  var ext: String = _

  var cate0Id: Long = _
  @Pointer
  var cate0: Category = _

  var cate1Id: Long = _
  @Pointer
  var cate1: Category = _
}
