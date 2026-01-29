package com.flipfit.dao;

import com.flipfit.bean.User;
import java.util.List;

/**
 * Data Access Object interface for User entity.
 * Defines methods for CRUD operations on users.
 */
public interface UserDAO {
    /**
     * Registers a new user in the database.
     * @param user the user to register
     * @return true if successful, false otherwise
     */
    boolean registerUser(User user);

    /**
     * Validates user credentials.
     * @param email the user's email
     * @param password the user's password
     * @return true if credentials are valid, false otherwise
     */
    boolean isUserValid(String email, String password);

    /**
     * Retrieves a user profile by email.
     * @param email the user's email
     * @return the User object or null if not found
     */
    User getUserProfile(String email);

    /**
     * Updates an existing user.
     * @param user the user with updated information
     * @return true if successful, false otherwise
     */
    boolean updateUser(User user);

    /**
     * Retrieves a user by their ID.
     * @param userId the user ID
     * @return the User object or null if not found
     */
    User getUserById(String userId);

    /**
     * Retrieves all users in the system.
     * @return a list of all users
     */
    List<User> getAllUsers();
    
    /**
     * Deletes a user by their ID.
     * @param userId the user ID to delete
     * @return true if successful, false otherwise
     */
    boolean deleteUser(String userId);
}