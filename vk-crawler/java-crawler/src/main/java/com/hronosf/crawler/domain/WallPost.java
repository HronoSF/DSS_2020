package com.hronosf.crawler.domain;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@NoArgsConstructor
@Document(indexName = "wall_posts")
public class WallPost {

    @Id
    private String id;

    @JsonAlias("from_id")
    private Long fromId;

    @JsonAlias("owner_id")
    private Long ownerId;

    @JsonAlias("signed_id")
    private Long signerId;

    private String text;

    private Long edited;

    private Long date;
}
