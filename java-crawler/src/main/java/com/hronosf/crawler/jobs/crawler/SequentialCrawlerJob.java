package com.hronosf.crawler.jobs.crawler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hronosf.crawler.domain.WallPost;
import com.hronosf.crawler.dto.vk.Response;
import com.hronosf.crawler.dto.vk.VkResponseDto;
import com.hronosf.crawler.services.ElasticSearchWrapperService;
import com.hronosf.crawler.util.BeanUtilService;
import com.hronosf.crawler.util.CrawlerStateStorage;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.ServiceActor;
import com.vk.api.sdk.queries.wall.WallGetQuery;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class SequentialCrawlerJob implements Runnable {

    private final String domain;
    private final Integer timeout;

    // stuff fields:
    private int offset = 0;
    private int wallPostCount = 1;
    private int retryAttemptCount = 1;

    // temporary crawling result storage:
    private final List<WallPost> parsedPosts = new ArrayList<>();

    // "autowired" beans:
    private final WallGetQuery query;
    private final ObjectMapper objectMapper;
    private final ElasticSearchWrapperService elasticSearchWrapperService;

    // constructor:
    public SequentialCrawlerJob(String domain, Integer timeout) {
        this.domain = domain;
        this.timeout = timeout;

        // "autowire" beans:
        objectMapper = BeanUtilService.getBean(ObjectMapper.class);
        elasticSearchWrapperService = BeanUtilService.getBean(ElasticSearchWrapperService.class);

        // build query:
        query = BeanUtilService.getBean(VkApiClient.class)
                .wall()
                .get(BeanUtilService.getBean(ServiceActor.class))
                .domain(domain).count(10);
    }

    @Override
    @SneakyThrows
    public void run() {
        // start crawling:
        while (offset < wallPostCount) {
            crawlWallWithOffset();
        }

        log.info("Stop parsing https://vk.com/{}, all post were processed!", domain);

        // join current thread - allow to re-use it by WorkStealingPool to parse next domain:
        Thread.currentThread().join();
    }

    private void crawlWallWithOffset() throws InterruptedException {
        // TrashHold - if parsed post count >=500 save to ElasticSearch && clean temporary store:
        if (parsedPosts.size() >= 500) {
            saveToElasticSearch();
        }

        // parse new posts if they are && start with saved offset if crawling were interrupted:
        Integer offsetToStart = CrawlerStateStorage.getOffsetToStart(domain);
        if (offsetToStart != null && !parsedPosts.isEmpty()) {
            // save new posts to Es:
            int newPostCount = saveToElasticSearch();

            // move offset to position before interruption:
            offset = 0;
            offset += offsetToStart + newPostCount;

            // clean up state:
            CrawlerStateStorage.remove(domain);
        }

        // log start of process with work thread name:
        log.info("Start parsing https://vk.com/{} with offset {}, work-thread {}", domain, offset, Thread.currentThread().getName());

        try {
            // make response via VK API wall.get method:
            String content = query.offset(offset).executeAsString();
            VkResponseDto vkRestResponse = objectMapper.readValue(content, VkResponseDto.class);

            // Parse response:
            if (isResponseItemsPresent(vkRestResponse)) {
                // map from WallPostFull.java (DTO which contains too much unused info) to Elastic Search entity - CrawledPost.java:
                Response response = vkRestResponse.getResponse();
                parsedPosts.addAll(response.getItems());

                // update post count on the wall:
                Integer postCountOnWall = response.getCount();
                wallPostCount = (wallPostCount != postCountOnWall) ? postCountOnWall : wallPostCount;

            } else if (isResponsePresentButErrorsReturned(vkRestResponse)) {
                // if response has vk api errors - log and stop:
                log.info("Stop https://vk.com/{} parsing with status code {}, reason \"{}\""
                        , domain, vkRestResponse.getError().getError_code(), vkRestResponse.getError().getError_msg());

                // save crawler state i.e. count of parsed posts to start with later:
                CrawlerStateStorage.put(domain, offset);

                // save all that was parsed on current moment to DB:
                saveToElasticSearch();
                return;
            } else {
                // save crawler state i.e. count of parsed posts to start with later:
                CrawlerStateStorage.put(domain, offset);

                return;
            }

            // move offset:
            offset += 100;
        } catch (Exception ex) {
            // catch and log any exception while proceeding:
            log.error("Failed to parse response while crawling https://vk.com/{} with exception: \n{},\n offset : {}, sleep for {} millis and retrying.\n Attempt {}/3"
                    , domain, ex, offset, timeout, retryAttemptCount);

            // if attempt count <3 - trying again:
            if (retryAttemptCount < 3) {
                // increment recursion depth trying to crawl offset:
                retryAttemptCount++;

                // sleep before retrying request:
                Thread.sleep(timeout);
            } else {
                // clear retry attempts count:
                retryAttemptCount = 0;

                log.info("Skipping https://vk.com/{} broken offset {}", domain, offset);

                // move offset:
                offset += 100;
            }
        }
    }

    private int saveToElasticSearch() {
        // save new posts:
        int newPostsCount = 0;

        try {
            newPostsCount = elasticSearchWrapperService.saveOnlyNewOrChangedPosts(parsedPosts);
        } catch (Exception ex) {
            log.error("Data Base error occurred while saving crawled posts:\n {}", ex.getMessage());
        }

        // clear temporary storage:
        parsedPosts.clear();

        return newPostsCount;
    }

    private boolean isResponsePresentButErrorsReturned(VkResponseDto vkResponse) {
        return vkResponse != null && vkResponse.getError() != null;
    }

    private boolean isResponseItemsPresent(VkResponseDto vkResponse) {
        return vkResponse != null && vkResponse.getResponse().getItems() != null && !vkResponse.getResponse().getItems().isEmpty();
    }
}
