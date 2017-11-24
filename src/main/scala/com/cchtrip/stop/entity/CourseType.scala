package com.cchtrip.stop.entity

import io.github.yuemenglong.orm.lang.anno.{Column, Entity, OneToMany, Pointer}

/**
  * Created by <yuemenglong@126.com> on 2017/11/21.
  */
@Entity
class CourseType extends EntityBase {
  @Column(length = 32)
  var name: String = _
}
