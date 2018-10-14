package com.chuangdata.userprofile.job.tag;

import com.chuangdata.userprofile.utils.Strings;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/** 
 * @author luxiaofeng
 */
public class GroupTagReducer extends Reducer<Text, LongWritable, Text, LongWritable>{
	
	private Text outKey = new Text();
	private String separator = "\\|";

	private static LongWritable ZERO = new LongWritable(0);
	
    private String inputLogDay = null;
	
	public void setup(Context context) {
		Configuration conf = context.getConfiguration();
		separator = conf.get("mapreduce.output.textoutputformat.separator", "\\|");
        inputLogDay = conf.get("chuangdata.dmu.userprofile.input.log.day");
	}

    public void reduce(Text key, Iterable<LongWritable> value, Context context) throws IOException, InterruptedException {
    	long sumLogCount = 0;
    	long userCount = 0;
    	String[] keyStr = Strings.split(key.toString(), "\t");
    	String userId = keyStr[3];
    	if (Strings.isNotEmpty(userId)) {
    		userCount += 1;
    	}
    	for (LongWritable logCount : value) {
            /**
             *  keyBuilder.append(this.dataSourceId).append(outputSeparator);
    			keyBuilder.append(this.networkTypeId).append(outputSeparator);
    			keyBuilder.append(tagId).append(outputSeparator);
    			keyBuilder.append(userId);
             */
    		String currentUserId = Strings.split(key.toString(),"\t")[3];
            if (userId.equals(currentUserId)) {
            	// DO NOTHING
            } else {
            	// userId changed
            	userCount += 1;
            	userId = currentUserId;
            }
            sumLogCount += logCount.get();
    	}
    	StringBuilder builder = new StringBuilder();
    	builder.append(keyStr[0]).append(separator); // data_source_id
    	builder.append(keyStr[1]).append(separator); // network_type_id
    	builder.append(keyStr[2]).append(separator); // tag_id
    	builder.append(inputLogDay).append(separator); // inputLogDay
    	builder.append(sumLogCount).append(separator); // sumLogCount
    	builder.append(userCount); // userCount
    	outKey.set(builder.toString());
    	context.write(outKey, ZERO); // value is virtual user count, just set 0
    }
}
