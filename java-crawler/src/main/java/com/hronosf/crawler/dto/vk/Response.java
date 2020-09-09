package com.hronosf.crawler.dto.vk;

import com.vk.api.sdk.objects.wall.WallpostFull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class Response {

    private Integer count;
    private List<WallpostFull> items;
}
