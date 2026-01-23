package com.flipfit.business;

import java.util.List;

import com.flipfit.bean.Booking;

public class BookingServiceImpl implements BookingService {

	@Override
	public Booking createBooking(String customerId, String slotId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean cancelBooking(String bookingId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Booking> getUpcomingBookings(String customerId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean checkConcurrency(String slotId) {
		// TODO Auto-generated method stub
		return false;
	}

}
