package com.bing.data.scalatest

/**
  *
  * @author guobing
  * @date 2020/6/3 下午4:29
  * @description
  * @version 1.0
  *
  */

class User(var id:Int,var name:String) {

  var salary:Double = _

  def this(id:Int,name:String,salary:Double){
    this(id,name)
    this.salary = salary

  }

  override def toString: String = s"User($salary,$id,$name)"

}

object User{

  def apply(id:Int,name:String): User = new User(id,name)

  def apply(id:Int,name:String,salary:Double): User = new User(id,name,salary)

  def unapply(arg: User): Option[(Int, String,Double)] = {
    Some(arg.id,arg.name,arg.salary)
  }

}
