import scala.io.Source

/**
  * Created by Administrator on 2018/9/29.
  */
object FileApp {
    def main(args: Array[String]): Unit = {
        //
        val f =  Source.fromFile("d:\\java\\1.txt")
        val it = f.getLines()
        for(line <- it){
            println(line)
        }
        //
        println(f.mkString)
    }
}
