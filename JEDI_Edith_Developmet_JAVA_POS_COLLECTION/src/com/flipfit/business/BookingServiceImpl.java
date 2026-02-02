package com.flipfit.business;

import java.util.List;
import java.util.Date;
import java.util.UUID;

import com.flipfit.bean.Booking;
import com.flipfit.bean.BookingStatus;
import com.flipfit.bean.Slot;
import com.flipfit.dao.BookingDAO;
import com.flipfit.dao.BookingDAOImpl;
import com.flipfit.dao.GymOwnerDAO;
import com.flipfit.dao.GymOwnerDAOImpl;


public class BookingServiceImpl implements BookingService {

	private BookingDAO bookingDAO = new BookingDAOImpl();
	private GymOwnerDAO gymOwnerDAO = new GymOwnerDAOImpl();
	private INotificationProvider notificationService = new NotificationServiceImpl();
	//private static java.util.Map<String, Slot> slotMap = GymServiceImpl.slotMap;

	@Override
	public Booking createBooking(String customerId, String slotId) {
		// TODO Auto-generated method stub
		Slot slot = gymOwnerDAO.getSlotById(slotId);
		if (slot == null) {
			System.out.println("Slot not found.");
			return null;
		}

		if (slot.getCurrentBookings() >= slot.getMaxCapacity()) {
			System.out.println("Slot full. Cannot book.");
			return null;
		}
		Booking booking = new Booking();
		booking.setBookingId(UUID.randomUUID().toString());
		booking.setUserId(customerId);
		booking.setSlotId(slotId);
		booking.setBookingDate(new Date());
		booking.setStatus(BookingStatus.CONFIRMED);

		//Update slot count
		slot.setCurrentBookings(slot.getCurrentBookings() + 1);

		//Persist booking via DAO
		bookingDAO.addBooking(booking);
		notificationService.sendNotification(
				customerId,
				"Booking CONFIRMED. Booking ID: " + booking.getBookingId()
		);

		return booking;
	}

	@Override
	public boolean cancelBooking(String bookingId) {
		// TODO Auto-generated method stub

		Booking booking = bookingDAO.getBookingsByUserId("")
				.stream()
				.filter(b -> b.getBookingId().equals(bookingId))
				.findFirst()
				.orElse(null);

		if (booking == null) {
			return false;
		}

		Slot slot = gymOwnerDAO.getSlotById(booking.getSlotId());
		if (slot != null) {
			slot.setCurrentBookings(slot.getCurrentBookings() - 1);
		}

		bookingDAO.cancelBooking(bookingId);

		notificationService.sendNotification(
				booking.getUserId(),
				"Booking CANCELLED. Booking ID: " + bookingId
		);

		return true;	}

	@Override
	public List<Booking> getUpcomingBookings(String customerId) {
		// TODO Auto-generated method stub

		return bookingDAO.getBookingsByUserId(customerId);
	}

	@Override
	public boolean checkConcurrency(String slotId) {
		// TODO Auto-generated method stub
		Slot slot = gymOwnerDAO.getSlotById(slotId);
		return slot != null && slot.getCurrentBookings() < slot.getMaxCapacity();
	}
//
}
