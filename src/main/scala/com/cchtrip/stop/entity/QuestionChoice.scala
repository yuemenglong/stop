package com.cchtrip.stop.entity

import io.github.yuemenglong.orm.lang.anno.{Column, Entity, Pointer}
import io.github.yuemenglong.orm.lang.types.Types._

/**
  * Created by <yuemenglong@126.com> on 2017/11/21.
  */
@Entity
class QuestionChoice extends EntityBase {
  var a: String = _
  var b: String = _
  var c: String = _
  var d: String = _
}
