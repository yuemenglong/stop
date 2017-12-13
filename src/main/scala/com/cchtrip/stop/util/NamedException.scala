package com.cchtrip.stop.util

/**
  * Created by <yuemenglong@126.com> on 2017/12/5.
  */
object NamedException {
  val INVALID_PARAM = "INVALID_PARAM"
  val DEL_CATE_FAIL = "DEL_CATE_FAIL"
}

case class NamedException(name: String, message: String) extends RuntimeException(message) {
}
