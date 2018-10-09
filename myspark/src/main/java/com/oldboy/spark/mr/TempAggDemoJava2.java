package com.oldboy.spark.mr;

import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import scala.Tuple2;
import scala.Tuple3;
import scala.Tuple4;

import java.util.List;

/**
 * Created by Administrator on 2018/10/9.
 */
public class TempAggDemoJava2 {
	public static void main(String[] args) {
		SparkConf conf = new SparkConf();
		conf.setAppName("tempAgg") ;
		conf.setMaster("local") ;

		JavaSparkContext sc = new JavaSparkContext(conf) ;
		//1. 加载文件
		JavaRDD<String> rdd1= sc.textFile("file:///d:/mr/temp3.dat") ;

		//2. 变换(1903,(32,23,23,1))
		JavaPairRDD<Integer, Tuple4<Integer, Integer, Integer, Integer>> rdd2 = rdd1.mapToPair(new PairFunction<String, Integer, Tuple4<Integer,Integer,Integer,Integer>>() {
			public Tuple2<Integer, Tuple4<Integer, Integer, Integer, Integer>> call(String s) throws Exception {
				String[] arr = s.split(" ") ;
				int year = Integer.parseInt(arr[0]) ;
				int tmp = Integer.parseInt(arr[1]) ;

				Tuple4<Integer,Integer,Integer,Integer> v = new Tuple4<Integer, Integer, Integer, Integer>(tmp,tmp,tmp,1) ;

				return new Tuple2<Integer, Tuple4<Integer, Integer, Integer, Integer>>(year , v);
			}
		}) ;

		//3. 聚合
		JavaPairRDD<Integer, Tuple4<Integer, Integer, Integer, Integer>> rdd3 = rdd2.reduceByKey(
				new Function2<Tuple4<Integer, Integer, Integer, Integer>, Tuple4<Integer, Integer, Integer, Integer>, Tuple4<Integer, Integer, Integer, Integer>>() {
					public Tuple4<Integer, Integer, Integer, Integer> call(Tuple4<Integer, Integer, Integer, Integer> v1, Tuple4<Integer, Integer, Integer, Integer> v2) throws Exception {
						int mx = Math.max(v1._1(),v2._1()) ;
						int mn = Math.max(v1._2(),v2._2()) ;
						int sum = v1._3() + v2._3() ;
						int count = v1._4() + v2._4() ;

						return new Tuple4<Integer, Integer, Integer, Integer>(mx ,mn , sum , count);
					}
				}) ;

		//4. map取出avg
		JavaPairRDD<Integer, Tuple3<Integer, Integer, Float>> rdd4 = rdd3.mapValues(new Function<Tuple4<Integer,Integer,Integer,Integer>, Tuple3<Integer,Integer, Float>>() {
			public Tuple3<Integer, Integer, Float> call(Tuple4<Integer, Integer, Integer, Integer> v1) throws Exception {
				return new Tuple3<Integer, Integer, Float>(v1._1(),v1._2(),(float)v1._3() / v1._4());
			}
		}) ;

		//5. 排序
		JavaPairRDD<Integer, Tuple3<Integer, Integer, Float>> rdd5 = rdd4.sortByKey() ;

		//6. 列表
		List<Tuple2<Integer, Tuple3<Integer, Integer, Float>>> list = rdd5.collect() ;
		for(Tuple2<Integer, Tuple3<Integer, Integer, Float>> t : list){
			System.out.println(t);
		}
	}
}
