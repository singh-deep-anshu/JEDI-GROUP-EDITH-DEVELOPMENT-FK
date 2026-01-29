package com.flipfit.exception;

/**
 * Exception thrown when a slot lookup fails.
 * This is used when searching for a slot by ID and no matching slot is found.
 */
public class SlotNotFoundException extends FlipfitException {
    
    private static final String ERROR_CODE = "SLOT_002";
    private static final String DEFAULT_MESSAGE = "Slot not found.";
    
    private final String slotId;
    
    /**
     * Creates a new SlotNotFoundException with the default message.
     */
    public SlotNotFoundException() {
        super(DEFAULT_MESSAGE, ERROR_CODE);
        this.slotId = null;
    }
    
    /**
     * Creates a new SlotNotFoundException for a specific slot ID.
     * @param slotId The slot ID that was not found
     */
    public SlotNotFoundException(String slotId) {
        super(String.format("Slot with ID '%s' does not exist.", slotId), ERROR_CODE);
        this.slotId = slotId;
    }
    
    /**
     * Creates a new SlotNotFoundException with gym context.
     * @param slotId The slot ID that was not found
     * @param gymId The gym ID where the slot was searched
     */
    public SlotNotFoundException(String slotId, String gymId) {
        super(String.format("Slot '%s' was not found in gym '%s'.", slotId, gymId), ERROR_CODE);
        this.slotId = slotId;
    }
    
    /**
     * Gets the slot ID that was searched for.
     * @return The slot ID, or null if not specified
     */
    public String getSlotId() {
        return slotId;
    }
}
