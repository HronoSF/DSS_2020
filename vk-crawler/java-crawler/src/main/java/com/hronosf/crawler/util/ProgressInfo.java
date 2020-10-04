package com.hronosf.crawler.util;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class ProgressInfo {

    private double total;
    private double currentOffset;
}
