package com.flipfit.business;

import java.util.List;

import com.flipfit.bean.Booking;

public interface BookingService {
	public Booking createBooking(String customerId, String slotId);
    public boolean cancelBooking(String bookingId);
    public List<Booking> getUpcomingBookings(String customerId);
    public boolean checkConcurrency(String slotId);
}
