package com.flipfit.exception;

/**
 * Base exception class for all Flipfit application exceptions.
 * Provides a consistent structure for error handling with error codes.
 */
public class FlipfitException extends Exception {
    
    private final String errorCode;
    
    /**
     * Creates a new FlipfitException with a message.
     * @param message The error message
     */
    public FlipfitException(String message) {
        super(message);
        this.errorCode = "FLIPFIT_ERROR";
    }
    
    /**
     * Creates a new FlipfitException with a message and error code.
     * @param message The error message
     * @param errorCode The error code for categorization
     */
    public FlipfitException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
    
    /**
     * Creates a new FlipfitException with a message, error code, and cause.
     * @param message The error message
     * @param errorCode The error code for categorization
     * @param cause The underlying cause of this exception
     */
    public FlipfitException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }
    
    /**
     * Gets the error code associated with this exception.
     * @return The error code
     */
    public String getErrorCode() {
        return errorCode;
    }
    
    @Override
    public String toString() {
        return String.format("[%s] %s", errorCode, getMessage());
    }
}
