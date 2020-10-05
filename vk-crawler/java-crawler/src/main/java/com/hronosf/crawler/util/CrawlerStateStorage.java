package com.hronosf.crawler.util;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class CrawlerStateStorage {

    private static final Map<String, String> domainToError = new ConcurrentHashMap<>();
    private static final Map<String, Integer> domainToOffset = new ConcurrentHashMap<>();
    private static final Map<String, Double> domainToProgress = new ConcurrentHashMap<>();

    public static synchronized void saveCrawlerState(String domain, Integer offsetToStart) {
        log.info("Saving crawler state for domain {}, offset is {}", domain, offsetToStart);
        domainToOffset.put(domain, offsetToStart);
    }

    public static synchronized Integer getCrawlerState(String domain) {
        return domainToOffset.get(domain);
    }

    public static synchronized void removeDomainFromCache(String key) {
        domainToOffset.remove(key);
    }

    public static synchronized void updateCrawlerProgress(String domain, ProgressInfo progressInfo) {
        domainToProgress.put(domain, progressInfo.getCurrentOffset() / progressInfo.getTotal() * 100);
    }

    public static Map<String, Double> getCrawlerProgressByDomain() {
        return domainToProgress;
    }

    public static synchronized void saveErrorToNotifyAdmin(String domain, String errorMessage) {
        domainToError.put(domain, errorMessage);
    }

    public static synchronized Map<String, String> getCrawlerErrorsToNotify() {
        return domainToError;
    }
}
