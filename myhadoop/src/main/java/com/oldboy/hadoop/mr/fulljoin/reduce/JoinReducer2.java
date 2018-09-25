package com.oldboy.hadoop.mr.fulljoin.reduce;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text ;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.Iterator;

/**
 */
public class JoinReducer2 extends Reducer<ComboKey,Text,Text,NullWritable> {
	protected void reduce(ComboKey key, Iterable<Text> values, Context context) throws IOException, InterruptedException {

		Iterator<Text> it = values.iterator();
		String custInfo = "null" ;
		//首行是customer
		if(key.getType() == 0){
			String line = it.next().toString();
			custInfo = line ;
		}
		if(it.hasNext()){
			//
			while(it.hasNext()){
				String orderInfo = it.next().toString() ;
				context.write(new Text(custInfo + "," + orderInfo) , NullWritable.get());
			}
		}
		else{
			context.write(new Text(custInfo + ",null"), NullWritable.get());
		}
	}
}
