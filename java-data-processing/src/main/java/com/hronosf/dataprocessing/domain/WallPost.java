package com.hronosf.dataprocessing.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
public class WallPost {

    private Integer id;

    private Integer fromId;

    private Integer ownerId;

    private Integer signerId;

    private String text;

    private Integer edited;

    private Integer date;

    private String summary;

    private Map<String, String> personToAttitude;
}
