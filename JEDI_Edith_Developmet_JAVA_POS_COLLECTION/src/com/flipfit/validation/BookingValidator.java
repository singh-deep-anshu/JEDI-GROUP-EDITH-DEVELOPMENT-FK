package com.flipfit.validation;

import com.flipfit.bean.Booking;
import com.flipfit.bean.BookingStatus;

import java.util.Date;

/**
 * Validator for Booking objects.
 * Validates booking date (not in the past) and required IDs.
 */
public class BookingValidator implements Validator<Booking> {
    
    /**
     * Validates a Booking object.
     * @param booking The booking to validate
     * @return ValidationResult with validation status and any errors
     */
    @Override
    public ValidationResult validate(Booking booking) {
        ValidationResult result = new ValidationResult();
        
        if (booking == null) {
            result.addError("Booking cannot be null");
            return result;
        }
        
        // Validate user ID
        result.merge(validateUserId(booking.getUserId()));
        
        // Validate slot ID
        result.merge(validateSlotId(booking.getSlotId()));
        
        // Validate booking date
        result.merge(validateBookingDate(booking.getBookingDate()));
        
        // Validate status
        result.merge(validateStatus(booking.getStatus()));
        
        return result;
    }
    
    /**
     * Validates the user ID.
     * @param userId The user ID to validate
     * @return ValidationResult
     */
    public ValidationResult validateUserId(String userId) {
        ValidationResult result = new ValidationResult();
        
        if (userId == null || userId.trim().isEmpty()) {
            result.addFieldError("userId", "User ID is required for booking");
        }
        
        return result;
    }
    
    /**
     * Validates the slot ID.
     * @param slotId The slot ID to validate
     * @return ValidationResult
     */
    public ValidationResult validateSlotId(String slotId) {
        ValidationResult result = new ValidationResult();
        
        if (slotId == null || slotId.trim().isEmpty()) {
            result.addFieldError("slotId", "Slot ID is required for booking");
        }
        
        return result;
    }
    
    /**
     * Validates the booking date.
     * Booking date should not be in the past.
     * @param bookingDate The booking date to validate
     * @return ValidationResult
     */
    public ValidationResult validateBookingDate(Date bookingDate) {
        ValidationResult result = new ValidationResult();
        
        if (bookingDate == null) {
            result.addFieldError("bookingDate", "Booking date is required");
            return result;
        }
        
        // Get current date without time for comparison
        Date today = new Date();
        
        // Clear time portion for date-only comparison
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTime(today);
        cal.set(java.util.Calendar.HOUR_OF_DAY, 0);
        cal.set(java.util.Calendar.MINUTE, 0);
        cal.set(java.util.Calendar.SECOND, 0);
        cal.set(java.util.Calendar.MILLISECOND, 0);
        Date todayStart = cal.getTime();
        
        if (bookingDate.before(todayStart)) {
            result.addFieldError("bookingDate", "Booking date cannot be in the past");
        }
        
        return result;
    }
    
    /**
     * Validates the booking status.
     * @param status The booking status to validate
     * @return ValidationResult
     */
    public ValidationResult validateStatus(BookingStatus status) {
        ValidationResult result = new ValidationResult();
        
        // Status is optional for new bookings (will be set to PENDING or CONFIRMED)
        // No validation needed if null
        
        return result;
    }
    
    /**
     * Validates a new booking request.
     * @param userId The user making the booking
     * @param slotId The slot being booked
     * @param bookingDate The date of booking
     * @return ValidationResult
     */
    public ValidationResult validateNewBooking(String userId, String slotId, Date bookingDate) {
        ValidationResult result = new ValidationResult();
        
        result.merge(validateUserId(userId));
        result.merge(validateSlotId(slotId));
        result.merge(validateBookingDate(bookingDate));
        
        return result;
    }
    
    /**
     * Checks if a booking can be cancelled.
     * @param booking The booking to check
     * @return true if the booking can be cancelled
     */
    public boolean canCancel(Booking booking) {
        if (booking == null || booking.getStatus() == null) {
            return false;
        }
        
        // Can only cancel CONFIRMED or PENDING bookings
        return booking.getStatus() == BookingStatus.CONFIRMED || 
               booking.getStatus() == BookingStatus.PENDING ||
               booking.getStatus() == BookingStatus.WAITLISTED;
    }
    
    /**
     * Checks if a date is valid for booking (not in the past).
     * @param date The date to check
     * @return true if the date is valid
     */
    public boolean isValidBookingDate(Date date) {
        if (date == null) {
            return false;
        }
        
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.set(java.util.Calendar.HOUR_OF_DAY, 0);
        cal.set(java.util.Calendar.MINUTE, 0);
        cal.set(java.util.Calendar.SECOND, 0);
        cal.set(java.util.Calendar.MILLISECOND, 0);
        
        return !date.before(cal.getTime());
    }
}
