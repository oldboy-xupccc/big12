import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by Administrator on 2018/10/8.
  */
object WordCountScala {
    def main(args: Array[String]): Unit = {
        //创建spark配置对象
        val conf = new SparkConf()
        conf.setAppName("WCScala")
        conf.setMaster("local")

        //创建上下文
        val sc = new SparkContext(conf)
//
//        //加载文档
//        val rdd1 = sc.textFile("file:///d:/mr/word.txt")
//
//        //压扁
//        val rdd2 = rdd1.flatMap(_.split(" "))
//
//        //标1成对
//        val rdd3 = rdd2.map((_,1))
//
//        //聚合
//        val rdd4 = rdd3.reduceByKey(_+_)
//
//        val arr = rdd4.collect()
//
//        arr.foreach(println)

        //链式编程
        sc.textFile("file:///d:/mr/word.txt").flatMap(_.split(" ")).map((_,1)).reduceByKey(_+_).collect().foreach(println)
    }
}
