package com.hsbc.twitter.service;

import com.hsbc.twitter.domain.Tweet;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class MapBasedTweetService implements TweetService {

    private Map<String, List<Tweet>> userTweets = new ConcurrentHashMap<>();

    @Override
    public Tweet save(String username, Tweet tweet) {
        userTweets.compute(username, (s, tweets) -> {
            if (tweets == null)
                tweets = new CopyOnWriteArrayList<>();
            tweets.add(tweet);
            return tweets;
        });
        return tweet;
    }

    @Override
    public List<Tweet> getTweetsFor(String username) {
        return userTweets.getOrDefault(username, Collections.emptyList());
    }

    @Override
    public List<Tweet> getTweetsFor(String username, Page page) {
        return getTweetsPage(getTweetsFor(username).stream(), page);
    }

    @Override
    public List<Tweet> getOrderedTweetsFor(String username, Page page) {
        return getTweetsPage(getTweetsFor(username).stream().sorted(Comparator.comparing(Tweet::getCreateDate).reversed()), page);
    }


    private List<Tweet> getTweetsPage(Stream<Tweet> stream, Page page) {
        return stream.skip(page.getPage() * page.getSize()).limit(page.getSize()).collect(Collectors.toList());
    }

}
