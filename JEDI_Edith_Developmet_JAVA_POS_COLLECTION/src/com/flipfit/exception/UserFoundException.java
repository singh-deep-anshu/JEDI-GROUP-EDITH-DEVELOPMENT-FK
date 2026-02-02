package com.flipfit.exception;

/**
 * Exception thrown when attempting to register a user with an email that already exists.
 * This prevents duplicate user registrations in the system.
 */
public class UserFoundException extends FlipfitException {
    
    private static final String ERROR_CODE = "USER_001";
    private static final String DEFAULT_MESSAGE = "A user with this email already exists.";
    
    private final String email;
    
    /**
     * Creates a new UserFoundException with the default message.
     */
    public UserFoundException() {
        super(DEFAULT_MESSAGE, ERROR_CODE);
        this.email = null;
    }
    
    /**
     * Creates a new UserFoundException for a specific email.
     * @param email The email that already exists
     */
    public UserFoundException(String email) {
        super(String.format("User with email '%s' already exists. Please use a different email or login.", email), ERROR_CODE);
        this.email = email;
    }
    
    /**
     * Gets the email that caused the conflict.
     * @return The duplicate email, or null if not specified
     */
    public String getEmail() {
        return email;
    }
}
