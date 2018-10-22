package com.oldboy.spark.streaming;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.Seconds;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.dstream.DStream;
import scala.Tuple2;

import java.util.Arrays;
import java.util.Iterator;

/**
 */
public class WordCountStreamingJava1 {
	public static void main(String[] args) throws InterruptedException {
		SparkConf conf = new SparkConf() ;
		conf.setAppName("streaming") ;
		conf.setMaster("local[*]") ;
		JavaStreamingContext sc = new JavaStreamingContext(conf , Durations.seconds(2)) ;

		JavaDStream<String> ds1 = sc.socketTextStream("192.168.231.101" , 8888) ;
		//压扁
		JavaDStream<String> ds2 = ds1.flatMap(new FlatMapFunction<String, String>() {

			public Iterator<String> call(String s) throws Exception {
				return Arrays.asList(s.split(" ")).iterator();
			}
		}) ;

		//变换成对
		JavaPairDStream<String,Integer> ds3 = ds2.mapToPair(new PairFunction<String, String, Integer>() {
			public Tuple2<String, Integer> call(String s) throws Exception {
				return new Tuple2<String, Integer>(s , 1);
			}
		}) ;

		//聚合
		JavaPairDStream<String,Integer> ds4 = ds3.reduceByKey(new Function2<Integer, Integer, Integer>() {
			public Integer call(Integer v1, Integer v2) throws Exception {
				return v1 + v2;
			}
		}) ;
		ds4.print();

		sc.start();
		sc.awaitTermination();


	}
}
