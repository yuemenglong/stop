package com.cchtrip.stop.entity.res

import io.github.yuemenglong.orm.lang.anno._
import io.github.yuemenglong.orm.lang.types.Types._

/**
  * Created by <yuemenglong@126.com> on 2017/11/21.
  */
@Entity(db = "stop_res")
class Courseware extends EntityBase {
  @Column(length = 64)
  var name: String = _
  @Pointer
  var file: FileInfo = _

  @Pointer
  var swf: FileInfo = _

  var cate0Id: Long = _
  @Pointer
  var cate0: Category = _

  var cate1Id: Long = _
  @Pointer
  var cate1: Category = _
}
