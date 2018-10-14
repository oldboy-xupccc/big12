package com.chuangdata.userprofile.job.tag;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

/** 
 * @author luxiaofeng
 */
public class GroupTagGroupingComparator extends WritableComparator{
    protected GroupTagGroupingComparator() {
        super(Text.class, true);
    }

    @Override
    @SuppressWarnings("rawtypes")
    public int compare(WritableComparable a, WritableComparable b) {
        String k1 = a.toString();
        String k2 = b.toString();
        
        String[] k1Array = k1.split("\t");
        String gk1 = k1Array[0] + "\t" + k1Array[1] + "\t" + k1Array[2];
        String[] k2Array = k2.split("\t");
        String gk2 = k2Array[0] + "\t" + k2Array[1] + "\t" + k2Array[2];
        
        return gk1.compareTo(gk2);
    }
}
