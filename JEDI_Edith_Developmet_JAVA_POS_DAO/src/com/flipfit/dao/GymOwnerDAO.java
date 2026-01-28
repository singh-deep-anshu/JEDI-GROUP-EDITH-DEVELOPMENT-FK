package com.flipfit.dao;

import com.flipfit.bean.GymOwner;
import java.util.List;

/**
 * Data Access Object interface for GymOwner entity.
 * Defines methods for CRUD operations on gym owners.
 */
public interface GymOwnerDAO {
    
    /**
     * Registers a new gym owner in the database.
     * @param gymOwner the gym owner to register
     * @return true if successful, false otherwise
     */
    boolean registerGymOwner(GymOwner gymOwner);
    
    /**
     * Retrieves a gym owner by their user ID.
     * @param userId the user ID
     * @return the GymOwner object or null if not found
     */
    GymOwner getGymOwnerByUserId(String userId);
    
    /**
     * Retrieves a gym owner by their PAN number.
     * @param panNumber the PAN number
     * @return the GymOwner object or null if not found
     */
    GymOwner getGymOwnerByPanNumber(String panNumber);
    
    /**
     * Retrieves all gym owners in the system.
     * @return a list of all gym owners
     */
    List<GymOwner> getAllGymOwners();
    
    /**
     * Retrieves all verified gym owners.
     * @return a list of verified gym owners
     */
    List<GymOwner> getVerifiedGymOwners();
    
    /**
     * Retrieves all unverified gym owners.
     * @return a list of unverified gym owners
     */
    List<GymOwner> getUnverifiedGymOwners();
    
    /**
     * Updates an existing gym owner.
     * @param gymOwner the gym owner with updated information
     * @return true if successful, false otherwise
     */
    boolean updateGymOwner(GymOwner gymOwner);
    
    /**
     * Updates the verification status of a gym owner.
     * @param userId the user ID of the gym owner
     * @param isVerified the verification status
     * @return true if successful, false otherwise
     */
    boolean updateVerificationStatus(String userId, boolean isVerified);
    
    /**
     * Deletes a gym owner by their user ID.
     * @param userId the user ID to delete
     * @return true if successful, false otherwise
     */
    boolean deleteGymOwner(String userId);
}