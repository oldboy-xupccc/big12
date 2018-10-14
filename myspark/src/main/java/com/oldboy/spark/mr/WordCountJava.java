package com.oldboy.spark.mr;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFlatMapFunction;
import org.apache.spark.api.java.function.PairFunction;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 *
 */
public class WordCountJava {
	public static void main(String[] args) {
		SparkConf conf = new SparkConf() ;
		conf.setAppName("wcJava") ;
		conf.setMaster("local") ;
		JavaSparkContext sc = new JavaSparkContext(conf) ;

		//加载文件
		JavaRDD<String> rdd1 = sc.textFile("file:///d:/mr/word.txt") ;

		//压扁
		JavaRDD<String> rdd2 = rdd1.flatMap(new FlatMapFunction<String, String>() {
			public Iterator<String> call(String s) throws Exception {
				String[] arr = s.split(" ") ;
				return Arrays.asList(arr).iterator() ;
			}
		}) ;

		//标1成对
		JavaPairRDD<String,Integer> rdd3 = rdd2.mapToPair(new PairFunction<String, String, Integer>() {
			public Tuple2<String, Integer> call(String s) throws Exception {
				return new Tuple2<String, Integer>(s , 1);
			}
		}) ;
		//按照key聚合
		JavaPairRDD<String, Integer> rdd4 = rdd3.reduceByKey(new Function2<Integer, Integer, Integer>() {
			public Integer call(Integer v1, Integer v2) throws Exception {
				return v1 + v2;
			}
		}) ;

		//收集
		List<Tuple2<String,Integer>> coll = rdd4.collect();

		for(Tuple2<String,Integer> t : coll){
			System.out.println(t);
		}
	}
}
