package com.chuangdata.userprofile.job.decrypt.row;

import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.sql.Timestamp;

/**
 * Created by lucaslu on 16/11/11.
 */
public class DecryptActionRow implements Writable {


    Timestamp log_time;
    String msisdn;
    String imei;
    String imsi;
    String user_ip;
    int action_id;
    String hosts;
    String url_pattern;
    int detail_action_id;
    int action_type_id;
    String interest_tag;
    int app_name_id;
    int app_type_id;
    Long log_count;

    public DecryptActionRow(String inVal[]){

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
            this.action_id = Integer.parseInt(inVal[5]);
        }else{
            this.action_id = 0;
        };

        this.hosts = inVal[6];
        this.url_pattern = inVal[7];

        if(!"".equalsIgnoreCase(inVal[8])){
            this.detail_action_id = Integer.parseInt(inVal[8]);
        }else{
            this.detail_action_id=0;
        };

        if(!"".equalsIgnoreCase(inVal[9])){
            this.action_type_id = Integer.parseInt(inVal[9]);
        }else{
            this.action_type_id=0;
        };

        this.interest_tag = inVal[10];

        if(!"".equalsIgnoreCase(inVal[11])){
            this.app_name_id = Integer.parseInt(inVal[11]);
        }else{
            this.app_name_id=0;
        };


        if(!"".equalsIgnoreCase(inVal[12])){
            this.app_type_id = Integer.parseInt(inVal[12]);
        }else{
            this.app_type_id=0;
        };

        if(!"".equalsIgnoreCase(inVal[13])){
            this.log_count = Long.parseLong(inVal[13]);
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
