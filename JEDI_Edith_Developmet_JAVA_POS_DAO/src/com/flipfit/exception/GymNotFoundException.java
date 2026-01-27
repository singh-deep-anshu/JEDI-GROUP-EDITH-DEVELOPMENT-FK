package com.flipfit.exception;

/**
 * Exception thrown when trying to book a slot in a gym that doesn't exist.
 * This is used when the gym center ID provided is not found in the system.
 */
public class GymNotFoundException extends FlipfitException {
    
    private static final String ERROR_CODE = "GYM_001";
    private static final String DEFAULT_MESSAGE = "Gym center not found.";
    
    private final String gymId;
    
    /**
     * Creates a new GymNotFoundException with the default message.
     */
    public GymNotFoundException() {
        super(DEFAULT_MESSAGE, ERROR_CODE);
        this.gymId = null;
    }
    
    /**
     * Creates a new GymNotFoundException for a specific gym ID.
     * @param gymId The gym center ID that was not found
     */
    public GymNotFoundException(String gymId) {
        super(String.format("Gym center with ID '%s' does not exist.", gymId), ERROR_CODE);
        this.gymId = gymId;
    }
    
    /**
     * Creates a new GymNotFoundException with gym name context.
     * @param identifier The identifier used for lookup
     * @param identifierType The type of identifier (e.g., "name", "centerId")
     */
    public GymNotFoundException(String identifier, String identifierType) {
        super(String.format("Gym center with %s '%s' was not found.", identifierType, identifier), ERROR_CODE);
        this.gymId = identifier;
    }
    
    /**
     * Gets the gym ID that was searched for.
     * @return The gym ID, or null if not specified
     */
    public String getGymId() {
        return gymId;
    }
}
