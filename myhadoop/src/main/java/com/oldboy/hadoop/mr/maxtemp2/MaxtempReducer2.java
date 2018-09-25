package com.oldboy.hadoop.mr.maxtemp2;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * reduce
 */
public class MaxtempReducer2 extends Reducer<ComboKey,NullWritable,Text , NullWritable> {
	protected void reduce(ComboKey key, Iterable<NullWritable> values, Context context) throws IOException, InterruptedException {
		for(NullWritable v: values){
			int year = key.getYear();
			int temp = key.getTemp();
			context.write(new Text(year + "," + temp) , NullWritable.get());
		}
	}
}
