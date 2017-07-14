package com.hsbc.twitter.service;

import org.junit.Before;
import org.junit.Test;

import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;


public class MapBasedUserServiceTest {

    private MapBasedUserService service;

    @Before
    public void setUp() throws Exception {
        service = new MapBasedUserService();
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

    @Test
    public void shouldSaveUser() throws Exception {
        //when
        service.save("user");
        //then
        assertThat(service.exists("user")).isTrue();
    }


    @Test
    public void shouldExistsReturnFalseForUnknownUser() throws Exception {
        //when
        //then
        assertThat(service.exists("unknown")).isFalse();
    }


}