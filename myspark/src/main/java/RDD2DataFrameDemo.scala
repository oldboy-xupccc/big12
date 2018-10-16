import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

/**
  * rdd到数据框转换
  */
object RDD2DataFrameDemo {
    def main(args: Array[String]): Unit = {
        val conf = new SparkConf()
        conf.setAppName("sql")
        conf.setMaster("local")
        val spark = SparkSession.builder().config(conf).enableHiveSupport().getOrCreate()
        val rdd1 = spark.sparkContext.textFile("file:///d:/mr/temp3.dat")
        val rdd2 = rdd1.map(e=>{
            val arr = e.split(" ")
            (arr(0).toInt , arr(1).toInt)
        })

        import spark.implicits._
        val df = rdd2.toDF("year" , "temp")
        df.createOrReplaceTempView("temps")
        spark.sql("select year ,max(temp) ,min(temp) from temps group by year order by year asc").show(10000 , false)

    }
}
