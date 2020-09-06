package com.hronosf.crawler.dto;

import com.vk.api.sdk.objects.wall.WallpostFull;
import lombok.*;

import java.util.List;

@Getter
@ToString
@NoArgsConstructor
public class ResponseDto {

    private ErrorDto error;
    private Integer count;
    private List<WallpostFull> items;
}
