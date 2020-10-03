package com.hronosf.searchengine.services;

import com.hronosf.search.*;

public interface ElasticSearchService {

    TextSearchResponseDTO searchWithText(TestSearchRequestDTO text);

    IdSearchResponseDTO searchWithId(IdSearchRequestDTO text);
}
