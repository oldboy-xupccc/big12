package com.it18zhang.spider.download;

import com.it18zhang.spider.domain.PageInfo;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * HttpClient基础类
 */
public abstract class BaseDownloader {
	private Logger logger = LoggerFactory.getLogger(BaseDownloader.class);

	public InputStream getWebPageInputStream(String url) {
		try {
			CloseableHttpResponse response = DownloadUtil.getResponse(url);
			return response.getEntity().getContent();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 不使用代理时，通过该方法下载
	 */
	public PageInfo downloadPage(String url) throws IOException {
		return downloadPage(url, "UTF-8");
	}

	public PageInfo downloadPage(String url, String charset) throws IOException {
		PageInfo pageInfo = new PageInfo();
		CloseableHttpResponse response = null;
		response = DownloadUtil.getResponse(url);
		pageInfo.setStatusCode(response.getStatusLine().getStatusCode());
		pageInfo.setUrl(url);
		try {
			if (pageInfo.getStatusCode() == 200) {
				pageInfo.setHtml(EntityUtils.toString(response.getEntity(), charset));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				response.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return pageInfo;
	}

	/**
	 * 使用代理时，通过该方法下载
	 * @param request
	 * @return
	 * @throws IOException
	 */
	public PageInfo downloadPage(HttpRequestBase request) throws IOException {
		CloseableHttpResponse response = null;
		response = DownloadUtil.getResponse(request);
		PageInfo pageInfo = new PageInfo();
		pageInfo.setStatusCode(response.getStatusLine().getStatusCode());
		pageInfo.setHtml(EntityUtils.toString(response.getEntity()));
		pageInfo.setUrl(request.getURI().toString());
		return pageInfo;
	}

	/**
	 * 反序列化CookiesStore
	 *
	 * @return
	 */
	public boolean deserializeCookieStore(String path) {
		try {
			CookieStore cookieStore = (CookieStore) DownloadUtil.deserializeObject(path);
			DownloadUtil.setCookieStore(cookieStore);
		} catch (Exception e) {
			logger.warn("反序列化Cookie失败,没有找到Cookie文件");
			return false;
		}
		return true;
	}
}
