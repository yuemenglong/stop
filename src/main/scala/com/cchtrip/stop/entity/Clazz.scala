package com.cchtrip.stop.entity

import io.github.yuemenglong.orm.lang.anno._

/**
  * Created by <yuemenglong@126.com> on 2017/11/21.
  */
@Entity
class Clazz extends EntityBase {

  @Column(length = 32)
  var name: String = _

  var studentCount: Integer = _

  @OneToMany
  var students: Array[Student] = Array()

}
