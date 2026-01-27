package com.flipfit.validation;

/**
 * Base interface for all validators in the Flipfit application.
 * Validators ensure that input data meets the required criteria before
 * being processed by the business service layer.
 * 
 * @param <T> The type of object to validate
 */
public interface Validator<T> {
    
    /**
     * Validates the given object.
     * @param object The object to validate
     * @return ValidationResult containing the validation status and any errors
     */
    ValidationResult validate(T object);
    
    /**
     * Validates the given object and throws an exception if invalid.
     * @param object The object to validate
     * @throws IllegalArgumentException if validation fails
     */
    default void validateOrThrow(T object) throws IllegalArgumentException {
        ValidationResult result = validate(object);
        if (!result.isValid()) {
            throw new IllegalArgumentException(result.getErrorsAsString());
        }
    }
    
    /**
     * Checks if the given object is valid without returning detailed errors.
     * @param object The object to validate
     * @return true if the object is valid
     */
    default boolean isValid(T object) {
        return validate(object).isValid();
    }
}
