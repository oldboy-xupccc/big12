import java.sql.DriverManager

import org.apache.spark.{SparkConf, SparkContext}

/**
  */
object MapPartitionDemo {
    def main(args: Array[String]): Unit = {
        val conf = new SparkConf()
        conf.setAppName("wcDemo")
        conf.setMaster("local[4]")
        val sc = new SparkContext(conf)

        val rdd1 = sc.textFile("file:///d:/mr/word.txt" , 2)
        val rdd2 = rdd1.flatMap(_.split(" ")).map((_,1)).reduceByKey(_+_)
//        val rdd3 = rdd2.map(t=>{
//            val word = t._1
//            val cnt = t._2
//            Class.forName("com.mysql.jdbc.Driver")
//            val url = "jdbc:mysql://localhost:3306/big12" ;
//            val user = "root"
//            val pass = "root"
//            val conn = DriverManager.getConnection(url , user , pass)
//            println("new Conn : " + conn)
//            conn.setAutoCommit(false)
//            val ppst = conn.prepareStatement("insert into wc(word , cnt) values(?,?)")
//            ppst.setString(1 , word)
//            ppst.setInt(2 , cnt)
//            ppst.executeUpdate()
//            conn.commit()
//        })
//        rdd3.collect();
        rdd2.mapPartitions(it=>{
            Class.forName("com.mysql.jdbc.Driver")
            val url = "jdbc:mysql://localhost:3306/big12" ;
            val user = "root"
            val pass = "root"
            val conn = DriverManager.getConnection(url , user , pass)
            println("new Conn : " + conn)
            val ppst = conn.prepareStatement("insert into wc(word , cnt) values(?,?)")
            conn.setAutoCommit(false)
            for((k,v)<- it){
                ppst.setString(1, k)
                ppst.setInt(2 , v)
                ppst.executeUpdate()
            }
            conn.commit()
            it
        }).collect()
    }
}
