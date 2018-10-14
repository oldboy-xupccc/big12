package com.chuangdata.userprofile.bj.job;

import com.chuangdata.framework.resource.app.DomainUnit;
import com.chuangdata.userprofile.counter.UserProfileCounter;
import com.chuangdata.userprofile.job.KeyModel;
import com.chuangdata.userprofile.utils.Strings;
import com.chuangdata.framework.logmodel.FieldNotFoundException;
import com.chuangdata.framework.logmodel.LogModel;
import org.apache.log4j.Logger;

/**
 * @author luxiaofeng
 */
public class FixedUserAppMapper extends FixedMapper<UserAppKeyModel> {
    private static final Logger LOG = Logger.getLogger(FixedUserAppMapper.class);

    protected void buildKey(LogModel logModel, KeyModel keyModel) {
        try {
            keyModel.setUserId(logModel.getMeid());
        } catch (FieldNotFoundException e) {
        }
        try {
            DomainUnit unit = urlMatcher.getDomain(getHost(logModel), getUri(logModel));
            keyModel.setAppId(unit == null ? -1 : unit.getId());
        } catch (FieldNotFoundException e) {
        }
    }

    protected boolean filterOut(LogModel logModel, Context context) {
        try {
            if (!Strings.isNotEmpty(logModel.getMeid())) {
                // only in zj ct
                context.getCounter(UserProfileCounter.MAP_FILTER_OUT_BY_EMPTY_MEID).increment(1);
                return true;
            }
            if (!Strings.isNotEmpty(logModel.getUri())) {
                context.getCounter(UserProfileCounter.MAP_FILTER_OUT_BY_URI).increment(1);
                return true;
            }
            try {
                String host = getHost(logModel);
                if (!Strings.isNotEmpty(host)) {
                    context.getCounter(UserProfileCounter.MAP_FILTER_OUT_BY_EMPTY_HOST).increment(1);
                    return true;
                }
                if (urlMatcher.getDomain(host, getUri(logModel)) == null) {
                    context.getCounter(UserProfileCounter.MAP_FILTER_OUT_BY_HOST).increment(1);
                    return true;
                }
            } catch (FieldNotFoundException e) {
                context.getCounter(UserProfileCounter.MAP_FILTER_OUT_BY_HOST).increment(1);
                return true;
            }
            if (!Strings.isNotEmpty(logModel.getProcedureEndTime()) || Long.parseLong(logModel.getProcedureEndTime()) == 0) {
                context.getCounter(UserProfileCounter.MAP_FILTER_OUT_BY_TIME).increment(1);
                return true;
            }
        } catch (FieldNotFoundException e) {

        }
        return false;
    }

    protected boolean filterByApp(KeyModel keyModel) {
        return keyModel.getAppId().get() < 0;
    }

    protected KeyModel buildKeyModel() {
        return new UserAppKeyModel();
    }
}
