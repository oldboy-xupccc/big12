package com.oldboy.hadoop.mr.fulljoin.reduce;

import org.apache.hadoop.mapreduce.Partitioner;
import org.apache.hadoop.io.Text;


/**
 * 按照cid分区
 */
public class CIDPartitioner extends Partitioner<ComboKey,Text> {

	public int getPartition(ComboKey comboKey, Text text, int numPartitions) {
		int cid = comboKey.getCid() ;
		return cid % numPartitions ;
	}
}
