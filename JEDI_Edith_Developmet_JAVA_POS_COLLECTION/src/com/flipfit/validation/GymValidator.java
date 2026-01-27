package com.flipfit.validation;

import com.flipfit.bean.GymCenter;

import static com.flipfit.validation.ValidationConstants.*;

/**
 * Validator for GymCenter objects.
 * Validates gym city (must be a valid Flipfit location) and capacity (must be positive).
 */
public class GymValidator implements Validator<GymCenter> {
    
    /**
     * Validates a GymCenter object.
     * @param gym The gym center to validate
     * @return ValidationResult with validation status and any errors
     */
    @Override
    public ValidationResult validate(GymCenter gym) {
        ValidationResult result = new ValidationResult();
        
        if (gym == null) {
            result.addError("Gym center cannot be null");
            return result;
        }
        
        // Validate gym name
        result.merge(validateGymName(gym.getName()));
        
        // Validate city
        result.merge(validateCity(gym.getCityId()));
        
        // Validate capacity
        result.merge(validateCapacity(gym.getTotalCapacity()));
        
        // Validate address
        result.merge(validateAddress(gym.getAddress()));
        
        return result;
    }
    
    /**
     * Validates the gym name.
     * @param name The gym name to validate
     * @return ValidationResult
     */
    public ValidationResult validateGymName(String name) {
        ValidationResult result = new ValidationResult();
        
        if (name == null || name.trim().isEmpty()) {
            result.addFieldError("name", ERROR_GYM_NAME_REQUIRED);
            return result;
        }
        
        String trimmedName = name.trim();
        
        if (trimmedName.length() < MIN_GYM_NAME_LENGTH) {
            result.addFieldError("name", "Gym name must be at least " + MIN_GYM_NAME_LENGTH + " characters");
        }
        
        if (trimmedName.length() > MAX_GYM_NAME_LENGTH) {
            result.addFieldError("name", "Gym name must not exceed " + MAX_GYM_NAME_LENGTH + " characters");
        }
        
        return result;
    }
    
    /**
     * Validates the gym city.
     * Must be one of the valid Flipfit locations (e.g., "Bangalore", "Mumbai").
     * @param city The city to validate
     * @return ValidationResult
     */
    public ValidationResult validateCity(String city) {
        ValidationResult result = new ValidationResult();
        
        if (city == null || city.trim().isEmpty()) {
            result.addFieldError("cityId", ERROR_CITY_REQUIRED);
            return result;
        }
        
        // Case-insensitive city validation
        boolean validCity = VALID_CITIES.stream()
                .anyMatch(c -> c.equalsIgnoreCase(city.trim()));
        
        if (!validCity) {
            result.addFieldError("cityId", ERROR_CITY_INVALID + ". Valid cities: " + VALID_CITIES);
        }
        
        return result;
    }
    
    /**
     * Validates the gym capacity.
     * Must be a positive number within acceptable range.
     * @param capacity The capacity to validate
     * @return ValidationResult
     */
    public ValidationResult validateCapacity(int capacity) {
        ValidationResult result = new ValidationResult();
        
        if (capacity < MIN_GYM_CAPACITY) {
            result.addFieldError("totalCapacity", ERROR_CAPACITY_INVALID + ". Minimum: " + MIN_GYM_CAPACITY);
        }
        
        if (capacity > MAX_GYM_CAPACITY) {
            result.addFieldError("totalCapacity", "Capacity must not exceed " + MAX_GYM_CAPACITY);
        }
        
        return result;
    }
    
    /**
     * Validates the gym address.
     * @param address The address to validate
     * @return ValidationResult
     */
    public ValidationResult validateAddress(String address) {
        ValidationResult result = new ValidationResult();
        
        if (address == null || address.trim().isEmpty()) {
            result.addFieldError("address", "Address is required");
        }
        
        return result;
    }
    
    /**
     * Quick validation check for city only.
     * @param city The city to check
     * @return true if city is valid
     */
    public boolean isValidCity(String city) {
        if (city == null || city.trim().isEmpty()) {
            return false;
        }
        return VALID_CITIES.stream()
                .anyMatch(c -> c.equalsIgnoreCase(city.trim()));
    }
    
    /**
     * Quick validation check for capacity only.
     * @param capacity The capacity to check
     * @return true if capacity is valid (positive number)
     */
    public boolean isValidCapacity(int capacity) {
        return capacity >= MIN_GYM_CAPACITY && capacity <= MAX_GYM_CAPACITY;
    }
}
