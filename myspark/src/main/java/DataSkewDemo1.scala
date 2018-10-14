import org.apache.spark.{SparkConf, SparkContext}

import scala.util.Random

/**
  * 数据倾斜
  */
object DataSkewDemo1 {
    def main(args: Array[String]): Unit = {
        val conf = new SparkConf()
        conf.setAppName("wcDemo")
        conf.setMaster("local[4]")
        val sc = new SparkContext(conf)

        val rdd1 = sc.textFile("file:///d:/mr/word.txt")
        val rdd2 = rdd1.flatMap(_.split(" ")).map((_,1))
        //在key增加随机数
        val rdd3 = rdd2.map(t=>(t._1 + "_" + Random.nextInt(3) , t._2))
        val rdd4 = rdd3.reduceByKey(_+_)
        //去掉后缀
        val rdd5 = rdd4.map(t=>(t._1.substring(0,t._1.lastIndexOf("_")) ,t._2))

        val rdd6 = rdd5.reduceByKey(_+_)
        rdd6.collect().foreach(println)
        while(true){
            Thread.sleep(10000)
        }

    }
}
