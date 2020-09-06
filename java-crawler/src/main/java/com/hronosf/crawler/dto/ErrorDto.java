package com.hronosf.crawler.dto;

import lombok.*;

@Getter
@ToString
@NoArgsConstructor
public class ErrorDto {

    private String error_code;
    private String error_msg;
}
