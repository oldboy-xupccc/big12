package com.ifeng.classify.evaluate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import com.ifeng.classify.Util.NativeBayes;
import com.ifeng.classify.trainModel.NBModel;
import com.ifeng.classify.trainModel.TrainModel;

public class predict {
	/**
     * 用模型进行预测
     * 用于训练测试样本
     */
    public static void PredictTestData(NativeBayes nativeBayes) {
        List<String> reallist=new ArrayList<String>();
        List<String> pridlist=new ArrayList<String>();

        for (Entry<String, List<String>> entry : nativeBayes.getFiles_test().entrySet()) {
            String realclassname = entry.getKey();
            List<String> files = entry.getValue();

            for (String file : files) {
                reallist.add(realclassname);


                List<String> classnamelist=new ArrayList<String>();
                List<Double> scorelist=new ArrayList<Double>();
                for (Entry<String, Double> entry_1 : nativeBayes.getClassProb().entrySet()) {
                    String classname = entry_1.getKey();
                  //先验概率
                    Double score = Math.log(entry_1.getValue());

                    String[] words = file.split(" ");
//                    String[] words = null;
                    for (String word : words) {
                        if(!nativeBayes.getWordDict().contains(word)){
                            continue;
                        }

                        if(nativeBayes.getClassFeaProb().get(classname).containsKey(word)){
                            score += Math.log(nativeBayes.getClassFeaProb().get(classname).get(word));
                        }else{
                            score += Math.log(nativeBayes.getClassDefaultProb().get(classname));
                        }
                    }

                    classnamelist.add(classname);
                    scorelist.add(score);
                    
                }

                Double maxProb = Collections.max(scorelist);
                int idx = scorelist.indexOf(maxProb);
                pridlist.add(classnamelist.get(idx));
            }
        }

        CheckUp.Evaluate(reallist, pridlist);

        for (String cname : nativeBayes.getFiles_test().keySet()) {
        	CheckUp.CalPreRec(reallist, pridlist, cname);
        }

    }
    
    
    public static void main(String[] args) {
        NativeBayes bayes = new NativeBayes();
        TrainModel.splitData(bayes);
        TrainModel.loadTrainData(bayes);
        TrainModel.createModel(bayes);
        NBModel.SerializeNativeBayes(bayes);
        predict.PredictTestData(bayes);

    }
    
    
}
