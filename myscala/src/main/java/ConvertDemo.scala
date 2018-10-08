/**
  * 隐式转换测试
  */
object ConvertDemo {
    //定义分数类
    case class Fraction(val top:Int , val bot:Int){
        def mulby(f2:Fraction) = {
            Fraction(top * f2.top , bot * f2.bot)
        }
    }

    def main(args: Array[String]): Unit = {

        //定义隐式函数
//        implicit def int2Fraction(n:Int) = {
//            Fraction(n ,1)
//        }

        //导入具体的隐式转换函数
        import MyConverterUtil.int2Fraction

        val f1 = Fraction(1,2)
        val f2 = Fraction(2,3)
        val f3 = f1.mulby(f2)
        println(f3)
        println(f3.mulby(4))
        println(f3 mulby 4)
        println(4 mulby f3)
    }
}
