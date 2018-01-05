package com.cchtrip.stop.bean

import com.cchtrip.utils.key.Snowflake
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct
import io.github.yuemenglong.orm.lang.types.Types._


/**
  * Created by <yuemenglong@126.com> on 2017/7/25.
  */
@Component object IdGenerator {
  private var generator: Snowflake = _

  def generateId: Long = generator.nextId
}

@Component class IdGenerator {
  @Value("${workId}")
  private val workId: Long = _

  @SuppressWarnings(Array("unused"))
  @PostConstruct def init(): Unit = {
    IdGenerator.generator = Snowflake.getInstance(workId)
  }
}
