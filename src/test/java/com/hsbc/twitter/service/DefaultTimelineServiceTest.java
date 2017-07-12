package com.hsbc.twitter.service;

import com.hsbc.twitter.domain.Tweet;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DefaultTimelineServiceTest {

    @Mock
    private TweetService tweetService;

    @Mock
    private UserConnectionService userConnectionService;

    private Page page = new Page(0, 10);

    private DefaultTimelineService service;

    @Before
    public void setUp() throws Exception {
        service = new DefaultTimelineService(userConnectionService, tweetService);
        when(userConnectionService.getUsersFollowedBy("user")).thenReturn(Stream.of("user1", "user2").collect(Collectors.toSet()));

    }

    @Test
    public void shouldTimelineReturnEmptyListOnNoUserConnections() throws Exception {
        //given
        when(userConnectionService.getUsersFollowedBy("user")).thenReturn(Collections.emptySet());
        //when
        List<Tweet> result = service.getTimelineTweetsFor("user", page);
        //then
        assertThat(result).isEmpty();
    }

    @Test
    public void shouldTimelineReturnOrderedTweetListForAllConnections() throws Exception {
        //given
        List<Tweet> tweets = prepareServicesResponses();
        //when
        List<Tweet> result = service.getTimelineTweetsFor("user", page);
        //then
        Collections.reverse(tweets);
        assertThat(result).containsExactlyElementsOf(tweets);
    }

    @Test
    public void shouldTimelineReturnOrderedTweetsPageForAllConnections() throws Exception {
        //given
        List<Tweet> tweets = prepareServicesResponses();
        //when
        List<Tweet> result = service.getTimelineTweetsFor("user", new Page(1,2));
        //then
        Collections.reverse(tweets);
        assertThat(result).containsExactlyElementsOf(tweets.subList(2,4));
    }

    private List<Tweet> prepareServicesResponses(){
        AtomicInteger counter = new AtomicInteger();
        List<Tweet> tweets = Stream.generate(() -> {
            Tweet tweet = mock(Tweet.class);
            when(tweet.getCreateDate()).thenReturn(LocalDateTime.now().plus(counter.getAndIncrement(), ChronoUnit.MINUTES));
            return tweet;
        }).limit(4).collect(Collectors.toList());


        when(tweetService.getTweetsFor("user1")).thenReturn(Stream.of(tweets.get(0), tweets.get(2)).collect(Collectors.toList()));
        when(tweetService.getTweetsFor("user2")).thenReturn(Stream.of(tweets.get(3), tweets.get(1)).collect(Collectors.toList()));
        return tweets;
    }

}