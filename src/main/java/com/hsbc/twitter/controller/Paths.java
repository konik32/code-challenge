package com.hsbc.twitter.controller;

public interface Paths {

    String USER_TWEET_PATH = "/api/users/{username}/tweets";
    String USER_CONNECTION_PATH = "/api/users/{from}/connections/{to}";
    String USER_WALL_PATH = "/api/users/{username}/wall";
    String USER_TIMELINE_PATH = "/api/users/{username}/timeline";
}
