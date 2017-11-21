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
  var students: Array[Student] = Array()

}
