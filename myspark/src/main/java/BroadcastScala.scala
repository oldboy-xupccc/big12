import org.apache.spark.{HashPartitioner, SparkConf, SparkContext}

/**
  * Shuffle行为考察
  */
object BroadcastScala {
    def main(args: Array[String]): Unit = {

        //创建spark配置对象
        val conf = new SparkConf()
        conf.setAppName("WCScala")
        conf.setMaster("local[4]")
        conf.set("spark.shuffle.sort.bypassMergeThreshold" , "200")
        conf.set("spark.serializer" , "org.apache.spark.serializer.KryoSerializer")
        conf.set("spark.local.dir" , "D:/temp")


        val sc = new SparkContext(conf)
        class Dog extends Serializable
        val d1 = new Dog()
        val bc = sc.broadcast(d1)
        bc.value

        val rdd1 = sc.makeRDD(1 to 10)
        rdd1.map(e=>{bc.value ; e}).collect()

    }
}
