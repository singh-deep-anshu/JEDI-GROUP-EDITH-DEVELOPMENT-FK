package com.flipfit.client;

import com.flipfit.bean.GymCustomer;
import com.flipfit.bean.GymOwner;
import com.flipfit.business.AccountService;
import com.flipfit.business.AccountServiceImpl;

import java.util.Scanner;

public class FlipFitApplication {
    private static Scanner scanner = new Scanner(System.in);
    private static AccountService accountService = new AccountServiceImpl();

    public static void main(String[] args) {
        System.out.println("WELCOME TO FLIPFIT APP");
        while (true) {
            System.out.println("1. Login\n2. Register Customer\n3. Register Gym Owner\n4. Exit");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            switch (choice) {
                case 1:
                    login();
                    break;
                case 2:
                    registerCustomer();
                    break;
                case 3:
                    registerOwner();
                    break;
                case 4:
                    System.exit(0);
            }
        }
    }

    private static void registerOwner() {
        GymOwner owner = new GymOwner();
        System.out.println("--- Gym Owner Registration ---");
        System.out.print("Enter Name: ");
        owner.setName(scanner.nextLine());
        System.out.print("Enter Email: ");
        owner.setEmail(scanner.nextLine());
        System.out.print("Enter Password: ");
        owner.setPassword(scanner.nextLine());
        System.out.print("Enter Phone: ");
        owner.setPhoneNumber(scanner.nextLine());
        System.out.print("Enter City: ");
        owner.setCity(scanner.nextLine());
        System.out.print("Enter PAN Number: ");
        owner.setPanNumber(scanner.nextLine());
        System.out.print("Enter GST Number: ");
        owner.setGstNumber(scanner.nextLine());

        try {
            if (accountService.registerOwner(owner)) {
                System.out.println("Owner Registration Request Sent. Pending Admin Approval.");
            } else {
                System.out.println("Registration failed. Please check your details.");
            }
        } catch (Exception e) {
            System.out.println("Registration Error: " + e.getMessage());
        }
    }

    private static void registerCustomer() {
        GymCustomer customer = new GymCustomer(null, "", "", "", null, "", "");
        System.out.println("--- Customer Registration ---");
        System.out.print("Enter Name: ");
        customer.setName(scanner.nextLine());
        System.out.print("Enter Email: ");
        customer.setEmail(scanner.nextLine());
        System.out.print("Enter Password: ");
        customer.setPassword(scanner.nextLine());
        System.out.print("Enter Phone: ");
        customer.setPhoneNumber(scanner.nextLine());
        System.out.print("Enter City: ");
        customer.setCity(scanner.nextLine());

        try {
            if (accountService.registerCustomer(customer)) {
                System.out.println("Registration Successful!");
            } else {
                System.out.println("Registration failed. Please check your details.");
            }
        } catch (Exception e) {
            System.out.println("Registration Error: " + e.getMessage());
        }
    }

    private static void login() {
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String pass = scanner.nextLine();

        String role = accountService.login(email, pass);

        if (role == null) {
            System.out.println("Invalid Credentials!");
            return;
        }

        if (role.equals("ADMIN"))
            AdminFlipFitMenu.showMenu();
        else if (role.equals("GYM_OWNER"))
            GymOwnerFlipFitMenu.showMenu(email);
        else if (role.equals("GYM_CUSTOMER"))
            CustomerFlipFitMenu.showMenu();
    }
}