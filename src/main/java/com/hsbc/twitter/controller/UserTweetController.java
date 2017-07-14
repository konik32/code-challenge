package com.hsbc.twitter.controller;

import com.hsbc.twitter.domain.Tweet;
import com.hsbc.twitter.service.TweetService;
import com.hsbc.twitter.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.hsbc.twitter.controller.Paths.USER_TWEET_PATH;

@RestController
public class UserTweetController {

    private final TweetService tweetService;
    private final UserService userService;

    @Autowired
    public UserTweetController(TweetService tweetService, UserService userService) {
        this.tweetService = tweetService;
        this.userService = userService;
    }

    @PostMapping(path = USER_TWEET_PATH)
    @ApiOperation("Create new user tweet")
    public ResponseEntity<Tweet> create(@PathVariable("username") String username, @Valid @RequestBody Tweet tweet){
        if(!userService.exists(username))
            userService.save(username);
        return ResponseEntity.status(HttpStatus.CREATED).body(tweetService.save(username, tweet));
    }
}
