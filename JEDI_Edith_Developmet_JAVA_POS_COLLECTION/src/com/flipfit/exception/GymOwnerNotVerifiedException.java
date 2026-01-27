package com.flipfit.exception;

/**
 * Exception thrown when an unverified gym owner tries to perform restricted actions.
 * Gym owners must be verified before they can add gyms, manage slots, etc.
 */
public class GymOwnerNotVerifiedException extends FlipfitException {
    
    private static final String ERROR_CODE = "OWNER_001";
    private static final String DEFAULT_MESSAGE = "Your gym owner account is not verified yet. Please wait for admin approval.";
    
    private final String ownerId;
    private final String action;
    
    /**
     * Creates a new GymOwnerNotVerifiedException with the default message.
     */
    public GymOwnerNotVerifiedException() {
        super(DEFAULT_MESSAGE, ERROR_CODE);
        this.ownerId = null;
        this.action = null;
    }
    
    /**
     * Creates a new GymOwnerNotVerifiedException for a specific owner.
     * @param ownerId The unverified owner's ID
     */
    public GymOwnerNotVerifiedException(String ownerId) {
        super(String.format("Gym owner '%s' is not verified. Please wait for admin approval before performing this action.", 
                ownerId), ERROR_CODE);
        this.ownerId = ownerId;
        this.action = null;
    }
    
    /**
     * Creates a new GymOwnerNotVerifiedException with action context.
     * @param ownerId The unverified owner's ID
     * @param action The action being attempted
     */
    public GymOwnerNotVerifiedException(String ownerId, String action) {
        super(String.format("Cannot %s: Gym owner '%s' is not verified. Please wait for admin approval.", 
                action, ownerId), ERROR_CODE);
        this.ownerId = ownerId;
        this.action = action;
    }
    
    /**
     * Gets the owner ID.
     * @return The owner ID, or null if not specified
     */
    public String getOwnerId() {
        return ownerId;
    }
    
    /**
     * Gets the action that was attempted.
     * @return The action, or null if not specified
     */
    public String getAction() {
        return action;
    }
}
