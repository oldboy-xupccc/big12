package com.chuangdata.userprofile.bj.job;

import com.chuangdata.userprofile.job.KeyModel;

/**
 * @author luxiaofeng
 */
public class UserAppKeyModel extends KeyModel {

    @Override
    public String toString(String separator) {
        StringBuilder builder = new StringBuilder();
        builder.append(this.getUserId()).append(separator);
		builder.append(this.getTimeStamp()).append(separator);
        builder.append(this.getAppId()).append(separator);
        builder.append(this.getHost());
        return builder.toString();
    }

    public String toString() {
        return toString(",");
    }

    public int compareTo(KeyModel other) {
        if (other instanceof UserAppKeyModel) {
            return this.toString(",").compareTo(((UserAppKeyModel) other).toString(","));
        } else {
            return super.toString(",").compareTo(other.toString(","));
        }
    }
}
