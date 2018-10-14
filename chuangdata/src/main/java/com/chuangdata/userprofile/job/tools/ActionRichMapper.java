package com.chuangdata.userprofile.job.tools;

import com.chuangdata.framework.logmodel.LogModel;
import com.chuangdata.framework.resource.app.ActionUnit;
import com.chuangdata.userprofile.counter.UserProfileCounter;
import com.chuangdata.userprofile.job.extract.ExtractMapper;
import com.chuangdata.userprofile.model.MultipleModel;
import com.chuangdata.userprofile.utils.Strings;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.log4j.Logger;

public class ActionRichMapper extends ExtractMapper {
	private static final Logger LOG = Logger.getLogger(ActionRichMapper.class);
	private static final MultipleModel EMPTY_VALUE = new MultipleModel();
	
	public void map(Object key, Text value, Context context) {
        try {
            LogModel logModel = logModeParser.parse(value.toString());
            String keyModel = buildKey(logModel);
            if (Strings.isNotEmpty(keyModel)) {
            	outkey.set(keyModel);
                context.write(outkey, EMPTY_VALUE);
            }
        } catch (Exception e) {
            LOG.error(e);
            context.getCounter(UserProfileCounter.MAP_INVALID_INPUT_ERROR).increment(1);
        }
	}
	
	/**
	 * if not in app action resource, then return key, otherwise return empty string
	 * @param logModel
	 * @return
	 */
	private String buildKey(LogModel logModel) throws Exception {
		StringBuilder builder = new StringBuilder();
	    ActionUnit unit = urlMatcher.getAction(logModel.getHost(), logModel.getUri());
	    if (unit == null) {
	    	// not in resources, find it!
	    	builder.append(logModel.getHost()).append("|");
	    	builder.append(logModel.getUri());
	    }
		return builder.toString();
	}
        
        
}
