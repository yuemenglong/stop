package com.cchtrip.stop.entity

import io.github.yuemenglong.orm.lang.anno._
import io.github.yuemenglong.orm.lang.types.Types._

/**
  * Created by <yuemenglong@126.com> on 2017/11/21.
  */
@Entity
class FileInfo extends EntityBase {
  @Column(length = 80, nullable = false)
  var fileId: String = _
  @Column(length = 64)
  var fileName: String = _

  var size: Integer = _
  @Column(length = 16)
  var ext: String = _

  @Enum(Array("courseware", "video", "target", "avatar"))
  var tag: String = _
}
