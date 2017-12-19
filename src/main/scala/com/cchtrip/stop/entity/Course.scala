package com.cchtrip.stop.entity

import io.github.yuemenglong.orm.lang.anno._
import io.github.yuemenglong.orm.lang.types.Types._

/**
  * Created by <yuemenglong@126.com> on 2017/11/21.
  */
@Entity
class Course extends EntityBase {
  @Column(length = 32)
  var name: String = _

  var description: String = _

  var difficulty: String = _

  var categoryId: Long = _
  @Pointer
  var category: Category = _

  @OneToMany
  var coursewares: Array[CourseCourseware] = Array()
  @OneToMany
  var videos: Array[CourseVideo] = Array()
  @OneToMany
  var questions: Array[CourseQuestion] = Array()

  var questionCount: Integer = _
  var coursewareCount: Integer = _
  var videoCount: Integer = _
}

@Entity
class CourseCourseware extends EntityBase {
  var courseId: Long = _
  @Pointer
  var course: Course = _

  var coursewareId: Long = _
  @Pointer
  var courseware: Course = _


}

@Entity
class CourseVideo extends EntityBase {
  var courseId: Long = _
  @Pointer
  var course: Course = _

  var videoId: Long = _
  @Pointer
  var video: Video = _
}

@Entity
class CourseQuestion extends EntityBase {
  var courseId: Long = _
  @Pointer
  var course: Course = _

  var questionId: Long = _
  @Pointer
  var question: Question = _
}
