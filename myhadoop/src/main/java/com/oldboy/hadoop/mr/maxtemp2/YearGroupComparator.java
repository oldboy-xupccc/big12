package com.oldboy.hadoop.mr.maxtemp2;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

/**
 * 年度分组对比器
 */
public class YearGroupComparator extends WritableComparator {
	public YearGroupComparator(){
		super(ComboKey.class,true) ;
	}
	public int compare(WritableComparable a, WritableComparable b) {
		ComboKey k1 = (ComboKey) a;
		ComboKey k2 = (ComboKey) b;
		return k1.getYear() - k2.getYear() ;
	}
}
