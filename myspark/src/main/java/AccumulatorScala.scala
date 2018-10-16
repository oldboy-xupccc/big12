import org.apache.spark.{SparkConf, SparkContext}

/**
  * 累加器
  */
object AccumulatorScala {
    def main(args: Array[String]): Unit = {

        //创建spark配置对象
        val conf = new SparkConf()
        conf.setAppName("WCScala")
        conf.setMaster("local[4]")
        conf.set("spark.shuffle.sort.bypassMergeThreshold" , "200")
        conf.set("spark.serializer" , "org.apache.spark.serializer.KryoSerializer")
        conf.set("spark.local.dir" , "D:/temp")

        val sc = new SparkContext(conf)
        sc.longAccumulator("myacc")



    }
}
