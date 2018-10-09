import org.apache.spark.{SparkConf, SparkContext}

/**
  * 气温数据聚合应用
  */
object TempAggDemoScala {
    def main(args: Array[String]): Unit = {
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

        //3. 按照年度分组(1930->{23,34,67} , 1931->{...})
        val rdd3 = rdd2.groupByKey()

        //4. 对组内元素进行统计聚合
        val rdd4 = rdd3.mapValues(it=>{
            val mx = it.max
            val mn = it.min
            val sum = it.sum
            val size = it.size
            (mx , mn , sum.toFloat / size)
        })

        //5. 按照年度排序
        val rdd5 = rdd4.sortByKey(true)

        //6. 输出
        rdd5.collect().foreach(println)
    }
}
