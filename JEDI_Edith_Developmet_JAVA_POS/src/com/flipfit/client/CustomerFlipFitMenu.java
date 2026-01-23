package com.flipfit.client;

import java.util.Scanner;

public class CustomerFlipFitMenu {
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

			    if (choice == 6) break;

			    switch (choice) {
			        case 1: System.out.println("Listing all centers..."); break;
			        case 3: System.out.println("Enter Center and Slot details..."); break;
			        case 5: System.out.println("Enter Booking ID to cancel..."); break;
			        default: System.out.println("Invalid choice.");
			    }
			}
		}
    }
}