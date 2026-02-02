package com.flipfit.exception;

/**
 * Exception thrown for general booking failures.
 * This is a catch-all exception for booking-related issues that don't fit
 * other specific booking exceptions.
 */
public class BookingFailedException extends FlipfitException {
    
    private static final String ERROR_CODE = "BOOKING_001";
    private static final String DEFAULT_MESSAGE = "Booking failed. Please try again later.";
    
    private final String bookingId;
    private final String userId;
    private final String slotId;
    
    /**
     * Creates a new BookingFailedException with the default message.
     */
    public BookingFailedException() {
        super(DEFAULT_MESSAGE, ERROR_CODE);
        this.bookingId = null;
        this.userId = null;
        this.slotId = null;
    }
    
    /**
     * Creates a new BookingFailedException with a custom message.
     * @param message Custom error message
     */
    public BookingFailedException(String message) {
        super(message, ERROR_CODE);
        this.bookingId = null;
        this.userId = null;
        this.slotId = null;
    }
    
    /**
     * Creates a new BookingFailedException with booking context.
     * @param userId The user attempting the booking
     * @param slotId The slot being booked
     * @param reason The reason for failure
     */
    public BookingFailedException(String userId, String slotId, String reason) {
        super(String.format("Booking failed for user '%s' on slot '%s': %s", userId, slotId, reason), ERROR_CODE);
        this.bookingId = null;
        this.userId = userId;
        this.slotId = slotId;
    }
    
    /**
     * Creates a new BookingFailedException with full context.
     * @param bookingId The booking ID (if created before failure)
     * @param userId The user attempting the booking
     * @param slotId The slot being booked
     * @param reason The reason for failure
     */
    public BookingFailedException(String bookingId, String userId, String slotId, String reason) {
        super(String.format("Booking '%s' failed for user '%s' on slot '%s': %s", 
                bookingId, userId, slotId, reason), ERROR_CODE);
        this.bookingId = bookingId;
        this.userId = userId;
        this.slotId = slotId;
    }
    
    /**
     * Creates a new BookingFailedException with a cause.
     * @param message Custom error message
     * @param cause The underlying cause
     */
    public BookingFailedException(String message, Throwable cause) {
        super(message, ERROR_CODE, cause);
        this.bookingId = null;
        this.userId = null;
        this.slotId = null;
    }
    
    /**
     * Gets the booking ID associated with the failure.
     * @return The booking ID, or null if not applicable
     */
    public String getBookingId() {
        return bookingId;
    }
    
    /**
     * Gets the user ID associated with the failure.
     * @return The user ID, or null if not specified
     */
    public String getUserId() {
        return userId;
    }
    
    /**
     * Gets the slot ID associated with the failure.
     * @return The slot ID, or null if not specified
     */
    public String getSlotId() {
        return slotId;
    }
}
