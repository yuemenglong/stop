package com.cchtrip.stop.entity

import io.github.yuemenglong.orm.lang.anno._
import io.github.yuemenglong.orm.lang.types.Types._

/**
  * Created by <yuemenglong@126.com> on 2017/11/21.
  */
@Entity
class Question extends EntityBase {

  var courseId: Long = _

  @Column(nullable = false)
  var title: String = _

  @Column(nullable = false)
  var score: Double = _

  @Column(length = 64)
  var answer: String = _

  @Column(length = 8, nullable = false)
  var ty: String = _

  @OneToOne(right = "qtId")
  var sc: QuestionChoice = _
}
