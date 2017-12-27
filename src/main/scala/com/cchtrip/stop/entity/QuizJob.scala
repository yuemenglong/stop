package com.cchtrip.stop.entity

import io.github.yuemenglong.orm.lang.anno.{Column, Entity, Enum, OneToMany, Pointer}
import io.github.yuemenglong.orm.lang.types.Types._

/**
  * Created by <yuemenglong@126.com> on 2017/11/21.
  */
@Entity
class Quiz extends EntityBase {
  @Column(length = 32)
  var name: String = _
  var limitDate: Date = _

  @OneToMany
  var questions: Array[QuizQuestion] = Array()

  @Pointer
  var clazz: Clazz = _
  var clazzId: Long = _

  @OneToMany
  var jobs: Array[QuizJob] = Array()
}

@Entity
class QuizQuestion extends EntityBase {
  @Pointer
  var quiz: Quiz = _
  var quizId: Long = _

  @Pointer
  var question: Question = _
  var questionId: Long = _
}

@Entity
class QuizJob extends EntityBase {
  @Pointer
  var quiz: Quiz = _
  var quizId: Long = _

  @Pointer
  var student: Student = _
  var studentId: Long = _

  @Enum(Array("waiting", "succ"))
  var status: String = _
  var score: Double = _

  @OneToMany(right = "jobId")
  var items: Array[QuizJobItem] = Array()
}

@Entity
class QuizJobItem extends EntityBase {

  var jobId: Long = _
  var answer: String = _
  var score: Double = _
  var correct: Boolean = _
  @Enum(Array("waiting", "succ"))
  var status: String = _

  @Pointer
  var question: Question = _
  var questionId: Long = _
}
