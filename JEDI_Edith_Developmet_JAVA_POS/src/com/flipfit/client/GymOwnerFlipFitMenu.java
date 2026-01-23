package com.flipfit.client;

import java.util.Scanner;

public class GymOwnerFlipFitMenu {
    public static void showMenu() {
        try (Scanner sc = new Scanner(System.in)) {
			while (true) {
			    System.out.println("\n===== GYM OWNER DASHBOARD =====");
			    System.out.println("1. Add New Gym Center");
			    System.out.println("2. Add Slots to Center");
			    System.out.println("3. View My Registered Centers");
			    System.out.println("4. Update Center Details");
			    System.out.println("5. Logout");
			    System.out.print("Enter choice: ");
			    int choice = sc.nextInt();

			    if (choice == 5) break;

			    switch (choice) {
			        case 1: System.out.println("Redirecting to Add Center flow..."); break;
			        case 2: System.out.println("Redirecting to Slot management..."); break;
			        default: System.out.println("Invalid choice.");
			    }
			}
		}
    }
}