package com.hronosf.crawler.jobs.crawler;

public interface CancelableRunnable extends Runnable {

    void cancel();
}
