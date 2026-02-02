package com.flipfit.dao;

import com.flipfit.bean.Slot;
import java.sql.Connection;
import java.util.List;

/**
 * Data Access Object interface for Slot entity.
 * Defines methods for CRUD operations on gym slots.
 */
public interface SlotDAO {
    
    /**
     * Adds a new slot to the database.
     * @param slot the slot to add
     * @return true if successful, false otherwise
     */
    boolean addSlot(Slot slot);
    
    /**
     * Retrieves a slot by its ID.
     * @param slotId the slot ID
     * @return the Slot object or null if not found
     */
    Slot getSlotById(String slotId);
    
    /**
     * Retrieves all slots for a specific gym center.
     * @param centerId the center ID
     * @return a list of slots in that center
     */
    List<Slot> getSlotsByCenterId(String centerId);
    
    /**
     * Retrieves all slots in the system.
     * @return a list of all slots
     */
    List<Slot> getAllSlots();
    
    /**
     * Updates an existing slot.
     * @param slot the slot with updated information
     * @return true if successful, false otherwise
     */
    boolean updateSlot(Slot slot);
    
    /**
     * Updates the current bookings count for a slot.
     * @param slotId the slot ID
     * @param newBookingCount the new booking count
     * @return true if successful, false otherwise
     */
    boolean updateSlotBookingCount(String slotId, int newBookingCount);

    /** Transactional variant using provided Connection */
    boolean updateSlotBookingCount(Connection conn, String slotId, int newBookingCount);

    /**
     * Deletes a slot by its ID.
     * @param slotId the slot ID to delete
     * @return true if successful, false otherwise
     */
    boolean deleteSlot(String slotId);
}
