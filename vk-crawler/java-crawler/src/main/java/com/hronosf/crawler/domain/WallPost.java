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
    private String fromId;

    @JsonAlias("owner_id")
    private String ownerId;

    @JsonAlias("signed_id")
    private String signerId;

    private String text;

    private String edited;

    private String date;
}
