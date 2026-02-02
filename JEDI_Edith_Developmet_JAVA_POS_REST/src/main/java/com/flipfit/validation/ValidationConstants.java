package com.flipfit.validation;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Constants for validation rules used throughout the Flipfit application.
 * Centralizes all validation parameters for consistency and easy maintenance.
 */
public final class ValidationConstants {
    
    // Prevent instantiation
    private ValidationConstants() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
    
    // ==================== Password Validation ====================
    
    /**
     * Minimum password length required.
     */
    public static final int MIN_PASSWORD_LENGTH = 8;
    
    /**
     * Maximum password length allowed.
     */
    public static final int MAX_PASSWORD_LENGTH = 128;
    
    /**
     * Whether password must contain at least one uppercase letter.
     */
    public static final boolean PASSWORD_REQUIRE_UPPERCASE = true;
    
    /**
     * Whether password must contain at least one digit.
     */
    public static final boolean PASSWORD_REQUIRE_DIGIT = true;
    
    /**
     * Whether password must contain at least one special character.
     */
    public static final boolean PASSWORD_REQUIRE_SPECIAL_CHAR = false;
    
    // ==================== Email Validation ====================
    
    /**
     * Allowed email domains. Empty set means all domains are allowed.
     */
    public static final Set<String> ALLOWED_EMAIL_DOMAINS = Collections.unmodifiableSet(
            new HashSet<>(Arrays.asList("gmail.com", "yahoo.com", "outlook.com", "hotmail.com"))
    );
    
    /**
     * Primary email domain for validation (as mentioned in requirements: @gmail.com).
     */
    public static final String PRIMARY_EMAIL_DOMAIN = "gmail.com";
    
    /**
     * Whether to restrict emails to only allowed domains.
     */
    public static final boolean RESTRICT_EMAIL_DOMAINS = false;
    
    /**
     * Email regex pattern for basic format validation.
     */
    public static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );
    
    // ==================== Phone Validation ====================
    
    /**
     * Minimum phone number length (digits only).
     */
    public static final int MIN_PHONE_LENGTH = 10;
    
    /**
     * Maximum phone number length (digits only).
     */
    public static final int MAX_PHONE_LENGTH = 15;
    
    /**
     * Phone number regex pattern for Indian phone numbers.
     */
    public static final Pattern PHONE_PATTERN = Pattern.compile(
            "^[6-9]\\d{9}$"
    );
    
    // ==================== Name Validation ====================
    
    /**
     * Minimum name length.
     */
    public static final int MIN_NAME_LENGTH = 2;
    
    /**
     * Maximum name length.
     */
    public static final int MAX_NAME_LENGTH = 100;
    
    // ==================== City Validation ====================
    
    /**
     * Valid cities where Flipfit operates.
     */
    public static final Set<String> VALID_CITIES = Collections.unmodifiableSet(
            new HashSet<>(Arrays.asList(
                    "Bangalore", "Mumbai", "Delhi", "Chennai", "Hyderabad",
                    "Pune", "Kolkata", "Ahmedabad", "Jaipur", "Lucknow"
            ))
    );
    
    // ==================== Gym Validation ====================
    
    /**
     * Minimum gym capacity.
     */
    public static final int MIN_GYM_CAPACITY = 1;
    
    /**
     * Maximum gym capacity.
     */
    public static final int MAX_GYM_CAPACITY = 1000;
    
    /**
     * Minimum gym name length.
     */
    public static final int MIN_GYM_NAME_LENGTH = 3;
    
    /**
     * Maximum gym name length.
     */
    public static final int MAX_GYM_NAME_LENGTH = 200;
    
    // ==================== Slot Validation ====================
    
    /**
     * Exact slot duration in minutes as required (1 hour).
     */
    public static final int EXACT_SLOT_DURATION_MINUTES = 60;
    
    /**
     * Minimum slot capacity.
     */
    public static final int MIN_SLOT_CAPACITY = 1;
    
    /**
     * Maximum slot capacity.
     */
    public static final int MAX_SLOT_CAPACITY = 100;
    
    // ==================== GymOwner Validation ====================
    
    /**
     * PAN number regex pattern (format: AAAAA1234A).
     */
    public static final Pattern PAN_PATTERN = Pattern.compile(
            "^[A-Z]{5}[0-9]{4}[A-Z]$"
    );
    
    /**
     * GST number regex pattern (format: 15 characters).
     */
    public static final Pattern GST_PATTERN = Pattern.compile(
            "^[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z][1-9A-Z]Z[0-9A-Z]$"
    );
    
    // ==================== Error Messages ====================
    
    public static final String ERROR_EMAIL_REQUIRED = "Email is required";
    public static final String ERROR_EMAIL_INVALID_FORMAT = "Email format is invalid";
    public static final String ERROR_EMAIL_INVALID_DOMAIN = "Email domain is not allowed. Please use @gmail.com";
    public static final String ERROR_PASSWORD_REQUIRED = "Password is required";
    public static final String ERROR_PASSWORD_TOO_SHORT = "Password must be at least " + MIN_PASSWORD_LENGTH + " characters";
    public static final String ERROR_PASSWORD_TOO_LONG = "Password must not exceed " + MAX_PASSWORD_LENGTH + " characters";
    public static final String ERROR_PASSWORD_NO_UPPERCASE = "Password must contain at least one uppercase letter";
    public static final String ERROR_PASSWORD_NO_DIGIT = "Password must contain at least one digit";
    public static final String ERROR_NAME_REQUIRED = "Name is required";
    public static final String ERROR_NAME_TOO_SHORT = "Name must be at least " + MIN_NAME_LENGTH + " characters";
    public static final String ERROR_PHONE_REQUIRED = "Phone number is required";
    public static final String ERROR_PHONE_INVALID = "Phone number must be a valid 10-digit Indian mobile number";
    public static final String ERROR_CITY_REQUIRED = "City is required";
    public static final String ERROR_CITY_INVALID = "City is not a valid Flipfit location";
    public static final String ERROR_GYM_NAME_REQUIRED = "Gym name is required";
    public static final String ERROR_CAPACITY_INVALID = "Capacity must be a positive number";
    public static final String ERROR_SLOT_TIME_INVALID = "End time must be after start time";
    public static final String ERROR_SLOT_DURATION_INVALID = "Slot duration must be exactly 1 hour";
    public static final String ERROR_PAN_INVALID = "PAN number format is invalid (expected: AAAAA1234A)";
    public static final String ERROR_GST_INVALID = "GST number format is invalid";
}
