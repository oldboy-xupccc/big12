import org.apache.spark.{SparkConf, SparkContext}

/**
  * 迪尔卡积
  */
object CartesianDemo {
    def main(args: Array[String]): Unit = {
        val conf = new SparkConf()
        conf.setAppName("wcDemo")
        conf.setMaster("local[4]")
        val sc = new SparkContext(conf)

        //学号
        val rdd1 = sc.makeRDD(Array((1 -> "tom"), (2 -> "tomas"), (3 -> "tomasLee"), (4 -> "tomason-1")))
        //成绩
        val rdd2 = sc.makeRDD(Array(1 -> 504, 2 -> 600, 3 -> 480, 4 -> 720))

        val rdd3 = rdd1.cartesian(rdd2)
        rdd3.collect().foreach(println)
    }

}
