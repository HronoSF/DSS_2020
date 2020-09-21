package com.hronosf.crawler.dto.vk;

import com.hronosf.crawler.domain.WallPost;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class Response {

    private Integer count;
    private List<WallPost> items;
}
