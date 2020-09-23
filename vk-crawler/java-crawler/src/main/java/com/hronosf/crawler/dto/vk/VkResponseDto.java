package com.hronosf.crawler.dto.vk;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class VkResponseDto {

    private Error error;
    private Response response;
}
