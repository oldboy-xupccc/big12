import org.apache.spark.{HashPartitioner, SparkConf, SparkContext}

/**
  * 合成函数基本类使用
  */
object CombineByKeyWithClassTagDemo {
    def main(args: Array[String]): Unit = {
        val conf = new SparkConf()
        conf.setAppName("wcDemo")
        conf.setMaster("local[4]")
        val sc = new SparkContext(conf)
        val rdd1 = sc.textFile("file:///d:/mr/word.txt" , 3)
        val rdd2 = rdd1.flatMap(_.split(" ")).mapPartitionsWithIndex((idx,it)=>{
            var list:List[(String,String)] = Nil
            for(e <- it){
                list = (e, e+"_"+idx) :: list
            }
            list.iterator
        })
        rdd2.collect().foreach(println)
        println("=======================")

        //合成器函数
        def createCombiner(a:String):String = "[" + a + "]"

        //合并值函数
        def mergeValues(a:String,b:String) = {
            a + b + ","
        }

        def mergeCombiner(a:String,b:String) = {
            a + "$" + b
        }
        val rdd3 = rdd2.combineByKeyWithClassTag(createCombiner _ , mergeValues _ , mergeCombiner _ ,new HashPartitioner(4) ,false )
        rdd3.collect().foreach(println)

    }

}
