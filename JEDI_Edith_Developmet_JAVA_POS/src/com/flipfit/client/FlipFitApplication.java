package com.flipfit.client;

import com.flipfit.bean.GymCustomer;
import com.flipfit.bean.GymOwner;

import java.util.Scanner;

public class FlipFitApplication {
    private static Scanner scanner = new Scanner(System.in);
    private static AccountService accountService = new AccountServiceImpl();

    public static void main(String[] args) {
        System.out.println("WELCOME TO FLIPFIT APP");
        while (true) {
            System.out.println("1. Login\n2. Register Customer\n3. Register Gym Owner\n4. Exit");
            int choice = scanner.nextInt();
            switch (choice) {
                case 1: login(); break;
                case 2: registerCustomer(); break;
                case 3: registerOwner(); break;
                case 4: System.exit(0);
            }
        }
    }

    private static void registerOwner() {
		// TODO Auto-generated method stub
    	GymOwner owner = new GymOwner();
        System.out.println("--- Gym Owner Registration ---");
        System.out.print("Enter Name: "); owner.setName(scanner.next());
        System.out.print("Enter Email: "); owner.setEmail(scanner.next());
        System.out.print("Enter Password: "); owner.setPassword(scanner.next());
        System.out.print("Enter PAN Number: "); owner.setPanNumber(scanner.next());
        System.out.print("Enter GST Number: "); owner.setGstNumber(scanner.next());
        
        owner.setRole("OWNER");

        accountService.registerOwner(owner);
        System.out.println("Owner Registration Request Sent. Pending Admin Approval.");
	}

	private static void registerCustomer() {
		// TODO Auto-generated method stub
		GymCustomer customer = new GymCustomer();
	    System.out.println("--- Customer Registration ---");
	    System.out.print("Enter Name: "); customer.setName(scanner.next());
	    System.out.print("Enter Email: "); customer.setEmail(scanner.next());
	    System.out.print("Enter Password: "); customer.setPassword(scanner.next());
	    System.out.print("Enter Phone: "); customer.setPhoneNumber(scanner.next());
	    System.out.print("Enter City: "); customer.setCity(scanner.next());
	    
	    // Set role automatically for internal tracking
	    customer.setRole("CUSTOMER");

	    accountService.registerCustomer(customer);
	    System.out.println("Registration Request Sent Successfully!");
		
	}

	private static void login() {
        System.out.print("Email: "); String email = scanner.next();
        System.out.print("Password: "); String pass = scanner.next();
        
        String role = accountService.login(email, pass);
        
        if (role.equals("ADMIN")) AdminFlipFitMenu.showMenu();
        else if (role.equals("OWNER")) GymOwnerFlipFitMenu.showMenu();
        else if (role.equals("CUSTOMER")) CustomerFlipFitMenu.showMenu();
        else System.out.println("Invalid Credentials!");
    }
}