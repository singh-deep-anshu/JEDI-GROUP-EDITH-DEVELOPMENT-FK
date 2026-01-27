package com.flipfit.business;

import com.flipfit.bean.Slot;
import com.flipfit.dao.GymOwnerDAO;
import com.flipfit.dao.GymOwnerDAOImpl;
import com.flipfit.exception.SlotFullException;
import com.flipfit.exception.SlotNotFoundException;

import java.util.List;

/**
 * Utility class for slot-related operations.
 * Handles slot queries, availability checks, and booking management.
 */
public class SlotManager {

  private GymOwnerDAO gymOwnerDAO = new GymOwnerDAOImpl();

  /**
   * Checks if a slot has available capacity for a booking.
   * 
   * @param slotId The slot ID
   * @return true if slots are available
   * @throws SlotNotFoundException if slot doesn't exist
   * @throws SlotFullException     if slot is fully booked
   */
  public boolean isSlotAvailable(String slotId) throws SlotNotFoundException, SlotFullException {
    Slot slot = gymOwnerDAO.getSlotById(slotId);

    if (slot == null) {
      throw new SlotNotFoundException(slotId);
    }

    if (slot.getCurrentBookings() >= slot.getMaxCapacity()) {
      throw new SlotFullException(slotId, slot.getCurrentBookings(), slot.getMaxCapacity());
    }

    return true;
  }

  /**
   * Gets the number of available seats in a slot.
   * 
   * @param slotId The slot ID
   * @return Number of available seats
   * @throws SlotNotFoundException if slot doesn't exist
   */
  public int getAvailableSeats(String slotId) throws SlotNotFoundException {
    Slot slot = gymOwnerDAO.getSlotById(slotId);

    if (slot == null) {
      throw new SlotNotFoundException(slotId);
    }

    return slot.getMaxCapacity() - slot.getCurrentBookings();
  }

  /**
   * Gets all slots for a gym center.
   * 
   * @param centerId The gym center ID
   * @return List of slots for the center
   */
  public List<Slot> getSlotsByCenter(String centerId) {
    return gymOwnerDAO.getSlotsByCenterId(centerId);
  }

  /**
   * Gets the total number of slots in a gym center.
   * 
   * @param centerId The gym center ID
   * @return Total number of slots
   */
  public int getTotalSlots(String centerId) {
    return gymOwnerDAO.getSlotsByCenterId(centerId).size();
  }

  /**
   * Books a seat in a slot (increments current bookings).
   * 
   * @param slotId The slot ID
   * @throws SlotNotFoundException if slot doesn't exist
   * @throws SlotFullException     if slot is fully booked
   */
  public void bookSlot(String slotId) throws SlotNotFoundException, SlotFullException {
    Slot slot = gymOwnerDAO.getSlotById(slotId);

    if (slot == null) {
      throw new SlotNotFoundException(slotId);
    }

    if (slot.getCurrentBookings() >= slot.getMaxCapacity()) {
      throw new SlotFullException(slotId, slot.getCurrentBookings(), slot.getMaxCapacity());
    }

    slot.setCurrentBookings(slot.getCurrentBookings() + 1);
  }

  /**
   * Cancels a booking in a slot (decrements current bookings).
   * 
   * @param slotId The slot ID
   * @throws SlotNotFoundException if slot doesn't exist
   */
  public void cancelSlotBooking(String slotId) throws SlotNotFoundException {
    Slot slot = gymOwnerDAO.getSlotById(slotId);

    if (slot == null) {
      throw new SlotNotFoundException(slotId);
    }

    if (slot.getCurrentBookings() > 0) {
      slot.setCurrentBookings(slot.getCurrentBookings() - 1);
    }
  }
}
