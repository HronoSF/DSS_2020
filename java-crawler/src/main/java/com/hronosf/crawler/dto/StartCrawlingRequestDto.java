package com.hronosf.crawler.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class StartCrawlingRequestDto {

    private Integer userId;
    private String accessToken;
    private List<String> toParse;
}
