package com.flipfit.dao;

import com.flipfit.bean.Slot;
import com.flipfit.utils.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of SlotDAO using JDBC and MySQL database.
 */
public class SlotDAOImpl implements SlotDAO {

    @Override
    public boolean addSlot(Slot slot) {
        String query = "INSERT INTO slot (slotID, centerID, startTime, endTime, maxCapacity, currentBookings) " +
                       "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, slot.getSlotId());
            pstmt.setString(2, slot.getCenterId());
            pstmt.setTimestamp(3, Timestamp.valueOf(LocalDateTime.of(java.time.LocalDate.now(), slot.getStartTime())));
            pstmt.setTimestamp(4, Timestamp.valueOf(LocalDateTime.of(java.time.LocalDate.now(), slot.getEndTime())));
            pstmt.setInt(5, slot.getMaxCapacity());
            pstmt.setInt(6, slot.getCurrentBookings());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("[DAO] Error adding slot: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Slot getSlotById(String slotId) {
        String query = "SELECT * FROM slot WHERE slotID = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, slotId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToSlot(rs);
            }
        } catch (SQLException e) {
            System.err.println("[DAO] Error getting slot: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Slot> getSlotsByCenterId(String centerId) {
        String query = "SELECT * FROM slot WHERE centerID = ?";
        List<Slot> slots = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, centerId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                slots.add(mapResultSetToSlot(rs));
            }
        } catch (SQLException e) {
            System.err.println("[DAO] Error getting slots by center: " + e.getMessage());
        }
        return slots;
    }

    @Override
    public List<Slot> getAllSlots() {
        String query = "SELECT * FROM slot";
        List<Slot> slots = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                slots.add(mapResultSetToSlot(rs));
            }
        } catch (SQLException e) {
            System.err.println("[DAO] Error getting all slots: " + e.getMessage());
        }
        return slots;
    }

    @Override
    public boolean updateSlot(Slot slot) {
        String query = "UPDATE slot SET centerID = ?, startTime = ?, endTime = ?, " +
                       "maxCapacity = ?, currentBookings = ? WHERE slotID = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, slot.getCenterId());
            pstmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.of(java.time.LocalDate.now(), slot.getStartTime())));
            pstmt.setTimestamp(3, Timestamp.valueOf(LocalDateTime.of(java.time.LocalDate.now(), slot.getEndTime())));
            pstmt.setInt(4, slot.getMaxCapacity());
            pstmt.setInt(5, slot.getCurrentBookings());
            pstmt.setString(6, slot.getSlotId());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("[DAO] Error updating slot: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean updateSlotBookingCount(String slotId, int newBookingCount) {
        String query = "UPDATE slot SET currentBookings = ? WHERE slotID = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, newBookingCount);
            pstmt.setString(2, slotId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("[DAO] Error updating slot booking count: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean updateSlotBookingCount(Connection conn, String slotId, int newBookingCount) {
        String query = "UPDATE slot SET currentBookings = ? WHERE slotID = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, newBookingCount);
            pstmt.setString(2, slotId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("[DAO] Error updating slot booking count (transactional): " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteSlot(String slotId) {
        String query = "DELETE FROM slot WHERE slotID = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, slotId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("[DAO] Error deleting slot: " + e.getMessage());
            return false;
        }
    }

    /**
     * Helper method to map a ResultSet row to a Slot object.
     */
    private Slot mapResultSetToSlot(ResultSet rs) throws SQLException {
        Slot slot = new Slot();
        slot.setSlotId(rs.getString("slotID"));
        slot.setCenterId(rs.getString("centerID"));
        
        Timestamp startTimestamp = rs.getTimestamp("startTime");
        Timestamp endTimestamp = rs.getTimestamp("endTime");
        
        if (startTimestamp != null) {
            slot.setStartTime(startTimestamp.toLocalDateTime().toLocalTime());
        }
        if (endTimestamp != null) {
            slot.setEndTime(endTimestamp.toLocalDateTime().toLocalTime());
        }
        
        slot.setMaxCapacity(rs.getInt("maxCapacity"));
        slot.setCurrentBookings(rs.getInt("currentBookings"));
        return slot;
    }
}
