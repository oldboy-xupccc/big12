/**
  * Created by Administrator on 2018/9/29.
  */
class BigDog extends MoreMoneyPet{

    //private[this]约束属性只能在当前对象内访问
    private var height :Int = 100

    def getHeight = height

    def incre(n:Int) = {
        this.height += n
        this.height
    }

    override def meng(): Unit = {

    }

    def higherThan(another : BigDog): Boolean ={
        //不可以访问another.height属性.
        this.height > another.getHeight
    }
}

object BigDogApp{
    def main(args: Array[String]): Unit = {
        val a = new BigDog
        val b = new BigDog
        println(a.higherThan(b))
        println(1.→(1))
        println(a incre 10)
        for(x <- (1 to 10)){
            println(x)
        }
    }
}















