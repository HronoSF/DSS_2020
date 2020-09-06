package com.hronosf.crawler.jobs;

import com.hronosf.crawler.domain.WallPost;
import com.hronosf.crawler.util.RecursiveCrawledDataTransferObject;
import com.vk.api.sdk.actions.Execute;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class ExecuteCrawlerJob implements Runnable {

    private Integer offset = 0;
    private final List<WallPost> parsedPosts = new ArrayList<>();

    // constructor variables::
    private final Execute execute;
    private final RecursiveCrawledDataTransferObject dataTransfer;

    @Override
    public void run() {

    }

    @SneakyThrows
    private void parseWallWithOffset() {
//        JsonElement response = execute.code(actor, "return API.wall.get({"count": 1})");
    }
}
