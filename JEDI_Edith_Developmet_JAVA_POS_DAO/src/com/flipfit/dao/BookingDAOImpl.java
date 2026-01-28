package com.flipfit.dao;

import com.flipfit.bean.Booking;
import com.flipfit.bean.BookingStatus;
import com.flipfit.utils.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of BookingDAO using JDBC and MySQL database.
 */
public class BookingDAOImpl implements BookingDAO {

    @Override
    public boolean createBooking(Booking booking) {
        String query = "INSERT INTO booking (bookingID, userID, slotID, bookingDate, status) " +
                       "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, booking.getBookingId());
            pstmt.setString(2, booking.getUserId());
            pstmt.setString(3, booking.getSlotId());
            pstmt.setTimestamp(4, new Timestamp(booking.getBookingDate().getTime()));
            pstmt.setString(5, booking.getStatus().toString());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("[DAO] Error creating booking: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean createBooking(Connection conn, Booking booking) {
        String query = "INSERT INTO booking (bookingID, userID, slotID, bookingDate, status) " +
                       "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, booking.getBookingId());
            pstmt.setString(2, booking.getUserId());
            pstmt.setString(3, booking.getSlotId());
            pstmt.setTimestamp(4, new Timestamp(booking.getBookingDate().getTime()));
            pstmt.setString(5, booking.getStatus().toString());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("[DAO] Error creating booking (transactional): " + e.getMessage());
            return false;
        }
    }

    @Override
    public Booking getBookingById(String bookingId) {
        String query = "SELECT * FROM booking WHERE bookingID = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, bookingId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToBooking(rs);
            }
        } catch (SQLException e) {
            System.err.println("[DAO] Error getting booking: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Booking> getBookingsByUserId(String userId) {
        String query = "SELECT * FROM booking WHERE userID = ?";
        List<Booking> bookings = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, userId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                bookings.add(mapResultSetToBooking(rs));
            }
        } catch (SQLException e) {
            System.err.println("[DAO] Error getting bookings by user: " + e.getMessage());
        }
        return bookings;
    }

    @Override
    public List<Booking> getBookingsBySlotId(String slotId) {
        String query = "SELECT * FROM booking WHERE slotID = ?";
        List<Booking> bookings = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, slotId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                bookings.add(mapResultSetToBooking(rs));
            }
        } catch (SQLException e) {
            System.err.println("[DAO] Error getting bookings by slot: " + e.getMessage());
        }
        return bookings;
    }

    @Override
    public List<Booking> getBookingsByStatus(BookingStatus status) {
        String query = "SELECT * FROM booking WHERE status = ?";
        List<Booking> bookings = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, status.toString());
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                bookings.add(mapResultSetToBooking(rs));
            }
        } catch (SQLException e) {
            System.err.println("[DAO] Error getting bookings by status: " + e.getMessage());
        }
        return bookings;
    }

    @Override
    public List<Booking> getAllBookings() {
        String query = "SELECT * FROM booking";
        List<Booking> bookings = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                bookings.add(mapResultSetToBooking(rs));
            }
        } catch (SQLException e) {
            System.err.println("[DAO] Error getting all bookings: " + e.getMessage());
        }
        return bookings;
    }

    @Override
    public boolean updateBooking(Booking booking) {
        String query = "UPDATE booking SET userID = ?, slotID = ?, bookingDate = ?, status = ? " +
                       "WHERE bookingID = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, booking.getUserId());
            pstmt.setString(2, booking.getSlotId());
            pstmt.setTimestamp(3, new Timestamp(booking.getBookingDate().getTime()));
            pstmt.setString(4, booking.getStatus().toString());
            pstmt.setString(5, booking.getBookingId());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("[DAO] Error updating booking: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean updateBookingStatus(String bookingId, BookingStatus status) {
        String query = "UPDATE booking SET status = ? WHERE bookingID = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, status.toString());
            pstmt.setString(2, bookingId);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("[DAO] Error updating booking status: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean updateBookingStatus(Connection conn, String bookingId, BookingStatus status) {
        String query = "UPDATE booking SET status = ? WHERE bookingID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, status.toString());
            pstmt.setString(2, bookingId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("[DAO] Error updating booking status (transactional): " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean cancelBooking(String bookingId) {
        return updateBookingStatus(bookingId, BookingStatus.CANCELLED);
    }

    @Override
    public boolean cancelBooking(Connection conn, String bookingId) {
        return updateBookingStatus(conn, bookingId, BookingStatus.CANCELLED);
    }

    @Override
    public boolean deleteBooking(String bookingId) {
        String query = "DELETE FROM booking WHERE bookingID = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, bookingId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("[DAO] Error deleting booking: " + e.getMessage());
            return false;
        }
    }

    /**
     * Helper method to map a ResultSet row to a Booking object.
     */
    private Booking mapResultSetToBooking(ResultSet rs) throws SQLException {
        Booking booking = new Booking();
        booking.setBookingId(rs.getString("bookingID"));
        booking.setUserId(rs.getString("userID"));
        booking.setSlotId(rs.getString("slotID"));
        
        Timestamp bookingDate = rs.getTimestamp("bookingDate");
        if (bookingDate != null) {
            booking.setBookingDate(new java.util.Date(bookingDate.getTime()));
        }
        
        String statusStr = rs.getString("status");
        if (statusStr != null) {
            booking.setStatus(BookingStatus.valueOf(statusStr));
        }
        
        return booking;
    }


}
