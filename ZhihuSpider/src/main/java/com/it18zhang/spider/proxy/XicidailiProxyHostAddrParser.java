package com.it18zhang.spider.proxy;

import com.it18zhang.spider.util.Constants;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * 西刺代理
 */
public class XicidailiProxyHostAddrParser implements ProxyHostAddrParser{

	public List<ProxyHost> parse(String html) {
		Document document = Jsoup.parse(html);
		Elements elements = document.select("table[id=ip_list] tr[class]");
		List<ProxyHost> proxyHosts = new ArrayList<ProxyHost>(elements.size());
		for (Element element : elements) {
			String ip = element.select("td:eq(1)").first().text();
			String port = element.select("td:eq(2)").first().text();
			String isAnonymous = element.select("td:eq(4)").first().text();
			if (!anonymous || isAnonymous.contains("匿")) {
				proxyHosts.add(new ProxyHost(ip, Integer.valueOf(port), Constants.SPIDER_IP_ACCESS_INTERVAL_MS));
			}
		}
		return proxyHosts;
	}
}
