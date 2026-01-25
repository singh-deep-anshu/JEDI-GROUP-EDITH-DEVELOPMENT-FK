package com.flipfit.dao;

import com.flipfit.bean.User;
import java.util.HashMap;
import java.util.Map;

public class UserDAOImpl implements UserDAO {
    // The "Database" for Users
    private static Map<String, User> usersMap = new HashMap<>();

    @Override
    public boolean registerUser(User user) {
        if (usersMap.containsKey(user.getEmail())) {
            return false; // User already exists
        }
        usersMap.put(user.getEmail(), user);
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
        return true;
    }
}