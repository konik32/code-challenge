package com.hsbc.twitter.service;

public interface UserService {
    String save(String username);
    boolean exists(String username);
}
