package com.flipfit.client;

import java.util.List;
import java.util.Scanner;
import com.flipfit.bean.GymCenter;
import com.flipfit.business.GymService;
import com.flipfit.business.GymServiceImpl;
import com.flipfit.dao.GymOwnerDAOImpl;

public class CustomerFlipFitMenu {
	private static GymService gymService = new GymServiceImpl();
	private static GymOwnerDAOImpl gymDAO = new GymOwnerDAOImpl();

	public static void showMenu() {
		try (Scanner sc = new Scanner(System.in)) {
			while (true) {
				System.out.println("\n===== CUSTOMER DASHBOARD =====");
				System.out.println("1. View All Gym Centers");
				System.out.println("2. Search Gyms by City");
				System.out.println("3. Book a Slot");
				System.out.println("4. View My Bookings");
				System.out.println("5. Cancel a Booking");
				System.out.println("6. Logout");
				System.out.print("Enter choice: ");
				int choice = sc.nextInt();
				sc.nextLine(); // Consume newline

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
						System.out.println("Enter Center and Slot details...");
						break;
					case 4:
						System.out.println("Viewing your bookings...");
						break;
					case 5:
						System.out.println("Enter Booking ID to cancel...");
						break;
					default:
						System.out.println("Invalid choice.");
				}
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
}