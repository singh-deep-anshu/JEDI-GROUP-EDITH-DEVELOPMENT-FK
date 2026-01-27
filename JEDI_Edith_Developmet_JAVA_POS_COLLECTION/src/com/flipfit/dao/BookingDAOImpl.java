package com.flipfit.dao;

import com.flipfit.bean.Booking;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BookingDAOImpl implements BookingDAO {
    // The "Database" for Bookings
    private static Map<String, Booking> bookingsMap = new HashMap<>();

    @Override
    public void addBooking(Booking booking) {
        bookingsMap.put(booking.getBookingId(), booking);
    }

    @Override
    public List<Booking> getBookingsByUserId(String userId) {
        return bookingsMap.values().stream()
                .filter(b -> b.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public boolean cancelBooking(String bookingId) {
        if (bookingsMap.containsKey(bookingId)) {
            bookingsMap.remove(bookingId);
            return true;
        }
        return false;
    }
}