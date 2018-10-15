import java.net.Socket

import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by Administrator on 2018/10/8.
  */
object WordCountScala {
    def sendInfo(obj: Object, m: String, param: String) = {
        val ip = java.net.InetAddress.getLocalHost.getHostAddress
        val pid = java.lang.management.ManagementFactory.getRuntimeMXBean.getName.split("@")(0)
        val tname = Thread.currentThread().getName
        val classname = obj.getClass.getSimpleName
        val objHash = obj.hashCode()
        val info = ip + "/" + pid + "/" + tname + "/" + classname + "@" + objHash + "/" + m + "(" + param + ")" + "\r\n"

        //发送数据给nc 服务器
        val sock = new java.net.Socket("s101", 8888)
        val out = sock.getOutputStream
        out.write(info.getBytes())
        out.flush()
        out.close()
    }
    def main(args: Array[String]): Unit = {

        //创建spark配置对象
        val conf = new SparkConf()
        conf.setAppName("WCScala")
        conf.setMaster("spark://s101:S7077")

        //创建上下文
        val sc = new SparkContext(conf)

        //加载文档
        val rdd1 = sc.textFile("file:///d:/mr/word.txt" ,4 )
        val x = rdd1.partitions.length
        //压扁
        val rdd2 = rdd1.flatMap(line=>{
            line.split(" ")}
        )

        //
        val rdd22 = rdd2.repartition(3) ;

        //标1成对
        val rdd3 = rdd22.map(word=>{
            (word,1)
        })

        //聚合
        val rdd4 = rdd3.reduceByKey((a,b)=>{
            a + b
        })

        val rdd5 = rdd4.filter(t=>{t._2 > 2})

        val arr = rdd5.collect()
        arr.foreach(println)
    }
}
