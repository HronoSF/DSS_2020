package com.hronosf.crawler.util;

import com.hronosf.crawler.jobs.crawler.CancelableRunnable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.concurrent.Future;

@Getter
@AllArgsConstructor
public class CrawlerTaskInfo {

    private final Future<?> future;
    private final CancelableRunnable runnable;
}
