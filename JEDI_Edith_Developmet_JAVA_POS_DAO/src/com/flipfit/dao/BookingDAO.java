package com.flipfit.dao;

import com.flipfit.bean.Booking;
import com.flipfit.bean.BookingStatus;
import java.util.List;

/**
 * Data Access Object interface for Booking entity.
 * Defines methods for CRUD operations on gym bookings.
 */
public interface BookingDAO {
    
    /**
     * Creates a new booking in the database.
     * @param booking the booking to create
     * @return true if successful, false otherwise
     */
    boolean createBooking(Booking booking);
    
    /**
     * Retrieves a booking by its ID.
     * @param bookingId the booking ID
     * @return the Booking object or null if not found
     */
    Booking getBookingById(String bookingId);
    
    /**
     * Retrieves all bookings for a specific user.
     * @param userId the user ID
     * @return a list of bookings for that user
     */
    List<Booking> getBookingsByUserId(String userId);
    
    /**
     * Retrieves all bookings for a specific slot.
     * @param slotId the slot ID
     * @return a list of bookings for that slot
     */
    List<Booking> getBookingsBySlotId(String slotId);
    
    /**
     * Retrieves all bookings with a specific status.
     * @param status the booking status
     * @return a list of bookings with that status
     */
    List<Booking> getBookingsByStatus(BookingStatus status);
    
    /**
     * Retrieves all bookings in the system.
     * @return a list of all bookings
     */
    List<Booking> getAllBookings();
    
    /**
     * Updates an existing booking.
     * @param booking the booking with updated information
     * @return true if successful, false otherwise
     */
    boolean updateBooking(Booking booking);
    
    /**
     * Updates the status of a booking.
     * @param bookingId the booking ID
     * @param status the new status
     * @return true if successful, false otherwise
     */
    boolean updateBookingStatus(String bookingId, BookingStatus status);
    
    /**
     * Cancels a booking by updating its status to CANCELLED.
     * @param bookingId the booking ID to cancel
     * @return true if successful, false otherwise
     */
    boolean cancelBooking(String bookingId);
    
    /**
     * Deletes a booking by its ID.
     * @param bookingId the booking ID to delete
     * @return true if successful, false otherwise
     */
    boolean deleteBooking(String bookingId);
}