import java.sql.DriverManager

import org.apache.spark.{SparkConf, SparkContext}

/**
  * 排序
  */
object SortByKeyDemo {
    def main(args: Array[String]): Unit = {
        val conf = new SparkConf()
        conf.setAppName("wcDemo")
        conf.setMaster("local[4]")
        val sc = new SparkContext(conf)

        val rdd1 = sc.makeRDD(Array(3,4,6,2,3,1,9,8,4) , 2)
        //sortBy
        var rdd2 = rdd1.map((_,1)).sortByKey(true)

        //
        rdd2.mapPartitionsWithIndex((idx,it)=>{
            for(e <- it){
                println( idx + " : " + e)
            }
            it
        }).collect()
    }
}
