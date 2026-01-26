package com.flipfit.business;

import java.util.Date;
import java.util.UUID;

import com.flipfit.bean.Booking;
import com.flipfit.bean.BookingStatus;
import com.flipfit.bean.GymCustomer;
import com.flipfit.bean.Slot;
import com.flipfit.dao.WaitlistDAO;
import com.flipfit.dao.WaitlistDAOImpl;

public class WaitlistServiceImpl implements WaitlistService {

	private WaitlistDAO waitlistDAO = new WaitlistDAOImpl();
	private BookingService bookingService = new BookingServiceImpl();
	private INotificationProvider notificationService = new NotificationServiceImpl();

	// Shared slot storage
	private static java.util.Map<String, Slot> slotMap = GymServiceImpl.slotMap;

	@Override
	public void addToWaitlist(String customerId, String slotId) {
		// TODO Auto-generated method stub
		Slot slot = slotMap.get(slotId);
		if (slot == null) {
			System.out.println("Slot not found. Cannot add to waitlist.");
			return;
		}

		waitlistDAO.addUserToWaitlist(slotId, customerId);

		notificationService.sendNotification(
				customerId,
				"Slot is full. You have been added to the WAITLIST for Slot ID: " + slotId
		);
		
	}

	@Override
	public GymCustomer promoteNextUser(String slotId) {
		// TODO Auto-generated method stub
		Slot slot= slotMap.get(slotId);
		if (slot == null) {
			return null;
		}

		// Check if slot has capacity
		if (slot.getCurrentBookings() >= slot.getMaxCapacity()) {
			return null;
		}

		// Get next user from waitlist
		String nextUserId = waitlistDAO.getNextUser(slotId);
		if (nextUserId == null) {
			return null;
		}

		// Create booking for promoted user
		Booking booking = bookingService.createBooking(nextUserId, slotId);
		if (booking == null) {
			return null;
		}
		// Notify promoted user
		notificationService.sendNotification(
				nextUserId,
				"Good news! You have been PROMOTED from the waitlist. Booking ID: "
						+ booking.getBookingId()
		);

		// Return promoted user (minimal object for now)
		GymCustomer promotedCustomer = new GymCustomer(
				nextUserId, "", "", "", null, "", ""
		);
		promotedCustomer.setActive(true);
		promotedCustomer.setRegistrationDate(new Date());

		return promotedCustomer;
	}

}
