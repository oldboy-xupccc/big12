/**
  * 主构造
  */
class Cat(private var name :String , val blood:Int ,age:Int) {

    println("hello world")

    //辅助构造
    def this(name:String,blood:Int) = {
        this(name , blood , 10) ;
    }
}

/**
  *
  */
class JiafeiCat(name:String,blood:Int,var color:String) extends Cat( name,blood){
    def mm()={
        println(color)
    }
}

/**
  * 伴生对象
  */
object Cat{
    def main(args: Array[String]): Unit = {
        val cat = new Cat("t1" , 1)
        println(cat.blood)
        val j = new JiafeiCat("jj" , 2 , "black")
        j.color

        val x :AnyVal = 100
        val y :Any = ""
        val u :Unit = "sss"
        println(u)
        val list1:List[String] = Nil
        val list2:List[Int] = Nil



    }
}
