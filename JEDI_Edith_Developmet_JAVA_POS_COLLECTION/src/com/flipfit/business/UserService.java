package com.flipfit.business;

import com.flipfit.bean.User;
import com.flipfit.dao.UserDAO;
import com.flipfit.dao.UserDAOImpl;

public class UserService implements IUserService {
    private UserDAO userDAO = new UserDAOImpl();

    @Override
    public boolean register(User user) {
        return userDAO.registerUser(user);
    }

    @Override
    public boolean login(String email, String password) {
        return userDAO.isUserValid(email, password);
    }

    @Override
    public User getUserProfile(String email) {
        return userDAO.getUserProfile(email);
    }
}