package com.cchtrip.stop.entity

import com.cchtrip.stop.entity.res.EntityBase
import io.github.yuemenglong.json.lang.JsonDate
import io.github.yuemenglong.orm.lang.anno._
import io.github.yuemenglong.orm.lang.types.Types._

/**
  * Created by <yuemenglong@126.com> on 2017/11/21.
  */

@Entity(db = "stop")
class StudyJob extends EntityBase {
  var name: String = _
  @Pointer
  var course: Course = _
  var courseId: Long = _
  @Pointer
  var clazz: Clazz = _
  var clazzId: Long = _

  @JsonDate
  var limitDate: Date = _

  @OneToMany(right = "jobId")
  var jobs: Array[StudentStudyJob] = Array()
}

@Entity(db = "stop")
class StudentStudyJob extends EntityBase {
  @Pointer
  var job: StudyJob = _
  var jobId: Long = _
  @Pointer
  var student: Student = _
  var studentId: Long = _

  @Enum(Array("waiting", "succ"))
  var status: String = _

  @OneToMany
  var items: Array[StudentStudyJobItem] = Array()
}

@Entity(db = "stop")
class StudentStudyJobItem extends EntityBase {
  var studentStudyJobId: Long = _
  var targetId: Long = _
  @Enum(Array("courseware", "video", "question"))
  var ty: String = _ // 可能为courseware,video,question
  @Enum(Array("waiting", "succ"))
  var status: String = _

  // 兼容题目
  @Column(length = 4)
  var answer: String = _
  var correct: Boolean = _
  var score: Double = _
}

