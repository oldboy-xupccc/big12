import java.util

import com.alibaba.fastjson.JSON
import com.oldboy.spark.util.TagUtil
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
  * 标签生成
  */
object TaggenScala2 {
    def main(args: Array[String]): Unit = {
        //定义函数，抽取标签列表
        def extractTag(json:String) = {
            import com.alibaba.fastjson.JSON
            var list:List[String] = Nil
            //将字符串解析成json对象
            val obj = JSON.parseObject(json)
            val arr = obj.getJSONArray("extInfoList")
            if (arr != null && arr.size > 0) {
                //得到数组的第一个json对象
                val firstObj = arr.getJSONObject(0)
                val values = firstObj.getJSONArray("values")
                if (values != null && values.size > 0) {
                    var i = 0
                    while (i < values.size) {
                        val tag = values.getString(i)
                        list = tag :: list
                        i += 1;
                    }
                }
            }
            list
        }
        val conf = new SparkConf()
        conf.setAppName("tagGenScala")
        conf.setMaster("local")
        val sc = new SparkContext(conf)

        //1. 加载文件
        val rdd1:RDD[String] = sc.textFile("file:///d:/temptags.txt")

        //2. 解析每行的json数据成为集合
        val rdd2 = rdd1.map(line=>{
            val arr = line.split("\t")
            //商家id
            val busid = arr(0)
            //json
            val json= arr(1)
            val list= extractTag(json)
            (busid , list)
        })

        //3. 过滤空集合
        val rdd3 =rdd2.filter(t=>{
            !t._2.isEmpty
        })

        //4. 将值压扁
        val rdd4 = rdd3.flatMapValues(list=>{
            list
        })

        //5. 滤除数字的tag
        val rdd5 = rdd4.filter(t=>{
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
        val rdd6 = rdd5.map(t=>{
            (t,1)
        })

        //7. 聚合
        val rdd7 = rdd6.reduceByKey(_+_)

        //8. chongzu重组
        val rdd8 = rdd7.map(t=>{
            (t._1._1,(t._1._2 , t._2)::Nil)
        })

        //9. reduceByKey
        val rdd9 =rdd8.reduceByKey(_ ::: _)

        //10. 分组内排序
        val rdd10=rdd9.mapValues(list=>{
            list.sortBy(t=>{
                -t._2
            }).take(5)
        })
        //11. 商家间排序
        val rdd11= rdd10.sortBy(t=>{
            t._2(0)._2
        } ,false)

        rdd11.collect()

        rdd11.count()
    }
}
