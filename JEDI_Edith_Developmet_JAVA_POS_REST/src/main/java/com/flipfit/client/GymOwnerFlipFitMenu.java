package com.flipfit.client;

import java.time.LocalTime;
import java.util.List;
import java.util.Scanner;
import com.flipfit.bean.GymCenter;
import com.flipfit.bean.Slot;
import com.flipfit.business.GymService;
import com.flipfit.business.GymServiceImpl;
import com.flipfit.dao.GymCenterDAO;
import com.flipfit.dao.GymCenterDAOImpl;

public class GymOwnerFlipFitMenu {
	private static GymService gymService = new GymServiceImpl();
	private static GymCenterDAO gymCenterDAO = new GymCenterDAOImpl();

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
					addSlotsToCenter(sc, ownerId);
					break;
				case 3:
					viewRegisteredCenters(ownerId);
					break;
				case 4:
					updateCenterDetails(sc, ownerId);
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

	private static void addSlotsToCenter(Scanner sc, String ownerId) {
		System.out.println("\n--- Add Slots to Center ---");

		// First, display owner's registered centers
		List<GymCenter> centers = gymCenterDAO.getGymCentersByOwnerId(ownerId);
		if (centers == null || centers.isEmpty()) {
			System.out.println("You have no registered gym centers.");
			return;
		}

		System.out.println("\nYour Registered Centers:");
		for (int i = 0; i < centers.size(); i++) {
			System.out.println((i + 1) + ". " + centers.get(i).getName() + " (ID: " + centers.get(i).getCenterId() + ")");
		}

		System.out.print("Select a center (enter number): ");
		int centerChoice = sc.nextInt();
		sc.nextLine();

		if (centerChoice < 1 || centerChoice > centers.size()) {
			System.out.println("Invalid center selection.");
			return;
		}

		GymCenter selectedCenter = centers.get(centerChoice - 1);
		Slot slot = new Slot();
		slot.setCenterId(selectedCenter.getCenterId());

		System.out.println("\n--- Enter Slot Details ---");

		System.out.print("Enter Start Time (HH:MM format, e.g., 06:00): ");
		String startTimeStr = sc.nextLine();
		try {
			slot.setStartTime(LocalTime.parse(startTimeStr));
		} catch (Exception e) {
			System.out.println("Invalid time format. Please use HH:MM format.");
			return;
		}

		System.out.print("Enter End Time (HH:MM format, e.g., 07:00): ");
		String endTimeStr = sc.nextLine();
		try {
			slot.setEndTime(LocalTime.parse(endTimeStr));
		} catch (Exception e) {
			System.out.println("Invalid time format. Please use HH:MM format.");
			return;
		}

		System.out.print("Enter Maximum Capacity: ");
		slot.setMaxCapacity(sc.nextInt());
		sc.nextLine();

		try {
			Slot createdSlot = gymService.addSlot(slot);
			System.out.println("\nSlot Added Successfully!");
			System.out.println("Slot ID: " + createdSlot.getSlotId());
			System.out.println("Time: " + createdSlot.getStartTime() + " to " + createdSlot.getEndTime());
			System.out.println("Capacity: " + createdSlot.getMaxCapacity());
		} catch (Exception e) {
			System.out.println("Error adding slot: " + e.getMessage());
		}
	}

	private static void viewRegisteredCenters(String ownerId) {
		System.out.println("\n--- Your Registered Gym Centers ---");

		List<GymCenter> centers = gymCenterDAO.getGymCentersByOwnerId(ownerId);
		if (centers == null || centers.isEmpty()) {
			System.out.println("You have no registered gym centers.");
			return;
		}

		System.out.println("\nTotal Centers: " + centers.size());
		for (GymCenter center : centers) {
			System.out.println("\n" + (centers.indexOf(center) + 1) + ". " + center.getName());
			System.out.println("   Center ID: " + center.getCenterId());
			System.out.println("   Address: " + center.getAddress());
			System.out.println("   City: " + center.getCityId());
			System.out.println("   Total Capacity: " + center.getTotalCapacity());
			System.out.println("   Status: " + (center.isActive() ? "Active (Approved)" : "Pending Admin Approval"));
		}
	}

	private static void updateCenterDetails(Scanner sc, String ownerId) {
		System.out.println("\n--- Update Center Details ---");

		// First, display owner's registered centers
		List<GymCenter> centers = gymCenterDAO.getGymCentersByOwnerId(ownerId);
		if (centers == null || centers.isEmpty()) {
			System.out.println("You have no registered gym centers.");
			return;
		}

		System.out.println("\nYour Registered Centers:");
		for (int i = 0; i < centers.size(); i++) {
			System.out.println((i + 1) + ". " + centers.get(i).getName() + " (ID: " + centers.get(i).getCenterId() + ")");
		}

		System.out.print("Select a center to update (enter number): ");
		int centerChoice = sc.nextInt();
		sc.nextLine();

		if (centerChoice < 1 || centerChoice > centers.size()) {
			System.out.println("Invalid center selection.");
			return;
		}

		GymCenter selectedCenter = centers.get(centerChoice - 1);

		System.out.println("\nCurrent Details:");
		System.out.println("Name: " + selectedCenter.getName());
		System.out.println("Address: " + selectedCenter.getAddress());
		System.out.println("City: " + selectedCenter.getCityId());
		System.out.println("Total Capacity: " + selectedCenter.getTotalCapacity());

		System.out.println("\nWhat would you like to update?");
		System.out.println("1. Name");
		System.out.println("2. Address");
		System.out.println("3. City");
		System.out.println("4. Total Capacity");
		System.out.print("Enter choice: ");
		int updateChoice = sc.nextInt();
		sc.nextLine();

		switch (updateChoice) {
			case 1:
				System.out.print("Enter new name: ");
				selectedCenter.setName(sc.nextLine());
				break;
			case 2:
				System.out.print("Enter new address: ");
				selectedCenter.setAddress(sc.nextLine());
				break;
			case 3:
				System.out.print("Enter new city: ");
				selectedCenter.setCityId(sc.nextLine());
				break;
			case 4:
				System.out.print("Enter new total capacity: ");
				selectedCenter.setTotalCapacity(sc.nextInt());
				sc.nextLine();
				break;
			default:
				System.out.println("Invalid choice.");
				return;
		}

		try {
			gymService.updateCenterInfo(selectedCenter.getCenterId(), selectedCenter);
			System.out.println("\nGym Center Updated Successfully!");
		} catch (Exception e) {
			System.out.println("Error updating center: " + e.getMessage());
		}
	}
}