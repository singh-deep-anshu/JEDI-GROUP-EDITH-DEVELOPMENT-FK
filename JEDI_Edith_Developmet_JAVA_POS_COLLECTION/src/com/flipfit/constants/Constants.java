package com.flipfit.constants;

public class Constants {
    // Color Codes
    public static final String GREEN_COLOR = "\u001B[32m";
    public static final String RED_COLOR = "\u001B[31m";
    public static final String RESET_COLOR = "\u001B[0m";
    public static final String YELLOW_COLOR = "\u001B[33m";

    // General Messages
    public static final String DASHED_LINE = GREEN_COLOR + "------------------------------------------------------------------------" + RESET_COLOR;
    public static final String INVALID_CHOICE_ERROR = RED_COLOR + "\nPlease enter valid choice\n" + RESET_COLOR;
    public static final String EXIT_MESSAGE = RED_COLOR + "\nEXITING THE APPLICATION\n" + RESET_COLOR;
    public static final String PREVIOUS_MENU_MESSAGE = "\nGOING BACK TO PREVIOUS MENU\n";
    public static final String WELCOME_MESSAGE = GREEN_COLOR + "\nWELCOME TO THE FLIPFIT APPLICATION\n" + RESET_COLOR;
    
    // Gym Owner Messages
    public static final String APPROVAL_GYM_OWNER_CONFIRMATION = GREEN_COLOR + "\nAdmin Approved the Gym Owner\n" + RESET_COLOR;
    public static final String DISAPPROVAL_GYM_OWNER_CONFIRMATION = RED_COLOR + "\nAdmin Disapproved the Gym Owner\n" + RESET_COLOR;
    
    // Gym Centre Messages
    public static final String APPROVAL_GYM_CENTRE_CONFIRMATION = GREEN_COLOR + "\nAdmin Approved the Gym Centre\n" + RESET_COLOR;
    public static final String DISAPPROVAL_GYM_CENTRE_CONFIRMATION = RED_COLOR + "\nAdmin Disapproved the Gym Centre\n" + RESET_COLOR;
    
    // Slot/Booking Messages
    public static final String INVALID_SLOT = RED_COLOR + "PLEASE CHOOSE A VALID SLOT" + RESET_COLOR;
}
