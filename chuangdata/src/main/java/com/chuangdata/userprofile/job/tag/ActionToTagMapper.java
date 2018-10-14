package com.chuangdata.userprofile.job.tag;

import com.chuangdata.framework.resource.app.ActionMatcher;
import com.chuangdata.framework.resource.app.ActionUnit;
import com.chuangdata.framework.resource.source.DataCollectEnvMatcher;
import com.chuangdata.framework.resource.source.DataSourceMatcher;
import com.chuangdata.framework.resource.tag.TagMatcher;
import com.chuangdata.userprofile.counter.UserProfileCounter;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.List;

/** 
 * @author luxiaofeng
 */
public class ActionToTagMapper extends Mapper<Object, Text, Text, LongWritable> {
    private static final Logger LOG = Logger.getLogger(ActionToTagMapper.class);
    
    private TagMatcher tagMatcher;
    private ActionMatcher actionMatcher;
    private DataSourceMatcher dataSourceMatcher;
    private DataCollectEnvMatcher dataCollectEnvMatcher;
    private String separator = "\\|";
    private String outputSeparator;
    
    private Text outKey = new Text();
    
    private int dataSourceId = 0;
    private int networkTypeId = 0;
    
    public void setup(Context context) throws IOException {
    	Configuration configuration = context.getConfiguration();
    	String tagFilePath = configuration.get("chuangdata.dmu.userprofile.tag.resource");
    	tagMatcher = new TagMatcher(tagFilePath, false); // do not support encrypted tag resource file
    	String appActionFilePath = configuration.get("chuangdata.dmu.userprofile.app.action");
    	actionMatcher = new ActionMatcher(appActionFilePath, false);
        String dataSourceFilePath = configuration.get("chuangdata.dmu.userprofile.data.source");
        String dataCollectEnvFilePath = configuration.get("chuangdata.dmu.userprofile.data.collectenv");
        dataSourceMatcher = new DataSourceMatcher(dataSourceFilePath, false); // Don't support encrypted resource files.
        dataCollectEnvMatcher = new DataCollectEnvMatcher(dataCollectEnvFilePath, false); // Don't support encrypted resource files.
        
        String province = configuration.get("chuangdata.dmu.userprofile.province");
        String isp = configuration.get("chuangdata.dmu.userprofile.isp");
        String collectEnv = configuration.get("chuangdata.dmu.userprofile.collectenv");
        dataSourceId = dataSourceMatcher.get(province, isp).getId();
        networkTypeId = dataCollectEnvMatcher.get(collectEnv).getNetworkTypeId();
        
    	outputSeparator = configuration.get("mapreduce.output.textoutputformat.separator", "\\|");
    }

	public void map(Object key, Text value, Context context) {
	    try {
	        String[] input = value.toString().split(separator);
	        ActionUnit actionUnit = actionMatcher.getAction(Integer.parseInt(input[5]));
	        List<Integer> tags = tagMatcher.getAllTagIdsByAction(
	        		actionUnit.getAppId(),
	        		actionUnit.getActionTypeId(),
	        		actionUnit.getDetailActionId());
	        for (Integer tagId : tags) {
		        StringBuilder outKeyStr = new StringBuilder();
		        outKeyStr.append(dataSourceId).append(outputSeparator);
		        outKeyStr.append(input[1]).append(outputSeparator); // msisdn
		        outKeyStr.append(input[2]).append(outputSeparator); // imei
		        outKeyStr.append(input[6]).append(outputSeparator); // host
		        outKeyStr.append(networkTypeId).append(outputSeparator);
		        outKeyStr.append(tagId).append(outputSeparator);
		        String date = input[0].split(" ")[0];
		        outKeyStr.append(date);
		        outKey.set(outKeyStr.toString());
		        context.write(outKey, new LongWritable(Long.parseLong(input[13]))); // input[13] is logCount
	        }
	    } catch (Exception e) {
	    	LOG.error(e);
            context.getCounter(UserProfileCounter.MAP_INVALID_INPUT_ERROR).increment(1);
	    }
	}
    
    

}
