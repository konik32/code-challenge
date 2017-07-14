package com.hsbc.twitter.controller;

import com.hsbc.twitter.domain.Tweet;
import com.hsbc.twitter.service.TweetService;
import com.hsbc.twitter.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static com.hsbc.twitter.controller.Paths.USER_TWEET_PATH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(SpringRunner.class)
@WebMvcTest(UserTweetController.class)
public class UserTweetControllerTest {

    @MockBean
    private TweetService tweetService;

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    private String tweet = "{\"message\":\"message\"}";

    @Before
    public void setUp() throws Exception {
        when(userService.exists(anyString())).thenReturn(true);
    }

    @Test
    public void shouldSaveNotExistingUser() throws Exception {
        //given
        when(userService.exists(anyString())).thenReturn(false);
        //when
        mockMvc.perform(post(USER_TWEET_PATH,"user").contentType(APPLICATION_JSON).content(tweet));
        //then
        verify(userService,times(1)).save("user");
    }

    @Test
    public void shouldSaveUserTweet() throws Exception {
        //given
        //when
        mockMvc.perform(post(USER_TWEET_PATH,"user").contentType(APPLICATION_JSON).content(tweet));
        //then
        ArgumentCaptor<Tweet> captor = ArgumentCaptor.forClass(Tweet.class);
        verify(tweetService,times(1)).save(eq("user"),captor.capture() );
        assertThat(captor.getValue().getMessage()).isEqualTo("message");
    }

}