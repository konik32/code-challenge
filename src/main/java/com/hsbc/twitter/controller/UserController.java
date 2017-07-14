package com.hsbc.twitter.controller;

import com.hsbc.twitter.domain.Tweet;
import com.hsbc.twitter.exception.UserNotFoundException;
import com.hsbc.twitter.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.hsbc.twitter.controller.Paths.USER_CONNECTION_PATH;
import static com.hsbc.twitter.controller.Paths.USER_TIMELINE_PATH;
import static com.hsbc.twitter.controller.Paths.USER_WALL_PATH;

@RestController
public class UserController {

    private final UserConnectionService userConnectionService;
    private final TweetService tweetService;
    private final TimelineService timelineService;
    private final UserService userService;

    @Autowired
    public UserController(UserConnectionService userConnectionService, TweetService tweetService, TimelineService timelineService, UserService userService) {
        this.userConnectionService = userConnectionService;
        this.tweetService = tweetService;
        this.timelineService = timelineService;
        this.userService = userService;
    }

    @PostMapping(path = USER_CONNECTION_PATH)
    public ResponseEntity<Void> addConnection(@PathVariable String from, @PathVariable String to){
        assertUserExists(from);
        assertUserExists(to);
        userConnectionService.addConnection(from,to);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping(path = USER_WALL_PATH)
    public List<Tweet> wall(@PathVariable String username,
                            @RequestParam(required = false, defaultValue = "0") Integer page,
                            @RequestParam(required = false, defaultValue = "10") Integer size){
        assertUserExists(username);
        return tweetService.getOrderedTweetsFor(username,new Page(page,size));
    }

    @GetMapping(path = USER_TIMELINE_PATH)
    public List<Tweet> timeline(@PathVariable String username,
                                @RequestParam(required = false, defaultValue = "0") Integer page,
                                @RequestParam(required = false, defaultValue = "10") Integer size){
        assertUserExists(username);
        return timelineService.getTimelineTweetsFor(username, new Page(page,size));
    }

    private void assertUserExists(String username) throws UserNotFoundException{
        if(!userService.exists(username))
            throw new UserNotFoundException();
    }
}
