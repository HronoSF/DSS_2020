package com.hronosf.crawler.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CrawlerStateStorage {

    private static final Map<String, Integer> domainToOffset = new ConcurrentHashMap<>();

    public static synchronized void put(String domain, Integer offsetToStart) {
        domainToOffset.put(domain, offsetToStart);
    }

    public static synchronized Integer getOffsetToStart(String domain) {
        return domainToOffset.get(domain);
    }

    public static synchronized void remove(String key) {
        domainToOffset.remove(key);
    }
}
