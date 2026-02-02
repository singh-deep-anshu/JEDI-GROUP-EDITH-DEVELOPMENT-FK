package com.flipfit.dao;

import com.flipfit.bean.Role;
import com.flipfit.bean.User;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.Handle;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserJdbiDAO implements UserDAO {
    private final Jdbi jdbi;

    public UserJdbiDAO(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    @Override
    public boolean registerUser(User user) {
        String sql = "INSERT INTO user (userID, name, email, password, phoneNumber, city, role) VALUES (:userId, :name, :email, :password, :phoneNumber, :city, :role)";
        return jdbi.withHandle(handle -> handle.createUpdate(sql)
                .bind("userId", user.getUserId())
                .bind("name", user.getName())
                .bind("email", user.getEmail())
                .bind("password", user.getPassword())
                .bind("phoneNumber", user.getPhoneNumber())
                .bind("city", user.getCity())
                .bind("role", user.getRole() != null ? user.getRole().toString() : null)
                .execute() > 0);
    }

    @Override
    public boolean isUserValid(String email, String password) {
        String sql = "SELECT COUNT(*) FROM user WHERE email = :email AND password = :password";
        Integer count = jdbi.withHandle(handle -> handle.createQuery(sql)
                .bind("email", email)
                .bind("password", password)
                .mapTo(Integer.class)
                .one());
        return count != null && count > 0;
    }

    @Override
    public User getUserProfile(String email) {
        String sql = "SELECT * FROM user WHERE email = :email";
        Optional<User> opt = jdbi.withHandle(handle -> handle.createQuery(sql)
                .bind("email", email)
                .map((rs, ctx) -> {
                    User u = new User(
                            rs.getString("userID"),
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("password"),
                            rs.getString("role") != null ? Role.valueOf(rs.getString("role")) : null,
                            rs.getString("phoneNumber"),
                            rs.getString("city")
                    );
                    return u;
                })
                .findOne());
        return opt.orElse(null);
    }

    @Override
    public boolean updateUser(User user) {
        String sql = "UPDATE user SET name = :name, email = :email, password = :password, phoneNumber = :phoneNumber, city = :city, role = :role WHERE userID = :userId";
        return jdbi.withHandle(handle -> handle.createUpdate(sql)
                .bind("name", user.getName())
                .bind("email", user.getEmail())
                .bind("password", user.getPassword())
                .bind("phoneNumber", user.getPhoneNumber())
                .bind("city", user.getCity())
                .bind("role", user.getRole() != null ? user.getRole().toString() : null)
                .bind("userId", user.getUserId())
                .execute() > 0);
    }

    @Override
    public User getUserById(String userId) {
        String sql = "SELECT * FROM user WHERE userID = :userId";
        Optional<User> opt = jdbi.withHandle(handle -> handle.createQuery(sql)
                .bind("userId", userId)
                .map((rs, ctx) -> new User(
                        rs.getString("userID"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("role") != null ? Role.valueOf(rs.getString("role")) : null,
                        rs.getString("phoneNumber"),
                        rs.getString("city")
                ))
                .findOne());
        return opt.orElse(null);
    }

    @Override
    public List<User> getAllUsers() {
        String sql = "SELECT * FROM user";
        return jdbi.withHandle(handle -> handle.createQuery(sql)
                .map((rs, ctx) -> new User(
                        rs.getString("userID"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("role") != null ? Role.valueOf(rs.getString("role")) : null,
                        rs.getString("phoneNumber"),
                        rs.getString("city")
                ))
                .list());
    }

    @Override
    public boolean deleteUser(String userId) {
        String sql = "DELETE FROM user WHERE userID = :userId";
        return jdbi.withHandle(handle -> handle.createUpdate(sql)
                .bind("userId", userId)
                .execute() > 0);
    }
}
