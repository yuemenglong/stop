/**
  * Created by <yuemenglong@126.com> on 2017/9/29.
  */
package com.cchtrip.stop

import org.springframework.boot._
import org.springframework.boot.autoconfigure._


object App {
  def main(args: Array[String]): Unit = {
    SpringApplication.run(classOf[App])
  }
}

@SpringBootApplication
class App {}
