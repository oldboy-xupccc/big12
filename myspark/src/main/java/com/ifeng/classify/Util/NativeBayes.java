package com.ifeng.classify.Util;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class NativeBayes implements Serializable {
	
   /**
    * 序列化ID
    */
    private static final long serialVersionUID = -5809782578272943999L;
	
	 /**
     * 默认频率
     */
    private double defaultFreq = 0.1;

    /**
     * 训练数据的比例
     */
    private Double trainingPercent = 0.8;

    private Map<String, List<String>> files_all = new HashMap<String, List<String>>();

    private Map<String, List<String>> files_train = new HashMap<String, List<String>>();

    private Map<String, List<String>> files_test = new HashMap<String, List<String>>();

    public NativeBayes() {

    }

    /**
     * 每个分类的频数
     */
    private Map<String, Integer> classFreq = new HashMap<String, Integer>();

    /**
     * 每个分类所占的百分比     先验概率 p(yi)
     */
    private Map<String, Double> ClassProb = new HashMap<String, Double>();

    /**
     * 特征总数
     */
    private Set<String> WordDict = new HashSet<String>();

    /**
     * 每个分类中每个特征的频数
     */
    private Map<String, Map<String, Integer>> classFeaFreq = new HashMap<String, Map<String, Integer>>();

    /**
     * 每个分类中每个特征的概率    p(xi/yi)
     */
    private Map<String, Map<String, Double>> ClassFeaProb = new HashMap<String, Map<String, Double>>();

    /**
     * 每个分类默认的概率
     */
    private Map<String, Double> ClassDefaultProb = new HashMap<String, Double>();
    
    
	public double getDefaultFreq() {
		return defaultFreq;
	}

	public void setDefaultFreq(double defaultFreq) {
		this.defaultFreq = defaultFreq;
	}

	public Double getTrainingPercent() {
		return trainingPercent;
	}

	public void setTrainingPercent(Double trainingPercent) {
		this.trainingPercent = trainingPercent;
	}

	public Map<String, List<String>> getFiles_all() {
		return files_all;
	}

	public void setFiles_all(Map<String, List<String>> files_all) {
		this.files_all = files_all;
	}

	public Map<String, List<String>> getFiles_train() {
		return files_train;
	}

	public void setFiles_train(Map<String, List<String>> files_train) {
		this.files_train = files_train;
	}

	public Map<String, List<String>> getFiles_test() {
		return files_test;
	}

	public void setFiles_test(Map<String, List<String>> files_test) {
		this.files_test = files_test;
	}

	public Map<String, Integer> getClassFreq() {
		return classFreq;
	}

	public void setClassFreq(Map<String, Integer> classFreq) {
		this.classFreq = classFreq;
	}

	public Map<String, Double> getClassProb() {
		return ClassProb;
	}

	public void setClassProb(Map<String, Double> classProb) {
		ClassProb = classProb;
	}

	public Set<String> getWordDict() {
		return WordDict;
	}

	public void setWordDict(Set<String> wordDict) {
		WordDict = wordDict;
	}

	public Map<String, Map<String, Integer>> getClassFeaFreq() {
		return classFeaFreq;
	}

	public void setClassFeaFreq(Map<String, Map<String, Integer>> classFeaFreq) {
		this.classFeaFreq = classFeaFreq;
	}

	public Map<String, Map<String, Double>> getClassFeaProb() {
		return ClassFeaProb;
	}

	public void setClassFeaProb(Map<String, Map<String, Double>> classFeaProb) {
		ClassFeaProb = classFeaProb;
	}

	public Map<String, Double> getClassDefaultProb() {
		return ClassDefaultProb;
	}

	public void setClassDefaultProb(Map<String, Double> classDefaultProb) {
		ClassDefaultProb = classDefaultProb;
	}
}
