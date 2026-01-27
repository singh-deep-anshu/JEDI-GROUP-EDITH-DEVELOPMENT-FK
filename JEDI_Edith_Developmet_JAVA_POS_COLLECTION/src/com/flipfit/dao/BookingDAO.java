package com.flipfit.dao;
import com.flipfit.bean.Booking;
import java.util.List;

public interface BookingDAO {
    void addBooking(Booking booking);
    List<Booking> getBookingsByUserId(String userId);
    boolean cancelBooking(String bookingId);
}