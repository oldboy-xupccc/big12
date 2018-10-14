package com.chuangdata.userprofile.job.ct.kvjob;

import org.apache.hadoop.io.GenericWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;

/**
 * @author luxiaofeng
 * @date 2016-10-05
 */

public class ValueGenericWritable extends GenericWritable {

    public ValueGenericWritable() {

    }

    public ValueGenericWritable(TagToUserCountModel tagToUserCountModel) {
        super.set(tagToUserCountModel);
    }

    public ValueGenericWritable(Text imeiId) {
        super.set(imeiId);
    }

    public ValueGenericWritable(UserIdToTagLogCountModel userIdToTagLogCountModel) {
        super.set(userIdToTagLogCountModel);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Class<? extends Writable>[] getTypes() {

        return new Class[]{TagToUserCountModel.class, Text.class, UserIdToTagLogCountModel.class};
    }

}
