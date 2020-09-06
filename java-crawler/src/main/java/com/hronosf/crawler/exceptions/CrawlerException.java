package com.hronosf.crawler.exceptions;

public class CrawlerException extends Exception {

    public CrawlerException(String message, Object... objects) {
        super(String.format(message, objects));
    }
}
