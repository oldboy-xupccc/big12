package com.oldboy.hadoop.mr.maxtemp2;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Partitioner;

/**
 * 自定义分区类
 */
public class YearPartitioner extends Partitioner<ComboKey,NullWritable> {
	public int getPartition(ComboKey comboKey, NullWritable nullWritable, int numPartitions) {
		int year = comboKey.getYear() ;
		if(year < 1930){
			return 0 ;
		}
		else if(year > 1960){
			return 2 ;
		}
		else {
			return 1 ;
		}
	}
}
