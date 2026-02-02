package com.flipfit.validation;

import com.flipfit.bean.Slot;

import java.time.Duration;
import java.time.LocalTime;

import static com.flipfit.validation.ValidationConstants.*;

/**
 * Validator for Slot objects.
 * Validates slot timing (endTime must be after startTime) and duration (must be exactly 1 hour).
 */
public class SlotValidator implements Validator<Slot> {
    
    /**
     * Validates a Slot object.
     * @param slot The slot to validate
     * @return ValidationResult with validation status and any errors
     */
    @Override
    public ValidationResult validate(Slot slot) {
        ValidationResult result = new ValidationResult();
        
        if (slot == null) {
            result.addError("Slot cannot be null");
            return result;
        }
        
        // Validate times
        result.merge(validateTimes(slot.getStartTime(), slot.getEndTime()));
        
        // Validate duration (exactly 1 hour)
        result.merge(validateDuration(slot.getStartTime(), slot.getEndTime()));
        
        // Validate max capacity
        result.merge(validateMaxCapacity(slot.getMaxCapacity()));
        
        // Validate center ID
        if (slot.getCenterId() == null || slot.getCenterId().trim().isEmpty()) {
            result.addFieldError("centerId", "Center ID is required");
        }
        
        return result;
    }
    
    /**
     * Validates that end time is after start time.
     * @param startTime The start time
     * @param endTime The end time
     * @return ValidationResult
     */
    public ValidationResult validateTimes(LocalTime startTime, LocalTime endTime) {
        ValidationResult result = new ValidationResult();
        
        if (startTime == null) {
            result.addFieldError("startTime", "Start time is required");
        }
        
        if (endTime == null) {
            result.addFieldError("endTime", "End time is required");
        }
        
        if (startTime != null && endTime != null) {
            if (!endTime.isAfter(startTime)) {
                result.addFieldError("endTime", ERROR_SLOT_TIME_INVALID);
            }
        }
        
        return result;
    }
    
    /**
     * Validates that slot duration is exactly 1 hour as required.
     * @param startTime The start time
     * @param endTime The end time
     * @return ValidationResult
     */
    public ValidationResult validateDuration(LocalTime startTime, LocalTime endTime) {
        ValidationResult result = new ValidationResult();
        
        if (startTime == null || endTime == null) {
            return result; // Time validation will catch this
        }
        
        Duration duration = Duration.between(startTime, endTime);
        long durationMinutes = duration.toMinutes();
        
        if (durationMinutes != EXACT_SLOT_DURATION_MINUTES) {
            result.addFieldError("duration", ERROR_SLOT_DURATION_INVALID + 
                    String.format(" (current duration: %d minutes)", durationMinutes));
        }
        
        return result;
    }
    
    /**
     * Validates the maximum capacity for a slot.
     * @param maxCapacity The max capacity to validate
     * @return ValidationResult
     */
    public ValidationResult validateMaxCapacity(int maxCapacity) {
        ValidationResult result = new ValidationResult();
        
        if (maxCapacity < MIN_SLOT_CAPACITY) {
            result.addFieldError("maxCapacity", ERROR_CAPACITY_INVALID + ". Minimum: " + MIN_SLOT_CAPACITY);
        }
        
        if (maxCapacity > MAX_SLOT_CAPACITY) {
            result.addFieldError("maxCapacity", "Slot capacity must not exceed " + MAX_SLOT_CAPACITY);
        }
        
        return result;
    }
    
    /**
     * Quick validation check for slot times.
     * @param startTime The start time
     * @param endTime The end time
     * @return true if end time is after start time
     */
    public boolean isValidTimeRange(LocalTime startTime, LocalTime endTime) {
        if (startTime == null || endTime == null) {
            return false;
        }
        return endTime.isAfter(startTime);
    }
    
    /**
     * Quick validation check for 1-hour duration.
     * @param startTime The start time
     * @param endTime The end time
     * @return true if duration is exactly 1 hour
     */
    public boolean isExactlyOneHour(LocalTime startTime, LocalTime endTime) {
        if (startTime == null || endTime == null) {
            return false;
        }
        Duration duration = Duration.between(startTime, endTime);
        return duration.toMinutes() == EXACT_SLOT_DURATION_MINUTES;
    }
    
    /**
     * Calculates the duration in minutes between start and end times.
     * @param startTime The start time
     * @param endTime The end time
     * @return Duration in minutes, or -1 if times are invalid
     */
    public long getDurationMinutes(LocalTime startTime, LocalTime endTime) {
        if (startTime == null || endTime == null) {
            return -1;
        }
        return Duration.between(startTime, endTime).toMinutes();
    }
}
