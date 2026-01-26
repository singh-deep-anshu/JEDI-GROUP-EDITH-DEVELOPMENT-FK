package com.flipfit.business;

import java.util.List;
import java.util.Date;
import java.util.UUID;

import com.flipfit.bean.Booking;
import com.flipfit.bean.BookingStatus;
import com.flipfit.bean.Slot;
import com.flipfit.dao.BookingDAO;
import com.flipfit.dao.BookingDAOImpl;

public class BookingServiceImpl implements BookingService {

	private BookingDAO bookingDAO = new BookingDAOImpl();
	private static java.util.Map<String, Slot> slotMap = GymServiceImpl.slotMap;

	@Override
	public Booking createBooking(String customerId, String slotId) {
		// TODO Auto-generated method stub
		Slot slot = slotMap.get(slotId);
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

		System.out.println("Booking successful. Booking ID: " + booking.getBookingId());
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

		Slot slot = slotMap.get(booking.getSlotId());
		if (slot != null) {
			slot.setCurrentBookings(slot.getCurrentBookings() - 1);
		}

		return bookingDAO.cancelBooking(bookingId);
	}

	@Override
	public List<Booking> getUpcomingBookings(String customerId) {
		// TODO Auto-generated method stub

		return bookingDAO.getBookingsByUserId(customerId);
	}

	@Override
	public boolean checkConcurrency(String slotId) {
		// TODO Auto-generated method stub
		Slot slot = slotMap.get(slotId);
		return slot != null && slot.getCurrentBookings() < slot.getMaxCapacity();
	}

}
