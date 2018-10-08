/**
  * Created by Administrator on 2018/10/8.
  */
object MyConverterUtil {
    //在单例对象中定义隐式函数
    implicit def int2Fraction(n:Int):ConvertDemo.Fraction = {
        ConvertDemo.Fraction(n,1)
    }

    //定义隐式值
    implicit val prefix:String = "<<<<"
    implicit val prefix2:String = "{{{{{"
}
