package com.cchtrip.stop.bean

import java.util
import java.util.regex.Pattern
import javax.annotation.{PostConstruct, PreDestroy}

import com.cchtrip.stop.entity.StudentStudyJobItem
import com.cchtrip.stop.util.Kit
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

  @Value("${res.host}") private val resHost = ""
  @Value("${res.port}") private val resPort = 0
  @Value("${res.user}") private val resUser = ""
  @Value("${res.password}") private val resPassword = ""
  @Value("${res.database}") private val resDatabase = ""
  @Value("${res.minConn}") private val resMinConn = 1
  @Value("${res.maxConn}") private val resMaxConn = 1
  @Value("${res.partition}") private val resPartition = 1

  var db: Db = _
  var res: Db = _

  @PostConstruct def init(): Unit = {
    val list = Kit.scanPackage("com.cchtrip.stop.entity")
    Orm.init(list)
    OrmTool.exportTsClass("entity.ts")
    Orm.setLogger(show_sql)
    JSON.setConstructorMap(OrmTool.getEmptyConstructorMap)
    db = new Db(host, port, user, password, database, minConn, maxConn, partition)
    db.check()
    res = new Db(resHost, resPort, resUser, resPassword, resDatabase, resMinConn, resMaxConn, resPartition)
    res.check()
  }

  @PreDestroy
  def destroy(): Unit = {
    db.shutdown()
    res.shutdown()
  }

  def beginTransaction[T](fn: (Session) => T): T = {
    db.beginTransaction(fn)
  }

  def resTransaction[T](fn: (Session) => T): T = {
    res.beginTransaction(fn)
  }
}
