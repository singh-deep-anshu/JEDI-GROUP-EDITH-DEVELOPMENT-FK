package com.flipfit.constants;

public class SQLConstants {

    // ------------------------ GYM OWNER ------------------------
    public static final String FETCH_ALL_GYM_OWNERS_QUERY = "SELECT * FROM FlipFit.GymOwner";
    public static final String LOGIN_GYM_OWNER = "SELECT * FROM FlipFit.GymOwner WHERE email=? AND password=?";
    public static final String REGISTER_GYM_OWNER = "INSERT INTO FlipFit.GymOwner (userId, userName, email, password, phoneNumber, city, panNumber, gstNumber, isVerified) VALUES (?,?,?,?,?,?,?,?,?)";
    public static final String SQL_APPROVE_GYM_OWNER_BY_ID_QUERY = "UPDATE FlipFit.GymOwner SET isVerified=? WHERE userId=?";
    public static final String FETCH_ALL_PENDING_GYM_OWNERS_QUERY = "SELECT * FROM FlipFit.GymOwner WHERE isVerified = 0";

    // ------------------------ GYM CENTRE ------------------------
    public static final String FETCH_ALL_GYM_CENTRES = "SELECT * FROM FlipFit.GymCenter";
    public static final String FETCH_GYM_CENTRES_BY_OWNER_ID = "SELECT * FROM FlipFit.GymCenter WHERE ownerId = ?";
    public static final String ADD_GYM_CENTRE_QUERY = "INSERT INTO FlipFit.GymCenter (centerId, ownerId, name, address, city, capacity, isActive) VALUES (?, ?, ?, ?, ?, ?, ?)";
    public static final String SQL_APPROVE_GYM_CENTRE_BY_ID_QUERY = "UPDATE FlipFit.GymCenter SET isActive=? WHERE centerId=?";
    public static final String FETCH_GYM_CENTRES_BY_CITY = "SELECT * FROM FlipFit.GymCenter WHERE city = ?";

    // ------------------------ CUSTOMER ------------------------
    public static final String CUSTOMER_LOGIN_QUERY = "SELECT * FROM FlipFit.GymCustomer WHERE name = ? AND password = ?";
    public static final String ADD_NEW_CUSTOMER = "INSERT INTO FlipFit.GymCustomer (userId, userName, password, email, phoneNumber, city) VALUES (?, ?, ?, ?, ?, ?)";
    public static final String GET_CUSTOMER_BY_ID = "SELECT * FROM FlipFit.GymCustomer WHERE userId = ?";

    // ------------------------ BOOKING ------------------------
    public static final String ADD_BOOKING = "INSERT INTO FlipFit.Booking (bookingId, userId, slotId, bookingDate, status) VALUES (?, ?, ?, ?, ?)";
    public static final String GET_BOOKING_BY_CUSTOMER_ID = "SELECT * FROM FlipFit.Booking WHERE userId = ?";
    public static final String CANCEL_BOOKING_BY_ID = "UPDATE FlipFit.Booking SET status = 'CANCELLED' WHERE bookingId = ?";
    
    // ------------------------ SLOT ------------------------
    public static final String ADD_SLOT = "INSERT INTO FlipFit.Slot (slotId, centerId, startTime, endTime, maxCapacity, currentBookings) VALUES (?, ?, ?, ?, ?, ?)";
    public static final String FETCH_SLOT_BY_CENTRE = "SELECT * FROM FlipFit.Slot WHERE centerId=?";
    public static final String FETCH_SLOT_BY_ID = "SELECT * FROM FlipFit.Slot WHERE slotId=?";
    public static final String DELETE_SLOT = "DELETE FROM FlipFit.Slot WHERE slotId=?";
}
