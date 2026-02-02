package com.flipfit.business;

import com.flipfit.bean.GymCustomer;
import com.flipfit.bean.WaitingList;

import java.util.LinkedList;
import java.util.Queue;

public class WaitlistOperation {

    // Create a WaitingList object to manage the waiting list
    WaitingList waitingList = new WaitingList();

    // Method to initialize the waiting list with a specific waitlist ID, centre ID, and slot ID
    public void initializeWaitingList(long waitListId, long centreId, int slotId) {
        // Set the properties of the waiting list
        waitingList.setWaitListId(waitListId);
        waitingList.setCentreId(centreId);
        waitingList.setSlotId(slotId);
        // Initialize an empty queue for the waiting list
        waitingList.setWaiting(new LinkedList<>());
        // Log that the waiting list has been initialized with the provided IDs
        System.out.println("initializeWaitingList() called: Waiting list initialized with ID: " + waitListId + ", Centre ID: " + centreId + ", Slot ID: " + slotId);
    }

    // Method to add a GymCustomer to the waiting list
    public void addGymCustomerToWaitlist(GymCustomer GymCustomer) {
        // Add the GymCustomer to the waiting list (this would be modified to actually add the GymCustomer to the queue)
        System.out.println("addGymCustomerToWaitlist() called: GymCustomer " + GymCustomer.getGymCustomerName() + " added to the waiting list.");
    }

    // Method to serve the next GymCustomer in the waiting list
    public void serveNextGymCustomer(GymCustomer GymCustomer) {
        // Serve the next GymCustomer (this should ideally pop from the waiting list queue)
        System.out.println("serveNextGymCustomer() called: GymCustomer " + GymCustomer.getGymCustomerName() + " has been served.");
    }

    // Method to view the current status of the waiting list
    public void viewWaitingList() {
        // Print the current state of the waiting list (this would be expanded to display actual data)
        System.out.println("viewWaitingList() called: Current waiting list: ");
    }

    // Method to check if the waiting list is empty
    public boolean isWaitingListEmpty() {
        // Log that the waiting list is being checked if it’s empty
        System.out.println("waitinglist is empty");
        // Return true for simplicity; this would be modified to return the actual status of the waiting list
        return true;
    }
}