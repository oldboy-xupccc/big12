package com.oldboy.spark.mr;

import org.apache.spark.SparkConf;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

/**
 * Created by Administrator on 2018/10/16.
 */
public class SparkSqlJavaDemo {
	public static void main(String[] args) {
		SparkConf conf = new SparkConf();
		conf.setAppName("sqlJava") ;
		conf.setMaster("local") ;
		SparkSession spark = SparkSession.builder().config(conf).enableHiveSupport().getOrCreate();
		spark.sql("use big12").show();
		spark.sql("select * from orders").show(1000, false);

//		for(Row r : rows){
//			Object o = r.get(0) ;
//			System.out.print(o + " : ");
//			System.out.println(r);
//		}


	}
}
