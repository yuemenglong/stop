package com.cchtrip.stop.entity

import io.github.yuemenglong.orm.lang.anno.{Column, Entity, OneToMany, Pointer}
import io.github.yuemenglong.orm.lang.types.Types._

/**
  * Created by <yuemenglong@126.com> on 2017/11/21.
  */
@Entity
class CourseCategory extends EntityBase {
  @Column(length = 32)
  var name: String = _

  var level: Integer = _

  var parentId: Long = _

  @Pointer
  var parent: CourseCategory = _

  @OneToMany(left = "id", right = "parentId")
  var children: Array[CourseCategory] = Array()
}
