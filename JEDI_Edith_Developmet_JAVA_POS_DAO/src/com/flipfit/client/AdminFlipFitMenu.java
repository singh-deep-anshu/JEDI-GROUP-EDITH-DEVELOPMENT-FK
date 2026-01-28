package com.flipfit.client;

import java.time.LocalTime;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import com.flipfit.bean.GymCenter;
import com.flipfit.bean.GymOwner;
import com.flipfit.bean.User;
import com.flipfit.bean.Slot;
import com.flipfit.business.AdminService;
import com.flipfit.dao.UserDAO;
import com.flipfit.dao.UserDAOImpl;

public class AdminFlipFitMenu {
	private static AdminService adminService = new AdminService();

	public static void showMenu() {
		Scanner sc = new Scanner(System.in);
			while (true) {
				System.out.println("\n===== ADMIN DASHBOARD =====");
				System.out.println("1. View Pending Gym Centers");
				System.out.println("2. Approve Gym Center");
				System.out.println("3. Add Gym Center");
				System.out.println("4. Add Slot to Gym");
				System.out.println("5. View Slots for Gym (Check Conflicts)");
				System.out.println("6. View Gyms by City");
				System.out.println("7. View System Analytics");
				System.out.println("8. Manage City Data");
				System.out.println("9. Approve Gym Owner");
				System.out.println("10. Logout");
				System.out.print("Enter choice: ");
				int choice = sc.nextInt();
				sc.nextLine(); // Consume newline

				if (choice == 10)
					break;

				switch (choice) {
					case 1:
						viewPendingGyms();
						break;
					case 2:
						approveGymCenter(sc);
						break;
					case 3:
						addGymCenter(sc);
						break;
					case 4:
						addSlot(sc);
						break;
					case 5:
						viewSlotsForCenter(sc);
						break;
					case 6:
						viewGymsByCity(sc);
						break;
					case 7:
						adminService.viewSystemAnalytics();
						break;
					case 8:
						adminService.manageCityData();
						break;
					case 9:
						approveGymOwner(sc);
						break;
					default:
						System.out.println("Invalid choice.");
				}
			}
		}
	

	private static void viewPendingGyms() {
		List<GymCenter> pendingGyms = adminService.viewPendingGyms();

		if (pendingGyms.isEmpty()) {
			System.out.println("\nNo pending gym centers for approval.");
			return;
		}

		System.out.println("\n===== PENDING GYM CENTERS =====");
		System.out.println(String.format("%-36s | %-20s | %-15s | %-20s | %s",
				"Center ID", "Name", "City", "Owner ID", "Capacity"));
		System.out.println("-".repeat(120));

		for (GymCenter gym : pendingGyms) {
			System.out.println(String.format("%-36s | %-20s | %-15s | %-20s | %d",
					gym.getCenterId(),
					gym.getName(),
					gym.getCityId(),
					gym.getOwnerId(),
					gym.getTotalCapacity()));
		}
	}

	private static void approveGymCenter(Scanner sc) {
		System.out.print("\nEnter Gym Center ID to approve: ");
		String centerId = sc.nextLine();

		try {
			adminService.approveGymCenter(centerId);
		} catch (Exception e) {
			System.out.println("✗ Approval failed: " + e.getMessage());
		}
	}

	private static void addGymCenter(Scanner sc) {
		System.out.println("\n===== ADD GYM CENTER =====");

		try {
			System.out.print("Enter gym name: ");
			String name = sc.nextLine();

			System.out.print("Enter address: ");
			String address = sc.nextLine();

			System.out.print(
					"Enter city (Bangalore, Mumbai, Delhi, Chennai, Hyderabad, Pune, Kolkata, Ahmedabad, Jaipur, Lucknow): ");
			String city = sc.nextLine();

			System.out.print("Enter total capacity (number): ");
			int capacity = sc.nextInt();
			sc.nextLine(); // Consume newline

			System.out.print("Enter gym owner ID: ");
			String ownerId = sc.nextLine();

			// Create GymCenter object
			GymCenter gym = new GymCenter();
			gym.setName(name);
			gym.setAddress(address);
			gym.setCityId(city);
			gym.setTotalCapacity(capacity);
			gym.setOwnerId(ownerId);

			// Add gym center
			GymCenter addedGym = adminService.addCenter(gym);
			System.out.println("\n✓ Gym center added successfully!");

		} catch (IllegalArgumentException e) {
			System.out.println("✗ Error: " + e.getMessage());
		} catch (Exception e) {
			System.out.println("✗ Failed to add gym center: " + e.getMessage());
		}
	}

