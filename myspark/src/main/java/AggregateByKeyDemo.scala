import org.apache.spark.{HashPartitioner, SparkConf, SparkContext}

/**
  * 按key聚合
  */
object AggregateByKeyDemo {
    def main(args: Array[String]): Unit = {
        val conf = new SparkConf()
        conf.setAppName("wcDemo")
        conf.setMaster("local[4]")
        val sc = new SparkContext(conf)
        val rdd1 = sc.textFile("file:///d:/mr/word.txt" , 3)
        val rdd2 = rdd1.flatMap(_.split(" ")).mapPartitionsWithIndex((idx, it) => {
            var list: List[(String, String)] = Nil
            for (e <- it) {
                list = (e, e + "_" + idx) :: list
            }
            list.iterator
        })
        rdd2.collect().foreach(println)
        println("=======================")
        val zeroU:String = "[]"
        def seqOp(a:String,b:String) = {
            a + b + " ,"
        }
        def comOp(a:String,b:String) = {
            a + "$" + b
        }

        val rdd3 = rdd2.aggregateByKey(zeroU)(seqOp,comOp)
        rdd3.collect().foreach(println)



    }

}
