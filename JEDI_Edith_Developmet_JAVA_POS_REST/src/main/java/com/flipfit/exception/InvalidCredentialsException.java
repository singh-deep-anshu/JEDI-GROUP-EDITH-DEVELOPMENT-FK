package com.flipfit.exception;

/**
 * Exception thrown when login authentication fails.
 * This includes invalid email/password combinations or locked accounts.
 */
public class InvalidCredentialsException extends FlipfitException {
    
    private static final String ERROR_CODE = "AUTH_001";
    private static final String DEFAULT_MESSAGE = "Invalid email or password. Please try again.";
    
    /**
     * Creates a new InvalidCredentialsException with the default message.
     */
    public InvalidCredentialsException() {
        super(DEFAULT_MESSAGE, ERROR_CODE);
    }
    
    /**
     * Creates a new InvalidCredentialsException with a custom message.
     * @param message Custom error message
     */
    public InvalidCredentialsException(String message) {
        super(message, ERROR_CODE);
    }
    
    /**
     * Creates a new InvalidCredentialsException for a specific email.
     * @param email The email that failed authentication
     * @param reason The reason for failure
     */
    public InvalidCredentialsException(String email, String reason) {
        super(String.format("Authentication failed for '%s': %s", email, reason), ERROR_CODE);
    }
}
