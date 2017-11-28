package com.cchtrip.stop.entity

import io.github.yuemenglong.orm.lang.anno.{Column, Entity, Pointer}
import io.github.yuemenglong.orm.lang.types.Types._

/**
  * Created by <yuemenglong@126.com> on 2017/11/21.
  */
@Entity
class QuestionChoice extends EntityBase {
  @Pointer
  var qt: Question = _

  var opt1: String = _
  var opt2: String = _
  var opt3: String = _
  var opt4: String = _

  var answer: Integer = _
}
