package com.hronosf.crawler.jobs;

import com.hronosf.crawler.domain.WallPost;
import com.hronosf.crawler.dto.ResponseDto;
import com.hronosf.crawler.exceptions.CrawlerException;
import com.hronosf.crawler.repository.CrawledPostRepository;
import com.hronosf.crawler.util.CrawlerStateStorage;
import com.hronosf.crawler.util.RecursiveCrawledDataTransferObject;
import com.vk.api.sdk.exceptions.ClientException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@SuppressWarnings("java:S1948")
public class RecursiveSequentialCrawlerJob implements Runnable {

    private Integer offset = 0;
    private int retryRecursionDepth = 0;
    private final List<WallPost> parsedPosts = new ArrayList<>();

    // constructor variables::
    private final RecursiveCrawledDataTransferObject dataTransfer;

    @Override
    public void run() {
        parseWallWithOffset();
    }

    @SneakyThrows
    private void parseWallWithOffset() {
        String domain = dataTransfer.getDomain();


        // crawl new posts && start with saved offset if crawling were interrupted:
        Integer offsetToStart = CrawlerStateStorage.getOffsetToStart(domain);
        if (offsetToStart != null) {
            // delete already saved posts:
            deleteAlreadySavedPostsFromTemporaryStorage();

            // if we have new posts - save to Es:
            if (!parsedPosts.isEmpty()) {
                // save new posts to Es:
                saveToElasticSearch(false);

                // move offset to position before interruption:
                offset += offsetToStart + parsedPosts.size();

                // clean up state:
                CrawlerStateStorage.remove(domain);
            }
        }

        // Trash hold - if parsed post count >=500 save to ElasticSearch && clean temporary store:
        if (parsedPosts.size() >= 500) {
            saveToElasticSearch(true);
        }

        // log start of process with work thread name:
        log.info("Start domain \"{}\" parsing with offset {}, work-thread {}", domain, offset, Thread.currentThread().getName());
        ResponseDto response = null;

        try {
            // make response via VK API wall.get method:
            String content = dataTransfer.getQuery().offset(offset).executeAsString();
            response = dataTransfer.getObjectMapper().readValue(content, ResponseDto.class);

        } catch (ClientException ex) {
            // catch and log any exception while proceeding:
            log.info("Failed to parse response json with exception {} in domain \"{}\", offset : {}, sleep for {} and retrying. Attempt {}/3"
                    , ex, domain, offset, dataTransfer.getRequestTimeout(), retryRecursionDepth);

            // sleep before retrying request:
            Thread.sleep(dataTransfer.getRequestTimeout());

            // increment recursion depth trying to crawl offset:
            retryRecursionDepth++;

            // if depth too big - exit from recursion, rollback offset and proceed:
            if (retryRecursionDepth > 3) {
                offset -= 100;
                retryRecursionDepth = 0;

                log.info("Exiting domain \"{}\" parsing recursion, rolling back offset to {}", domain, offset);
                return;
            }

            // try to parse again:
            parseWallWithOffset();
        }

        // Parse response:
        if (response != null && response.getError() == null) {
            // map from WallPostFull.java (DTO which contains too much unused info) to Elastic Search entity - CrawledPost.java:
            response.getItems().forEach(item -> parsedPosts.add(dataTransfer.getMapper().fromDto(item)));

        } else if (response != null) {
            // if response has vk api errors - log and stop:
            log.info("Stop domain parsing \"{}\" with status code {}, reason \"{}\""
                    , domain, response.getError().getError_code(), response.getError().getError_msg());

            // save crawler state i.e. count of parsed posts to start with later:
            CrawlerStateStorage.put(domain, offset);

            return;
        } else {
            throw new CrawlerException("Response from VK is null, something went wrong at all, crawler dead!");
        }

        // if offset is more or equals count of posts on the wall - stop task:
        if (offset >= response.getCount()) {
            log.info("Stop domain parsing \"{}\", all post were processed!", domain);

            // join current thread - allow to re-use to parse next domain:
            Thread.currentThread().join();
        }

        // move offset and proceed recursion:
        offset += 100;
        parseWallWithOffset();
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
}
