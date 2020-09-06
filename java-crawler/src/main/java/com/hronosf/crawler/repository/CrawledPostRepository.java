package com.hronosf.crawler.repository;

import com.hronosf.crawler.domain.WallPost;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CrawledPostRepository extends ElasticsearchRepository<WallPost, Long> {
}
