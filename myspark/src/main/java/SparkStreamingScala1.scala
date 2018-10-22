import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
  * 流计算
  */
object SparkStreamingScala1 {
    def main(args: Array[String]): Unit = {
        val conf = new SparkConf()
        conf.setAppName("Streaming")
        //至少2以上
        conf.setMaster("local[1]")

        //创建上下文
        val sc = new StreamingContext(conf , Seconds(1))

        //常见socker文本流
        val lines = sc.socketTextStream("192.168.231.101", 8888)
        val words = lines.flatMap(_.split(" "))
        val pair = words.map((_,1))
        val rdd = pair.reduceByKey(_+_)
        rdd.foreachRDD(rdd=>{
            print(rdd.partitions.length)
            println(rdd.collect())
        })
        //rdd.print()

        sc.start()
        sc.awaitTermination()
    }
}
