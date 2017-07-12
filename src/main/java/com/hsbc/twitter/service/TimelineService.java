package com.hsbc.twitter.service;

import com.hsbc.twitter.domain.Tweet;

import java.util.List;

public interface TimelineService {
    List<Tweet> getTimelineTweetsFor(String username, Page page);
}
