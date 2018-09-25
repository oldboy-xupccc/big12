package com.oldboy.hadoop.mr.fulljoin.map;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * map端实现全外连接
 */
public class JoinMapper extends Mapper<LongWritable,Text, Text , NullWritable> {

	public static String CUST_FILE_PATH = "cust.file.path" ;

	public Map<Integer , String> customers ;
	/**
	 * 加载customer数据
	 */
	protected void setup(Context context) throws IOException, InterruptedException {
		//初始化customer集合
		customers = new HashMap<Integer, String>() ;
		String path = context.getConfiguration().get(CUST_FILE_PATH) ;
		FileSystem fs = FileSystem.get(context.getConfiguration());
		InputStream in = fs.open(new Path(path)) ;
		BufferedReader br = new BufferedReader(new InputStreamReader(in)) ;
		String line = null ;
		while((line =br.readLine()) != null){
			String[] arr = line.split(",") ;
			Integer cid = Integer.parseInt(arr[0]) ;
			customers.put(cid , arr[0]) ;
		}
		br.close();
		in.close();
	}

	protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
		String[] arr = value.toString().split(",");
		int cid = Integer.parseInt(arr[arr.length - 1]) ;
		String custInfo = customers.get(cid) ;
		if(custInfo == null){
			context.write(new Text("null," + value.toString()), NullWritable.get());
		}
		else{
			context.write(new Text(custInfo + "," + value.toString()), NullWritable.get());
		}
	}
}
