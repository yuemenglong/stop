package com.cchtrip.stop.entity

import java.util.Date

import io.github.yuemenglong.orm.lang.anno.{Column, Entity, OneToMany, Pointer}

/**
  * Created by <yuemenglong@126.com> on 2017/11/21.
  */
@Entity
class StudyTask extends EntityBase {
  var dateLimit: Date = _

  @Pointer
  var clazz: Clazz = _

  @Pointer
  var course: Course = _
}
