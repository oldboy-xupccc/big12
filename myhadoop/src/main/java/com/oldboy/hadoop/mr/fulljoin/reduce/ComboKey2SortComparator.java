package com.oldboy.hadoop.mr.fulljoin.reduce;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

/**
 * 排序对比器
 *
 */
public class ComboKey2SortComparator extends WritableComparator{
	public ComboKey2SortComparator(){
		super(ComboKey.class ,true);
	}
	public int compare(WritableComparable a, WritableComparable b) {
		return a.compareTo(b);
	}
}
