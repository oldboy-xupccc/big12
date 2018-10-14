package com.chuangdata.userprofile.job.transform;

import com.chuangdata.framework.resource.app.ActionMatcher;
import com.chuangdata.framework.resource.app.ActionUnit;
import com.chuangdata.framework.resource.app.AppMatcher;
import com.chuangdata.framework.resource.tag.TagMatcher;
import com.chuangdata.framework.resource.tag.TagUnit;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hive.ql.io.orc.OrcStruct;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * Created by Lucas on 2016/12/21.
 */
public class ActionTransMapper extends Mapper<NullWritable, OrcStruct, Text, IntWritable> {

    private TagMatcher tagMatcher;
    private AppMatcher appMatcher;
    private ActionMatcher actionMatcher;
    private Boolean isResourceFileEncrypted;
    private int datasourceid;
    private int networktypeid;

    private NullWritable nw = NullWritable.get();
    //private String hivePath;

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        super.setup(context);
        Configuration configuration = context.getConfiguration();
        String tagFilePath = configuration
                .get("chuangdata.dmu.userprofile.tag.resource");
        String appHostFilePath = configuration.get("chuangdata.dmu.userprofile.app.host");
        String appActionFilePath = configuration.get("chuangdata.dmu.userprofile.app.action");
        isResourceFileEncrypted = configuration.getBoolean(
                "chuangdata.dmu.userprofile.resource.encrypted", true);
        datasourceid = configuration.getInt("chuangdata.dmu.userprofile.transform.datasourceid", 2);
        networktypeid = configuration.getInt("chuangdata.dmu.userprofile.transform.networktypeid", 1);

        tagMatcher = new TagMatcher(tagFilePath, isResourceFileEncrypted);
        appMatcher = new AppMatcher(appHostFilePath, isResourceFileEncrypted);
        actionMatcher = new ActionMatcher(appActionFilePath, isResourceFileEncrypted);
        //hivePath = configuration.get("userprofile.result.hive.path", "");

    }

    @Override
    protected void map(NullWritable key, OrcStruct value, Context context) throws IOException, InterruptedException {

        try {
            //read orc line.
            String tmp[] = value.toString().replace("{", "").replace("}", "").split(",");

            //build key
            String outKey[] = new String[7];
            outKey[0] = datasourceid + "";
            outKey[1] = tmp[1];
            outKey[2] = tmp[2];
            outKey[3] = tmp[6];
            outKey[4] = networktypeid + "";
            outKey[5] = tmp[5].trim();
            outKey[6] = tmp[0];
            //outKey[7] = tmp[13];
            IntWritable val = new IntWritable(Integer.parseInt(tmp[13].trim()));

            //get all 5 levels name id
            ActionUnit actionUnit = actionMatcher.getAction(Integer.parseInt(outKey[5]));
            TagUnit tagUnit;

            if (actionUnit != null) {
                int appTypeId = appMatcher.getAppTypeId(actionUnit.getAppId());

                //1、get Type Tag_id
                tagUnit = tagMatcher.getOldIdToTagIdMap(1, 0, appTypeId);
                if(null == tagUnit){
                    System.out.println("Level 1 get type tagid fail. paentTagId: 0, appTypeID: "+appTypeId);
                    return;
                }
                int typeTagID = tagUnit.getTagId();
                if (null != String.valueOf(typeTagID)) {
                    outKey[5] = typeTagID + "";
                    context.write(new Text(buildKey(outKey)), val);

                }

                //2、get App Tag_id
                tagUnit = tagMatcher.getOldIdToTagIdMap(2, typeTagID, actionUnit.getAppId());
                if(null == tagUnit){
                    System.out.println("Level 2 get App tagid fail. paentTagId: "+typeTagID+", appID: "+actionUnit.getAppId());
                    return;
                }
                int appTagID = tagUnit.getTagId();
                if (null != String.valueOf(appTagID)) {
                    outKey[5] = appTagID + "";
                    context.write(new Text(buildKey(outKey)), val);

                }

                //3、get 业务行为 Tag_id
                tagUnit = tagMatcher.getOldIdToTagIdMapByName(3, appTagID, "业务行为");
                if(null == tagUnit){
                    System.out.println("Level 3 get App tagid fail. paentTagId: "+appTagID+", tagName: 业务行为");
                    return;
                }
                int bzActionTagID = tagUnit.getTagId();
                if (null != String.valueOf(bzActionTagID)) {
                    outKey[5] = bzActionTagID + "";
                    context.write(new Text(buildKey(outKey)), val);
                }

                //4、get action Tag_id
                tagUnit = tagMatcher.getOldIdToTagIdMap(4, bzActionTagID, actionUnit.getActionTypeId());
                if(null == tagUnit){
                    System.out.println("Level 4 get App tagid fail. paentTagId: "+bzActionTagID+", actionID: "+actionUnit.getActionTypeId());
                    return;
                }
                int actionTagID = tagUnit.getTagId();
                if (null != String.valueOf(actionTagID)) {
                    outKey[5] = actionTagID + "";
                    context.write(new Text(buildKey(outKey)), val);
                }

                //5、get detail action Tag_id
                tagUnit = tagMatcher.getOldIdToTagIdMap(5, actionTagID, actionUnit.getDetailActionId());
                if(null == tagUnit){
                    System.out.println("Level 5 get App tagid fail. paentTagId: "+actionTagID+", detailActionId: "+actionUnit.getDetailActionId());
                    return;
                }
                int detailActionTagID = tagUnit.getTagId();
                if (null != String.valueOf(detailActionTagID)) {
                    outKey[5] = detailActionTagID + "";
                    context.write(new Text(buildKey(outKey)), val);
                }

            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private String buildKey(String[] key){
        StringBuffer sbf = new StringBuffer();
        for (int i=0;i<key.length;i++){
            if(i != (key.length -1)){
               sbf.append(key[i]).append("|");
            }else{
               sbf.append(key[i]);
            }
        }

        return  sbf.toString();
    }


    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {

        super.cleanup(context);
    }
}
