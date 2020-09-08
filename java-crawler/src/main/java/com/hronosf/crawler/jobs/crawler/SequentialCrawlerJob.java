package com.hronosf.crawler.jobs.crawler;

import com.hronosf.crawler.domain.WallPost;
import com.hronosf.crawler.dto.Response;
import com.hronosf.crawler.exceptions.CrawlerException;
import com.hronosf.crawler.repository.CrawledPostRepository;
import com.hronosf.crawler.util.CrawlerStateStorage;
import com.hronosf.crawler.util.DataTransferObject;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@SuppressWarnings("java:S1948")
public class SequentialCrawlerJob implements Runnable {

    private Integer offset = 0;
    private final String domain;
    private Integer postCount = 1;
    private int retryRecursionDepth = 0;
    private final List<WallPost> parsedPosts = new ArrayList<>();
    private final DataTransferObject dataTransfer;

    // constructor:
    public SequentialCrawlerJob(DataTransferObject dataTransfer) {
        this.dataTransfer = dataTransfer;
        domain = dataTransfer.getDomain();
    }

    @Override
    @SneakyThrows
    public void run() {
        // start crawling:
        while (offset < postCount) {
            crawlWallWithOffset();
        }

        log.info("Stop parsing https://vk.com/{}, all post were processed!", domain);
        CrawlerStateStorage.put(domain, 0);

        // join current thread - allow to re-use it by WorkStealingPool to parse next domain:
        Thread.currentThread().join();
    }

    @SneakyThrows
    private void crawlWallWithOffset() {
        // TrashHold - if parsed post count >=500 save to ElasticSearch && clean temporary store:
        if (parsedPosts.size() >= 500) {
            saveToElasticSearch(true);
        }

        // parse new posts if they are && start with saved offset if crawling were interrupted:
        Integer offsetToStart = CrawlerStateStorage.getOffsetToStart(domain);
        if (offsetToStart != null && !parsedPosts.isEmpty()) {
            // delete already saved posts:
            deleteAlreadySavedPostsFromTemporaryStorage();

            // save new posts to Es:
            saveToElasticSearch(false);

            // move offset to position before interruption:
            offset = 0;
            offset += offsetToStart + parsedPosts.size();

            // clean up state:
            CrawlerStateStorage.remove(domain);
        }

        // log start of process with work thread name:
        log.info("Start parsing https://vk.com/{} with offset {}, work-thread {}", domain, offset, Thread.currentThread().getName());
        Response response = null;

        try {
            // make response via VK API wall.get method:
            String content = dataTransfer.getQuery().offset(offset).executeAsString();
            response = dataTransfer.getObjectMapper().readValue(content, Response.class);

            // update post count on the wall:
            if (!postCount.equals(response.getCount())) {
                postCount = response.getCount();
            }
        } catch (Exception ex) {
            // catch and log any exception while proceeding:
            log.info("Failed to parse response with exception {} while crawling https://vk.com/{}, offset : {}, sleep for {} millis and retrying. Attempt {}/3"
                    , ex, domain, offset, dataTransfer.getRequestTimeout(), retryRecursionDepth);

            // sleep before retrying request:
            Thread.sleep(dataTransfer.getRequestTimeout());

            // increment recursion depth trying to crawl offset:
            retryRecursionDepth++;

            // if depth too big - exit from recursion, rollback offset and proceed:
            if (retryRecursionDepth > 3) {
                retryRecursionDepth = 0;

                log.info("Exiting https://vk.com/{} parsing recursion, rolling back offset to {}", domain, offset);
                return;
            }

            // try to parse again:
            crawlWallWithOffset();
        }

        // Parse response:
        if (isResponsePresentAndNoErrorsReturned(response) && isResponseItemsPresent(response)) {
            // map from WallPostFull.java (DTO which contains too much unused info) to Elastic Search entity - CrawledPost.java:
            response.getItems().forEach(item -> parsedPosts.add(dataTransfer.getMapper().fromDto(item)));

        } else if (isResponsePresentAndErrorsReturned(response)) {
            // if response has vk api errors - log and stop:
            log.info("Stop https://vk.com/{} parsing with status code {}, reason \"{}\""
                    , domain, response.getError().getError_code(), response.getError().getError_msg());

            // save crawler state i.e. count of parsed posts to start with later:
            CrawlerStateStorage.put(domain, offset);

            return;
        } else {
            // save crawler state i.e. count of parsed posts to start with later:
            CrawlerStateStorage.put(domain, offset);
            throw new CrawlerException("Response is defected, something went wrong at all!");
        }

        // move offset:
        offset += 100;
    }

    private void saveToElasticSearch(boolean validateData) {
        CrawledPostRepository repository = dataTransfer.getCrawledPostRepository();

        if (validateData) {
            deleteAlreadySavedPostsFromTemporaryStorage();
        }

        // save to elastic Search:
        log.info("Saving to Elastic Search {} documents", parsedPosts.size());
        repository.saveAll(parsedPosts);

        // clear temporary storage:
        parsedPosts.clear();
    }

    private void deleteAlreadySavedPostsFromTemporaryStorage() {
        CrawledPostRepository repository = dataTransfer.getCrawledPostRepository();

        // filter crawled result:
        List<WallPost> alreadySavedPosts = parsedPosts.stream()
                // distinct if we had error and re-parsed offset:
                .distinct()
                // filter posts which we already know:
                .filter(post -> repository.existsById(post.getId()))
                // filter only not edited posts/edited. but we know it:
                .filter(post -> {
                    // if post didn't edited - skip:
                    if (post.getEdited() == null) return true;

                    // if post edited && edit date same as we known:
                    return post.getEdited() != null
                            // ignore "isPresent" i.e. previously we filtered to stay only existing in Es documents => it's present 100%:
                            && repository.findById(post.getId()).get().getEdited().equals(post.getEdited());
                })
                .collect(Collectors.toList());

        // remove what we already know from crawled result:
        parsedPosts.removeAll(alreadySavedPosts);
    }

    private boolean isResponsePresentAndNoErrorsReturned(Response response) {
        return response != null && response.getError() == null;
    }

    private boolean isResponsePresentAndErrorsReturned(Response response) {
        return response != null && response.getError() != null;

    }

    private boolean isResponseItemsPresent(Response response) {
        return response.getItems() != null && !response.getItems().isEmpty();
    }
}
