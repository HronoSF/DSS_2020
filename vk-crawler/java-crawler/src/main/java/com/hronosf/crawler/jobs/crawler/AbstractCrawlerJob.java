package com.hronosf.crawler.jobs.crawler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hronosf.crawler.domain.WallPost;
import com.hronosf.crawler.dto.vk.Response;
import com.hronosf.crawler.dto.vk.VkResponseDto;
import com.hronosf.crawler.exceptions.CrawlerException;
import com.hronosf.crawler.services.ElasticSearchWrapperService;
import com.hronosf.crawler.util.*;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.mutable.MutableBoolean;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public abstract class AbstractCrawlerJob implements CancelableRunnable {

    protected final String domain;
    protected final Integer timeout;

    protected int offset = 0;
    protected int wallPostCount = 1;
    protected int retryAttemptCount = 1;
    protected final MutableBoolean isTaskStopped = new MutableBoolean();

    // "autowired" beans:
    protected final ObjectMapper objectMapper;
    protected final ElasticSearchWrapperService elasticSearchWrapperService;

    // temporary crawling result storage:
    protected final List<WallPost> parsedPosts = new ArrayList<>();

    public AbstractCrawlerJob(String domain, Integer timeout) {
        this.domain = domain;
        this.timeout = timeout;

        // "autowire" beans:
        objectMapper = BeanUtilService.getBean(ObjectMapper.class);
        elasticSearchWrapperService = BeanUtilService.getBean(ElasticSearchWrapperService.class);
    }

    protected abstract void moveOffset();

    protected abstract VkResponseDto executeCrawlingLogic() throws ClientException, JsonProcessingException, ApiException;

    @Override
    @SneakyThrows
    public void run() {
        // start crawling:
        isTaskStopped.setFalse();
        while (!isTaskStopped.booleanValue() && offset < wallPostCount) {
            crawlWallWithOffset();
        }

        log.info("Stop parsing https://vk.com/{}", domain);

        // join crawler thread to re-use it for another domain after parsing stopped:
        Thread.currentThread().join();
    }

    @Override
    public void cancel() {
        isTaskStopped.setTrue();
    }

    protected void crawlWallWithOffset() throws InterruptedException {
        try {
            // TrashHold - if parsed post count >=500 save to ElasticSearch && clean temporary store:
            if (parsedPosts.size() >= 500) {
                saveToElasticSearch();
            }

            // parse new posts if they are && start with saved offset if crawling were interrupted:
            Integer offsetToStart = CrawlerStateStorage.getCrawlerState(domain);
            if (offsetToStart != null && !parsedPosts.isEmpty()) {
                // save new posts to Es:
                int newPostCount = saveToElasticSearch();

                // move offset to position before interruption:
                offset = 0;
                offset += offsetToStart + newPostCount;

                // clean up state:
                CrawlerStateStorage.removeDomainFromCache(domain);
            }

            log.info("Execution in thread {}:", Thread.currentThread().getName());
            VkResponseDto vkRestResponse = executeCrawlingLogic();

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
                CrawlerStateStorage.saveCrawlerState(domain, offset);

                // save all that was parsed on current moment to DB:
                saveToElasticSearch();
                return;
            } else {
                // save crawler state i.e. count of parsed posts to start with later:
                CrawlerStateStorage.saveCrawlerState(domain, offset);
                return;
            }
            // move offset:
            moveOffset();

        } catch (ClientException ex) {
            // catch and log any exception while proceeding:
            log.error("Failed to request while crawling https://vk.com/{} with exception: \n{},\n offset : {}, sleep for {} millis and retrying.\n Attempt {}/3"
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
                moveOffset();
            }
        } catch (Exception ex) {
            String exceptionMessage = ex.getMessage();
            log.info("Exception occurred while crawling https://vk.com/{}, offset {}: \n{}", domain, offset, exceptionMessage);

            // save error to send admin report:
            CrawlerStateStorage.saveErrorToNotifyAdmin(domain, exceptionMessage);

            // stop crawler:
            isTaskStopped.setTrue();
        }
        // if after retrying was success:
        retryAttemptCount = 0;

        // save progress:
        CrawlerStateStorage.updateCrawlerProgress(domain, new ProgressInfo().setCurrentOffset(offset).setTotal(wallPostCount));
    }

    private int saveToElasticSearch() throws CrawlerException {
        // save new posts:
        int newPostsCount;

        try {
            newPostsCount = elasticSearchWrapperService.saveOnlyNewOrChangedPosts(parsedPosts);
        } catch (Exception ex) {
            throw new CrawlerException("DB error occurred, crawler will be stop. Fix db issue! \n{}", ex.getMessage());
        }

        // clear temporary storage:
        parsedPosts.clear();

        return newPostsCount;
    }

    private boolean isResponsePresentButErrorsReturned(VkResponseDto vkResponse) {
        return vkResponse != null && vkResponse.getError() != null;
    }

    private boolean isResponseItemsPresent(VkResponseDto vkResponse) {
        return vkResponse != null && vkResponse.getResponse() != null &&
                vkResponse.getResponse().getItems() != null && !vkResponse.getResponse().getItems().isEmpty();
    }
}
