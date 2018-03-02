package com.cchtrip.stop.entity.res

import io.github.yuemenglong.orm.lang.anno._
import io.github.yuemenglong.orm.lang.types.Types._

/**
  * Created by <yuemenglong@126.com> on 2017/11/21.
  */
@Entity(db = "stop_res")
class FileInfo extends EntityBase {
  @Column(length = 80, nullable = false)
  var fileId: String = _
  @Column(length = 64)
  var fileName: String = _

  var size: Integer = _
  @Column(length = 16)
  var ext: String = _

  @Enum(Array("courseware", "video", "target"))
  var tag: String = _
}
