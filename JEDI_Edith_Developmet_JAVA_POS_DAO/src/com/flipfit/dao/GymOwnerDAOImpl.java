package com.flipfit.dao;

import com.flipfit.bean.GymOwner;
import com.flipfit.bean.Role;
import com.flipfit.utils.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of GymOwnerDAO using JDBC and MySQL database.
 */
public class GymOwnerDAOImpl implements GymOwnerDAO {

    @Override
    public boolean registerGymOwner(GymOwner gymOwner) {
        String query = "INSERT INTO gym_owner (gstNumber, userID, panNumber, isVerified) " +
                       "VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, gymOwner.getGstNumber());
            pstmt.setString(2, gymOwner.getUserId());
            pstmt.setString(3, gymOwner.getPanNumber());
            pstmt.setInt(4, gymOwner.isVerified() ? 1 : 0);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("[DAO] Error registering gym owner: " + e.getMessage());
            return false;
        }
    }

    @Override
    public GymOwner getGymOwnerByUserId(String userId) {
        String query = "SELECT * FROM gym_owner WHERE userID = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToGymOwner(rs);
            }
        } catch (SQLException e) {
            System.err.println("[DAO] Error getting gym owner: " + e.getMessage());
        }
        return null;
    }

    @Override
    public GymOwner getGymOwnerByPanNumber(String panNumber) {
        String query = "SELECT * FROM gym_owner WHERE panNumber = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, panNumber);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToGymOwner(rs);
            }
        } catch (SQLException e) {
            System.err.println("[DAO] Error getting gym owner by PAN: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<GymOwner> getAllGymOwners() {
        String query = "SELECT * FROM gym_owner";
        List<GymOwner> owners = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                owners.add(mapResultSetToGymOwner(rs));
            }
        } catch (SQLException e) {
            System.err.println("[DAO] Error getting all gym owners: " + e.getMessage());
        }
        return owners;
    }

    @Override
    public List<GymOwner> getVerifiedGymOwners() {
        String query = "SELECT * FROM gym_owner WHERE isVerified = 1";
        List<GymOwner> owners = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                owners.add(mapResultSetToGymOwner(rs));
            }
        } catch (SQLException e) {
            System.err.println("[DAO] Error getting verified gym owners: " + e.getMessage());
        }
        return owners;
    }

    @Override
    public List<GymOwner> getUnverifiedGymOwners() {
        String query = "SELECT * FROM gym_owner WHERE isVerified = 0";
        List<GymOwner> owners = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                owners.add(mapResultSetToGymOwner(rs));
            }
        } catch (SQLException e) {
            System.err.println("[DAO] Error getting unverified gym owners: " + e.getMessage());
        }
        return owners;
    }

    @Override
    public boolean updateGymOwner(GymOwner gymOwner) {
        String query = "UPDATE gym_owner SET gstNumber = ?, panNumber = ?, isVerified = ? " +
                       "WHERE userID = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, gymOwner.getGstNumber());
            pstmt.setString(2, gymOwner.getPanNumber());
            pstmt.setInt(3, gymOwner.isVerified() ? 1 : 0);
            pstmt.setString(4, gymOwner.getUserId());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("[DAO] Error updating gym owner: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean updateVerificationStatus(String userId, boolean isVerified) {
        String query = "UPDATE gym_owner SET isVerified = ? WHERE userID = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, isVerified ? 1 : 0);
            pstmt.setString(2, userId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("[DAO] Error updating verification status: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteGymOwner(String userId) {
        String query = "DELETE FROM gym_owner WHERE userID = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, userId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("[DAO] Error deleting gym owner: " + e.getMessage());
            return false;
        }
    }

    /**
     * Helper method to map a ResultSet row to a GymOwner object.
     */
    private GymOwner mapResultSetToGymOwner(ResultSet rs) throws SQLException {
        GymOwner gymOwner = new GymOwner();
        gymOwner.setUserId(rs.getString("userID"));
        gymOwner.setPanNumber(rs.getString("panNumber"));
        gymOwner.setGstNumber(rs.getString("gstNumber"));
        gymOwner.setVerified(rs.getInt("isVerified") == 1);
        gymOwner.setRole(Role.GYM_OWNER);
        return gymOwner;
    }
}