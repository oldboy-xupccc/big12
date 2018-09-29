import scala.io.Source

/**
  * Created by Administrator on 2018/9/29.
  */
object URL {
    def main(args: Array[String]): Unit = {
        val f = Source.fromURL("https://www.cnblogs.com/xupccc/p/9694708.html")
        println(f.mkString)
    }
}
