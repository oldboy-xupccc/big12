/**
  * 单例对象
  */
object App {
    def main(args:Array[String])={
        val d = new Dog
        d.increment
        d.color = "black"

        println(d.current)
        d.setAge(4)

    }
}
