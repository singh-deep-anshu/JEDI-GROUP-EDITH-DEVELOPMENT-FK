package com.flipfit.client;

import java.util.Scanner;

public class AdminFlipFitMenu {
    public static void showMenu() {
        try (Scanner sc = new Scanner(System.in)) {
			while (true) {
			    System.out.println("\n===== ADMIN DASHBOARD =====");
			    System.out.println("1. View Pending Gym Owner Requests");
			    System.out.println("2. Approve Gym Center Onboarding");
			    System.out.println("3. View System Analytics");
			    System.out.println("4. Manage City Data");
			    System.out.println("5. Logout");
			    System.out.print("Enter choice: ");
			    int choice = sc.nextInt();

			    if (choice == 5) break;
			    
			    switch (choice) {
			        case 1: System.out.println("Fetching pending requests..."); break;
			        case 2: System.out.println("Enter Center ID to approve: "); break;
			        case 3: System.out.println("Generating reports..."); break;
			        default: System.out.println("Invalid choice.");
			    }
			}
		}
    }
}