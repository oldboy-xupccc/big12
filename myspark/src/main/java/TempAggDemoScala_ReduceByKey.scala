import org.apache.spark.{SparkConf, SparkContext}

/**
  * 气温数据聚合应用
  */
object TempAggDemoScala_ReduceByKey {
    def main(args: Array[String]): Unit = {
        val start = System.currentTimeMillis()
        val conf = new SparkConf()
        conf.setAppName("tempAgg")
        conf.setMaster("local")
        val sc = new SparkContext(conf)

        //1. 加载文件
        val rdd1 = sc.textFile("file:///d:/mr/temp3.dat")

        //2. 切割成对(1930,54)
        val rdd2 = rdd1.map(line=>{
            var arr = line.split(" ")
            (arr(0).toInt , arr(1).toInt)
        })

        //
        val rdd3 = rdd2.mapValues(temp=>{
            temp :: Nil
        })
        //
        val rdd4 = rdd3.reduceByKey(_ ::: _)
        rdd4.collect().foreach(println)
        println(System.currentTimeMillis() - start)
    }
}
