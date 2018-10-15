import org.apache.spark.{HashPartitioner, SparkConf, SparkContext}

/**
  * Shuffle行为考察
  */
object UnsafeShuffleWriterScala {
    def main(args: Array[String]): Unit = {

        //创建spark配置对象
        val conf = new SparkConf()
        conf.setAppName("WCScala")
        conf.setMaster("local[1]")
        conf.set("spark.shuffle.sort.bypassMergeThreshold" , "1")
        conf.set("spark.serializer" , "org.apache.spark.serializer.KryoSerializer")
        //达到2条记录就溢出到磁盘
        conf.set("spark.shuffle.spill.numElementsForceSpillThreshold" , "2")
        conf.set("spark.local.dir" , "D:/temp")

        val sc = new SparkContext(conf)
        val rdd1 = sc.makeRDD(Array("hello"))
        val rdd2 = rdd1.flatMap(line => {
            line.split(" ")
        })

        val rdd3 = rdd2.map(word => {
            (word , 1)
        })

        val rdd4 = rdd3.sortByKey(true , 2)
        rdd4.collect()
    }
}
