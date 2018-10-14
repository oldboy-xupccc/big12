package com.chuangdata.userprofile.job.tag;

import com.chuangdata.userprofile.utils.Strings;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

/** 
 * @author luxiaofeng
 */
public class GroupTagPartitioner extends Partitioner<Text, LongWritable> {

    @Override
    public int getPartition(Text key, LongWritable value, int numPartitions) {
        String keyStr = key.toString();
        String[] keyArray = Strings.split(keyStr,"\t");
        
        /**
         *  keyBuilder.append(this.dataSourceId).append(outputSeparator);
			keyBuilder.append(this.networkTypeId).append(outputSeparator);
			keyBuilder.append(tagId).append(outputSeparator);
			keyBuilder.append(userId);
         */
        String realKey = keyArray[0] + "\t" + keyArray[1] + "\t" + keyArray[2]; 
        // userid不出现在partition的key中，这样会引起数据倾斜问题
        // TODO 如何解决数据倾斜？
        return (realKey.hashCode() & Integer.MAX_VALUE) % numPartitions;
    }
}
