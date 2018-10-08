package com.crawl.zhihu.parser.tourist;

import com.crawl.core.parser.DetailPageParser;
import com.crawl.zhihu.parser.ZhiHuNewUserDetailPageParser;
import com.crawl.zhihu.entity.Page;
import com.crawl.zhihu.entity.ZhihuUserInfo;
import com.crawl.core.util.HttpClientUtil;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;


public class ZhiHuNewZhihuUserInfoDetailPageParserTest {
    @Test
    public void testParse(){
        /**
         * 新版本页面
         */
//        String url = "https://www.zhihu.com/people/cheng-yi-nan/following";
//        String url = "https://www.zhihu.com/people/excited-vczh/following";
        Page page = new Page();
        String url = "https://www.zhihu.com/people/Vincen.t/following";
        try {
            page.setHtml(HttpClientUtil.getWebPage(url));
        } catch (IOException e) {
            e.printStackTrace();
        }
        page.setUrl(url);
        DetailPageParser parser = ZhiHuNewUserDetailPageParser.getInstance();
        ZhihuUserInfo zhihuUserInfo = parser.parseDetailPage(page);
        Assert.assertNotNull(zhihuUserInfo.getUsername());
    }
}