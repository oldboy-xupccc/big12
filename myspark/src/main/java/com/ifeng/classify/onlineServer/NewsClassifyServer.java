package com.ifeng.classify.onlineServer;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ifeng.classify.Util.NativeBayes;
import com.ifeng.classify.evaluate.CheckUp;
import com.ifeng.classify.evaluate.predict;
import com.ifeng.classify.trainModel.NBModel;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;



//http://127.0.0.1:8080/server  
public class NewsClassifyServer {
	public static void main(String[] args) {
		HttpServer server = null;
		try {
			InetSocketAddress add = new InetSocketAddress(8082);
			server = HttpServer.create(add, 0);
			server.createContext("/news/classifyServer", new MyHandler());
			server.setExecutor(null);
			server.start();
			System.out.println("Server is listening on port 8082");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}



class MyHandler implements HttpHandler {

	static Logger LOG = Logger.getLogger(MyHandler.class);
	static NativeBayes nativeBayes = NBModel.DeserializeNativeBayes();
	
	public void handle(HttpExchange exchange) {
		URI uri = exchange.getRequestURI();
		String query = null;
		if ("post".equalsIgnoreCase(exchange.getRequestMethod())) {
			try {
				query = IOUtils.toString(exchange.getRequestBody());
//				LOG.info("Query[request Body] :" + decode(query));
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			query = uri.getQuery();
		}
		LOG.info("request Query :" + decode(query));
		Map<String, String> param = this.parseParam(query);
		String titleParam = param.get("title");
		String contentParam = param.get("content");
		String docUrlParam = param.get("docUrl");
		String docIdParam = param.get("docId");
		String docTypeParam = param.get("docType");
		String fromParam = param.get("from");
		
		
		System.out.println("content:" + contentParam);
		
		
		if(StringUtils.isBlank(titleParam)){
			titleParam = "";
		}
		
		if(StringUtils.isBlank(contentParam)){
			contentParam = "";
		}
		
		if(StringUtils.isBlank(docUrlParam)){
			docUrlParam = "";
		}
		
		if(StringUtils.isBlank(docIdParam)){
			docIdParam = "";
		}
		
		if(StringUtils.isBlank(docTypeParam)){
			docTypeParam = "";
		}
		
		if(StringUtils.isBlank(fromParam)){
			fromParam = "";
		}
		
		List<String> pridlist = PredictNewsData(contentParam);
//		List<String> pridlist = PredictNewsDataMap(contentParam);
		System.out.println(pridlist); 
		result(exchange, pridlist.toString());
		
		return;
	}
	
	public static String decode(String con) {
		if (StringUtils.isNotBlank(con)) {
			try {
				con = URLDecoder.decode(con, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return con;
	}
	
	private Map<String, String> parseParam(String query) {
		Map<String, String> param = new HashMap<String, String>();
		for (String pair : query.split("&")) {
			String[] ss = pair.split("=");
			if (ss.length != 1) {
				param.put(ss[0], decode(ss[1]));
			} else {
				param.put(ss[0], "");
			}
		}
		return param;
	}
	
	public void result(HttpExchange exchange, String pridlist){
		JSONObject jobj = new JSONObject();
		jobj.put("categoryList", pridlist);
		String ret = jobj.toJSONString();
		Headers responseHeaders = exchange.getResponseHeaders();
		responseHeaders.set("Content-Type","application/json;charset=utf-8");
		try {
			exchange.sendResponseHeaders(200, ret.getBytes().length);
			OutputStream out = exchange.getResponseBody(); // 获得输出流
			out.write(ret.getBytes());
			out.flush();
			out.close();
			exchange.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return;
	}
	
	public void resultMap(HttpExchange exchange, TreeMap<String , Double> pridMap){
		JSONObject jobj = new JSONObject();
		jobj.put("categoryList", pridMap.toString());
		String ret = jobj.toJSONString();
		Headers responseHeaders = exchange.getResponseHeaders();
		responseHeaders.set("Content-Type","application/json;charset=utf-8");
		try {
			exchange.sendResponseHeaders(200, ret.getBytes().length);
			OutputStream out = exchange.getResponseBody(); // 获得输出流
			out.write(ret.getBytes());
			out.flush();
			out.close();
			exchange.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return;
	}
	
    public List<String> PredictNewsData(String file) {
        List<String> pridlist=new ArrayList<String>();
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
        return pridlist;

    }
	
	
    public List<String> PredictNewsDataMap(String file) {
    	List<String> pridlist=new ArrayList<String>();
    	TreeMap<String, Double> categoryValueMap=new TreeMap<String, Double>();
		
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
            System.out.println("score:"+ score);
            categoryValueMap.put(classname, score);
        }
        
        
    	List<Entry<String, Double>> list = new ArrayList<Entry<String, Double>>(categoryValueMap.entrySet());
    	Collections.sort(list, new Comparator<Entry<String, Double>>() {
    	//降序排序
    	@Override
    	public int compare(Entry<String, Double> o1, Entry<String, Double> o2) {
	    	//return o1.getValue().compareTo(o2.getValue());
	    	return o2.getValue().compareTo(o1.getValue());   // 降序排列
    	        }
    	});
        
        for (Entry<String, Double> mapping : list) {
        	pridlist.add(mapping.getKey() + ":" + mapping.getValue());
        }
        
        
        return pridlist;

    }
	
}

