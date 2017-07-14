package com.hsbc.twitter.service;

import com.hsbc.twitter.domain.Tweet;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;


public class MapBasedTweetServiceTest {

    private MapBasedTweetService service;

    private Page page = new Page(0, 10);

    private AtomicInteger counter;

    @Before
    public void setUp() throws Exception {
        service = new MapBasedTweetService();
        counter = new AtomicInteger();
    }

    @Test
    public void shouldGetTweetsForReturnEmptyListForUnknownUser() throws Exception {
        //when
        List<Tweet> result = service.getTweetsFor("unknown");
        //then
        assertThat(result.isEmpty());
    }

    @Test
    public void shouldGetTweetsForReturnSavedUserTweets() throws Exception {
        //given
        List<Tweet> userTweets = generateSavedTweets("user", 3);
        //when
        List<Tweet> result = service.getTweetsFor("user");
        //then
        assertThat(result).containsExactlyElementsOf(userTweets);
    }

    @Test
    public void shouldGetOrderedTweetsForReturnUsersTweetsInReverseChronologicalOrder() throws Exception {
        //given
        List<Tweet> userTweets = generateSavedTweets(this::createAndSetCreateDate, "user", 3);
        //when
        List<Tweet> result = service.getOrderedTweetsFor("user", page);
        //then
        Collections.reverse(userTweets);
        assertThat(result).containsExactlyElementsOf(userTweets);
    }

    @Test
    public void shouldGetOrderedTweetsForReturnUsersTweetsPageInReverseChronologicalOrder() throws Exception {
        //given
        List<Tweet> userTweets = generateSavedTweets(this::createAndSetCreateDate, "user", 6);
        //when
        List<Tweet> result = service.getOrderedTweetsFor("user", new Page(1, 2));
        //then
        Collections.reverse(userTweets);
        List<Tweet> subList = userTweets.subList(2, 4);
        assertThat(result).containsExactlyElementsOf(subList);
    }


    private List<Tweet> generateSavedTweets(String username, int limit) {
        return Stream.generate(() -> new Tweet(UUID.randomUUID().toString()))
                .limit(limit)
                .map(t -> service.save(username, t))
                .collect(Collectors.toList());
    }

    private List<Tweet> generateSavedTweets(Supplier<Tweet> supplier, String username, int limit) {
        return Stream.generate(supplier)
                .limit(limit)
                .map(t -> service.save(username, t))
                .collect(Collectors.toList());
    }

    private Tweet createAndSetCreateDate() {
        Tweet tweet = new Tweet(UUID.randomUUID().toString());
        Tweet spy = spy(tweet);
        when(spy.getCreateDate()).thenReturn(LocalDateTime.now().plus(counter.getAndIncrement(), ChronoUnit.MINUTES));
        return spy;
    }


}