package com.hronosf.crawler.services;

import com.hronosf.crawler.domain.WallPost;

import java.util.List;

public interface ElasticSearchWrapperService {

    int saveOnlyNewOrChangedPosts(List<WallPost> parsedPosts);
}
