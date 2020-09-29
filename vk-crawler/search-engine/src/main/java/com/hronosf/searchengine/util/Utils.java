package com.hronosf.searchengine.util;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hronosf.search.ObjectToRelation;
import com.hronosf.search.WallPost;
import lombok.SneakyThrows;

import java.util.*;
import java.util.stream.Collectors;

public class Utils {

    static ObjectMapper mapper = new ObjectMapper();

    public static WallPost mapToPost(Map<String, Object> map) {
        map = new HashMap<>(map);
        WallPost.Builder builder = WallPost.newBuilder();

        map.computeIfPresent("id", (key, value) -> builder.setId(value.toString()));
        map.computeIfPresent("date", (key, value) -> builder.setDate(value.toString()));
        map.computeIfPresent("text", (key, value) -> builder.setText(value.toString()));
        map.computeIfPresent("edited", (key, value) -> builder.setEdited(value.toString()));
        map.computeIfPresent("fromId", (key, value) -> builder.setFromId(value.toString()));
        map.computeIfPresent("ownerId", (key, value) -> builder.setOwnerId(value.toString()));
        map.computeIfPresent("summary", (key, value) -> builder.setSummary(value.toString()));
        map.computeIfPresent("signerId", (key, value) -> builder.setSignerId(value.toString()));
        map.computeIfPresent("processedIn", (key, value) -> builder.setProcessedIn(value.toString()));
        map.computeIfPresent("relationMap", (key, value) -> builder.addAllRelationMap(stringToMap(value)));

        return builder.build();
    }

    @SneakyThrows
    private static List<ObjectToRelation> stringToMap(Object target) {
        JavaType type = mapper.getTypeFactory().
                constructCollectionType(List.class, Map.class);

        List<Map<String, String>> listMap = mapper.readValue(target.toString(), type);

        Map<String, String> values = listMap.stream().flatMap(map -> map.entrySet().stream())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));


        // Protoc generates object classes => we can not use object mapping i.e. there are no constructors,setters, so vanilla java:
        return values.entrySet().stream()
                .map(entry -> ObjectToRelation.newBuilder()
                        .setObject(entry.getKey())
                        .setRelation(entry.getValue())
                        .build()).collect(Collectors.toList());
    }
}

