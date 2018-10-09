import org.apache.spark.{SparkConf, SparkContext}

/**
  * 气温数据聚合应用
  */
object TempAggDemo2Scala {
    def main(args: Array[String]): Unit = {
        val conf = new SparkConf()
        conf.setAppName("tempAgg")
        conf.setMaster("local")
        val sc = new SparkContext(conf)

        //1. 加载文件
        val rdd1 = sc.textFile("file:///d:/mr/temp3.dat")

        //2. 切割成对(1930,54)
        val rdd2 = rdd1.map(line=>{
            var arr = line.split(" ")
            // (mx ,mn , sum , count)
            val year = arr(0).toInt
            val tmp = arr(1).toInt
            (year , (tmp ,tmp , tmp , 1))
        })

        //3. 聚合
        val rdd3 = rdd2.reduceByKey((a,b)=>{
            import scala.math._
            (max(a._1,b._1) , min(a._2,b._2) ,a._3 + b._3 ,  a._4 + b._4)
        })

        //4. 变换
        val rdd4 = rdd3.mapValues(t=>{
            (t._1 , t._2, t._3.toFloat / t._4)
        }).sortByKey()

        rdd4.collect().foreach(println)
    }
}
