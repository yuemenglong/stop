package com.cchtrip.stop.entity

import io.github.yuemenglong.orm.lang.anno._
import io.github.yuemenglong.orm.lang.types.Types._

/**
  * Created by <yuemenglong@126.com> on 2017/11/21.
  */

@Entity
class StudyJob extends EntityBase {
  var name: String = _
  @Pointer
  var course: Course = _
  var courseId: Long = _
  @Pointer
  var clazz: Clazz = _
  var clazzId: Long = _

  var limitDate: Date = _

  @OneToMany(right = "jobId")
  var jobs: Array[StudentStudyJob] = Array()
}

@Entity
class StudentStudyJob extends EntityBase {
  @Pointer
  var job: StudyJob = _
  var jobId: Long = _
  @Pointer
  var student: Student = _
  var studentId: Long = _

  @OneToMany
  var jobItems: Array[StudentStudyJobRel] = Array()
}

@Entity
class StudentStudyJobRel extends EntityBase {
  var studentStudyJobId: Long = _
  var targetId: Long = _
  var ty: String = _ // 可能为courseware,video,question
  var status: String = _
}

