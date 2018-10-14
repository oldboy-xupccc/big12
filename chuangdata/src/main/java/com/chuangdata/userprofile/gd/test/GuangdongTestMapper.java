package com.chuangdata.userprofile.gd.test;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;

/** 
 * @author luxiaofeng
 */
public class GuangdongTestMapper extends Mapper<Object, Text, Text, Text> {
	private static final Set<String> SEARCH_HOST = new HashSet<String>();
	private static final List<String> keywords = new ArrayList<String>();
	private MultipleOutputs<Text, Text> multipleOutputs;
	static {
		SEARCH_HOST.add("m.baidu.com");
		SEARCH_HOST.add("www.sogou.com");
		SEARCH_HOST.add("wap.sogou.com");
		SEARCH_HOST.add("sg.search.yahoo.com");
		SEARCH_HOST.add("search.yahoo.com");
		SEARCH_HOST.add("www.youdao.com");
		SEARCH_HOST.add("www.haosou.com");
		SEARCH_HOST.add("m.haosou.com");
		SEARCH_HOST.add("www.google.com.hk");
		SEARCH_HOST.add("cn.bing.com");
		SEARCH_HOST.add("www.baidu.com");
	}
	
	static {
		keywords.add("长隆");
		keywords.add("changlong");
		keywords.add("车贷");
		keywords.add("车险");
		keywords.add("4s店");
		keywords.add("4S店");
		keywords.add("车展");
		keywords.add("车讯网");
		keywords.add("易贷网");
		keywords.add("融360");
		keywords.add("银率网");
		keywords.add("网通社");
		keywords.add("购车网");
		keywords.add("车主之家");
		keywords.add("汽车报价");
		keywords.add("好车e贷");
		keywords.add("宜车贷");
		keywords.add("好车贷");
		keywords.add("起亚");
		int size = keywords.size();
		for (int i=0; i<size; i++) {
			keywords.add(URLEncoder.encode(keywords.get(i)));
		}
	}
	
	private Text emptyText = new Text();
	
	public void setup(Context context) {
		multipleOutputs = new MultipleOutputs<Text, Text>(context);
	}
	
	public void map(Object key, Text value, Context context) {
		try {
			String log = value.toString();
			String[] fields = log.split("\\|");
			String host = fields[49];
			String url = fields[50];
			if (SEARCH_HOST.contains(host)) {
				if (keywords(url)) {
					multipleOutputs.write(value, emptyText, "keywords/part");
				}
			}
			String serviceId = fields[27];
			if (serviceId.equals("1148") || host.contains("autohome")) {
				// 汽车之家
				multipleOutputs.write(value, emptyText, "autohome/part");
			}
		} catch (Exception e) {
			// TODO
			context.getCounter("GuangdongTestException", e.getClass().getName()).increment(1L);;
		}
	}
	
	private boolean keywords(String uri) {
		try {
			String utf8 = URLDecoder.decode(uri, "UTF-8");
			for (String kw : keywords) {
				if (utf8.contains(kw)) {
					return true;
				}
			}
		} catch (UnsupportedEncodingException e) {
			// DO NOTHING
		}
		try {
			String gbk = URLDecoder.decode(uri, "GBK");
			for (String kw : keywords) {
				if (gbk.contains(kw)) {
					return true;
				}
			}
		} catch (UnsupportedEncodingException e) {
			// DO NOTHING
		}
		return false;
	}

}
