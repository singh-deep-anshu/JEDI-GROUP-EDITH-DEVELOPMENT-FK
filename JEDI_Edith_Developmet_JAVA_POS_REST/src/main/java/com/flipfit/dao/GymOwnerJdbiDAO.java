package com.flipfit.dao;

import com.flipfit.bean.GymOwner;
import org.jdbi.v3.core.Jdbi;
import java.util.List;
import java.util.Optional;

public class GymOwnerJdbiDAO implements GymOwnerDAO {
    private final Jdbi jdbi;

    public GymOwnerJdbiDAO(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    @Override
    public boolean registerGymOwner(GymOwner gymOwner) {
        String sql = "INSERT INTO gym_owner (gstNumber, userID, panNumber, isVerified) VALUES (:gstNumber, :userId, :panNumber, :isVerified)";
        return jdbi.withHandle(handle -> handle.createUpdate(sql)
                .bind("gstNumber", gymOwner.getGstNumber())
                .bind("userId", gymOwner.getUserId())
                .bind("panNumber", gymOwner.getPanNumber())
                .bind("isVerified", gymOwner.isVerified() ? 1 : 0)
                .execute() > 0);
    }

    @Override
    public GymOwner getGymOwnerByUserId(String userId) {
        String sql = "SELECT * FROM gym_owner WHERE userID = :userId";
        Optional<GymOwner> opt = jdbi.withHandle(handle -> handle.createQuery(sql)
                .bind("userId", userId)
                .map((rs, ctx) -> {
                    GymOwner g = new GymOwner();
                    g.setUserId(rs.getString("userID"));
                    g.setPanNumber(rs.getString("panNumber"));
                    g.setGstNumber(rs.getString("gstNumber"));
                    g.setVerified(rs.getInt("isVerified") == 1);
                    return g;
                }).findOne());
        return opt.orElse(null);
    }

    @Override
    public GymOwner getGymOwnerByPanNumber(String panNumber) {
        String sql = "SELECT * FROM gym_owner WHERE panNumber = :panNumber";
        Optional<GymOwner> opt = jdbi.withHandle(handle -> handle.createQuery(sql)
                .bind("panNumber", panNumber)
                .map((rs, ctx) -> {
                    GymOwner g = new GymOwner();
                    g.setUserId(rs.getString("userID"));
                    g.setPanNumber(rs.getString("panNumber"));
                    g.setGstNumber(rs.getString("gstNumber"));
                    g.setVerified(rs.getInt("isVerified") == 1);
                    return g;
                }).findOne());
        return opt.orElse(null);
    }

    @Override
    public List<GymOwner> getAllGymOwners() {
        String sql = "SELECT * FROM gym_owner";
        return jdbi.withHandle(handle -> handle.createQuery(sql)
                .map((rs, ctx) -> {
                    GymOwner g = new GymOwner();
                    g.setUserId(rs.getString("userID"));
                    g.setPanNumber(rs.getString("panNumber"));
                    g.setGstNumber(rs.getString("gstNumber"));
                    g.setVerified(rs.getInt("isVerified") == 1);
                    return g;
                }).list());
    }

    @Override
    public List<GymOwner> getVerifiedGymOwners() {
        String sql = "SELECT * FROM gym_owner WHERE isVerified = 1";
        return jdbi.withHandle(handle -> handle.createQuery(sql)
                .map((rs, ctx) -> {
                    GymOwner g = new GymOwner();
                    g.setUserId(rs.getString("userID"));
                    g.setPanNumber(rs.getString("panNumber"));
                    g.setGstNumber(rs.getString("gstNumber"));
                    g.setVerified(rs.getInt("isVerified") == 1);
                    return g;
                }).list());
    }

    @Override
    public List<GymOwner> getUnverifiedGymOwners() {
        String sql = "SELECT * FROM gym_owner WHERE isVerified = 0";
        return jdbi.withHandle(handle -> handle.createQuery(sql)
                .map((rs, ctx) -> {
                    GymOwner g = new GymOwner();
                    g.setUserId(rs.getString("userID"));
                    g.setPanNumber(rs.getString("panNumber"));
                    g.setGstNumber(rs.getString("gstNumber"));
                    g.setVerified(rs.getInt("isVerified") == 1);
                    return g;
                }).list());
    }

    @Override
    public boolean updateGymOwner(com.flipfit.bean.GymOwner gymOwner) {
        String sql = "UPDATE gym_owner SET gstNumber = :gstNumber, panNumber = :panNumber, isVerified = :isVerified WHERE userID = :userId";
        return jdbi.withHandle(handle -> handle.createUpdate(sql)
                .bind("gstNumber", gymOwner.getGstNumber())
                .bind("panNumber", gymOwner.getPanNumber())
                .bind("isVerified", gymOwner.isVerified() ? 1 : 0)
                .bind("userId", gymOwner.getUserId())
                .execute() > 0);
    }

    @Override
    public boolean updateVerificationStatus(String userId, boolean isVerified) {
        String sql = "UPDATE gym_owner SET isVerified = :isVerified WHERE userID = :userId";
        return jdbi.withHandle(handle -> handle.createUpdate(sql)
                .bind("isVerified", isVerified ? 1 : 0)
                .bind("userId", userId)
                .execute() > 0);
    }

    @Override
    public boolean deleteGymOwner(String userId) {
        String sql = "DELETE FROM gym_owner WHERE userID = :userId";
        return jdbi.withHandle(handle -> handle.createUpdate(sql)
                .bind("userId", userId)
                .execute() > 0);
    }
}
