package com.flipfit.business;

import java.util.List;

import com.flipfit.bean.GymCenter;
import com.flipfit.bean.Slot;

public interface GymService {
    /**
     * Registers a new gym center for a verified gym owner.
     * 
     * @param ownerId The gym owner's ID
     * @param details The gym center details
     * @return The registered GymCenter with generated ID
     */
    public GymCenter registerGymCenter(String ownerId, GymCenter details);

    /**
     * Adds a new time slot to a gym center.
     * 
     * @param centerId    The gym center ID
     * @param slotDetails The slot details to add
     * @return The created Slot with generated ID
     */
    public Slot addSlot(String centerId, Slot slotDetails);

    /**
     * Adds a new time slot to a gym center.
     * Simplified version where centerId is included in slot object.
     * 
     * @param slot The slot to add (must have centerId set)
     * @return The created Slot with generated ID
     */
    public Slot addSlot(Slot slot);

    /**
     * Searches for active gym centers in a specific city.
     * 
     * @param city    The city to search in
     * @param filters Optional filters for the search
     * @return List of active gym centers in the city
     */
    public List<GymCenter> searchCenters(String city, String filters);

    /**
     * Gets available slots for a specific gym center on a given date.
     * 
     * @param centerId The gym center ID
     * @param date     The date to search for slots
     * @return List of available slots
     */
    public List<Slot> getAvailableSlots(String centerId, String date);

    /**
     * Updates gym center information.
     * 
     * @param centerId The gym center ID
     * @param details  The updated gym details
     */
    public void updateCenterInfo(String centerId, GymCenter details);

    /**
     * Approves a gym center for operation (admin action).
     * 
     * @param centerId The gym center ID to approve
     * @return true if approval successful
     */
    public boolean approveGymCenter(String centerId);

    /**
     * Verifies (approves) a gym center (same as approveGymCenter).
     * 
     * @param centerId The gym center ID to verify
     * @return true if verification successful
     */
    public boolean verifyGym(String centerId);

    /**
     * Adds a new gym center with city and owner validation.
     * Admin operation to add gym centers to the system.
     * 
     * @param gym The gym center to add
     * @return The added GymCenter with generated ID
     * @throws IllegalArgumentException if gym is invalid
     * @throws GymNotFoundException     if owner does not exist
     */
    public GymCenter addCenter(GymCenter gym);

    /**
     * Views all gym centers in a specific city.
     * Admin operation to search gyms by city.
     * 
     * @param city The city to search in
     * @return List of all gym centers in the city (both active and pending)
     * @throws IllegalArgumentException if city is null or empty
     */
    public List<GymCenter> viewCenters(String city);
}
