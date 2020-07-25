package com.bing.data.scalatest

import java.util.Date

import scala.util.Random

/**
  *
  * @author guobing
  * @date 2020/5/27 上午8:55
  * @description
  * @version 1.0
  *
  */

object Test1 {

  def main(args: Array[String]): Unit = {

    //match-case 类型匹配
    var stu = Array(100,"张三",true,new Date())
    var i = stu(new Random().nextInt(4))
    var res = i match {
      case x:Int => s"age:${x}"
      case x:String => s"name:${x}"
      case x:Boolean => s"sex:${x}"
      case _ => "啥都不是"
    }
    println(res)

    //match-case 数值匹配
    var a = Array(1,2,3)
    var b = a(new Random().nextInt(3))
    var res2 = b match {
      case 1 => "one"
      case 2 => "two"
      case 3 => "three"
      case default => "other"
    }
    println(res2)

  }

}
