package com.flipfit.validation;

import com.flipfit.bean.User;

import static com.flipfit.validation.ValidationConstants.*;

/**
 * Validator for User objects.
 * Validates email format, password strength, phone number, and name.
 */
public class UserValidator implements Validator<User> {
    
    /**
     * Validates a User object.
     * @param user The user to validate
     * @return ValidationResult with validation status and any errors
     */
    @Override
    public ValidationResult validate(User user) {
        ValidationResult result = new ValidationResult();
        
        if (user == null) {
            result.addError("User cannot be null");
            return result;
        }
        
        // Validate email
        result.merge(validateEmail(user.getEmail()));
        
        // Validate password
        result.merge(validatePassword(user.getPassword()));
        
        // Validate name
        result.merge(validateName(user.getName()));
        
        // Validate phone number
        result.merge(validatePhoneNumber(user.getPhoneNumber()));
        
        // Validate city
        result.merge(validateCity(user.getCity()));
        
        return result;
    }
    
    /**
     * Validates an email address.
     * Checks format and optionally domain restrictions.
     * @param email The email to validate
     * @return ValidationResult
     */
    public ValidationResult validateEmail(String email) {
        ValidationResult result = new ValidationResult();
        
        if (email == null || email.trim().isEmpty()) {
            result.addFieldError("email", ERROR_EMAIL_REQUIRED);
            return result;
        }
        
        String trimmedEmail = email.trim().toLowerCase();
        
        // Check basic email format
        if (!EMAIL_PATTERN.matcher(trimmedEmail).matches()) {
            result.addFieldError("email", ERROR_EMAIL_INVALID_FORMAT);
            return result;
        }
        
        // Check domain restrictions if enabled
        if (RESTRICT_EMAIL_DOMAINS) {
            String domain = trimmedEmail.substring(trimmedEmail.indexOf('@') + 1);
            if (!ALLOWED_EMAIL_DOMAINS.contains(domain)) {
                result.addFieldError("email", ERROR_EMAIL_INVALID_DOMAIN);
            }
        }
        
        return result;
    }
    
    /**
     * Validates a password for strength requirements.
     * Must be at least 8 characters (minimum requirement from reference).
     * @param password The password to validate
     * @return ValidationResult
     */
    public ValidationResult validatePassword(String password) {
        ValidationResult result = new ValidationResult();
        
        if (password == null || password.isEmpty()) {
            result.addFieldError("password", ERROR_PASSWORD_REQUIRED);
            return result;
        }
        
        // Check minimum length
        if (password.length() < MIN_PASSWORD_LENGTH) {
            result.addFieldError("password", ERROR_PASSWORD_TOO_SHORT);
        }
        
        // Check maximum length
        if (password.length() > MAX_PASSWORD_LENGTH) {
            result.addFieldError("password", ERROR_PASSWORD_TOO_LONG);
        }
        
        // Check for uppercase letter if required
        if (PASSWORD_REQUIRE_UPPERCASE && !containsUppercase(password)) {
            result.addFieldError("password", ERROR_PASSWORD_NO_UPPERCASE);
        }
        
        // Check for digit if required
        if (PASSWORD_REQUIRE_DIGIT && !containsDigit(password)) {
            result.addFieldError("password", ERROR_PASSWORD_NO_DIGIT);
        }
        
        return result;
    }
    
    /**
     * Validates a user's name.
     * @param name The name to validate
     * @return ValidationResult
     */
    public ValidationResult validateName(String name) {
        ValidationResult result = new ValidationResult();
        
        if (name == null || name.trim().isEmpty()) {
            result.addFieldError("name", ERROR_NAME_REQUIRED);
            return result;
        }
        
        if (name.trim().length() < MIN_NAME_LENGTH) {
            result.addFieldError("name", ERROR_NAME_TOO_SHORT);
        }
        
        return result;
    }
    
    /**
     * Validates a phone number.
     * Must be a valid 10-digit Indian mobile number.
     * @param phoneNumber The phone number to validate
     * @return ValidationResult
     */
    public ValidationResult validatePhoneNumber(String phoneNumber) {
        ValidationResult result = new ValidationResult();
        
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            result.addFieldError("phoneNumber", ERROR_PHONE_REQUIRED);
            return result;
        }
        
        // Remove any non-digit characters for validation
        String digitsOnly = phoneNumber.replaceAll("[^0-9]", "");
        
        if (!PHONE_PATTERN.matcher(digitsOnly).matches()) {
            result.addFieldError("phoneNumber", ERROR_PHONE_INVALID);
        }
        
        return result;
    }
    
    /**
     * Validates a city.
     * Must be one of the valid Flipfit locations.
     * @param city The city to validate
     * @return ValidationResult
     */
    public ValidationResult validateCity(String city) {
        ValidationResult result = new ValidationResult();
        
        if (city == null || city.trim().isEmpty()) {
            result.addFieldError("city", ERROR_CITY_REQUIRED);
            return result;
        }
        
        // Case-insensitive city validation
        boolean validCity = VALID_CITIES.stream()
                .anyMatch(c -> c.equalsIgnoreCase(city.trim()));
        
        if (!validCity) {
            result.addFieldError("city", ERROR_CITY_INVALID + ". Valid cities: " + VALID_CITIES);
        }
        
        return result;
    }
    
    /**
     * Checks if a string contains at least one uppercase letter.
     */
    private boolean containsUppercase(String str) {
        return str.chars().anyMatch(Character::isUpperCase);
    }
    
    /**
     * Checks if a string contains at least one digit.
     */
    private boolean containsDigit(String str) {
        return str.chars().anyMatch(Character::isDigit);
    }
    
    /**
     * Quick validation check for email format only.
     * @param email The email to check
     * @return true if email format is valid
     */
    public boolean isValidEmailFormat(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email.trim().toLowerCase()).matches();
    }
    
    /**
     * Quick validation check for password strength only.
     * @param password The password to check
     * @return true if password meets minimum requirements
     */
    public boolean isValidPassword(String password) {
        return password != null && password.length() >= MIN_PASSWORD_LENGTH;
    }
}
