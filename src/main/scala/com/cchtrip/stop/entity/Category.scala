package com.cchtrip.stop.entity

import io.github.yuemenglong.orm.lang.anno.{Column, Entity, Enum, OneToMany, Pointer}
import io.github.yuemenglong.orm.lang.types.Types._

/**
  * Created by <yuemenglong@126.com> on 2017/11/21.
  */
@Entity
class Category extends EntityBase {
  @Column(length = 32)
  var name: String = _

  var level: Integer = _

  var parentId: Long = _

  @Enum(Array("course", "courseware", "video", "question"))
  @Column(nullable = false)
  var ty: String = _

  @Pointer
  var parent: Category = _

  @OneToMany(left = "id", right = "parentId")
  var children: Array[Category] = Array()
}
