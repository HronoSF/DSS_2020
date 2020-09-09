package com.hronosf.crawler.services.impl;

import com.hronosf.crawler.domain.WallPost;
import com.hronosf.crawler.repository.CrawledPostRepository;
import com.hronosf.crawler.services.ElasticSearchWrapperService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ElasticSearchWrapperServiceImpl implements ElasticSearchWrapperService {

    private final CrawledPostRepository repository;

    @Override
    public int saveOnlyNewOrChangedPosts(List<WallPost> parsedPosts) {
        String ids = parsedPosts.stream().map(post -> post.getId().toString()).collect(Collectors.joining(","));

        // query ES to get existing post by id set:
        Map<Long, WallPost> existedPosts = repository.findAllById(ids, PageRequest.of(0, 100))
                .getContent().stream()
                .collect(Collectors.toMap(WallPost::getId, Function.identity()));

        // filter crawled result:
        if (!existedPosts.isEmpty()) {
            List<WallPost> alreadySavedPosts = parsedPosts.stream()
                    // distinct if we had error and re-parsed offset:
                    .distinct()
                    // filter posts which we already know:
                    .filter(post -> existedPosts.get(post.getId()) != null)
                    // filter only not edited posts/edited. but we know it:
                    .filter(post -> {
                        // if post didn't edited - skip:
                        if (post.getEdited() == null) return true;

                        // if post edited && edit date same as we known:
                        return post.getEdited() != null
                                // ignore "isPresent" i.e. previously we filtered to stay only existing in Es documents => it's present 100%:
                                && existedPosts.get(post.getId()).getEdited().equals(post.getEdited());
                    })
                    .collect(Collectors.toList());

            // remove what we already know from crawled result:
            parsedPosts.removeAll(alreadySavedPosts);
        }

        // save posts:
        repository.saveAll(parsedPosts);

        log.info("Saving {} new/updated crawled posts", parsedPosts.size());

        return parsedPosts.size();
    }
}
