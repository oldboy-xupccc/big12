import org.apache.spark.rdd.RDD
import org.apache.spark.{Partitioner, SparkConf, SparkContext}

/**
  * 再分区并在分区内排序
  */
object RepartitionAndSortWithPartitionDemo {
    def main(args: Array[String]): Unit = {
        val conf = new SparkConf()
        conf.setAppName("wcDemo")
        conf.setMaster("local[4]")
        val sc = new SparkContext(conf)

        //
        val rdd1 = sc.makeRDD(Array(3,2,1,6,8,4,5,5,9,2,6,1)).map((_,1))

        class MyPartitioner(val n:Int) extends Partitioner{
            override def numPartitions: Int = n

            override def getPartition(key: Any): Int = {
                val k = key.asInstanceOf[Int]
                k % n
            }
        }

        //部分排序，分区内排序
        val p = new MyPartitioner(3)
        val rdd2 = rdd1.repartitionAndSortWithinPartitions(p)
        val rdd3 = rdd2.mapPartitionsWithIndex((idx,it)=>{
            for(e <- it){
                println(idx + " : " + e)
            }
            it
        })
        rdd3.collect()
    }
}
