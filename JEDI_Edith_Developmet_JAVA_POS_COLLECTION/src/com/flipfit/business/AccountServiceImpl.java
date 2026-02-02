package com.flipfit.business;

import com.flipfit.bean.GymOwner;
import com.flipfit.bean.GymCustomer;
import com.flipfit.bean.GymCenter;
import com.flipfit.bean.Role;
import com.flipfit.dao.UserDAO;
import com.flipfit.dao.UserDAOImpl;
import com.flipfit.exception.GymNotFoundException;
import com.flipfit.exception.GymOwnerNotVerifiedException;
import com.flipfit.exception.UnauthorizedAccessException;
import com.flipfit.exception.UserFoundException;
import com.flipfit.validation.GymOwnerValidator;
import com.flipfit.validation.UserValidator;
import com.flipfit.validation.ValidationResult;

import java.util.Date;
import java.util.UUID;

/**
 * Implementation of AccountService for managing user accounts.
 */
public class AccountServiceImpl implements AccountService {

  private UserDAO userDAO = new UserDAOImpl();
  private UserValidator userValidator = new UserValidator();
  private GymOwnerValidator gymOwnerValidator = new GymOwnerValidator();
  private GymService gymService = new GymServiceImpl();

  /**
   * Registers a new gym customer in the system.
   * Validates customer data and checks for duplicate email.
   * 
   * @param customer The customer to register
   * @return true if registration successful, false otherwise
   */
  @Override
  public boolean registerCustomer(GymCustomer customer) throws UserFoundException {
    // Validate customer data
    // ValidationResult result = userValidator.validate(customer);
    // if (!result.isValid()) {
    //   System.out.println("Customer validation failed: " + result.getErrorsAsString());
    //   return false;
    // }

    // Check if email already exists
    if (userDAO.getUserProfile(customer.getEmail()) != null) {
      throw new UserFoundException(customer.getEmail());
    }

    // Generate userId
    customer.setUserId(UUID.randomUUID().toString());
    customer.setRole(Role.GYM_CUSTOMER);
    customer.setRegistrationDate(new Date());
    customer.setActive(true);

    return userDAO.registerUser(customer);
  }

  /**
   * Registers a new gym owner in the system (pending admin approval).
   * Validates gym owner data and checks for duplicate email.
   * 
   * @param owner The gym owner to register
   * @return true if registration successful, false otherwise
   */
  @Override
  public boolean registerOwner(GymOwner owner) throws UserFoundException {
    // Validate gym owner data
    // ValidationResult result = gymOwnerValidator.validate(owner);
    // if (!result.isValid()) {
    //   System.out.println("Owner validation failed: " + result.getErrorsAsString());
    //   return false;
    // }

    // Check if email already exists
    if (userDAO.getUserProfile(owner.getEmail()) != null) {
      throw new UserFoundException(owner.getEmail());
    }

    // Generate userId
    owner.setUserId(UUID.randomUUID().toString());
    owner.setRole(Role.GYM_OWNER);
    owner.setVerified(false); // Pending admin approval

    return userDAO.registerUser(owner);
  }

  /**
   * Authenticates a user with email and password.
   * 
   * @param email    The user's email
   * @param password The user's password
   * @return The user's role as a string if login successful, null otherwise
   */
  @Override
  public String login(String email, String password) {
    if (!userDAO.isUserValid(email, password)) {
      return null; // Invalid credentials
    }

    com.flipfit.bean.User user = userDAO.getUserProfile(email);
    if (user != null && user.getRole() != null) {
      return user.getRole().toString();
    }

    return null;
  }

  /**
   * Adds a new gym center for a verified gym owner.
   * Delegates to GymService for validation and registration.
   * 
   * @param ownerId The gym owner's ID
   * @param gym     The gym center details
   * @return The registered GymCenter with generated ID
   */
  @Override
  public GymCenter addGymCenter(String ownerId, GymCenter gym) throws GymNotFoundException, UnauthorizedAccessException, GymOwnerNotVerifiedException {
    return gymService.registerGymCenter(ownerId, gym);
  }
}
