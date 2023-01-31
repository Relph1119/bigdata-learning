package com.teapot.demo

/**
 * 接口多继承案例
 * Created by xuwei
 */
object PersonDemo {
  def main(args: Array[String]): Unit = {
    val p1 = new Person("tom")
    val p2 = new Person("jack")
    p1.sayHello(p2.name)
    p1.makeFriends(p2)
  }
}

trait HelloTrait {
  def sayHello(name: String)
}

trait MakeFriendsTrait {
  def makeFriends(p: Person)
}

class Person(val name: String) extends HelloTrait with MakeFriendsTrait {
  def sayHello(name: String): Unit = {
    println("hello," + name)
  }

  def makeFriends(p: Person): Unit = {
    println("my name:" + name + ", your name:" + p.name)
  }

}
