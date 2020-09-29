package com.hronosf.searchengine.services;

import com.hronosf.search.SearchRequest;
import com.hronosf.search.SearchResponse;

public interface ElasticSearchService {

    SearchResponse searchWithText(SearchRequest text);
}
