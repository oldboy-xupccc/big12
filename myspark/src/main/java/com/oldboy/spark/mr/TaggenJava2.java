package com.oldboy.spark.mr;

import com.oldboy.spark.util.TagUtil;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Administrator on 2018/10/9.
 */
public class TaggenJava2 {

	public static void main(String[] args) throws Exception{
		if (args == null || args.length == 0) {
			throw new Exception("需要指定文件路径") ;
		}
		SparkConf conf = new SparkConf();
		conf.setAppName("tagTenJava");
//		conf.setMaster("local");

		JavaSparkContext sc = new JavaSparkContext(conf);
		//1. 加载文件
		JavaRDD<String> rdd1 = sc.textFile(args[0]);

		//2. 切割
		JavaPairRDD<String,List<String>> rdd2 = rdd1.mapToPair(new PairFunction<String, String, List<String>>() {
			public Tuple2<String, List<String>> call(String s) throws Exception {
				String[] arr = s.split("\t");
				String busid = arr[0] ;
				List<String> tags = TagUtil.extractTag(arr[1]) ;
				return new Tuple2<String, List<String>>(busid,tags);
			}
		}) ;

		//3. 过滤空集合
		JavaPairRDD<String, List<String>> rdd3 = rdd2.filter(new Function<Tuple2<String, List<String>>, Boolean>() {
			public Boolean call(Tuple2<String, List<String>> t) throws Exception {
				return !t._2().isEmpty() ;
			}
		}) ;

		//4. 压扁值
		JavaPairRDD<String,String> rdd4 = rdd3.flatMapValues(new Function<List<String>, Iterable<String>>() {
			public Iterable<String > call(List<String> v1) throws Exception {
				return v1;
			}
		}) ;

		//5. 过滤掉数字标签
		JavaPairRDD<String, String> rdd5 = rdd4.filter(new Function<Tuple2<String, String>, Boolean>() {
			public Boolean call(Tuple2<String, String> t) throws Exception {
				try {
					Integer.parseInt(t._2());
					return false ;
				} catch (Exception e) {
					return true;
				}
			}
		}) ;

		//6. 重组，标1成对
		JavaPairRDD<Tuple2<String, String>, Integer> rdd6 = rdd5.mapToPair(new PairFunction<Tuple2<String,String>, Tuple2<String,String>, Integer>() {
			public Tuple2<Tuple2<String, String>, Integer> call(Tuple2<String, String> t) throws Exception {
				return new Tuple2<Tuple2<String, String>, Integer>(t, 1) ;
			}
		}) ;

		//7. 聚合值
		JavaPairRDD<Tuple2<String, String>, Integer> rdd7 =rdd6.reduceByKey(new Function2<Integer, Integer, Integer>() {
			public Integer call(Integer v1, Integer v2) throws Exception {
				return v1 + v2;
			}
		}) ;

		//8. 重组组员(busid ,(tag,num))
		JavaPairRDD<String,List<Tuple2<String,Integer>>> rdd8=rdd7.mapToPair(new PairFunction<Tuple2<Tuple2<String,String>,Integer>, String, List<Tuple2<String,Integer>>>() {
			public Tuple2<String, List<Tuple2<String, Integer>>> call(Tuple2<Tuple2<String, String>, Integer> t) throws Exception {
				List<Tuple2<String,Integer>> list =new ArrayList<Tuple2<String, Integer>>() ;
				list.add(new Tuple2<String, Integer>(t._1._2, t._2)) ;
				return new Tuple2<String, List<Tuple2<String, Integer>>>(t._1._1,list);
			}
		}) ;

		//9. 聚合
		JavaPairRDD<String, List<Tuple2<String, Integer>>> rdd9 =rdd8.reduceByKey(
				new Function2<List<Tuple2<String, Integer>>, List<Tuple2<String, Integer>>, List<Tuple2<String, Integer>>>() {
					public List<Tuple2<String, Integer>> call(List<Tuple2<String, Integer>> v1, List<Tuple2<String, Integer>> v2) throws Exception {
						v1.addAll(v2);
						return v1 ;
					}
				}) ;
		
		//10. 商家内排序
		JavaPairRDD<String, List<Tuple2<String, Integer>>> rdd10 = rdd9.mapValues(new Function<List<Tuple2<String,Integer>>, List<Tuple2<String, Integer>>>() {
			public List<Tuple2<String, Integer>> call(List<Tuple2<String, Integer>> v1) throws Exception {
				v1.sort(new Comparator<Tuple2<String, Integer>>() {
					public int compare(Tuple2<String, Integer> o1, Tuple2<String, Integer> o2) {
						return - (o1._2 - o2._2);
					}
				});
				List<Tuple2<String,Integer>> newList = new ArrayList<Tuple2<String, Integer>>() ;
				//sublist方法返回的集合不能串行化
				newList.addAll(v1.subList(0, v1.size() > 5 ? 5 : v1.size())) ;
				return newList ;
			}
		}) ;

		//11. 变换pairRDD到普通RDD，否则没有sortBy方法
		JavaRDD<Tuple2<String,List<Tuple2<String,Integer>>>> rdd11 = rdd10.map(new Function<Tuple2<String,List<Tuple2<String,Integer>>>, Tuple2<String, List<Tuple2<String, Integer>>>>() {
			public Tuple2<String, List<Tuple2<String, Integer>>> call(Tuple2<String, List<Tuple2<String, Integer>>> t) throws Exception {
				return t ;
			}
		}) ;

		//12. 商家间排序
		JavaRDD<Tuple2<String, List<Tuple2<String, Integer>>>> rdd12 = rdd11.sortBy(new Function<Tuple2<String,List<Tuple2<String,Integer>>>, Integer>() {
			public Integer call(Tuple2<String, List<Tuple2<String, Integer>>> t) throws Exception {
				return - t._2.get(0)._2;
			}
		}, true,2) ;

		List  list = rdd12.collect();
		for(Object o: list){
			System.out.println(o);
		}
	}
}
