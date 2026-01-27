package com.flipfit.exception;

import com.flipfit.bean.Role;

/**
 * Exception thrown when a user tries to access features they are not authorized for.
 * This implements role-based access control in the application.
 */
public class UnauthorizedAccessException extends FlipfitException {
    
    private static final String ERROR_CODE = "AUTH_002";
    private static final String DEFAULT_MESSAGE = "You are not authorized to access this feature.";
    
    private final Role userRole;
    private final Role requiredRole;
    private final String resource;
    
    /**
     * Creates a new UnauthorizedAccessException with the default message.
     */
    public UnauthorizedAccessException() {
        super(DEFAULT_MESSAGE, ERROR_CODE);
        this.userRole = null;
        this.requiredRole = null;
        this.resource = null;
    }
    
    /**
     * Creates a new UnauthorizedAccessException with a custom message.
     * @param message Custom error message
     */
    public UnauthorizedAccessException(String message) {
        super(message, ERROR_CODE);
        this.userRole = null;
        this.requiredRole = null;
        this.resource = null;
    }
    
    /**
     * Creates a new UnauthorizedAccessException for role mismatch.
     * @param userRole The role of the user attempting access
     * @param requiredRole The role required for access
     */
    public UnauthorizedAccessException(Role userRole, Role requiredRole) {
        super(String.format("Access denied. Your role '%s' is not authorized. Required role: '%s'", 
                userRole, requiredRole), ERROR_CODE);
        this.userRole = userRole;
        this.requiredRole = requiredRole;
        this.resource = null;
    }
    
    /**
     * Creates a new UnauthorizedAccessException with resource context.
     * @param userRole The role of the user attempting access
     * @param requiredRole The role required for access
     * @param resource The resource being accessed
     */
    public UnauthorizedAccessException(Role userRole, Role requiredRole, String resource) {
        super(String.format("Access denied to '%s'. Your role '%s' is not authorized. Required role: '%s'", 
                resource, userRole, requiredRole), ERROR_CODE);
        this.userRole = userRole;
        this.requiredRole = requiredRole;
        this.resource = resource;
    }
    
    /**
     * Creates a new UnauthorizedAccessException for resource access.
     * @param resource The resource being accessed
     * @param reason The reason for denial
     */
    public UnauthorizedAccessException(String resource, String reason) {
        super(String.format("Access denied to '%s': %s", resource, reason), ERROR_CODE);
        this.userRole = null;
        this.requiredRole = null;
        this.resource = resource;
    }
    
    /**
     * Gets the user's role.
     * @return The user's role, or null if not specified
     */
    public Role getUserRole() {
        return userRole;
    }
    
    /**
     * Gets the required role for access.
     * @return The required role, or null if not specified
     */
    public Role getRequiredRole() {
        return requiredRole;
    }
    
    /**
     * Gets the resource being accessed.
     * @return The resource name, or null if not specified
     */
    public String getResource() {
        return resource;
    }
}
