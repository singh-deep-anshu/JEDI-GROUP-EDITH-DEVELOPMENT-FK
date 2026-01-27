package com.flipfit.client;

import java.util.List;
import java.util.Scanner;
import com.flipfit.bean.GymCenter;
import com.flipfit.bean.Slot;
import com.flipfit.bean.Booking;
import com.flipfit.business.GymService;
import com.flipfit.business.GymServiceImpl;
import com.flipfit.business.BookingService;
import com.flipfit.business.BookingServiceImpl;
import com.flipfit.dao.GymOwnerDAOImpl;

public class CustomerFlipFitMenu {
	private static GymService gymService = new GymServiceImpl();
	private static GymOwnerDAOImpl gymDAO = new GymOwnerDAOImpl();
	private static BookingService bookingService = new BookingServiceImpl();

	public static void showMenu(String userId) {
		Scanner sc = new Scanner(System.in);
		while (true) {
			System.out.println("\n===== CUSTOMER DASHBOARD =====");
			System.out.println("1. View All Gym Centers");
			System.out.println("2. Search Gyms by City");
			System.out.println("3. Book a Slot");
			System.out.println("4. View My Bookings");
			System.out.println("5. Cancel a Booking");
			System.out.println("6. Logout");
			System.out.print("Enter choice: ");
			int choice = -1;
			try {
				choice = Integer.parseInt(sc.nextLine());
			} catch (NumberFormatException e) {
				System.out.println("Invalid input.");
				continue;
			}

			if (choice == 6)
				break;

			switch (choice) {
				case 1:
					viewAllCenters();
					break;
				case 2:
					searchGymsByCity(sc);
					break;
				case 3:
					bookSlotFlow(sc, userId);
					break;
				case 4:
					viewMyBookings(userId);
					break;
				case 5:
					cancelBookingFlow(sc, userId);
					break;
				default:
					System.out.println("Invalid choice.");
			}
		}
	}

	private static void viewAllCenters() {
		List<GymCenter> allCenters = gymDAO.getAllCenters();
		List<GymCenter> activeCenters = allCenters.stream()
				.filter(GymCenter::isActive)
				.toList();

		if (activeCenters.isEmpty()) {
			System.out.println("\nNo active gym centers available.");
			return;
		}

		System.out.println("\n===== ALL GYM CENTERS =====");
		displayCenters(activeCenters);
	}

	private static void searchGymsByCity(Scanner sc) {
		System.out.print("\nEnter City Name: ");
		String city = sc.nextLine();

		try {
			List<GymCenter> centers = gymService.searchCenters(city, null);

			if (centers.isEmpty()) {
				System.out.println("No gym centers found in '" + city + "'.");
				return;
			}

			System.out.println("\n===== GYM CENTERS IN " + city.toUpperCase() + " =====");
			displayCenters(centers);

		} catch (IllegalArgumentException e) {
			System.out.println("Error: " + e.getMessage());
		}
	}

	private static void displayCenters(List<GymCenter> centers) {
		System.out.println(String.format("%-36s | %-20s | %-15s | %s",
				"Center ID", "Name", "City", "Capacity"));
		System.out.println("-".repeat(100));

		for (GymCenter gym : centers) {
			System.out.println(String.format("%-36s | %-20s | %-15s | %d",
					gym.getCenterId(),
					gym.getName(),
					gym.getCityId(),
					gym.getTotalCapacity()));
		}
	}

	private static void bookSlotFlow(Scanner sc, String userId) {
		List<GymCenter> allCenters = gymDAO.getAllCenters();
		List<GymCenter> activeCenters = allCenters.stream().filter(GymCenter::isActive).toList();

		if (activeCenters.isEmpty()) {
			System.out.println("No active gym centers available to book.");
			return;
		}

		System.out.println("\nSelect a gym to view slots:");
		for (int i = 0; i < activeCenters.size(); i++) {
			GymCenter g = activeCenters.get(i);
			System.out.println((i + 1) + ". " + g.getName() + " (" + g.getCityId() + ") - ID: " + g.getCenterId());
		}
		System.out.print("Enter choice number: ");
		int idx;
		try {
			idx = Integer.parseInt(sc.nextLine()) - 1;
		} catch (NumberFormatException e) {
			System.out.println("Invalid input.");
			return;
		}

		if (idx < 0 || idx >= activeCenters.size()) {
			System.out.println("Invalid selection.");
			return;
		}

		String centerId = activeCenters.get(idx).getCenterId();
		List<Slot> slots = gymDAO.getSlotsByCenterId(centerId);
		if (slots.isEmpty()) {
			System.out.println("No slots available for this center.");
			return;
		}

		System.out.println("\nAvailable slots:");
		List<Slot> available = slots.stream().filter(s -> s.getCurrentBookings() < s.getMaxCapacity()).toList();
		for (int i = 0; i < available.size(); i++) {
			Slot s = available.get(i);
			System.out.println((i + 1) + ". Slot ID: " + s.getSlotId() + " | " + s.getStartTime() + " - " + s.getEndTime() + " | " + s.getCurrentBookings() + "/" + s.getMaxCapacity());
		}
		if (available.isEmpty()) {
			System.out.println("No available slots (all full).");
			return;
		}

		System.out.print("Select slot number to book: ");
		int sidx;
		try {
			sidx = Integer.parseInt(sc.nextLine()) - 1;
		} catch (NumberFormatException e) {
			System.out.println("Invalid input.");
			return;
		}

		if (sidx < 0 || sidx >= available.size()) {
			System.out.println("Invalid selection.");
			return;
		}

		String slotId = available.get(sidx).getSlotId();
		Booking booking = bookingService.createBooking(userId, slotId);
		if (booking != null) {
			System.out.println("Booking successful. Booking ID: " + booking.getBookingId());
		} else {
			System.out.println("Booking failed.");
		}
	}

	private static void viewMyBookings(String userId) {
		List<Booking> bookings = bookingService.getUpcomingBookings(userId);
		if (bookings == null || bookings.isEmpty()) {
			System.out.println("No upcoming bookings.");
			return;
		}

		System.out.println("\n===== YOUR BOOKINGS =====");
		for (Booking b : bookings) {
			System.out.println("Booking ID: " + b.getBookingId() + " | Slot ID: " + b.getSlotId() + " | Date: " + b.getBookingDate() + " | Status: " + b.getStatus());
		}
	}

	private static void cancelBookingFlow(Scanner sc, String userId) {
		System.out.print("Enter Booking ID to cancel: ");
		String bookingId = sc.nextLine();
		boolean ok = bookingService.cancelBooking(bookingId);
		if (ok) {
			System.out.println("Booking cancelled.");
		} else {
			System.out.println("Failed to cancel booking. Check Booking ID.");
		}
	}
}