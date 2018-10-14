package com.chuangdata.userprofile.job.tag;

import com.chuangdata.framework.resource.source.DataCollectEnvMatcher;
import com.chuangdata.framework.resource.source.DataSourceMatcher;
import com.chuangdata.framework.resource.tag.TagMatcher;
import com.chuangdata.userprofile.counter.UserProfileCounter;
import com.chuangdata.userprofile.utils.Strings;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.List;

/** 
 * 行业、场景标签
 * @author luxiaofeng
 */
public class GroupTagMapper extends Mapper<Object, Text, Text, LongWritable> {
    private static final Logger LOG = Logger.getLogger(GroupTagMapper.class);
    
    private TagMatcher tagMatcher;
    private DataSourceMatcher dataSourceMatcher;
    private DataCollectEnvMatcher dataCollectEnvMatcher;
    private String separator = "\001";
    private String outputSeparator = "\t";
    
    private Text outKey = new Text();
    
    private int dataSourceId = 0;
    private int networkTypeId = 0;
    
    public void setup(Context context) throws IOException {
    	Configuration configuration = context.getConfiguration();
    	String tagFilePath = configuration.get("chuangdata.dmu.userprofile.tag.resource");
    	tagMatcher = new TagMatcher(tagFilePath, false); // do not support encrypted tag resource file
        String dataSourceFilePath = configuration.get("chuangdata.dmu.userprofile.data.source");
        String dataCollectEnvFilePath = configuration.get("chuangdata.dmu.userprofile.data.collectenv");
        dataSourceMatcher = new DataSourceMatcher(dataSourceFilePath, false); // Don't support encrypted resource files.
        dataCollectEnvMatcher = new DataCollectEnvMatcher(dataCollectEnvFilePath, false); // Don't support encrypted resource files.
        
        String province = configuration.get("chuangdata.dmu.userprofile.province");
        String isp = configuration.get("chuangdata.dmu.userprofile.isp");
        String collectEnv = configuration.get("chuangdata.dmu.userprofile.collectenv");
        dataSourceId = dataSourceMatcher.get(province, isp).getId();
        networkTypeId = dataCollectEnvMatcher.get(collectEnv).getNetworkTypeId();
        separator = configuration.get("mapreduce.input.textinputformat.separator", "\001");
    }

	public void map(Object key, Text value, Context context) {
	    try {
	        String[] input = Strings.split(value.toString(), separator);
	        if (input.length != 4) {
	        	throw new Exception("Invalid input: " + value.toString());
	        }
	        int userIdType = Integer.parseInt(input[2]);
	        if (userIdType == 1) {
	        	// imei
	        	int tagId = Integer.parseInt(input[0]);
	        	List<Integer> tagLists = this.tagMatcher.getParentTagIds(tagId);
	        	for (int id : tagLists) {
	        		// write
	        		write(id, Long.parseLong(input[3]), input[1], context);
	        	}
	        } else if (userIdType == 2) {
	        	// msisdn
	        	// TODO FIXME just ignore msisdn for now
	        } else {
	        	// never get here
	        }
	    } catch (Exception e) {
	    	LOG.error(e);
            context.getCounter(UserProfileCounter.MAP_INVALID_INPUT_ERROR).increment(1);
	    }
	}
	
	private void write(int tagId, long logCount, String userId, Context context) throws IOException, InterruptedException {
		StringBuilder keyBuilder = new StringBuilder();
		keyBuilder.append(this.dataSourceId).append(outputSeparator);
		keyBuilder.append(this.networkTypeId).append(outputSeparator);
		keyBuilder.append(tagId).append(outputSeparator);
		keyBuilder.append(userId);
		this.outKey.set(keyBuilder.toString());
		context.write(outKey, new LongWritable(logCount));
	}
}
