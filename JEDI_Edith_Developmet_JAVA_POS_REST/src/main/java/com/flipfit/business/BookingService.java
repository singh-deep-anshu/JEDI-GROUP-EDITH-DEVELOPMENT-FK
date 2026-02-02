package com.flipfit.business;

import java.util.List;

import com.flipfit.bean.Booking;

public interface BookingService {
	public Booking createBooking(String customerId, String slotId);
    public boolean cancelBooking(String bookingId);
    public List<Booking> getUpcomingBookings(String customerId);
    public boolean checkConcurrency(String slotId);

    // Returns bookings for the user that time-overlap with the given slotId
    public java.util.List<Booking> getConflictingBookings(String userId, String slotId);

    // Cancels provided bookings and creates a new booking in a DB transaction. Returns new Booking on success.
    public Booking createBookingWithReplace(String customerId, String slotId, java.util.List<String> bookingIdsToCancel);
}