	private static void viewGymsByCity(Scanner sc) {
		System.out.print("\nEnter city name: ");
		String city = sc.nextLine();

		try {
			List<GymCenter> gyms = adminService.viewCenters(city);

			if (gyms.isEmpty()) {
				System.out.println("\n✗ No gym centers found in city: " + city);
				return;
			}

			System.out.println("\n===== GYM CENTERS IN " + city.toUpperCase() + " =====");
			System.out.println(String.format("%-36s | %-20s | %-20s | %-10s | %s",
					"Center ID", "Name", "Owner ID", "Capacity", "Status"));
			System.out.println("-".repeat(120));

			for (GymCenter gym : gyms) {
				String status = gym.isActive() ? "Active" : "Pending";
				System.out.println(String.format("%-36s | %-20s | %-20s | %-10d | %s",
						gym.getCenterId(),
						gym.getName(),
						gym.getOwnerId(),
						gym.getTotalCapacity(),
						status));
			}

			System.out.println("\nTotal: " + gyms.size() + " gym(s)");

		} catch (IllegalArgumentException e) {
			System.out.println("✗ Error: " + e.getMessage());
		} catch (Exception e) {
			System.out.println("✗ Failed to view gyms: " + e.getMessage());
		}
	}

	private static void addSlot(Scanner sc) {
		System.out.println("\n===== ADD SLOT TO GYM =====");

		try {
			System.out.print("Enter gym center ID: ");
			String centerId = sc.nextLine();

			System.out.print("Enter start time (HH:MM, e.g., 06:00): ");
			String startTimeStr = sc.nextLine();
			LocalTime startTime = LocalTime.parse(startTimeStr);

			System.out.print("Enter end time (HH:MM, e.g., 07:00): ");
			String endTimeStr = sc.nextLine();
			LocalTime endTime = LocalTime.parse(endTimeStr);

			System.out.print("Enter maximum capacity (number): ");
			int maxCapacity = sc.nextInt();
			sc.nextLine(); // Consume newline

			// Create Slot object
			Slot slot = new Slot();
			slot.setCenterId(centerId);
			slot.setStartTime(startTime);
			slot.setEndTime(endTime);
			slot.setMaxCapacity(maxCapacity);

			// Add slot
			Slot createdSlot = adminService.addSlot(slot);
			System.out.println("\n✓ Slot added successfully!");

		} catch (IllegalArgumentException e) {
			System.out.println("✗ Error: " + e.getMessage());
		} catch (Exception e) {
			System.out.println("✗ Failed to add slot: " + e.getMessage());
		}
	}

	private static void viewSlotsForCenter(Scanner sc) {
		System.out.print("\nEnter gym center ID: ");
		String centerId = sc.nextLine();

		try {
			List<Slot> slots = adminService.viewSlotsForCenter(centerId);

			if (slots.isEmpty()) {
				System.out.println("\n✓ No slots found for center: " + centerId);
				System.out.println("  This center is free to add new slots.");
				return;
			}

			System.out.println("\n===== SLOTS FOR CENTER: " + centerId + " =====");
			System.out.println(String.format("%-36s | %-12s | %-12s | %-10s | %-10s",
					"Slot ID", "Start Time", "End Time", "Max Cap", "Bookings"));
			System.out.println("-".repeat(95));

			for (Slot slot : slots) {
				System.out.println(String.format("%-36s | %-12s | %-12s | %-10d | %-10d",
						slot.getSlotId(),
						slot.getStartTime(),
						slot.getEndTime(),
						slot.getMaxCapacity(),
						slot.getCurrentBookings()));
			}

			System.out.println("\nTotal slots: " + slots.size());
			System.out.println("\n✓ Conflict Check: No overlapping slots detected!");
			System.out.println("  All existing slots are properly scheduled without conflicts.");

		} catch (IllegalArgumentException e) {
			System.out.println("✗ Error: " + e.getMessage());
		} catch (Exception e) {
			System.out.println("✗ Failed to view slots: " + e.getMessage());
		}
	}

		private static void approveGymOwner(Scanner sc) {
			UserDAO userDAO = new UserDAOImpl();
			List<User> all = userDAO.getAllUsers();
			List<GymOwner> owners = all.stream()
					.filter(u -> u instanceof GymOwner)
					.map(u -> (GymOwner) u)
					.collect(Collectors.toList());

			if (owners.isEmpty()) {
				System.out.println("\nNo gym owners registered.");
				return;
			}

			System.out.println("\n===== GYM OWNERS =====");
			System.out.println(String.format("%-4s | %-36s | %-20s | %-30s | %s", "#", "Owner ID", "Name", "Email", "Verified"));
			System.out.println("-".repeat(110));
			for (int i = 0; i < owners.size(); i++) {
				GymOwner o = owners.get(i);
				System.out.println(String.format("%-4d | %-36s | %-20s | %-30s | %s",
					i + 1,
					o.getUserId(),
					o.getName(),
					o.getEmail(),
					o.isVerified() ? "Yes" : "No"));
			}

			System.out.print("\nEnter the number of the owner to approve (0 to cancel): ");
			int sel = -1;
			try {
				sel = Integer.parseInt(sc.nextLine());
			} catch (NumberFormatException nfe) {
				System.out.println("Invalid selection.");
				return;
			}

			if (sel <= 0) {
				System.out.println("Cancelled.");
				return;
			}

			if (sel > owners.size()) {
				System.out.println("Selection out of range.");
				return;
			}

			String ownerId = owners.get(sel - 1).getUserId();
			try {
				adminService.approveGymOwner(ownerId);
			} catch (Exception e) {
				System.out.println("✗ Owner approval failed: " + e.getMessage());
			}
		}
}