package com.crawl.zhihu.support;


import com.crawl.core.util.Constants;
import com.crawl.zhihu.ZhiHuHttpClient;
import com.crawl.zhihu.entity.Page;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class PicAnswerTaskTest {

    public void testHandle(){
        Page page = null;
        try {
            String url = String.format(Constants.USER_ANSWER_URL, "WxzxzW");
            HttpRequestBase request = new HttpGet(url);
//            request.setHeader("authorization", "oauth " + ZhiHuHttpClient.getAuthorization());
            page = ZhiHuHttpClient.getInstance().getWebPage(request);
            PicAnswerTask picAnswerTask = new PicAnswerTask();
            picAnswerTask.handle(page);
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }
    @Test
    public void test1(){
        System.out.println((int)'0');
        System.out.println((int)'1');
        System.out.println((int)'2');
        System.out.println((int)'3');
        System.out.println((int)'4');
        System.out.println((int)'0');
        System.out.println((int)'9');
        System.out.println((int)'a');
        System.out.println((int)'b');
    }
}
