package com.hsbc.twitter.service;

import org.junit.Before;
import org.junit.Test;

import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;


public class MapBasedUserConnectionServiceTest {

    private MapBasedUserConnectionService service;

    @Before
    public void setUp() throws Exception {
        service = new MapBasedUserConnectionService();
    }

    @Test
    public void shouldGetUsersFollowedByReturnEmptySetOnNoConnections() throws Exception {
        //when
        Set<String> result = service.getUsersFollowedBy("unknown");
        //then
        assertThat(result).isEmpty();
    }

    @Test
    public void shouldGetUsersFollowedByReturnSavedConnections() throws Exception {
        //given
        Stream.of("user1", "user2").forEach(u -> service.addConnection("user", u));
        //when
        Set<String> result = service.getUsersFollowedBy("user");
        //then
        assertThat(result).containsExactlyInAnyOrder("user1", "user2");
    }


}