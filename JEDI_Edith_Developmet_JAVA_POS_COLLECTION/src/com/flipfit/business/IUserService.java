package com.flipfit.business;

import com.flipfit.bean.User;
import com.flipfit.bean.Role;

public interface IUserService {
    boolean register(User user);
    boolean login(String userName, String password);
    User getUserProfile(String email);
}