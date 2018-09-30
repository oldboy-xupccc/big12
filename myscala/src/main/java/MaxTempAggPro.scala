/**
  */
object MaxTempAggPro {
    def main(args: Array[String]): Unit = {
        //读取文件List[String]
        val lines = scala.io.Source.fromFile("d:\\mr\\temp3.dat").getLines().toList

        //变换成元组的集合List[(int,int)]
        val list2 = lines.map(line => {
            val arr = line.split(" ")
            (arr(0).toInt ,arr(1).toInt)
        })

        //分组 ,按年度Map[Int,List[(Int,Int)]]
        val map1 = list2.groupBy(t => t._1)

        //找出每个组内的气温的最大值Map[Int,Int]
        val map2 = map1.mapValues(l=>{
            val max = l.maxBy(t=>t._2)._2
            val min = l.maxBy(t => -t._2)._2
            val tempSum  = l.reduce((a,b)=>(a._1 , a._2 + b._2))._2.toDouble
            val avgTemp = tempSum / l.size
            (max,min,avgTemp)
        })
        //排序List[(Int,Int)]
        val list3 = map2.toList
        val list4 = list3.sortBy(t=>t._1)
        for(e <- list4) println(e)
    }
}
