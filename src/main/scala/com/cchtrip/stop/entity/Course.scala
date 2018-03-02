package com.cchtrip.stop.entity

import com.cchtrip.stop.entity.res._
import io.github.yuemenglong.orm.lang.anno._
import io.github.yuemenglong.orm.lang.types.Types._

/**
  * Created by <yuemenglong@126.com> on 2017/11/21.
  */
@Entity(db = "stop")
class Course extends EntityBase {
  @Column(length = 32)
  var name: String = _

  var description: String = _

  var difficulty: String = _

  @OneToMany
  var coursewares: Array[CourseCourseware] = Array()
  @OneToMany
  var videos: Array[CourseVideo] = Array()
  @OneToMany
  var questions: Array[CourseQuestion] = Array()

  @Ignore
  var questionCount: Long = _
  @Ignore
  var coursewareCount: Long = _
  @Ignore
  var videoCount: Long = _

  var cate0Id: Long = _
  @Pointer
  var cate0: Category = _

  var cate1Id: Long = _
  @Pointer
  var cate1: Category = _
}

@Entity(db = "stop")
class CourseCourseware extends EntityBase {
  var courseId: Long = _
  @Pointer
  var course: Course = _

  var coursewareId: Long = _
  @Pointer
  var courseware: Courseware = _


}

@Entity(db = "stop")
class CourseVideo extends EntityBase {
  var courseId: Long = _
  @Pointer
  var course: Course = _

  var videoId: Long = _
  @Pointer
  var video: Video = _
}

@Entity(db = "stop")
class CourseQuestion extends EntityBase {
  var courseId: Long = _
  @Pointer
  var course: Course = _

  var questionId: Long = _
  @Pointer
  var question: Question = _
}
