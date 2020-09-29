package com.hronosf.searchengine.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.*;
import org.springframework.util.FileCopyUtils;

import java.io.*;

import static java.nio.charset.StandardCharsets.UTF_8;

@Slf4j
public class ResourceReader {

    public static String readFileToString(String path) {
        ResourceLoader resourceLoader = new DefaultResourceLoader();
        Resource resource = resourceLoader.getResource(path);
        return asString(resource);
    }

    public static String asString(Resource resource) {
        try (Reader reader = new InputStreamReader(resource.getInputStream(), UTF_8)) {
            return FileCopyUtils.copyToString(reader);
        } catch (IOException e) {
            log.error("Exception while reading query from file: \n {}", e.getMessage());
            throw new UncheckedIOException(e);
        }
    }
}
