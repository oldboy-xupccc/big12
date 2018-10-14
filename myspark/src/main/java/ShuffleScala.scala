import org.apache.spark.{SparkConf, SparkContext}

/**
  * Shuffle行为考察
  */
object ShuffleScala {
    def main(args: Array[String]): Unit = {

        //创建spark配置对象
        val conf = new SparkConf()
        conf.setAppName("WCScala")
        conf.setMaster("local[4]")

        val sc = new SparkContext(conf)
        val rdd1 = sc.textFile("file:///d:/mr/word.txt" , 3)
        val rdd2 = rdd1.flatMap(line => {
            line.split(" ")
        })

        val rdd3 = rdd2.map(word => {
            (word , 1)
        })

        val rdd4 = rdd3.reduceByKey((a,b)=>{
            a + b
        })

        rdd4.collect()

    }
}
