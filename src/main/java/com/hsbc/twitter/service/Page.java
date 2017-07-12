package com.hsbc.twitter.service;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Page {
    private final int page;
    private final int size;
}
