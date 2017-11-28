package com.cchtrip.stop.entity

import io.github.yuemenglong.orm.lang.anno.{Entity, Pointer}
import io.github.yuemenglong.orm.lang.types.Types._

/**
  * Created by <yuemenglong@126.com> on 2017/11/21.
  */
@Entity
class QuestionTrueFalse extends EntityBase {
  @Pointer
  var qt: Question = _

  var answer: Boolean = _
}
