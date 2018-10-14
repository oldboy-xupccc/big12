package com.chuangdata.userprofile.job.decrypt.row;

import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.sql.Timestamp;

/**
 * Created by lucaslu on 16/11/11.
 */
public class DecryptParamsRow implements Writable {

    Timestamp log_time;
    String msisdn;
    String imei;
    String imsi;
    String user_ip;
    int param_id;
    int action_id;
    String param;
    String param_value;
    int param_type_id;
    Long log_count;

    public DecryptParamsRow(String inVal[]){

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
            this.param_id = Integer.parseInt(inVal[5]);
        }else{
            this.param_id = 0;
        };

        if(!"".equalsIgnoreCase(inVal[6])){
            this.action_id = Integer.parseInt(inVal[6]);
        }else{
            this.action_id=0;
        };

        this.param = inVal[7];
        this.param_value = inVal[8];

        if(!"".equalsIgnoreCase(inVal[9])){
            this.param_type_id = Integer.parseInt(inVal[9]);
        }else{
            this.param_type_id=0;
        };

        if(!"".equalsIgnoreCase(inVal[10])){
            this.log_count = Long.parseLong(inVal[10]);
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
