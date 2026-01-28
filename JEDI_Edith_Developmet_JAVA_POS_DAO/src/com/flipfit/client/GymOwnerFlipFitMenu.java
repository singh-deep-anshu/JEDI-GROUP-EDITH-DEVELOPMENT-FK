package com.flipfit.client;

import java.util.Scanner;
import com.flipfit.bean.GymCenter;
import com.flipfit.business.GymService;
import com.flipfit.business.GymServiceImpl;

public class GymOwnerFlipFitMenu {
	private static GymService gymService = new GymServiceImpl();

	public static void showMenu(String ownerId) {
		Scanner sc = new Scanner(System.in);

			while (true) {
				System.out.println("\n===== GYM OWNER DASHBOARD =====");
				System.out.println("1. Add New Gym Center");
				System.out.println("2. Add Slots to Center");
				System.out.println("3. View My Registered Centers");
				System.out.println("4. Update Center Details");
				System.out.println("5. Logout");
				System.out.print("Enter choice: ");
				int choice = sc.nextInt();
				sc.nextLine(); // Consume newline

				if (choice == 5)
					break;

				switch (choice) {
					case 1:
						addGymCenter(sc, ownerId);
						break;
					case 2:
						System.out.println("Redirecting to Slot management...");
						break;
					case 3:
						System.out.println("Viewing registered centers...");
						break;
					case 4:
						System.out.println("Updating center details...");
						break;
					default:
						System.out.println("Invalid choice.");
				}
			}
		}
	

	private static void addGymCenter(Scanner sc, String ownerId) {
		GymCenter gym = new GymCenter();

		System.out.println("\n--- Add New Gym Center ---");
		System.out.print("Enter Gym Name: ");
		gym.setName(sc.nextLine());

		System.out.print("Enter Address: ");
		gym.setAddress(sc.nextLine());

		System.out.print("Enter City: ");
		gym.setCityId(sc.nextLine());

		System.out.print("Enter Total Capacity: ");
		gym.setTotalCapacity(sc.nextInt());
		sc.nextLine(); // Consume newline

		try {
			GymCenter registeredGym = gymService.registerGymCenter(ownerId, gym);
			System.out.println("\nGym Center Registered Successfully!");
			System.out.println("Center ID: " + registeredGym.getCenterId());
			System.out.println("Status: Pending Admin Approval");
		} catch (IllegalArgumentException e) {
			System.out.println("Validation Error: " + e.getMessage());
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
	}
}