package com.cchtrip.stop.entity

import com.cchtrip.stop.entity.res.{EntityBase, Question}
import io.github.yuemenglong.json.lang.JsonDate
import io.github.yuemenglong.orm.lang.anno.{Column, Entity, Enum, Ignore, OneToMany, Pointer}
import io.github.yuemenglong.orm.lang.types.Types._

/**
  * Created by <yuemenglong@126.com> on 2017/11/21.
  */
@Entity(db = "stop")
class Quiz extends EntityBase {
  @Column(length = 32)
  var name: String = _
  @JsonDate
  var limitDate: Date = _

  @OneToMany
  var questions: Array[QuizQuestion] = Array()

  @Pointer
  var clazz: Clazz = _
  var clazzId: Long = _

  @Enum(Array("waiting", "succ"))
  var status: String = _

  @OneToMany
  var jobs: Array[QuizJob] = Array()
}

@Entity(db = "stop")
class QuizQuestion extends EntityBase {
  @Pointer
  var quiz: Quiz = _
  var quizId: Long = _

  @Pointer
  var question: Question = _
  var questionId: Long = _
}

@Entity(db = "stop")
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

  @Ignore
  var itemCount: Long = _
  @Ignore
  var finishCount: Long = _
  @Ignore
  var totalScore: Double = _
}

@Entity(db = "stop")
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
