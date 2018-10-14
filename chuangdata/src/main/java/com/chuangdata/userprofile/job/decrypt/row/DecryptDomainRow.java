package com.chuangdata.userprofile.job.decrypt.row;

import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.sql.Timestamp;

/**
 * Created by lucaslu on 16/11/11.
 */
public class DecryptDomainRow implements Writable {

    Timestamp log_time;
    String msisdn;
    String imei;
    String imsi;
    String user_ip;
    int clicked_code;
    int app_domain_id;
    String host;
    Long log_count;


    public DecryptDomainRow(String inVal[]) {

        if (!"".equalsIgnoreCase(inVal[0])) {
            this.log_time = Timestamp.valueOf(inVal[0]);
        } else {
            this.log_time = Timestamp.valueOf("1970-01-01 00:00:00");
        }
        ;
        this.msisdn = inVal[1];
        this.imei = inVal[2];
        this.imsi = inVal[3];
        this.user_ip = inVal[4];

        if (!"".equalsIgnoreCase(inVal[5])) {
            this.clicked_code = Integer.parseInt(inVal[5]);
        } else {
            this.clicked_code = 0;
        }
        ;

        if (!"".equalsIgnoreCase(inVal[6])) {
            this.app_domain_id = Integer.parseInt(inVal[6]);
        } else {
            this.app_domain_id = 0;
        }
        ;

        this.host = inVal[7];


        if (!"".equalsIgnoreCase(inVal[8])) {
            this.log_count = Long.parseLong(inVal[8]);
        } else {
            this.log_count = 0l;
        }
        ;


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
