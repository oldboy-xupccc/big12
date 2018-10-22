package com.ifeng.classify.evaluate;

import java.util.List;

public class CheckUp {
	/**
     * 计算准确率
     * @param reallist 真实类别
     * @param pridlist 预测类别
     */
    public static void Evaluate(List<String> reallist, List<String> pridlist){
        double correctNum = 0.0;
        for (int i = 0; i < reallist.size(); i++) {
            if(reallist.get(i) == pridlist.get(i)){
                correctNum += 1;
            }
        }
        double accuracy = correctNum / reallist.size();
        System.out.println("准确率为：" + accuracy);
    }

    /**
     * 计算精确率和召回率
     * @param reallist
     * @param pridlist
     * @param classname
     */
    public static void CalPreRec(List<String> reallist, List<String> pridlist, String classname){
        double correctNum = 0.0;
        double allNum = 0.0;//测试数据中，某个分类的文章总数
        double preNum = 0.0;//测试数据中，预测为该分类的文章总数

        for (int i = 0; i < reallist.size(); i++) {
            if(reallist.get(i) == classname){
                allNum += 1;
                if(reallist.get(i) == pridlist.get(i)){
                    correctNum += 1;
                }
            }
            if(pridlist.get(i) == classname){
                preNum += 1;
            }
        }
        System.out.println(classname + " 精确率(跟预测分类比较):" + correctNum / preNum + " 召回率（跟真实分类比较）:" + correctNum / allNum);
    }
}
