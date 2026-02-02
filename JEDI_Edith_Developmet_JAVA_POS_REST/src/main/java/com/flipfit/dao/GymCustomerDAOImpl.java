package com.flipfit.dao;

import com.flipfit.bean.GymCustomer;
import com.flipfit.bean.Role;
import com.flipfit.utils.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of GymCustomerDAO using JDBC and MySQL database.
 */
public class GymCustomerDAOImpl implements GymCustomerDAO {

    @Override
    public boolean registerGymCustomer(GymCustomer gymCustomer) {
        String query = "INSERT INTO gym_customer (registrationDate, isActive, userID) " +
                       "VALUES (?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setTimestamp(1, new Timestamp(gymCustomer.getRegistrationDate().getTime()));
            pstmt.setInt(2, gymCustomer.isActive() ? 1 : 0);
            pstmt.setString(3, gymCustomer.getUserId());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("[DAO] Error registering gym customer: " + e.getMessage());
            return false;
        }
    }

    @Override
    public GymCustomer getGymCustomerByUserId(String userId) {
        String query = "SELECT * FROM gym_customer WHERE userID = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToGymCustomer(rs);
            }
        } catch (SQLException e) {
            System.err.println("[DAO] Error getting gym customer: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<GymCustomer> getAllGymCustomers() {
        String query = "SELECT * FROM gym_customer";
        List<GymCustomer> customers = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                customers.add(mapResultSetToGymCustomer(rs));
            }
        } catch (SQLException e) {
            System.err.println("[DAO] Error getting all gym customers: " + e.getMessage());
        }
        return customers;
    }

    @Override
    public List<GymCustomer> getActiveGymCustomers() {
        String query = "SELECT * FROM gym_customer WHERE isActive = 1";
        List<GymCustomer> customers = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                customers.add(mapResultSetToGymCustomer(rs));
            }
        } catch (SQLException e) {
            System.err.println("[DAO] Error getting active gym customers: " + e.getMessage());
        }
        return customers;
    }

    @Override
    public List<GymCustomer> getInactiveGymCustomers() {
        String query = "SELECT * FROM gym_customer WHERE isActive = 0";
        List<GymCustomer> customers = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                customers.add(mapResultSetToGymCustomer(rs));
            }
        } catch (SQLException e) {
            System.err.println("[DAO] Error getting inactive gym customers: " + e.getMessage());
        }
        return customers;
    }

    @Override
    public boolean updateGymCustomer(GymCustomer gymCustomer) {
        String query = "UPDATE gym_customer SET registrationDate = ?, isActive = ? " +
                       "WHERE userID = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setTimestamp(1, new Timestamp(gymCustomer.getRegistrationDate().getTime()));
            pstmt.setInt(2, gymCustomer.isActive() ? 1 : 0);
            pstmt.setString(3, gymCustomer.getUserId());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("[DAO] Error updating gym customer: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean updateActiveStatus(String userId, boolean isActive) {
        String query = "UPDATE gym_customer SET isActive = ? WHERE userID = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, isActive ? 1 : 0);
            pstmt.setString(2, userId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("[DAO] Error updating customer active status: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteGymCustomer(String userId) {
        String query = "DELETE FROM gym_customer WHERE userID = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, userId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("[DAO] Error deleting gym customer: " + e.getMessage());
            return false;
        }
    }

    /**
     * Helper method to map a ResultSet row to a GymCustomer object.
     */
    private GymCustomer mapResultSetToGymCustomer(ResultSet rs) throws SQLException {
        GymCustomer gymCustomer = new GymCustomer(
            rs.getString("userID"),
            null, // userName not available in this table
            null, // email not available
            null, // password not available
            Role.GYM_CUSTOMER,
            null, // phoneNumber not available
            null  // city not available
        );
        
        Timestamp regDate = rs.getTimestamp("registrationDate");
        if (regDate != null) {
            gymCustomer.setRegistrationDate(new java.util.Date(regDate.getTime()));
        }
        
        gymCustomer.setActive(rs.getInt("isActive") == 1);
        return gymCustomer;
    }
}
