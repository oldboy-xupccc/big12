import org.apache.spark.{SparkConf, SparkContext}

/**
  * 气温数据聚合应用
  */
object TempAggDemoScala_GroupByKey {
    def main(args: Array[String]): Unit = {
        val conf = new SparkConf()
        conf.setAppName("tempAgg")
        val sc = new SparkContext(conf)

        //1. 加载文件
        val rdd1 = sc.textFile("/user/centos/data/temp3.dat")

        //2. 切割成对(1930,54)
        val rdd2 = rdd1.map(line=>{
            Thread.sleep(args(0).toInt)
            var arr = line.split(" ")
            (arr(0).toInt , arr(1).toInt)
        })

        val rdd3 = rdd2.groupByKey()
        //年度内气温降序
        val rdd4 = rdd3.mapValues(it=>{
            it.toList.sortBy(e=>{- e})
        })

        val rdd5 = rdd4.sortByKey(true)
        rdd5.collect().foreach(println)
    }
}