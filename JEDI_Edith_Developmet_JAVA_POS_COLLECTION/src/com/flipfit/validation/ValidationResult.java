package com.flipfit.validation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Container for validation results.
 * Holds the validation status, error messages, and field-specific errors.
 */
public class ValidationResult {
    
    private boolean valid;
    private final List<String> errors;
    private final List<FieldError> fieldErrors;
    
    /**
     * Creates a new successful ValidationResult.
     */
    public ValidationResult() {
        this.valid = true;
        this.errors = new ArrayList<>();
        this.fieldErrors = new ArrayList<>();
    }
    
    /**
     * Creates a new ValidationResult with initial status.
     * @param valid Initial validation status
     */
    public ValidationResult(boolean valid) {
        this.valid = valid;
        this.errors = new ArrayList<>();
        this.fieldErrors = new ArrayList<>();
    }
    
    /**
     * Checks if validation passed.
     * @return true if validation was successful
     */
    public boolean isValid() {
        return valid;
    }
    
    /**
     * Adds an error message and marks the result as invalid.
     * @param error The error message
     */
    public void addError(String error) {
        this.valid = false;
        this.errors.add(error);
    }
    
    /**
     * Adds a field-specific error and marks the result as invalid.
     * @param fieldName The name of the field with error
     * @param errorMessage The error message
     */
    public void addFieldError(String fieldName, String errorMessage) {
        this.valid = false;
        this.fieldErrors.add(new FieldError(fieldName, errorMessage));
        this.errors.add(String.format("%s: %s", fieldName, errorMessage));
    }
    
    /**
     * Gets all error messages.
     * @return Unmodifiable list of error messages
     */
    public List<String> getErrors() {
        return Collections.unmodifiableList(errors);
    }
    
    /**
     * Gets all field-specific errors.
     * @return Unmodifiable list of field errors
     */
    public List<FieldError> getFieldErrors() {
        return Collections.unmodifiableList(fieldErrors);
    }
    
    /**
     * Gets all errors as a single formatted string.
     * @return Formatted error string with newlines
     */
    public String getErrorsAsString() {
        return String.join("\n", errors);
    }
    
    /**
     * Gets the first error message.
     * @return First error message, or null if no errors
     */
    public String getFirstError() {
        return errors.isEmpty() ? null : errors.get(0);
    }
    
    /**
     * Checks if there are any errors.
     * @return true if there are errors
     */
    public boolean hasErrors() {
        return !errors.isEmpty();
    }
    
    /**
     * Merges another ValidationResult into this one.
     * @param other The other ValidationResult to merge
     */
    public void merge(ValidationResult other) {
        if (other != null && !other.isValid()) {
            this.valid = false;
            this.errors.addAll(other.errors);
            this.fieldErrors.addAll(other.fieldErrors);
        }
    }
    
    /**
     * Creates a successful validation result.
     * @return A valid ValidationResult
     */
    public static ValidationResult success() {
        return new ValidationResult(true);
    }
    
    /**
     * Creates a failed validation result with an error message.
     * @param error The error message
     * @return An invalid ValidationResult
     */
    public static ValidationResult failure(String error) {
        ValidationResult result = new ValidationResult(false);
        result.addError(error);
        return result;
    }
    
    /**
     * Creates a failed validation result with a field error.
     * @param fieldName The field name
     * @param errorMessage The error message
     * @return An invalid ValidationResult
     */
    public static ValidationResult fieldFailure(String fieldName, String errorMessage) {
        ValidationResult result = new ValidationResult(false);
        result.addFieldError(fieldName, errorMessage);
        return result;
    }
    
    @Override
    public String toString() {
        if (valid) {
            return "ValidationResult{valid=true}";
        }
        return String.format("ValidationResult{valid=false, errors=%s}", errors);
    }
    
    /**
     * Represents a field-specific validation error.
     */
    public static class FieldError {
        private final String fieldName;
        private final String message;
        
        public FieldError(String fieldName, String message) {
            this.fieldName = fieldName;
            this.message = message;
        }
        
        public String getFieldName() {
            return fieldName;
        }
        
        public String getMessage() {
            return message;
        }
        
        @Override
        public String toString() {
            return String.format("%s: %s", fieldName, message);
        }
    }
}
