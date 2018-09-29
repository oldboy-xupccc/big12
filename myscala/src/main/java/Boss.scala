/**
  *
  */
class Boss {
    def marry(obj : WRB) = {
        obj.w()
        obj.wrb()
    }
}

object Boss{
    def main(args: Array[String]): Unit = {
        val boss = new Boss
        val star = new WomenStar
        boss.marry(star)

        val map = Map(1->"tom1" , 2->"tom2")
        println(map.size)


        val jfcat = new JiafeiCat("t1" ,  1 , "rr")
        //模糊判断
        println(jfcat.isInstanceOf[Cat])
        //强转
        val cc = jfcat.asInstanceOf[Cat]
        //Cat.class ,精确判断
        println(cc.getClass == classOf[Cat])

        //匿名内部类对象
        val www = new White {
            override def w() = {
                //
            }
        }

    }
}
