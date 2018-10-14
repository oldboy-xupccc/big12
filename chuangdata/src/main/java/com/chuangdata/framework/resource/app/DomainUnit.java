package com.chuangdata.framework.resource.app;

import com.google.common.base.Strings;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author luxiaofeng
 */
public class DomainUnit {
    private int id;
    private String domain;
    private String searchUri;
    private Pattern searchUriPattern;

    public DomainUnit(int id, String domain, String searchUri) {
        this.id = id;
        this.domain = domain;
        this.searchUri = searchUri;
        if (!Strings.isNullOrEmpty(searchUri)) {
            searchUriPattern = Pattern.compile(searchUri);
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getSearchUri() {
        return searchUri;
    }

    public void setSearchUri(String searchUri) {
        this.searchUri = searchUri;
    }

    public boolean isMatch(String uri) {
        if (this.searchUriPattern != null) {
            Matcher m = this.searchUriPattern.matcher(uri);
            if (m.find()) {
                return true;
            }
            return false;
        }
        return true;
    }
}
