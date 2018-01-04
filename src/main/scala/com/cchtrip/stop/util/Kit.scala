package com.cchtrip.stop.util

import java.io.{InputStream, OutputStream}

import com.cchtrip.stop.bean.ErrorProcessor
import org.slf4j.LoggerFactory

/**
  * Created by <yuemenglong@126.com> on 2017/11/21.
  */
object Kit {
  private val logger = LoggerFactory.getLogger("")

  def logError(e: Throwable) {
    e match {
      case _: NamedException =>
      case _ => if (!e.isInstanceOf[NamedException]) {
        e.getMessage + "\n" +
          logger.error(e.getStackTrace.map(ste => {
            ste.toString
          }).mkString("\n"))
        if (e.getCause != null) {
          logError(e.getCause)
        }
      }
    }
  }

  def pipe(is: InputStream, os: OutputStream): Unit = {
    val buffer = new Array[Byte](4096)
    Stream.continually({
      is.read(buffer)
    }).takeWhile {
      case -1 => false
      case 0 => true
      case n if n > 0 =>
        os.write(buffer, 0, n)
        true
    }.last
  }
}
