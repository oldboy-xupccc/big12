/**
  */
object MaxTempAggPro2 {
    def main(args: Array[String]): Unit = {
        //读取文件List[String]
        val lines = scala.io.Source.fromFile("d:\\mr\\temp3.dat").getLines().toList

        //变换成元组的集合List[(int,int)]
        val list2 = lines.map(line => {
            val arr = line.split(" ")
            (arr(0).toInt ,arr(1).toInt)
        })
        //

        def sortFunc(a:(Int,Int)  ,b:(Int,Int)):Boolean ={
            if (a._1 == b._1) {
                (a._2 - b._2) > 0
            }
            else {
                (a._1 - b._1) < 0
            }
        }
        val list3 = list2.sortWith(sortFunc)
        for (e <- list3) println(e)

    }
}
