package com.hsbc.twitter.service;

import com.hsbc.twitter.domain.Tweet;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DefaultTimelineService implements TimelineService {

    private final UserConnectionService userConnectionService;
    private final TweetService tweetService;

    @Autowired
    public DefaultTimelineService(@NonNull UserConnectionService userConnectionService,@NonNull TweetService tweetService) {
        this.userConnectionService = userConnectionService;
        this.tweetService = tweetService;
    }

    @Override
    public List<Tweet> getTimelineTweetsFor(String username, Page page) {
        return userConnectionService.getUsersFollowedBy(username).stream()
                .flatMap(u -> tweetService.getTweetsFor(u).stream())
                .sorted(Comparator.comparing(Tweet::getCreateDate).reversed())
                .skip(page.getPage()*page.getSize())
                .limit(page.getSize())
                .collect(Collectors.toList());
    }
}
