import org.apache.hadoop.io.compress.GzipCodec
import org.apache.spark.{SparkConf, SparkContext}

/**
  * 保存文件
  */
object SaveTextFileActionDemo {
    def main(args: Array[String]): Unit = {

        //创建spark配置对象
        val conf = new SparkConf()
        conf.setAppName("WCScala")
        conf.setMaster("local[4]")

        //创建上下文
        val sc = new SparkContext(conf)

        val rdd1 = sc.textFile("file:///d:/mr/word.txt" , 5).flatMap(_.split(" ")).map((_,1)).reduceByKey(_+_)
        //rdd1.saveAsTextFile("file:///d:/mr/out",classOf[GzipCodec])
        //rdd1.saveAsSequenceFile("file:///d:/mr/outseq" )
        rdd1.saveAsSequenceFile("file:///d:/mr/outseq2" , Some(classOf[GzipCodec]))
    }
}
