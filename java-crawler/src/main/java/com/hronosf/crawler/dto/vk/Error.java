package com.hronosf.crawler.dto.vk;

import lombok.*;

@Getter
@ToString
@NoArgsConstructor
public class Error {

    private String error_code;
    private String error_msg;
}
