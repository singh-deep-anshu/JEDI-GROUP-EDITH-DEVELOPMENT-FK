package com.flipfit.dao;

import com.flipfit.bean.User;

public interface UserDAO {
    boolean registerUser(User user);

    boolean isUserValid(String email, String password);

    User getUserProfile(String email);

    boolean updateUser(User user);

    User getUserById(String userId);
}