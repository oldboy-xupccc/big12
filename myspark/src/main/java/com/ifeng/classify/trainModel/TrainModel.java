package com.ifeng.classify.trainModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ifeng.classify.Util.NativeBayes;

public class TrainModel {
	
	private static String dataDir = "F:\\big12\\big12-徐-10recomm-day01(李)\\data\\data";
	
	/**
     * 将数据分为训练数据和测试数据
     * 
     * @param dataDir
     */
    public static void splitData(NativeBayes nativeBayes) {
    	// 用文件名区分类别
        File f = new File(dataDir);
        File[] files = f.listFiles();
        for (File file : files) {
            String fname = file.getName().replaceAll(".txt", "");
            ArrayList<String> list = new ArrayList<String>();
            Scanner scanner = null;
			try {
				scanner = new Scanner(file);
				while(scanner.hasNext()){
					String line = scanner.nextLine().trim();
					list.add(line);
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
                if (nativeBayes.getFiles_all().containsKey(fname)) {
                	nativeBayes.getFiles_all().get(fname).addAll(list);
                } else {
                    nativeBayes.getFiles_all().put(fname, list);
                }
        }

        System.out.println("统计数据:");
        for (Entry<String, List<String>> entry : nativeBayes.getFiles_all().entrySet()) {
            String cname = entry.getKey();
            List<String> value = entry.getValue();
            // System.out.println(cname + " : " + value.size());

            // 训练集
            List<String> train = new ArrayList<String>();
            // 测试集
            List<String> test = new ArrayList<String>();

            for (String str : value) {
                if (Math.random() <= nativeBayes.getTrainingPercent()) {// 80%用来训练 , 20%测试
                    train.add(str);
                } else {
                    test.add(str);
                }
            }

            nativeBayes.getFiles_train().put(cname, train);
            nativeBayes.getFiles_test().put(cname, test);
        }

        System.out.println("所有文件数:");
        printStatistics(nativeBayes.getFiles_all());
        System.out.println("训练文件数:");
        printStatistics(nativeBayes.getFiles_train());
        System.out.println("测试文件数:");
        printStatistics(nativeBayes.getFiles_test());

    }
	
	
	
    /**
     * 将数据分为训练数据和测试数据
     * 
     * @param dataDir
     */
    public static void splitDataTwo(NativeBayes nativeBayes, String dataDir) {
    	// 用文件名区分类别
        Pattern pat = Pattern.compile("\\d+([a-z]+?)\\.");
        dataDir = "testdata/allfiles";
        File f = new File(dataDir);
        File[] files = f.listFiles();
        for (File file : files) {
            String fname = file.getName();
            Matcher m = pat.matcher(fname);
            if (m.find()) {
                String cname = m.group(1);
                if (nativeBayes.getFiles_all().containsKey(cname)) {
                	nativeBayes.getFiles_all().get(cname).add(file.toString());
                } else {
                    List<String> tmp = new ArrayList<String>();
                    tmp.add(file.toString());
                    nativeBayes.getFiles_all().put(cname, tmp);
                }
            } else {
                System.out.println("err: " + file);
            }
        }

        System.out.println("统计数据:");
        for (Entry<String, List<String>> entry : nativeBayes.getFiles_all().entrySet()) {
            String cname = entry.getKey();
            List<String> value = entry.getValue();
            // System.out.println(cname + " : " + value.size());

            List<String> train = new ArrayList<String>();
            List<String> test = new ArrayList<String>();

            for (String str : value) {
                if (Math.random() <= nativeBayes.getTrainingPercent()) {// 80%用来训练 , 20%测试
                    train.add(str);
                } else {
                    test.add(str);
                }
            }

            nativeBayes.getFiles_train().put(cname, train);
            nativeBayes.getFiles_test().put(cname, test);
        }

        System.out.println("所有文件数:");
        printStatistics(nativeBayes.getFiles_all());
        System.out.println("训练文件数:");
        printStatistics(nativeBayes.getFiles_train());
        System.out.println("测试文件数:");
        printStatistics(nativeBayes.getFiles_test());

    }
	
	
    /**
     * 加载训练数据
     */
    public static void loadTrainData(NativeBayes nativeBayes){
        for (Entry<String, List<String>> entry : nativeBayes.getFiles_train().entrySet()) {
            String classname = entry.getKey();
            List<String> docs = entry.getValue();

            nativeBayes.getClassFreq().put(classname, docs.size());

            Map<String, Integer> feaFreq = new HashMap<String, Integer>();
            nativeBayes.getClassFeaFreq().put(classname, feaFreq);

            for (String doc : docs) {
                String[] words = doc.split(" ");
//            	String[] words = null;
                for (String word : words) {
                	nativeBayes.getWordDict().add(word);
                    if(feaFreq.containsKey(word)){
                        int num = feaFreq.get(word) + 1;
                        feaFreq.put(word, num);
                    }else{
                        feaFreq.put(word, 1);
                    }
                }
            }    
        }
        System.out.println(nativeBayes.getClassFreq().size()+" 分类, " + nativeBayes.getWordDict().size()+" 特征词");
    }

	
    /**
     * 模型训练
     */
    public static void createModel(NativeBayes nativeBayes) {
        double sum = 0.0;
        for (Entry<String, Integer> entry : (nativeBayes.getClassFreq().entrySet())) {
            sum+=entry.getValue();
        }
        for (Entry<String, Integer> entry : nativeBayes.getClassFreq().entrySet()) {
        	nativeBayes.getClassProb().put(entry.getKey(), entry.getValue()/sum);
        }

        for (Entry<String, Map<String, Integer>> entry : nativeBayes.getClassFeaFreq().entrySet()) {
            sum = 0.0;
            String classname = entry.getKey();
            for (Entry<String, Integer> entry_1 : entry.getValue().entrySet()){
                sum += entry_1.getValue();
            }
            
            // 用于做平滑处理，防止分母为零
            double newsum = sum + nativeBayes.getWordDict().size()*nativeBayes.getDefaultFreq();

            // 在训练集中每个分类中每个特征词出现的概率值          p(xi/yi)
            Map<String, Double> feaProb = new HashMap<String, Double>();
            nativeBayes.getClassFeaProb().put(classname, feaProb);
            
            for (Entry<String, Integer> entry_1 : entry.getValue().entrySet()){
                String word = entry_1.getKey();
                feaProb.put(word, (entry_1.getValue() + nativeBayes.getDefaultFreq()) /newsum);
            }
            nativeBayes.getClassDefaultProb().put(classname, nativeBayes.getDefaultFreq()/newsum);
        }
    }
    
    /**
     * 打印统计信息
     * 
     * @param m
     */
    public static void printStatistics(Map<String, List<String>> m) {
        for (Entry<String, List<String>> entry : m.entrySet()) {
            String cname = entry.getKey();
            List<String> value = entry.getValue();
            System.out.println(cname + " : " + value.size());
        }
        System.out.println("--------------------------------");
    }
    
    
    
}
