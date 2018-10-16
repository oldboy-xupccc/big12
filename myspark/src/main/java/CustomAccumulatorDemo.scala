import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.util.AccumulatorV2

import scala.collection.mutable.{Map => MMap}

/**
  * 自定义累加器
  */
object CustomAccumulatorDemo {
    //自定年度气温累加器
    class YearTempAccumualtor extends AccumulatorV2[(Int,Int) , MMap[Int,(Int,Int)]]{

        //定义累计器的值

        var _value = MMap[Int, (Int, Int)]()

        override def isZero: Boolean = {
            _value.isEmpty
        }

        //复制累加器
        override def copy(): AccumulatorV2[(Int, Int), MMap[Int, (Int, Int)]] = {
            val newacc = new YearTempAccumualtor
            newacc._value = _value
            newacc
        }


        override def reset(): Unit = {
            _value = MMap[Int, (Int, Int)]()
        }

        //累加方法
        override def add(v: (Int, Int)): Unit = {
            val year = v._1
            val temp:Int = v._2
            val r = _value.get(year)
            if(r.isEmpty){
                _value.put(year ,(temp,temp))
            }else{
                val (mx,mn) = r.get
                import scala.math._
                _value.put(year, (max(mx,temp),min(mn,temp)))
            }
        }

        override def merge(other: AccumulatorV2[(Int, Int), MMap[Int, (Int, Int)]]): Unit = {
            val otherMap = other.value
            for((k,v) <- otherMap){
                val otherYear = k ;
                val otherMax = v._1
                val otherMin = v._2
                val vv = _value.get(otherMax)
                if(vv.isEmpty){
                    _value.put(k,v)
                }
                else{
                    import scala.math._
                    _value.put(otherYear ,(max(vv.get._1 , v._1),min(vv.get._2,v._2)) )
                }
            }
        }

        override def value: MMap[Int, (Int, Int)] = {
            _value
        }
    }
    def main(args: Array[String]): Unit = {
        val conf = new SparkConf()
        conf.setAppName("myacc")
        conf.setMaster("local")
        val sc =new  SparkContext(conf)
        val ytAcc = new YearTempAccumualtor()
        sc.register(ytAcc, "YearTempAggAcc")
        val rdd1 = sc.textFile("file:///d:/mr/temp3.dat")
        val rdd2 = rdd1.map(line=>{
            val arr = line.split(" ")
            (arr(0).toInt , arr(1).toInt)
        })

        rdd2.map(t=>{ytAcc.add(t)}).collect()
        val values = ytAcc.value
        for(t <- values){
            println(t)
        }
    }

}
