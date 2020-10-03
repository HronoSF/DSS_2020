package com.hronosf.searchengine.util;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hronosf.search.*;
import lombok.SneakyThrows;
import org.apache.spark.api.java.JavaRDD;

import java.util.*;
import java.util.stream.Collectors;

public class Utils {

    static ObjectMapper mapper = new ObjectMapper();

    public static WallPost mapToFullSearchWallPost(Map<String, Object> map) {
        map = new HashMap<>(map);
        WallPost.Builder builder = WallPost.newBuilder();

        map.computeIfPresent("id", (key, value) -> builder.setId(value.toString()));
        map.computeIfPresent("date", (key, value) -> builder.setDate(Long.parseLong(value.toString())));
        map.computeIfPresent("text", (key, value) -> builder.setText(value.toString()));
        map.computeIfPresent("edited", (key, value) -> builder.setEdited(Long.parseLong(value.toString())));
        map.computeIfPresent("fromId", (key, value) -> builder.setFromId(Long.parseLong(value.toString())));
        map.computeIfPresent("ownerId", (key, value) -> builder.setOwnerId(Long.parseLong(value.toString())));
        map.computeIfPresent("summary", (key, value) -> builder.setSummary(value.toString()));
        map.computeIfPresent("signerId", (key, value) -> builder.setSignerId(Long.parseLong(value.toString())));
        map.computeIfPresent("processedIn", (key, value) -> builder.setProcessedIn(Long.parseLong(value.toString())));
        map.computeIfPresent("relationMap", (key, value) -> builder.addAllRelationMap(stringToMap(value)));

        return builder.build();
    }

    public static IdSearchResponseDTO mapToIdSearchWallPost(String userId, JavaRDD<Map<String, Object>> extractedDocs) {
        IdSearchResponseDTO.Builder builder = IdSearchResponseDTO.newBuilder();

        List<ObjectToRelation> relationMaps = extractedDocs
                .map(data -> data.get("relationMap").toString())
                .map(Utils::stringToMap)
                .collect()
                .stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());

        builder.addAllRelationMap(relationMaps);
        builder.setFromId(Long.parseLong(userId));
        builder.setSignerId(Long.parseLong(userId));

        return builder.build();
    }

    @SneakyThrows
    private static List<ObjectToRelation> stringToMap(Object target) {
        JavaType type = mapper.getTypeFactory().
                constructMapType(Map.class, String.class, String.class);

        Map<String, String> listMap = mapper.readValue(target.toString(), type);

        // Protoc generates object classes => we can not use object mapping i.e. there are no constructors,setters, so vanilla java:
        return listMap.entrySet().stream()
                .map(entry -> ObjectToRelation.newBuilder()
                        .setObject(entry.getKey())
                        .setRelation(entry.getValue())
                        .build()).collect(Collectors.toList());
    }
}

