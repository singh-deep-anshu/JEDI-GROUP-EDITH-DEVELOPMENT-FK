package com.flipfit.dao;

import com.flipfit.bean.User;
import com.flipfit.bean.Role;
import com.flipfit.utils.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of UserDAO using JDBC and MySQL database.
 */
public class UserDAOImpl implements UserDAO {

    @Override
    public boolean registerUser(User user) {
        String query = "INSERT INTO user (userID, name, email, password, phoneNumber, city, role) " +
                       "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, user.getUserId());
            pstmt.setString(2, user.getName());
            pstmt.setString(3, user.getEmail());
            pstmt.setString(4, user.getPassword());
            pstmt.setString(5, user.getPhoneNumber());
            pstmt.setString(6, user.getCity());
            pstmt.setString(7, user.getRole().toString());
            
            int rowsAffected = pstmt.executeUpdate();

            System.out.println("log2 " + rowsAffected );

            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("[DAO] Error registering user: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean isUserValid(String email, String password) {
        String query = "SELECT * FROM user WHERE email = ? AND password = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, email);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            
            return rs.next();
        } catch (SQLException e) {
            System.err.println("[DAO] Error validating user: " + e.getMessage());
            return false;
        }
    }

    @Override
    public User getUserProfile(String email) {
        String query = "SELECT * FROM user WHERE email = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
        } catch (SQLException e) {
            System.err.println("[DAO] Error getting user profile: " + e.getMessage());
        }
        return null;
    }

    @Override
    public boolean updateUser(User user) {
        String query = "UPDATE user SET name = ?, email = ?, password = ?, phoneNumber = ?, city = ?, role = ? " +
                       "WHERE userID = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getPassword());
            pstmt.setString(4, user.getPhoneNumber());
            pstmt.setString(5, user.getCity());
            pstmt.setString(6, user.getRole().toString());
            pstmt.setString(7, user.getUserId());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("[DAO] Error updating user: " + e.getMessage());
            return false;
        }
    }

    @Override
    public User getUserById(String userId) {
        String query = "SELECT * FROM user WHERE userID = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
        } catch (SQLException e) {
            System.err.println("[DAO] Error getting user by ID: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<User> getAllUsers() {
        String query = "SELECT * FROM user";
        List<User> users = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
        } catch (SQLException e) {
            System.err.println("[DAO] Error getting all users: " + e.getMessage());
        }
        return users;
    }

    @Override
    public boolean deleteUser(String userId) {
        String query = "DELETE FROM user WHERE userID = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, userId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("[DAO] Error deleting user: " + e.getMessage());
            return false;
        }
    }

    /**
     * Helper method to map a ResultSet row to a User object.
     */
    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User(
            rs.getString("userID"),
            rs.getString("name"),
            rs.getString("email"),
            rs.getString("password"),
            Role.valueOf(rs.getString("role")),
            rs.getString("phoneNumber"),
            rs.getString("city")
        );
        return user;
    }
}