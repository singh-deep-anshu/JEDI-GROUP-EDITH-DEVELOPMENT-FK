package com.flipfit.exception;

/**
 * Exception thrown when a user lookup fails.
 * This is used when searching for a user by ID or email and no matching user is found.
 */
public class UserNotFoundException extends FlipfitException {
    
    private static final String ERROR_CODE = "USER_002";
    private static final String DEFAULT_MESSAGE = "User not found.";
    
    private final String userId;
    
    /**
     * Creates a new UserNotFoundException with the default message.
     */
    public UserNotFoundException() {
        super(DEFAULT_MESSAGE, ERROR_CODE);
        this.userId = null;
    }
    
    /**
     * Creates a new UserNotFoundException for a specific user ID.
     * @param userId The user ID that was not found
     */
    public UserNotFoundException(String userId) {
        super(String.format("User with ID '%s' was not found.", userId), ERROR_CODE);
        this.userId = userId;
    }
    
    /**
     * Creates a new UserNotFoundException with a custom message.
     * @param identifier The identifier used for lookup
     * @param identifierType The type of identifier (e.g., "email", "userId")
     */
    public UserNotFoundException(String identifier, String identifierType) {
        super(String.format("User with %s '%s' was not found.", identifierType, identifier), ERROR_CODE);
        this.userId = identifier;
    }
    
    /**
     * Gets the user ID that was searched for.
     * @return The user ID, or null if not specified
     */
    public String getUserId() {
        return userId;
    }
}
