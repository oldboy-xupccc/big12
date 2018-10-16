import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

/**
  * Spark SQL
  */
object SparkSQLDemo {
    def main(args: Array[String]): Unit = {
        val conf = new SparkConf()
        conf.setAppName("sql")
        conf.setMaster("spark://s101:7077")
        val spark = SparkSession.builder().config(conf).enableHiveSupport().getOrCreate()
        spark.sql("use big12").show()
//        val arr = spark.sql("select * from orders").collect()
//        for(e <- arr){
//            println(e)
//        }
        val df = spark.sql("select * from orders")
        val rdd = df.toJavaRDD
        val it= rdd.collect().iterator()
        while(it.hasNext){
            println(it.next())
        }
    }

}
