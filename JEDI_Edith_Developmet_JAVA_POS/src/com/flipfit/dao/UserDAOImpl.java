package com.flipfit.dao;

import com.flipfit.bean.User;
import java.util.HashMap;
import java.util.Map;

public class UserDAOImpl implements UserDAO {
    // The "Database" for Users (keyed by email)
    private static Map<String, User> usersMap = new HashMap<>();
    // Also maintain a map by userId for quick lookup
    private static Map<String, User> usersByIdMap = new HashMap<>();

    @Override
    public boolean registerUser(User user) {
        if (usersMap.containsKey(user.getEmail())) {
            return false; // User already exists
        }
        usersMap.put(user.getEmail(), user);
        if (user.getUserId() != null) {
            usersByIdMap.put(user.getUserId(), user);
        }
        return true;
    }

    @Override
    public boolean isUserValid(String email, String password) {
        User user = usersMap.get(email);
        return user != null && user.getPassword().equals(password);
    }

    @Override
    public User getUserProfile(String email) {
        return usersMap.get(email);
    }

    @Override
    public boolean updateUser(User user) {
        usersMap.put(user.getEmail(), user);
        if (user.getUserId() != null) {
            usersByIdMap.put(user.getUserId(), user);
        }
        return true;
    }

    @Override
    public User getUserById(String userId) {
        return usersByIdMap.get(userId);
    }
}