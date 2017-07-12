package com.hsbc.twitter.service;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

public class MapBasedUserConnectionService implements UserConnectionService {

    private Map<String, Set<String>> userConnections = new ConcurrentHashMap<>();

    @Override
    public void addConnection(String from, String to) {
        userConnections.compute(from, (s, users) -> {
            if (users == null)
                users = new CopyOnWriteArraySet<>();
            users.add(to);
            return users;
        });
    }

    @Override
    public Set<String> getUsersFollowedBy(String username) {
        return userConnections.getOrDefault(username, Collections.emptySet());
    }
}
