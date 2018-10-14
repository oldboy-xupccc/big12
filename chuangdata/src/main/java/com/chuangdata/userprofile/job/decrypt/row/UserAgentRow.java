package com.chuangdata.userprofile.job.decrypt.row;

import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.sql.Timestamp;

/**
 * Created by lucaslu on 17/03/30.
 */
public class UserAgentRow implements Writable {


    Timestamp log_time;
    String imei;
    String useragent;
    String preimei;
    Long log_count;

    public UserAgentRow(String inVal[]){

        if(!"".equalsIgnoreCase(inVal[0])){
            this.log_time = Timestamp.valueOf(inVal[0]);
        }else{
            this.log_time = Timestamp.valueOf("1970-01-01 00:00:00");
        };

        this.imei = inVal[1];
        this.useragent = inVal[2];
        this.preimei = inVal[3];

        if(!"".equalsIgnoreCase(inVal[4])){
            this.log_count = Long.parseLong(inVal[4]);
        }else{
            this.log_count = 0l;
        }

    }



    @Override
    public void write(DataOutput dataOutput) throws IOException {
        throw new UnsupportedOperationException("no write");
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        throw new UnsupportedOperationException("no read");
    }
}
