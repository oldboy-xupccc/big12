package com.oldboy.spark.bayes;

import java.io.*;
import java.util.*;

/**
 *
 */
public class ClassifyDemo {
	public static void main(String[] args) throws Exception {
		//1.切割数据集成为训练集合和测试集合
		Map[] split = splitData(0.8f);
		Map<String,List<String>> trainData = split[0] ;
		Map<String,List<String>> testData = split[1] ;

		//2.训练模型
		NaiveBayesModel model = trainModel(trainData) ;

		//3.对测试数据进行预测，评判模型的好坏
		List[] preArr = predictModel(testData , model) ;

		//4.验证
		for(String cate : model.getCategoryRatioMap().keySet()){
			recallRatio(preArr , cate);
		}


//		for(int i = 0 ; i < preArr[0].size() ; i ++){
//			System.out.println(preArr[0].get(i) + " : " + preArr[1].get(i));
//		}

		System.out.println("");
	}

	/**
	 * 计算指定类别下的召回率和精确率
	 */
	private static void recallRatio(List[] preArr , String category) {
		//真实类别集
		List<String> real=preArr[0] ;
		//预测类别
		List<String> predict = preArr[1] ;
		int realNum =0 ;
		int hit = 0 ;
		int preNum = 0 ;
		for(int i = 0 ; i < real.size() ; i ++){
			//是指定的类别
			if(real.get(i).equals(category)){
				realNum ++ ;
				if(predict.get(i).equals(category)){
					hit ++ ;
				}
			}
			if(predict.get(i).equals(category)){
				preNum ++ ;
			}
		}
		System.out.printf("%s : recall = %f , precision = %f\r\n", category , (double) hit / realNum, (double) hit / preNum);
	}

	/**
	 * 预测数据
	 */
	private static List[] predictModel(Map<String, List<String>> testData, NaiveBayesModel model) {
		//实际分类
		List<String> categories_real = new ArrayList<String>() ;
		//预测分类
		List<String> categories_predict = new ArrayList<String>() ;

		//1.迭代测试集合
		for(Map.Entry<String,List<String>> e : testData.entrySet()){
			//真实类别
			String category_real = e.getKey() ;
			//该类别下的文档集合
			List<String> docs = e.getValue() ;
			for(String doc : docs){
				//添加分类到集合中
				categories_real.add(category_real);

				//临时
				List<String> tmpCategories = new ArrayList<String>() ;
				//预测分支
				List<Double> scores = new ArrayList<Double>() ;

				//计算一片文章在所有分类下的评分值
				for(Map.Entry<String, Double> ee : model.getCategoryRatioMap().entrySet()){
					String category0 = ee.getKey() ;
					tmpCategories.add(category0) ;

					double score = Math.log(ee.getValue()) ;
					String[] words = doc.split(" ");
					//每个单词
					for(String word : words){
						if(!model.getWordsSet().contains(word)){
							continue;
						}
						if(model.getCategroyWordRatioMap().get(category0).containsKey(word)){
							score = score + Math.log(model.getCategroyWordRatioMap().get(category0).get(word));
						}
						else{
							score = score + Math.log(model.getCategroyDefaultRatio().get(category0));
						}
					}
					scores.add(score);
				}

				//找出最可能的分类
				double maxValue = Collections.max(scores) ;
				//得到预测分类
				String predictCategory = tmpCategories.get(scores.indexOf(maxValue));
				categories_predict.add(predictCategory) ;
			}
		}
		List[] arr = new List[2] ;
		arr[0] = categories_real ;
		arr[1] = categories_predict ;
		return arr ;
	}

