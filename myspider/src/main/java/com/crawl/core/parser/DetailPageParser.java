package com.crawl.core.parser;

import com.crawl.zhihu.entity.Page;
import com.crawl.zhihu.entity.ZhihuUserInfo;

public interface DetailPageParser extends Parser {
    ZhihuUserInfo parseDetailPage(Page page);
}
