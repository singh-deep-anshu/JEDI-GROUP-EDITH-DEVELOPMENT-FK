package com.flipfit.dao;

import com.flipfit.bean.GymCenter;
import com.flipfit.utils.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of GymCenterDAO using JDBC and MySQL database.
 */
public class GymCenterDAOImpl implements GymCenterDAO {

    @Override
    public boolean addGymCenter(GymCenter gymCenter) {
        String query = "INSERT INTO gym_center (centerID, name, address, cityID, ownerID, totalCapacity, isActive) " +
                       "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, gymCenter.getCenterId());
            pstmt.setString(2, gymCenter.getName());
            pstmt.setString(3, gymCenter.getAddress());
            pstmt.setString(4, gymCenter.getCityId());
            pstmt.setString(5, gymCenter.getOwnerId());
            pstmt.setInt(6, gymCenter.getTotalCapacity());
            pstmt.setString(7, gymCenter.isActive() ? "true" : "false");
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("[DAO] Error adding gym center: " + e.getMessage());
            return false;
        }
    }

    @Override
    public GymCenter getGymCenterById(String centerId) {
        String query = "SELECT * FROM gym_center WHERE centerID = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, centerId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToGymCenter(rs);
            }
        } catch (SQLException e) {
            System.err.println("[DAO] Error getting gym center: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<GymCenter> getGymCentersByOwnerId(String ownerId) {
        String query = "SELECT * FROM gym_center WHERE ownerID = ?";
        List<GymCenter> centers = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, ownerId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                centers.add(mapResultSetToGymCenter(rs));
            }
        } catch (SQLException e) {
            System.err.println("[DAO] Error getting gym centers by owner: " + e.getMessage());
        }
        return centers;
    }

    @Override
    public List<GymCenter> getGymCentersByCity(String cityId) {
        String query = "SELECT * FROM gym_center WHERE cityID = ?";
        List<GymCenter> centers = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, cityId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                centers.add(mapResultSetToGymCenter(rs));
            }
        } catch (SQLException e) {
            System.err.println("[DAO] Error getting gym centers by city: " + e.getMessage());
        }
        return centers;
    }

    @Override
    public List<GymCenter> getAllGymCenters() {
        String query = "SELECT * FROM gym_center";
        List<GymCenter> centers = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                centers.add(mapResultSetToGymCenter(rs));
            }
        } catch (SQLException e) {
            System.err.println("[DAO] Error getting all gym centers: " + e.getMessage());
        }
        return centers;
    }

    @Override
    public boolean updateGymCenter(GymCenter gymCenter) {
        String query = "UPDATE gym_center SET name = ?, address = ?, cityID = ?, ownerID = ?, " +
                       "totalCapacity = ?, isActive = ? WHERE centerID = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, gymCenter.getName());
            pstmt.setString(2, gymCenter.getAddress());
            pstmt.setString(3, gymCenter.getCityId());
            pstmt.setString(4, gymCenter.getOwnerId());
            pstmt.setInt(5, gymCenter.getTotalCapacity());
            pstmt.setString(6, gymCenter.isActive() ? "true" : "false");
            pstmt.setString(7, gymCenter.getCenterId());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("[DAO] Error updating gym center: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteGymCenter(String centerId) {
        String query = "DELETE FROM gym_center WHERE centerID = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, centerId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("[DAO] Error deleting gym center: " + e.getMessage());
            return false;
        }
    }

    /**
     * Helper method to map a ResultSet row to a GymCenter object.
     */
    private GymCenter mapResultSetToGymCenter(ResultSet rs) throws SQLException {
        GymCenter gymCenter = new GymCenter();
        gymCenter.setCenterId(rs.getString("centerID"));
        gymCenter.setName(rs.getString("name"));
        gymCenter.setAddress(rs.getString("address"));
        gymCenter.setCityId(rs.getString("cityID"));
        gymCenter.setOwnerId(rs.getString("ownerID"));
        gymCenter.setTotalCapacity(rs.getInt("totalCapacity"));
        gymCenter.setActive("true".equalsIgnoreCase(rs.getString("isActive")));
        return gymCenter;
    }
}
