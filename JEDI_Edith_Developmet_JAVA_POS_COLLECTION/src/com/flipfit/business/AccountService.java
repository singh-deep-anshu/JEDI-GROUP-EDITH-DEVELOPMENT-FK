package com.flipfit.business;

import com.flipfit.bean.GymOwner;
import com.flipfit.bean.GymCustomer;
import com.flipfit.bean.GymCenter;
import com.flipfit.exception.GymNotFoundException;
import com.flipfit.exception.GymOwnerNotVerifiedException;
import com.flipfit.exception.UnauthorizedAccessException;
import com.flipfit.exception.UserFoundException;

/**
 * Service interface for managing user accounts (registration and login).
 */
public interface AccountService {
  /**
   * Registers a new gym customer in the system.
   * 
   * @param customer The customer to register
   * @return true if registration successful, false otherwise
   */
  boolean registerCustomer(GymCustomer customer) throws UserFoundException;

  /**
   * Registers a new gym owner in the system (pending admin approval).
   * 
   * @param owner The gym owner to register
   * @return true if registration successful, false otherwise
   */
  boolean registerOwner(GymOwner owner) throws UserFoundException;

  /**
   * Authenticates a user with email and password.
   * 
   * @param email    The user's email
   * @param password The user's password
   * @return The user's role as a string if login successful, null otherwise
   */
  String login(String email, String password);

  /**
   * Adds a new gym center for a verified gym owner.
   * 
   * @param ownerId The gym owner's ID
   * @param gym     The gym center details
   * @return The registered GymCenter with generated ID
   */
  GymCenter addGymCenter(String ownerId, GymCenter gym) throws GymNotFoundException, UnauthorizedAccessException, GymOwnerNotVerifiedException;
}
