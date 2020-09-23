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
    private Integer id;

    @JsonAlias("from_id")
    private Integer fromId;

    @JsonAlias("owner_id")
    private Integer ownerId;

    @JsonAlias("signed_id")
    private Integer signerId;

    private String text;

    private Integer edited;

    private Integer date;
}
