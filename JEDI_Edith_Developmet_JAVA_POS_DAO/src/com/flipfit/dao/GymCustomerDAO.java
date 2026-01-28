package com.flipfit.dao;

import com.flipfit.bean.GymCustomer;
import java.util.List;

/**
 * Data Access Object interface for GymCustomer entity.
 * Defines methods for CRUD operations on gym customers.
 */
public interface GymCustomerDAO {
    
    /**
     * Registers a new gym customer in the database.
     * @param gymCustomer the gym customer to register
     * @return true if successful, false otherwise
     */
    boolean registerGymCustomer(GymCustomer gymCustomer);
    
    /**
     * Retrieves a gym customer by their user ID.
     * @param userId the user ID
     * @return the GymCustomer object or null if not found
     */
    GymCustomer getGymCustomerByUserId(String userId);
    
    /**
     * Retrieves all gym customers in the system.
     * @return a list of all gym customers
     */
    List<GymCustomer> getAllGymCustomers();
    
    /**
     * Retrieves all active gym customers.
     * @return a list of active gym customers
     */
    List<GymCustomer> getActiveGymCustomers();
    
    /**
     * Retrieves all inactive gym customers.
     * @return a list of inactive gym customers
     */
    List<GymCustomer> getInactiveGymCustomers();
    
    /**
     * Updates an existing gym customer.
     * @param gymCustomer the gym customer with updated information
     * @return true if successful, false otherwise
     */
    boolean updateGymCustomer(GymCustomer gymCustomer);
    
    /**
     * Updates the active status of a gym customer.
     * @param userId the user ID of the gym customer
     * @param isActive the active status
     * @return true if successful, false otherwise
     */
    boolean updateActiveStatus(String userId, boolean isActive);
    
    /**
     * Deletes a gym customer by their user ID.
     * @param userId the user ID to delete
     * @return true if successful, false otherwise
     */
    boolean deleteGymCustomer(String userId);
}
