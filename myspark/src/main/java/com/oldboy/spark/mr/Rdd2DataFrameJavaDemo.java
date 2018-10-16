package com.oldboy.spark.mr;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.Metadata;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

/**
 * Created by Administrator on 2018/10/16.
 */
public class Rdd2DataFrameJavaDemo {
	public static void main(String[] args) {
		SparkSession sess = SparkSession.builder().appName("sqlJava").master("local").enableHiveSupport().getOrCreate() ;
		JavaSparkContext sc = new JavaSparkContext(sess.sparkContext());
		JavaRDD<String> rdd1 = sc.textFile("file:///d:/mr/temp3.dat") ;
		JavaRDD<Row> rdd2 = rdd1.map(new Function<String, Row>() {
			public Row call(String v1) throws Exception {
				String[] arr = v1.split(" ") ;
				Row row = RowFactory.create(Integer.parseInt(arr[0]),Integer.parseInt(arr[1])) ;
				return row;
			}
		}) ;

		StructField[] fields = new StructField[2];
		fields[0] = new StructField("year", DataTypes.IntegerType, false, Metadata.empty());
		fields[1] = new StructField("temp", DataTypes.IntegerType, false, Metadata.empty());
		//构造结构体类型
		StructType type = new StructType(fields);
		Dataset<Row> df1 = sess.createDataFrame(rdd2 , type) ;
		df1.createOrReplaceTempView("temps");
		sess.sql("select year , max(temp) , min(temp) from temps group by year order by year asc").show(1000, false);



	}
}
