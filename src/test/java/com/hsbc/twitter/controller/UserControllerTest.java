package com.hsbc.twitter.controller;

import com.hsbc.twitter.domain.Tweet;
import com.hsbc.twitter.service.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static com.hsbc.twitter.controller.Paths.*;
import static org.hamcrest.Matchers.contains;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {


    @MockBean
    private TweetService tweetService;

    @MockBean
    private UserService userService;

    @MockBean
    private UserConnectionService userConnectionService;

    @MockBean
    private TimelineService timelineService;


    @Autowired
    private MockMvc mockMvc;


    @Before
    public void setUp() throws Exception {
        when(userService.exists(anyString())).thenReturn(true);
        when(userService.exists("unknown")).thenReturn(false);


    }

    @Test
    public void shouldAddConnection() throws Exception {
        mockMvc.perform(post(USER_CONNECTION_PATH, "user1", "user2"))
                .andExpect(status().isCreated());
        verify(userConnectionService, times(1)).addConnection("user1", "user2");


    }

    @Test
    public void shouldWallReturnOrderedUserTweetsPage() throws Exception {
        when(tweetService.getOrderedTweetsFor("user1", new Page(1,2))).thenReturn(Arrays.asList(new Tweet("message1"), new Tweet("message2")));
        mockMvc.perform(get(USER_WALL_PATH, "user1")
                .param("page", "1")
                .param("size", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].message", contains("message1", "message2")));
    }

    @Test
    public void shouldTimelineReturnOrderedUserConnectionTweetsPage() throws Exception {
        when(timelineService.getTimelineTweetsFor("user1", new Page(1,2))).thenReturn(Arrays.asList(new Tweet("message1"), new Tweet("message2")));
        mockMvc.perform(get(USER_TIMELINE_PATH, "user1")
                .param("page", "1")
                .param("size", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].message", contains("message1", "message2")));
    }

    @Test
    public void shouldWallEndpointRespondWithNotFoundStatusCodeOnNonExistingUser() throws Exception {
        verifyUserNotFoundException(USER_WALL_PATH);
    }

    @Test
    public void shouldTimelineEndpointRespondWithNotFoundStatusCodeOnNonExistingUser() throws Exception {
        verifyUserNotFoundException(USER_TIMELINE_PATH);
    }

    @Test
    public void shouldAddConnectionEndpointRespondWithNotFoundStatusCodeOnNonExistingFromUser() throws Exception {
        mockMvc.perform(post(USER_CONNECTION_PATH, "unknown", "user"))
                .andExpect(status().isNotFound()).andExpect(status().reason("User not found"));
    }

    @Test
    public void shouldAddConnectionEndpointRespondWithNotFoundStatusCodeOnNonExistingToUser() throws Exception {
        mockMvc.perform(post(USER_CONNECTION_PATH, "user", "unknown"))
                .andExpect(status().isNotFound()).andExpect(status().reason("User not found"));
    }

    private void verifyUserNotFoundException(String path) throws Exception {
        mockMvc.perform(get(path, "unknown"))
                .andExpect(status().isNotFound()).andExpect(status().reason("User not found"));
    }

}