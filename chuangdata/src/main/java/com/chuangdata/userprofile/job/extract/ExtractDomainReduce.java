package com.chuangdata.userprofile.job.extract;

import com.chuangdata.userprofile.job.decrypt.row.DecryptDomainRow;
import org.apache.hadoop.hive.ql.io.orc.OrcSerde;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.StructObjectInspector;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * Created by lucaslu on 16/11/17.
 */
public class ExtractDomainReduce  extends Reducer<NullWritable, Text, NullWritable,Writable > {
    private static final Logger LOG = Logger.getLogger(ExtractDomainReduce.class);

    @Override
    public void reduce(NullWritable key, Iterable<Text> value, Context context)
            throws IOException, InterruptedException {

        for(Text val:value) {
            //2016-10-13 Changed by luxiaofeng: write with orc file type
            String tmp[] = val.toString().split("\\|");
            OrcSerde orcSerde = new OrcSerde();
            StructObjectInspector inspector = (StructObjectInspector) ObjectInspectorFactory
                    .getReflectionObjectInspector(DecryptDomainRow.class, ObjectInspectorFactory.ObjectInspectorOptions.JAVA);

            Writable row = orcSerde.serialize(new DecryptDomainRow(tmp), inspector);
            context.write(NullWritable.get(), row);
        }
    }

}
