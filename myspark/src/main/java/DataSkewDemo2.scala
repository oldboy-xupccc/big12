import org.apache.spark.{HashPartitioner, Partitioner, SparkConf, SparkContext}

import scala.util.Random

/**
  * 数据倾斜
  */
object DataSkewDemo2 {
    def main(args: Array[String]): Unit = {
        val conf = new SparkConf()
        conf.setAppName("wcDemo")
        conf.setMaster("local[4]")
        val sc = new SparkContext(conf)

        val rdd1 = sc.textFile("file:///d:/mr/word.txt")
        val rdd11 = rdd1.flatMap(_.split(" ")).map(_ + "" + Random.nextInt(3))

        val rdd2 = rdd11.map((_,1))

        //自定义随机分区类
        class MyRandPartitioner(val n:Int) extends Partitioner{

            override def numPartitions: Int = n

            override def getPartition(key: Any): Int = {
                Random.nextInt(n)
            }
        }

        val p = new MyRandPartitioner(4)

        val rdd3 = rdd2.reduceByKey(p , (a,b)=>{a + b})

        val rdd4 = rdd3.reduceByKey(new HashPartitioner(3) , _+_ )
        rdd4.collect().foreach(println)
        while(true){
            Thread.sleep(10000)
        }

    }
}
