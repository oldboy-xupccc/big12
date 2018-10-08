/**
  * 隐式值测试
  * 隐式参数需要和隐式值配合使用
  */
object ImplictValueDemo {
    //
    def main(args: Array[String]) = {

        //修饰函数
        def decorate(str: String)(implicit pref:String)={
            pref + str
        }

        //
        import MyConverterUtil.prefix2
        println(decorate(str="hello"))
    }
}
