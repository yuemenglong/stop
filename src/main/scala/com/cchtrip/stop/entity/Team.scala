package com.cchtrip.stop.entity

import io.github.yuemenglong.orm.lang.anno.{Column, Entity, Id, OneToMany}

/**
  * Created by <yuemenglong@126.com> on 2017/11/21.
  */
@Entity
class Team extends EntityBase {
  @Column(length = 32)
  var name: String = _

  @OneToMany
  var students: Array[Student] = Array()

}
