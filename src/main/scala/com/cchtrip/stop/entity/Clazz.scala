package com.cchtrip.stop.entity

import com.cchtrip.stop.entity.res.EntityBase
import io.github.yuemenglong.orm.lang.anno._
import io.github.yuemenglong.orm.lang.types.Types._

/**
  * Created by <yuemenglong@126.com> on 2017/11/21.
  */
@Entity(db = "stop")
class Clazz extends EntityBase {

  @Column(length = 32)
  var name: String = _

  @Ignore
  var studentCount: Integer = _

  @OneToMany
  var students: Array[Student] = Array()

}
