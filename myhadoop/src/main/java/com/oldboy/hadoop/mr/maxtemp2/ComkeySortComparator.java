package com.oldboy.hadoop.mr.maxtemp2;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

/**
 * 排序对比器
 *
 */
public class ComkeySortComparator extends WritableComparator {

	public ComkeySortComparator(){
		super(ComboKey.class, true) ;
	}

	public int compare(WritableComparable a, WritableComparable b) {
		ComboKey k1 = (ComboKey) a;
		ComboKey k2 = (ComboKey) b;
		return k1.compareTo(k2) ;
	}
}
