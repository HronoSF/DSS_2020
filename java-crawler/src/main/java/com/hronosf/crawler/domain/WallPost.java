package com.hronosf.crawler.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@NoArgsConstructor
@Document(indexName = "wall_posts")
public class WallPost {

    @Id
    private Integer id;

    private String fromId;

    private Integer ownerId;

    private String signerId;

    private String text;

    private Integer edited;

    private Long date;
}
