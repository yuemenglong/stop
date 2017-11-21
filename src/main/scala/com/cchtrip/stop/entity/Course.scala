package com.cchtrip.stop.entity

import io.github.yuemenglong.orm.lang.anno._

/**
  * Created by <yuemenglong@126.com> on 2017/11/21.
  */
@Entity
class Course extends EntityBase {
  @Column(length = 32)
  var name: String = _

  var description: String = _

  var difficulty: String = _

  @Pointer
  var courseType: CourseType = _

  @OneToMany
  var questions: Array[Question] = Array()
  @OneToMany
  var coursewares: Array[Courseware] = Array()
  @OneToMany
  var videos: Array[Video] = Array()

  var questionCount: Integer = _
  var coursewareCount: Integer = _
  var videoCount: Integer = _
}
