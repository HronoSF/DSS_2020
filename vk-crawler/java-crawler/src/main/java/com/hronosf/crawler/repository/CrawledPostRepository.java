package com.hronosf.crawler.repository;

import com.hronosf.crawler.domain.WallPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CrawledPostRepository extends ElasticsearchRepository<WallPost, Long> {

    @Query("{ \"ids\": { \"values\": [?0] } }")
    Page<WallPost> findAllById(String ids, Pageable pageable);
}
