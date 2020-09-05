package com.hronosf.crawler.mappers;

import com.hronosf.crawler.configuration.MapStructConfiguration;
import com.hronosf.crawler.domain.WallPost;
import com.vk.api.sdk.objects.wall.WallpostFull;
import org.mapstruct.Mapper;

@Mapper(config = MapStructConfiguration.class)
public interface WallPostMapper {

    WallPost fromDto(WallpostFull dto);
}
