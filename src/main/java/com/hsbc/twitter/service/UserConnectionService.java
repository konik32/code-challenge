package com.hsbc.twitter.service;

import java.util.Set;

public interface UserConnectionService {
    void addConnection(String from, String to);
    Set<String> getUsersFollowedBy(String username);
}
