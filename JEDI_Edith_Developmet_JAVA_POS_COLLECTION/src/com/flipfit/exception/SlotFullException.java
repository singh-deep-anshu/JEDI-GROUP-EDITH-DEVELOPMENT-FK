package com.flipfit.exception;

/**
 * Exception thrown when a user tries to book a slot that has no seats left.
 * This exception triggers the Waitlist flow in the booking system.
 */
public class SlotFullException extends FlipfitException {
    
    private static final String ERROR_CODE = "SLOT_001";
    private static final String DEFAULT_MESSAGE = "This slot is fully booked. Would you like to join the waitlist?";
    
    private final String slotId;
    private final int currentBookings;
    private final int maxCapacity;
    
    /**
     * Creates a new SlotFullException with the default message.
     */
    public SlotFullException() {
        super(DEFAULT_MESSAGE, ERROR_CODE);
        this.slotId = null;
        this.currentBookings = 0;
        this.maxCapacity = 0;
    }
    
    /**
     * Creates a new SlotFullException for a specific slot.
     * @param slotId The slot ID that is full
     */
    public SlotFullException(String slotId) {
        super(String.format("Slot '%s' is fully booked. No seats available.", slotId), ERROR_CODE);
        this.slotId = slotId;
        this.currentBookings = 0;
        this.maxCapacity = 0;
    }
    
    /**
     * Creates a new SlotFullException with booking details.
     * @param slotId The slot ID that is full
     * @param currentBookings Current number of bookings
     * @param maxCapacity Maximum capacity of the slot
     */
    public SlotFullException(String slotId, int currentBookings, int maxCapacity) {
        super(String.format("Slot '%s' is fully booked (%d/%d seats taken). Would you like to join the waitlist?", 
                slotId, currentBookings, maxCapacity), ERROR_CODE);
        this.slotId = slotId;
        this.currentBookings = currentBookings;
        this.maxCapacity = maxCapacity;
    }
    
    /**
     * Gets the slot ID that is full.
     * @return The slot ID
     */
    public String getSlotId() {
        return slotId;
    }
    
    /**
     * Gets the current number of bookings.
     * @return Current bookings count
     */
    public int getCurrentBookings() {
        return currentBookings;
    }
    
    /**
     * Gets the maximum capacity of the slot.
     * @return Maximum capacity
     */
    public int getMaxCapacity() {
        return maxCapacity;
    }
    
    /**
     * Checks if waitlist is suggested.
     * @return true always, as this exception triggers waitlist flow
     */
    public boolean shouldOfferWaitlist() {
        return true;
    }
}
