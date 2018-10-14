package com.chuangdata.userprofile.job.transform;

import com.chuangdata.userprofile.job.decrypt.row.TransformActionRow;
import com.chuangdata.userprofile.job.encrypt.MD5Encrypt;
import org.apache.hadoop.hive.ql.io.orc.OrcSerde;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.StructObjectInspector;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * Created by Lucas on 2016/12/30.
 */
public class ActionTransReduce extends Reducer<Text, IntWritable, NullWritable, Writable> {

    private NullWritable nw = NullWritable.get();
    private MD5Encrypt md5Encrypter;

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        super.setup(context);
        md5Encrypter = MD5Encrypt.getInstance();
    }

    @Override
    protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
        //super.reduce(key, values, context);

        try {
            //build key
            String outKey[] = new String[8];
            String tmp[] = key.toString().split("\\|");
            if(tmp[1].trim().equalsIgnoreCase(" (null)")){tmp[1]="";}
            if(tmp[1].trim().equalsIgnoreCase("(null)")){tmp[1]="";}
            String msisdn = tmp[1].trim();
            msisdn = msisdn.replace(" ","");

            //encrypt as 13006352257_2
            if(msisdn.indexOf("_") > 0){
                msisdn = md5Encrypter.encrypt(msisdn.split("\\_")[0]);
            }

            //encrypt as 13006352257
            if(msisdn.length() == 11 && msisdn.startsWith("1")) {
                msisdn = md5Encrypter.encrypt(msisdn);
            }

            tmp[1]= msisdn;

            for (int i = 0; i < tmp.length; i++) {
                outKey[i] = tmp[i];
            }
            int value = 0;
            for (IntWritable val : values) {
                value += val.get();
            }

            outKey[7] = value + "";


            OrcSerde orcSerde = new OrcSerde();
            StructObjectInspector inspector =
                    (StructObjectInspector) ObjectInspectorFactory.getReflectionObjectInspector(TransformActionRow.class,
                            ObjectInspectorFactory.ObjectInspectorOptions.JAVA);


            Writable row = orcSerde.serialize(new TransformActionRow(outKey), inspector);

            context.write(nw, row);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
