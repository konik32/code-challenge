package com.hsbc.twitter.service;

import com.hsbc.twitter.domain.Tweet;

import java.util.List;

public interface TweetService {

    Tweet save(String username, Tweet tweet);
    List<Tweet> getTweetsFor(String username, Page page);
}
