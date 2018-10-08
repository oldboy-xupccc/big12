/**
  * 尾递归
  */
object TailRecursiveDemo {
    //定义递归实现累加求和
    def sum(list: List[BigInt]): BigInt = {
        if (list.isEmpty) 0 else list.head + sum(list.tail)
    }

    //使用尾递归，对函数调用进行优化，避免栈溢出
    def sum2(x: Seq[Int], part: BigInt): BigInt = {
        if (x.isEmpty) part else sum2(x.tail, x.head + part)
    }

    def main(args: Array[String]): Unit = {
        val list = List(1 to 100000000:_*)
        println(sum2(list , 0))
        val list2 = List(1,2,3)
        val set = Set(1,1,2)

    }
}
