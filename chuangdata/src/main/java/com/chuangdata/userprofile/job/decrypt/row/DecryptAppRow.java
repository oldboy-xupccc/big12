package com.chuangdata.userprofile.job.decrypt.row;

import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.sql.Timestamp;

/**
 * Created by lucaslu on 16/11/11.
 */
public class DecryptAppRow implements Writable {

    Timestamp log_time;
    String msisdn;
    String imei;
    String imsi;
    String user_ip;
    int app_id;
    int app_type_id;
    Long log_count;

    public DecryptAppRow(String inVal[]){

        if(!"".equalsIgnoreCase(inVal[0])){
            this.log_time = Timestamp.valueOf(inVal[0]);
        }else{
            this.log_time = Timestamp.valueOf("1970-01-01 00:00:00");
        };
        this.msisdn = inVal[1];
        this.imei = inVal[2];
        this.imsi = inVal[3];
        this.user_ip = inVal[4];

        if(!"".equalsIgnoreCase(inVal[5])){
            this.app_id = Integer.parseInt(inVal[5]);
        }else{
            this.app_id = 0;
        };

        if(!"".equalsIgnoreCase(inVal[6])){
            this.app_type_id = Integer.parseInt(inVal[6]);
        }else{
            this.app_type_id=0;
        };

        if(!"".equalsIgnoreCase(inVal[7])){
            this.log_count = Long.parseLong(inVal[7]);
        }else{
            this.log_count = 0l;
        };

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
