package com.chuangdata.userprofile.job.decrypt;

import com.chuangdata.framework.resource.app.DomainMatcher;
import com.chuangdata.framework.resource.app.DomainUnit;
import com.chuangdata.userprofile.counter.UserProfileCounter;
import com.chuangdata.userprofile.job.decrypt.row.DecryptDomainRow;
import com.google.common.annotations.VisibleForTesting;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hive.ql.io.orc.OrcSerde;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.StructObjectInspector;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author luxiaofeng
 */
public class UserAppDecryptMapper extends Mapper<Object, Text, NullWritable, Text> {
    private static final Logger LOG = Logger.getLogger(UserAppDecryptMapper.class);
    private Decrypter decrypter;

    private Text outKey = new Text();

    private boolean isEncrypted = false;

    private DomainMatcher appDomainMatcher;

    private String separator = "\\|";
    private String date = "1970-01-01";

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:00:00");

    NullWritable nw = NullWritable.get();

    @VisibleForTesting
    protected void setDecrypter(Decrypter decrypter) {
        this.decrypter = decrypter;
    }

    @VisibleForTesting
    protected void setDomainMatcher(DomainMatcher domainMatcher) {
        this.appDomainMatcher = domainMatcher;
    }

    public void setup(Context context) throws IOException {
        Configuration conf = context.getConfiguration();
        String privateKeyFile = conf.get("chuangdata.encrypt.private.key.file");
        try {
            decrypter = new Decrypter(privateKeyFile);
        } catch (Exception e) {
            LOG.error("Init private key error: ", e);
        }
        isEncrypted = conf.getBoolean("chuangdata.dmu.userprofile.result.encrypted", false);
        String appDomainFilePath = conf.get("chuangdata.dmu.userprofile.app.domain");
        appDomainMatcher = new DomainMatcher(appDomainFilePath, false);
        separator = conf.get("mapreduce.output.textoutputformat.separator", "\\|");
        date = conf.get("chuangdata.dmu.userprofile.log.day");
    }

    public void map(Object key, Text value, Context context) {
        try {
            String input = value.toString();
            String decrypted = isEncrypted ? decrypter.decrypt(input) : input;
            if (decrypted == null || decrypted.isEmpty()) {
                context.getCounter(UserProfileCounter.MAP_INVALID_INPUT_ERROR).increment(1);
                return;
            }
            String[] info = decrypted.split("\\t");
            if (info == null || info.length != 2) {
                context.getCounter(UserProfileCounter.MAP_INVALID_INPUT_ERROR).increment(1);
                return;
            }
            String[] keys = info[0].split("\\|");
            String encryptedMeid = keys[0];
            if(null !=keys[1] && !keys[1].equalsIgnoreCase("0")) {
                date = sdf.format(new Date(Long.parseLong(keys[1])));
            }
            int app_domain_id = Integer.parseInt(keys[2]);
            String host =  keys[3];
            DomainUnit domainUnit = appDomainMatcher.getDomainById(app_domain_id);
            if (domainUnit == null) {
                context.getCounter(UserProfileCounter.MAP_FILTER_OUT_BY_DOMAIN).increment(1);
                return;
            }
            String logCount = info[1].split("\\|")[0];
            StringBuilder builder = new StringBuilder();
            builder.append(date).append("|");
            builder.append(" ").append("|"); // msisdn
            builder.append(encryptedMeid).append("|"); // imei
            builder.append(" ").append("|"); // imsi
            builder.append(" ").append("|"); // userip
            builder.append("2").append("|"); // clicked code, 2 stands for unknown
            builder.append(app_domain_id).append("|");
            builder.append(host).append("|");
            builder.append(logCount);
            outKey.set(builder.toString());
			context.write(NullWritable.get(),outKey);
            //2016-10-13 Changed by luxiaofeng: write with orc file type
//            String tmp[] = outKey.toString().split("\\|");
//            OrcSerde orcSerde = new OrcSerde();
//            StructObjectInspector inspector =
//                    (StructObjectInspector) ObjectInspectorFactory.getReflectionObjectInspector(DecryptDomainRow.class,
//                                    ObjectInspectorFactory.ObjectInspectorOptions.JAVA);
//
//
//            Writable row = orcSerde.serialize(new DecryptDomainRow(tmp), inspector);
//            context.write(nw,row);

        } catch (Exception e) {
            LOG.error("Map error: ", e);
            context.getCounter(UserProfileCounter.MAP_ERROR).increment(1);
        }
    }
}
