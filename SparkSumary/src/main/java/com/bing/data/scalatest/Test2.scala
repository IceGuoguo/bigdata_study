package com.bing.data.scalatest

/**
  *
  * @author guobing
  * @date 2020/5/28 下午1:45
  * @description
  * @version 1.0
  *
  */

object Test2 {

  def main(args: Array[String]): Unit = {

    println(sum(1, 2))
    println(sub(3, 1))
    println(sum01(1, 2, 3, 4, 5))
    sayHello("张三", "hello")
    sayHello(name = "张三", msg = "hello")
    sayHello2("李四")
    sayHello2(name = "李四")
    println(factorial(10))
    println(sum02(1)(2))

    def sum(x: Int, y: Int): Int = {
      return x + y
    }

    def sub(x: Int, y: Int): Int = {
      x - y
    }

    //    可变长参数
    def sum01(x: Int, y: Int, values: Int*) = {
      var total = 0
      total = x + y
      for (i <- values) {
        total += i
      }
      total
    }

    //    命名参数
    def sayHello(name: String, msg: String) = {
      println(s"${name} ~ ${msg}")
    }

    //    参数默认值
    def sayHello2(name: String, msg: String = "Hello") = {
      println(s"${name} ~ ${msg}")
    }

    //    内嵌函数
    def factorial(x: Int): Int = {
      def muti(i: Int): Int = {
        if (i > 1) {
          i * muti(i - 1)
        } else {
          1
        }
      }

      muti(x)
    }

    //    柯里化
    def sum02(x: Int)(y: Int): Int = {
      x + y
    }

    //    隐式函数
    var sumV1 = (x: Int, y: Int) => x + y
    var sumV2: (Int, Int) => Int = (x, y) => x + y

    println(sumV1)
    println(sumV2)

    def method(opt:(Int,Int)=>Int)(x:Int,y:Int) = {
      opt(x,y)
    }

    println(method((x,y)=>{x+y})(1,2))
    println(method((x,y)=>{x*y})(1,2))

  }

}
