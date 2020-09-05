package com.hronosf.crawler.repository;

import com.hronosf.crawler.domain.WallPost;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface WallPostRepository extends ElasticsearchRepository<WallPost, Long> {
}
