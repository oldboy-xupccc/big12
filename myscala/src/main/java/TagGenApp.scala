import com.oldboy.scala.java.TagUtil

import scala.io.Source

/**
  * 标签生成
  */
object TagGenApp {
    def main(args: Array[String]): Unit = {
        val f = Source.fromFile("D:\\temptags.txt")
        val lines = f.getLines().toList

        //1. 提取商家评论
        val list1 = lines.map(line=>{
            val arr = line.split("\t")
            //商家id
            val busid = arr(0)
            //
            val tags = TagUtil.extractTag(arr(1))
            (busid , tags)

        })

        //2. filter掉空评论
        val list2 = list1.filter(e=>{
              e._2 != null && e._2.size() > 0
        })

        //3. 压扁 (busid ,[,,,])=> (busid,tag1),(busid,tag2)
        val list3 = list2.flatMap(t=>{
            var l:List[(String,String)] = Nil
            //需要是利用隐式转换，将java集合转换成scalabuffer，才可使用scala for循环
            import scala.collection.JavaConversions.asScalaBuffer
            for(tag <- t._2){
                l = (t._1 , tag.asInstanceOf[String]) :: l
            }
            l
        })

        //4. 分组统计每个(busid, tag)的个数
        val map1 = list3.groupBy(t=>t)

        //5. 统计个数
        val map2 = map1.mapValues(v=>{
            v.size
        })
        //6. 变换map成为list
        val list4 = map2.toList

        //7. 重新组合((busid,tag),num)=>(busid , (tag,num))
        val list5 = list4.map(t=>{
            (t._1._1,(t._1._2 , t._2))
        })

        //8. 分组，将同一busid的数据聚合在一起
        val map3 = list5.groupBy(t=>{
            t._1
        })

        //9. 变换values，只保留(tag,num)
        val map4 = map3.mapValues(list=>{
            var l:List[(String,Int)] = Nil
            for(t <- list){
                l = t._2 :: l
            }
            l
        })

        //10. 排序，商家内按照评论数降排
        val map5 = map4.mapValues(list=>{
            //按照评论数降排序
            val list22 = list.sortBy(t=>{-t._2})
            //提取前3个元素
            list22.take(5)
        })

        //11. 商家间降排序,map不能排序，需要先转成list
        val list6 = map5.toList

        //12. 对元素按照第一个评论个数降排序
        val list7 = list6.sortBy(t=>{- t._2(0)._2})

        list7.foreach(println(_))
    }
}
