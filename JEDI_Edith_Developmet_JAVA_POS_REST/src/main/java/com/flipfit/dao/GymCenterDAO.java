package com.flipfit.dao;

import com.flipfit.bean.GymCenter;
import java.util.List;

/**
 * Data Access Object interface for GymCenter entity.
 * Defines methods for CRUD operations on gym centers.
 */
public interface GymCenterDAO {
    
    /**
     * Adds a new gym center to the database.
     * @param gymCenter the gym center to add
     * @return true if successful, false otherwise
     */
    boolean addGymCenter(GymCenter gymCenter);
    
    /**
     * Retrieves a gym center by its ID.
     * @param centerId the center ID
     * @return the GymCenter object or null if not found
     */
    GymCenter getGymCenterById(String centerId);
    
    /**
     * Retrieves all gym centers owned by a specific owner.
     * @param ownerId the owner's user ID
     * @return a list of gym centers for that owner
     */
    List<GymCenter> getGymCentersByOwnerId(String ownerId);
    
    /**
     * Retrieves all gym centers in a specific city.
     * @param cityId the city ID
     * @return a list of gym centers in that city
     */
    List<GymCenter> getGymCentersByCity(String cityId);
    
    /**
     * Retrieves all gym centers in the system.
     * @return a list of all gym centers
     */
    List<GymCenter> getAllGymCenters();
    
    /**
     * Updates an existing gym center.
     * @param gymCenter the gym center with updated information
     * @return true if successful, false otherwise
     */
    boolean updateGymCenter(GymCenter gymCenter);
    
    /**
     * Deletes a gym center by its ID.
     * @param centerId the center ID to delete
     * @return true if successful, false otherwise
     */
    boolean deleteGymCenter(String centerId);
}
