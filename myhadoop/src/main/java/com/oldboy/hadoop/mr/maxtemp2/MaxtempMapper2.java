package com.oldboy.hadoop.mr.maxtemp2;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * mapper
 */
public class MaxtempMapper2 extends Mapper<LongWritable, Text, ComboKey, NullWritable> {
	public MaxtempMapper2(){
		System.out.println("new MaxtempMapper2()");
	}

	private ComboKey comKey = new ComboKey();
	protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
		String[] arr = value.toString().split(" ");
		int year = Integer.parseInt(arr[0]) ;
		int temp = Integer.parseInt(arr[1]);
		comKey.setYear(year);
		comKey.setTemp(temp);
		context.write(comKey,NullWritable.get());
	}
}
