package com.oldboy.hadoop.mr.fulljoin.reduce;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

import java.io.IOException;

/**
 * Created by Administrator on 2018/9/21.
 */
public class JoinMapper2 extends Mapper<LongWritable,Text ,ComboKey , Text> {
	protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
		String line = value.toString();
		String[] arr = line.split(",") ;
		FileSplit split = (FileSplit) context.getInputSplit();
		int type = 0 ;
		int cid = 0 ;
		int oid = 0 ;
		if(split.getPath().getName().contains("cust")){
			type = 0 ;
			cid = Integer.parseInt(arr[0]) ;
		}
		else{
			type = 1 ;
			cid = Integer.parseInt(arr[arr.length - 1]);
			oid = Integer.parseInt(arr[0]);
		}
		ComboKey keyOut = new ComboKey();
		keyOut.setType(type);
		keyOut.setCid(cid);
		keyOut.setOid(oid);
		context.write(keyOut, value);

	}
}
