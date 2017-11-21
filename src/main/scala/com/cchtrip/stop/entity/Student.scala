package com.cchtrip.stop.entity

import io.github.yuemenglong.orm.lang.anno.{Entity, Id}

/**
  * Created by <yuemenglong@126.com> on 2017/11/21.
  */
@Entity
class Student {
  @Id(auto = true)
  var id: Long = _
}
