package com.flipfit.business;

import java.util.List;
import java.util.Date;
import java.util.UUID;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.SQLException;

import com.flipfit.bean.Booking;
import com.flipfit.bean.BookingStatus;
import com.flipfit.bean.Slot;
import com.flipfit.dao.*;
import com.flipfit.utils.DBConnection;


public class BookingServiceImpl implements BookingService {

	private BookingDAO bookingDAO = new BookingDAOImpl();
	private GymOwnerDAO gymOwnerDAO = new GymOwnerDAOImpl();
	private INotificationProvider notificationService = new NotificationServiceImpl();
	private SlotDAO slotDAO = new SlotDAOImpl();
	private SlotManager slotManager = new SlotManager();
	//private static java.util.Map<String, Slot> slotMap = GymServiceImpl.slotMap;

	@Override
	public Booking createBooking(String customerId, String slotId) {
		Slot slot = slotDAO.getSlotById(slotId);
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

		//Update slot count (in-memory)
		slot.setCurrentBookings(slot.getCurrentBookings() + 1);

		//Persist booking via DAO
		boolean ok = bookingDAO.createBooking(booking);
		if (!ok) {
			// rollback in-memory counter
			slot.setCurrentBookings(slot.getCurrentBookings() - 1);
			System.out.println("Failed to persist booking.");
			return null;
		}

		notificationService.sendNotification(
				customerId,
				"Booking CONFIRMED. Booking ID: " + booking.getBookingId()
		);

		return booking;
	}

	@Override
	public boolean cancelBooking(String bookingId) {
		// retrieve booking by id
		Booking booking = bookingDAO.getBookingById(bookingId);
		if (booking == null) return false;

		Slot slot = slotDAO.getSlotById(booking.getSlotId());
		if (slot != null) {
			if (slot.getCurrentBookings() > 0) {
				slot.setCurrentBookings(slot.getCurrentBookings() - 1);
				// persist change
				slotDAO.updateSlot(slot);
			}
		}

		boolean ok = bookingDAO.cancelBooking(bookingId);
		if (ok) {
			notificationService.sendNotification(booking.getUserId(), "Booking CANCELLED. Booking ID: " + bookingId);
		}
		return ok;
	}

	@Override
	public List<Booking> getUpcomingBookings(String customerId) {
		return bookingDAO.getBookingsByUserId(customerId);
	}

	@Override
	public boolean checkConcurrency(String slotId) {
		Slot slot = slotDAO.getSlotById(slotId);
		return slot != null && slot.getCurrentBookings() < slot.getMaxCapacity();
	}

	/**
	 * Interval overlap: startA < endB && startB < endA
	 */
	@Override
	public List<Booking> getConflictingBookings(String userId, String slotId) {
		List<Booking> conflicts = new ArrayList<>();
		Slot target = slotDAO.getSlotById(slotId);
		if (target == null) return conflicts;

		List<Booking> userBookings = bookingDAO.getBookingsByUserId(userId);
		for (Booking b : userBookings) {
			Slot s = slotDAO.getSlotById(b.getSlotId());
			if (s == null || BookingStatus.CANCELLED.equals(b.getStatus())) continue;
			if (timesOverlap(target.getStartTime(), target.getEndTime(), s.getStartTime(), s.getEndTime())) {
				conflicts.add(b);
			}
		}
		return conflicts;
	}

	private boolean timesOverlap(java.time.LocalTime aStart, java.time.LocalTime aEnd, java.time.LocalTime bStart, java.time.LocalTime bEnd) {
		return aStart.isBefore(bEnd) && bStart.isBefore(aEnd);
	}

	/**
	 * Cancels provided bookings and creates a new booking inside a DB transaction.
	 * Cancels all conflicting bookings if user confirmed.
	 */
	@Override
	public Booking createBookingWithReplace(String customerId, String slotId, List<String> bookingIdsToCancel) {
		Connection conn = null;
		try {
			conn = DBConnection.getConnection();
			conn.setAutoCommit(false);

			// 1) cancel old bookings (transactional)
			for (String oldId : bookingIdsToCancel) {
				Booking old = bookingDAO.getBookingById(oldId);
				if (old == null) continue;
				Slot oldSlot = slotDAO.getSlotById(old.getSlotId());
				if (oldSlot != null) {
					int newCount = Math.max(0, oldSlot.getCurrentBookings() - 1);
					boolean slotUpdated = slotDAO.updateSlotBookingCount(conn, oldSlot.getSlotId(), newCount);
					if (!slotUpdated) { conn.rollback(); return null; }
					oldSlot.setCurrentBookings(newCount);
				}
				boolean canceled = bookingDAO.cancelBooking(conn, oldId);
				if (!canceled) { conn.rollback(); return null; }
			}

			// 2) check and increment new slot
			Slot newSlot = slotDAO.getSlotById(slotId);
			if (newSlot == null) { conn.rollback(); return null; }
			if (newSlot.getCurrentBookings() >= newSlot.getMaxCapacity()) { conn.rollback(); System.out.println("Target slot full."); return null; }
			int newCount = newSlot.getCurrentBookings() + 1;
			boolean slotUpdated = slotDAO.updateSlotBookingCount(conn, newSlot.getSlotId(), newCount);
			if (!slotUpdated) { conn.rollback(); return null; }
			newSlot.setCurrentBookings(newCount);

			// 3) create booking
			Booking booking = new Booking();
			booking.setBookingId(UUID.randomUUID().toString());
			booking.setUserId(customerId);
			booking.setSlotId(slotId);
			booking.setBookingDate(new Date());
			booking.setStatus(BookingStatus.CONFIRMED);

			boolean created = bookingDAO.createBooking(conn, booking);
			if (!created) { conn.rollback(); return null; }

			conn.commit();

			// send notifications
			notificationService.sendNotification(customerId, "Booking CONFIRMED. Booking ID: " + booking.getBookingId());
			for (String oldId : bookingIdsToCancel) {
				Booking old = bookingDAO.getBookingById(oldId);
				if (old != null) {
					notificationService.sendNotification(old.getUserId(), "Booking CANCELLED. Booking ID: " + oldId);
				}
			}

			return booking;
		} catch (SQLException e) {
			try { if (conn != null) conn.rollback(); } catch (SQLException ex) {}
			System.err.println("[Service] Transaction failed: " + e.getMessage());
			return null;
		} finally {
			if (conn != null) { try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) {} }
		}
	}
}
