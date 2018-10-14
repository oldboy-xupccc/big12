import org.apache.spark.{SparkConf, SparkContext}
import org.apache.hadoop.io.{IntWritable, Text} ;

/**
  * 读取序列文件
  */
object ReadSeqFileDemo {
    def main(args: Array[String]): Unit = {
        val conf = new SparkConf()
        conf.setAppName("wcDemo")
        conf.setMaster("local[4]")
        val sc = new SparkContext(conf)
        val rdd1 = sc.sequenceFile("file:///d:/mr/outseq" , classOf[Text] , classOf[IntWritable])
        rdd1.collect().foreach(println)
    }

}