	/**
	 * 训练模型 ,
	 * 训练数据集的size就是总的文档数。
	 */
	private static NaiveBayesModel trainModel(Map<String, List<String>> trainData) {

		NaiveBayesModel model = new NaiveBayesModel();

		//1.统计出所有单词个数
		for(Map.Entry<String , List<String>> e: trainData.entrySet() ){
			//分类
			String category = e.getKey() ;

			//该分类下的所有文章
			List<String> docs = e.getValue() ;

			//计算分类下的文档个数
			model.getCategoryDocsMap().put(category , docs.size()) ;
			//初始化每个分类的单词个数集合
			Map<String, Integer> wordCountMap = new HashMap<String, Integer>() ;
			//放置每个单词统计集合到类别下
			model.getCategoryWordCountMap().put(category , wordCountMap) ;

			//每行都是一片文章
			for(String doc : docs){
				String[] words = doc.split(" ");
				for(String word :words){
					//添加单词到词汇字典中(去重的)
					model.getWordsSet().add(word) ;
					if(wordCountMap.containsKey(word) ){
						wordCountMap.put(word , wordCountMap.get(word) + 1) ;
					}
					else{
						wordCountMap.put(word , 1) ;
					}
				}
			}
		}

		/*****************************************************
		 ******************  2.统计各分类的文档占比   ***********
		 *****************************************************/
		//2.1统计每个分类的比例
		Map<String , Integer> map = model.getCategoryDocsMap();
		//计算总文档数
		int allDocs = 0 ;
		for(Integer docsNum : map.values()){
			allDocs = allDocs + docsNum ;
		}

		//计算每个分类文档数占总文档数的比例
		for (Map.Entry<String,Integer> e : model.getCategoryDocsMap().entrySet()) {
			String category = e.getKey() ;
			Integer docs = e.getValue() ;
			model.getCategoryRatioMap().put(category , (double)docs / allDocs ) ;
		}

		/*****************************************************
		 ******************  3.统计各分类下的单词数占比***********
		 *****************************************************/
		for(Map.Entry<String,Map<String,Integer>> e : model.getCategoryWordCountMap().entrySet()){
			//得到类别
			String category = e.getKey() ;
			//类别每个单词的次数Map
			Map<String,Integer> wordCountMap = e.getValue() ;

			//存放每个类别的单词统计比例集合
			Map<String,Double> wordRatioMap = new HashMap<String, Double>() ;
			model.getCategroyWordRatioMap().put(category , wordRatioMap) ;


			//计算该类别下所有单词的总数
			double allWordCount = 0 ;
			for(Integer i : wordCountMap.values()){
				allWordCount = allWordCount + i ;
			}

			//拉普拉斯平滑
			allWordCount = allWordCount + model.getWordsSet().size() * model.getDefaultFreq() ;

			//计算每个单词的出现比例
			for (Map.Entry<String, Integer> ee : wordCountMap.entrySet()) {
				String word =  ee.getKey() ;
				Integer count = ee.getValue() ;
				wordRatioMap.put(word , (double)(count + model.getDefaultFreq()) / allWordCount );
			}

			//每个分类的默认比例
			model.getCategroyDefaultRatio().put(category, model.getDefaultFreq() / allWordCount) ;
		}

		return model ;
	}

	/**
	 * 切割数据
	 */
	private static Map<String,List<String>>[] splitData(float split) throws Exception {
		//训练数据{体育->[article1,article2,,, , 娱乐->[...]]}
		Map<String, List<String>> trainData = new HashMap<String, List<String>>() ;

		//测试数据
		Map<String, List<String>> testData = new HashMap<String, List<String>>() ;

		String dataDir = "F:\\big12\\big12-徐-10recomm-day01(李)\\data\\data" ;
		File dir = new File(dataDir);
		File[] files = dir.listFiles();
		Random r = new Random(1000) ;

		//迭带每个文件
		for(File f : files){
			String fname = f.getName();
			//得到分类
			String category = fname.substring(0 , fname.indexOf(".")) ;

			//在测试集合和训练集中同时存放分类
			trainData.put(category , new LinkedList<String>()) ;
			testData.put(category , new LinkedList<String>()) ;

			//读取每个文件
			FileInputStream in = new FileInputStream(f) ;
			BufferedReader br = new BufferedReader(new InputStreamReader(in)) ;
			String line = null;

			while((line = br.readLine()) != null){
				float rn = r.nextFloat() ;
				if(rn < split){
					trainData.get(category).add(line) ;
				}
				else{
					testData.get(category).add(line) ;
				}
			}
			br.close();
			in.close();
		}
		Map[] res = new Map[2];
		res[0] = trainData;
		res[1] = testData;
		return res;
	}
}
