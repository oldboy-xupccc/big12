package com.chuangdata.userprofile.job.decrypt.row;

import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.sql.Date;

/**
 * Created by Lucas on 2016/12/22.
 */
public class TransformActionRow  implements Writable {

    private Long data_source_id;
    private String msisdn;
    private String imei;
    private String host;
    private Long network_type_id;
    private Long tag_id;
    private Date date_time;
    private Long log_count;

    public TransformActionRow(String inVal[]){

        if(!"".equalsIgnoreCase(inVal[0])){
            this.data_source_id = Long.parseLong(inVal[0].trim());
        }else{
            this.data_source_id = 0l;
        };

        this.msisdn = inVal[1];
        this.imei = inVal[2];
        this.host = inVal[3];
        if(!"".equalsIgnoreCase(inVal[4])){
            this.network_type_id = Long.parseLong(inVal[4].trim());
        }else{
            this.network_type_id = 0l;
        };

        if(!"".equalsIgnoreCase(inVal[5])){
            this.tag_id = Long.parseLong(inVal[5].trim());
        }else{
            this.tag_id = 0l;
        };

        if(!"".equalsIgnoreCase(inVal[6])){
            this.date_time = Date.valueOf(inVal[6].substring(0,10));
        }else{
            this.date_time = Date.valueOf("1970-01-01");
        };
        if(!"".equalsIgnoreCase(inVal[7])){
            this.log_count = Long.parseLong(inVal[7].trim());
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
