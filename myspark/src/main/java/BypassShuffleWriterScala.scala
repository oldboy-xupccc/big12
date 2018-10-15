import org.apache.spark.{HashPartitioner, SparkConf, SparkContext}

/**
  * Shuffle行为考察
  */
object BypassShuffleWriterScala {
    def main(args: Array[String]): Unit = {

        //创建spark配置对象
        val conf = new SparkConf()
        conf.setAppName("WCScala")
        conf.setMaster("local[4]")
        conf.set("spark.shuffle.sort.bypassMergeThreshold" , "200")
        conf.set("spark.serializer" , "org.apache.spark.serializer.KryoSerializer")
        conf.set("spark.local.dir" , "D:/temp")

        val sc = new SparkContext(conf)
        val rdd1 = sc.textFile("file:///d:/mr/word.txt" , 3)
        val rdd2 = rdd1.flatMap(line => {
            line.split(" ")
        })

        val rdd3 = rdd2.map(word => {
            (word , 1)
        })

        val rdd4 = rdd3.combineByKeyWithClassTag((v:Int)=>v , (v:Int,w:Int)=> {v + w} , (v: Int, w: Int) => {
            v + w
        },new HashPartitioner(2) , mapSideCombine = false)

        rdd4.collect()

    }
}
