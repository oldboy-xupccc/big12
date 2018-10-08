/**
  * 分数类
  */
class Fraction2(var top :Int , var bot : Int){
    override def toString: String = {
        top + "/" + bot ;
    }

    def update(nv: (Int, Int)) = {
        this.top=nv._1
        this.bot =nv._2
    }

}

/**
  * 伴生对象
  */
object Fraction2{
    /**
      * 字定义apply方法
      */
    def apply(top:Int , bot:Int)= {
        new Fraction2(top , bot)
    }

    //
    //Unit :  ()
    //Null : null
    //Nothing : 所有类的子类
    //None Some
    //Option : 可空 ，None:无 Some:有值
    def unapply(f:Fraction2) = if(f.bot == 0) None else Some((f.top,f.bot))

}

object FractionApp{
    def main(args: Array[String]): Unit = {
        //val f1 = new Fraction(1, 2)
        //val f1 = Fraction(1,2)
        val f1 = Fraction2.apply(1,2)
//        println(f1)
//
//        import scala.collection.mutable.{Map=>MMap}
//        val map = MMap(1->"tom1" , 2->"tom2")
//        map(1) = "tomas"
//
//        val arr = Array.apply[Int](1,2,3)
//        arr(0) = 4
//        println(arr)
        //=等价于调用update()
        //f1() = (3, 4)
        //反向抽取出分子分母

        //unapply()反向抽取分数的分子和分母
        val Fraction2(a,b) = f1
        println(a)
        println(b)

        val list = List(1,2,3,2,4)
        list.sortWith((a,b)=> false)
    }
}


