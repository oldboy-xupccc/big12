import java.util

import com.oldboy.spark.util.TagUtil
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
  * 标签生成
  */
object TaggenScala1 {
    def main(args: Array[String]): Unit = {
        val conf = new SparkConf()
        conf.setAppName("Taggen")
        conf.setMaster("local")
        val sc = new SparkContext(conf)

        //1. 加载文件
        val rdd1:RDD[String] = sc.textFile("file:///d:/mr/temptags.txt")

        //2. 解析每行的json数据成为集合
        val rdd2:RDD[(String, util.List[String])] = rdd1.map(line=>{
            val arr:Array[String] = line.split("\t")
            //商家id
            val busid:String = arr(0)
            //json
            val json:String = arr(1)
            val list:java.util.List[String] = TagUtil.extractTag(json)
            Tuple2[String,java.util.List[String]](busid , list)
        })

        //3. 过滤空集合
        val rdd3: RDD[(String, util.List[String])] =rdd2.filter((t: Tuple2[String, java.util.List[String]])=>{
            !t._2.isEmpty
        })

        //4. 将值压扁
        val rdd4:RDD[(String, String)] = rdd3.flatMapValues((list:java.util.List[String])=>{
            //导入隐式转换
            import scala.collection.JavaConversions._
            list
        })

        //5. 滤除数字的tag
        val rdd5: RDD[(String, String)] = rdd4.filter((t:Tuple2[String,String])=>{
            try{
                //
                Integer.parseInt(t._2)
                false
            }
            catch {
                case _  => true
            }
        })

        //6. 标1成对
        val rdd6:RDD[Tuple2[Tuple2[String,String],Int]] = rdd5.map((t:Tuple2[String,String])=>{
            Tuple2[Tuple2[String,String] , Int](t,1)
        })

        //7. 聚合
        val rdd7: RDD[Tuple2[Tuple2[String, String], Int]] = rdd6.reduceByKey((a:Int, b:Int)=>{
            a + b
        })

        //8. chongzu重组
        val rdd8:RDD[Tuple2[String,List[Tuple2[String,Int]]]] = rdd7.map((t:Tuple2[Tuple2[String,String],Int])=>{
            Tuple2[String,List[Tuple2[String,Int]]](t._1._1,Tuple2[String,Int](t._1._2 , t._2)::Nil)
        })

        //9. reduceByKey
        val rdd9: RDD[Tuple2[String, List[Tuple2[String, Int]]]] =rdd8.reduceByKey((a:List[Tuple2[String,Int]], b: List[Tuple2[String, Int]])=>{
            a ::: b
        })

        //10. 分组内排序
        val rdd10: RDD[Tuple2[String, List[Tuple2[String, Int]]]] =rdd9.mapValues((list:List[Tuple2[String,Int]])=>{
            val list2:List[Tuple2[String,Int]] = list.sortBy((t:Tuple2[String,Int])=>{
                -t._2
            })
            list2.take(5)
        })
        //11. 商家间排序
        val rdd11: RDD[Tuple2[String, List[Tuple2[String, Int]]]] = rdd10.sortBy((t:Tuple2[String,List[Tuple2[String,Int]]])=>{
            t._2(0)._2

        } ,false)
        rdd11.collect().foreach(println)
    }
}





















