package com.cchtrip.stop.entity.res

import io.github.yuemenglong.orm.lang.anno._
import io.github.yuemenglong.orm.lang.types.Types._

/**
  * Created by <yuemenglong@126.com> on 2017/11/21.
  */
@Entity(db = "stop_res")
class Target extends EntityBase {
  @Column(nullable = false, length = 32)
  var name: String = _
  @Column(nullable = false)
  var title: String = _
  @Column(nullable = false)
  var score: Double = _
  @Column(length = 64, nullable = false)
  var answer: String = _

  @Enum(Array("web"))
  var ty: String = _

  @Column(nullable = false)
  @Index(unique = true)
  var baseDir: String = _

  @Pointer
  var file: FileInfo = _

  var cate0Id: Long = _
  @Pointer
  var cate0: Category = _

  var cate1Id: Long = _
  @Pointer
  var cate1: Category = _
}
