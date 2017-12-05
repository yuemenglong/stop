package com.cchtrip.stop.util

/**
  * Created by <yuemenglong@126.com> on 2017/12/5.
  */
case class NamedException(name: String, message: String) extends RuntimeException(message) {

}
