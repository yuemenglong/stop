package com.cchtrip.stop.bean

import java.util
import java.util.regex.Pattern
import javax.annotation.{PostConstruct, PreDestroy}

import com.cchtrip.stop.entity.StudentStudyJobRel
import io.github.yuemenglong.json.JSON
import io.github.yuemenglong.orm.Orm
import io.github.yuemenglong.orm.Session.Session
import io.github.yuemenglong.orm.db.Db
import io.github.yuemenglong.orm.tool.OrmTool
import org.springframework.beans.factory.annotation.Value
import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider
import org.springframework.core.`type`.filter.RegexPatternTypeFilter
import org.springframework.stereotype.Component

/**
  * Created by <yuemenglong@126.com> on 2017/9/29.
  */
@Component
class Dao {
  @Value("${db.host}") private val host = ""
  @Value("${db.port}") private val port = 0
  @Value("${db.user}") private val user = ""
  @Value("${db.password}") private val password = ""
  @Value("${db.database}") private val database = ""
  @Value("${db.minConn}") private val minConn = 1
  @Value("${db.maxConn}") private val maxConn = 1
  @Value("${db.partition}") private val partition = 1
  @Value("${db.show_sql:true}") private val show_sql: Boolean = true

  var db: Db = _

  @PostConstruct def init(): Unit = {
    val provider: ClassPathScanningCandidateComponentProvider = new ClassPathScanningCandidateComponentProvider(false)
    provider.addIncludeFilter(new RegexPatternTypeFilter(Pattern.compile(".*")))
    val classes: util.Set[BeanDefinition] = provider.findCandidateComponents("com.cchtrip.stop.entity")
    val iter = classes.iterator()
    val list = Stream.continually({
      if (iter.hasNext) {
        iter.next()
      } else {
        null
      }
    }).takeWhile(_ != null).map(_.getBeanClassName).toArray
    Orm.init(list)
    Orm.setLogger(show_sql)
    JSON.setConstructorMap(OrmTool.getEmptyConstructorMap)
    db = new Db(host, port, user, password, database, minConn, maxConn, partition)
    //    db.rebuild()
    OrmTool.exportTsClass("entity.ts")
    db.check()
  }

  @PreDestroy
  def destroy(): Unit = {
    db.shutdown()
  }

  def beginTransaction[T](fn: (Session) => T): T = {
    db.beginTransaction(fn)
  }
}
