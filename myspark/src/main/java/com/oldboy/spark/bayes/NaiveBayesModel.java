package com.oldboy.spark.bayes;

import java.io.Serializable;
import java.util.*;

/**
 * 朴素贝叶斯模型
 */
public class NaiveBayesModel implements Serializable {

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

	//数据集
	private Map<String, List<String>> allData = new HashMap<String, List<String>>();

	//训练数据集
	private Map<String, List<String>> trainData = new HashMap<String, List<String>>();

	//测试数据集
	private Map<String, List<String>> testData = new HashMap<String, List<String>>();

	/**
	 * 每个分类和文档数映射
	 */
	private Map<String, Integer> categoryDocsMap = new HashMap<String, Integer>();

	/**
	 * 分类百分比     先验概率 p(yi)
	 */
	private Map<String, Double> categoryRatioMap = new HashMap<String, Double>();

	/**
	 * 特征总数 , 词汇的总集合
	 */
	private Set<String> WordsSet = new HashSet<String>();

	/**
	 * 每个分类中每个特征的频数
	 */
	private Map<String, Map<String, Integer>> categoryWordCountMap = new HashMap<String, Map<String, Integer>>();

	/**
	 * 每个分类中每个特征的概率    p(xi/yi)
	 */
	private Map<String, Map<String, Double>> categroyWordRatioMap = new HashMap<String, Map<String, Double>>();

	/**
	 * 每个分类默认的概率
	 */
	private Map<String, Double> categroyDefaultRatio = new HashMap<String, Double>();

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

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

	public Map<String, List<String>> getAllData() {
		return allData;
	}

	public void setAllData(Map<String, List<String>> allData) {
		this.allData = allData;
	}

	public Map<String, List<String>> getTrainData() {
		return trainData;
	}

	public void setTrainData(Map<String, List<String>> trainData) {
		this.trainData = trainData;
	}

	public Map<String, List<String>> getTestData() {
		return testData;
	}

	public void setTestData(Map<String, List<String>> testData) {
		this.testData = testData;
	}

	public Map<String, Integer> getCategoryDocsMap() {
		return categoryDocsMap;
	}

	public void setCategoryDocsMap(Map<String, Integer> categoryDocsMap) {
		this.categoryDocsMap = categoryDocsMap;
	}

	public Map<String, Double> getCategoryRatioMap() {
		return categoryRatioMap;
	}

	public void setCategoryRatioMap(Map<String, Double> categoryRatioMap) {
		this.categoryRatioMap = categoryRatioMap;
	}

	public Set<String> getWordsSet() {
		return WordsSet;
	}

	public void setWordsSet(Set<String> wordsSet) {
		WordsSet = wordsSet;
	}

	public Map<String, Map<String, Integer>> getCategoryWordCountMap() {
		return categoryWordCountMap;
	}

	public void setCategoryWordCountMap(Map<String, Map<String, Integer>> categoryWordCountMap) {
		this.categoryWordCountMap = categoryWordCountMap;
	}

	public Map<String, Map<String, Double>> getCategroyWordRatioMap() {
		return categroyWordRatioMap;
	}

	public void setCategroyWordRatioMap(Map<String, Map<String, Double>> categroyWordRatioMap) {
		this.categroyWordRatioMap = categroyWordRatioMap;
	}

	public Map<String, Double> getCategroyDefaultRatio() {
		return categroyDefaultRatio;
	}

	public void setCategroyDefaultRatio(Map<String, Double> categroyDefaultRatio) {
		this.categroyDefaultRatio = categroyDefaultRatio;
	}
}
