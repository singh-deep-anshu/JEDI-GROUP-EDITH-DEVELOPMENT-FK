package com.flipfit.validation;

import com.flipfit.bean.GymOwner;

import static com.flipfit.validation.ValidationConstants.*;

/**
 * Validator for GymOwner objects.
 * Validates PAN number and GST number formats in addition to User validation.
 */
public class GymOwnerValidator implements Validator<GymOwner> {
    
    private final UserValidator userValidator;
    
    /**
     * Creates a new GymOwnerValidator.
     */
    public GymOwnerValidator() {
        this.userValidator = new UserValidator();
    }
    
    /**
     * Validates a GymOwner object.
     * @param owner The gym owner to validate
     * @return ValidationResult with validation status and any errors
     */
    @Override
    public ValidationResult validate(GymOwner owner) {
        ValidationResult result = new ValidationResult();
        
        if (owner == null) {
            result.addError("Gym owner cannot be null");
            return result;
        }
        
        // Validate base User fields
        result.merge(userValidator.validate(owner));
        
        // Validate PAN number
        result.merge(validatePanNumber(owner.getPanNumber()));
        
        // Validate GST number
        result.merge(validateGstNumber(owner.getGstNumber()));
        
        return result;
    }
    
    /**
     * Validates a PAN number.
     * Format: AAAAA1234A (5 letters, 4 digits, 1 letter)
     * @param panNumber The PAN number to validate
     * @return ValidationResult
     */
    public ValidationResult validatePanNumber(String panNumber) {
        ValidationResult result = new ValidationResult();
        
        if (panNumber == null || panNumber.trim().isEmpty()) {
            result.addFieldError("panNumber", "PAN number is required for gym owners");
            return result;
        }
        
        String trimmedPan = panNumber.trim().toUpperCase();
        
        if (!PAN_PATTERN.matcher(trimmedPan).matches()) {
            result.addFieldError("panNumber", ERROR_PAN_INVALID);
        }
        
        return result;
    }
    
    /**
     * Validates a GST number.
     * Format: 15 characters - 2 digits state code, 10 char PAN, 1 char entity, 1 Z, 1 checksum
     * @param gstNumber The GST number to validate
     * @return ValidationResult
     */
    public ValidationResult validateGstNumber(String gstNumber) {
        ValidationResult result = new ValidationResult();
        
        if (gstNumber == null || gstNumber.trim().isEmpty()) {
            result.addFieldError("gstNumber", "GST number is required for gym owners");
            return result;
        }
        
        String trimmedGst = gstNumber.trim().toUpperCase();
        
        if (!GST_PATTERN.matcher(trimmedGst).matches()) {
            result.addFieldError("gstNumber", ERROR_GST_INVALID);
        }
        
        return result;
    }
    
    /**
     * Validates GymOwner registration data.
     * @param owner The gym owner to validate
     * @param requireVerification Whether to check verification status
     * @return ValidationResult
     */
    public ValidationResult validateForRegistration(GymOwner owner, boolean requireVerification) {
        ValidationResult result = validate(owner);
        
        if (requireVerification && owner != null && !owner.isVerified()) {
            result.addFieldError("isVerified", "Gym owner must be verified to perform this action");
        }
        
        return result;
    }
    
    /**
     * Quick validation check for PAN number format.
     * @param panNumber The PAN number to check
     * @return true if format is valid
     */
    public boolean isValidPanNumber(String panNumber) {
        if (panNumber == null || panNumber.trim().isEmpty()) {
            return false;
        }
        return PAN_PATTERN.matcher(panNumber.trim().toUpperCase()).matches();
    }
    
    /**
     * Quick validation check for GST number format.
     * @param gstNumber The GST number to check
     * @return true if format is valid
     */
    public boolean isValidGstNumber(String gstNumber) {
        if (gstNumber == null || gstNumber.trim().isEmpty()) {
            return false;
        }
        return GST_PATTERN.matcher(gstNumber.trim().toUpperCase()).matches();
    }
    
    /**
     * Extracts the state code from a GST number.
     * @param gstNumber The GST number
     * @return The 2-digit state code, or null if invalid
     */
    public String extractStateCode(String gstNumber) {
        if (!isValidGstNumber(gstNumber)) {
            return null;
        }
        return gstNumber.trim().substring(0, 2);
    }
    
    /**
     * Extracts the PAN from a GST number.
     * @param gstNumber The GST number
     * @return The PAN number, or null if invalid
     */
    public String extractPanFromGst(String gstNumber) {
        if (!isValidGstNumber(gstNumber)) {
            return null;
        }
        return gstNumber.trim().substring(2, 12);
    }
    
    /**
     * Validates that PAN and GST are consistent (GST contains the same PAN).
     * @param panNumber The PAN number
     * @param gstNumber The GST number
     * @return ValidationResult
     */
    public ValidationResult validatePanGstConsistency(String panNumber, String gstNumber) {
        ValidationResult result = new ValidationResult();
        
        if (!isValidPanNumber(panNumber) || !isValidGstNumber(gstNumber)) {
            return result; // Individual validations will catch format issues
        }
        
        String panFromGst = extractPanFromGst(gstNumber);
        if (panFromGst != null && !panFromGst.equalsIgnoreCase(panNumber.trim())) {
            result.addError("PAN number does not match the PAN embedded in GST number");
        }
        
        return result;
    }
}
