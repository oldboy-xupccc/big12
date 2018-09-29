/**
  * 类扩展特质
  */
class Fan extends USB with USB2{
    override def play(): Unit = {
        println("~~~~")
    }

    override def play2(): Unit = {
        println("!!!!!")
    }
}

object Fan{
    def main(args: Array[String]): Unit = {
        val fan = new Fan
        fan.play()
        fan.play2()

        val arr = Array[Int](1,2,3)
        val map = Map(1->"tom1")
    }
}
