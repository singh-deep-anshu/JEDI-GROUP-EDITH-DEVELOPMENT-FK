package com.flipfit.exception;

import java.time.LocalTime;

/**
 * Exception thrown when slot timing is invalid.
 * This is used when creating or updating slots with invalid time configurations.
 */
public class InvalidSlotException extends FlipfitException {
    
    private static final String ERROR_CODE = "SLOT_003";
    private static final String DEFAULT_MESSAGE = "Invalid slot configuration.";
    
    private final LocalTime startTime;
    private final LocalTime endTime;
    
    /**
     * Creates a new InvalidSlotException with the default message.
     */
    public InvalidSlotException() {
        super(DEFAULT_MESSAGE, ERROR_CODE);
        this.startTime = null;
        this.endTime = null;
    }
    
    /**
     * Creates a new InvalidSlotException with a custom message.
     * @param message Custom error message
     */
    public InvalidSlotException(String message) {
        super(message, ERROR_CODE);
        this.startTime = null;
        this.endTime = null;
    }
    
    /**
     * Creates a new InvalidSlotException for invalid time range.
     * @param startTime The start time of the slot
     * @param endTime The end time of the slot
     */
    public InvalidSlotException(LocalTime startTime, LocalTime endTime) {
        super(String.format("Invalid slot timing: Start time '%s' must be before end time '%s'.", 
                startTime, endTime), ERROR_CODE);
        this.startTime = startTime;
        this.endTime = endTime;
    }
    
    /**
     * Creates a new InvalidSlotException for duration issues.
     * @param startTime The start time of the slot
     * @param endTime The end time of the slot
     * @param reason The reason for invalidity
     */
    public InvalidSlotException(LocalTime startTime, LocalTime endTime, String reason) {
        super(String.format("Invalid slot timing (%s - %s): %s", startTime, endTime, reason), ERROR_CODE);
        this.startTime = startTime;
        this.endTime = endTime;
    }
    
    /**
     * Gets the start time of the invalid slot.
     * @return The start time, or null if not specified
     */
    public LocalTime getStartTime() {
        return startTime;
    }
    
    /**
     * Gets the end time of the invalid slot.
     * @return The end time, or null if not specified
     */
    public LocalTime getEndTime() {
        return endTime;
    }
}
