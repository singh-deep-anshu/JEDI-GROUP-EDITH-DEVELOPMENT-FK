package com.flipfit.dao;

import com.flipfit.bean.GymCustomer;
import org.jdbi.v3.core.Jdbi;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public class GymCustomerJdbiDAO implements GymCustomerDAO {
    private final Jdbi jdbi;

    public GymCustomerJdbiDAO(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    @Override
    public boolean registerGymCustomer(GymCustomer gymCustomer) {
        String sql = "INSERT INTO gym_customer (registrationDate, isActive, userID) VALUES (:registrationDate, :isActive, :userId)";
        return jdbi.withHandle(handle -> handle.createUpdate(sql)
                .bind("registrationDate", new Timestamp(gymCustomer.getRegistrationDate().getTime()))
                .bind("isActive", gymCustomer.isActive() ? 1 : 0)
                .bind("userId", gymCustomer.getUserId())
                .execute() > 0);
    }

    @Override
    public GymCustomer getGymCustomerByUserId(String userId) {
        String sql = "SELECT * FROM gym_customer WHERE userID = :userId";
        Optional<GymCustomer> opt = jdbi.withHandle(handle -> handle.createQuery(sql)
                .bind("userId", userId)
                .map((rs, ctx) -> {
                    GymCustomer g = new GymCustomer(rs.getString("userID"), null, null, null, null, null, null);
                    Timestamp t = rs.getTimestamp("registrationDate");
                    if (t != null) g.setRegistrationDate(new java.util.Date(t.getTime()));
                    g.setActive(rs.getInt("isActive") == 1);
                    return g;
                })
                .findOne());
        return opt.orElse(null);
    }

    @Override
    public List<GymCustomer> getAllGymCustomers() {
        String sql = "SELECT * FROM gym_customer";
        return jdbi.withHandle(handle -> handle.createQuery(sql)
                .map((rs, ctx) -> {
                    GymCustomer g = new GymCustomer(rs.getString("userID"), null, null, null, null, null, null);
                    Timestamp t = rs.getTimestamp("registrationDate");
                    if (t != null) g.setRegistrationDate(new java.util.Date(t.getTime()));
                    g.setActive(rs.getInt("isActive") == 1);
                    return g;
                })
                .list());
    }

    @Override
    public List<GymCustomer> getActiveGymCustomers() {
        String sql = "SELECT * FROM gym_customer WHERE isActive = 1";
        return jdbi.withHandle(handle -> handle.createQuery(sql)
                .map((rs, ctx) -> {
                    GymCustomer g = new GymCustomer(rs.getString("userID"), null, null, null, null, null, null);
                    Timestamp t = rs.getTimestamp("registrationDate");
                    if (t != null) g.setRegistrationDate(new java.util.Date(t.getTime()));
                    g.setActive(rs.getInt("isActive") == 1);
                    return g;
                })
                .list());
    }

    @Override
    public List<GymCustomer> getInactiveGymCustomers() {
        String sql = "SELECT * FROM gym_customer WHERE isActive = 0";
        return jdbi.withHandle(handle -> handle.createQuery(sql)
                .map((rs, ctx) -> {
                    GymCustomer g = new GymCustomer(rs.getString("userID"), null, null, null, null, null, null);
                    Timestamp t = rs.getTimestamp("registrationDate");
                    if (t != null) g.setRegistrationDate(new java.util.Date(t.getTime()));
                    g.setActive(rs.getInt("isActive") == 1);
                    return g;
                })
                .list());
    }

    @Override
    public boolean updateGymCustomer(GymCustomer gymCustomer) {
        String sql = "UPDATE gym_customer SET registrationDate = :registrationDate, isActive = :isActive WHERE userID = :userId";
        return jdbi.withHandle(handle -> handle.createUpdate(sql)
                .bind("registrationDate", new Timestamp(gymCustomer.getRegistrationDate().getTime()))
                .bind("isActive", gymCustomer.isActive() ? 1 : 0)
                .bind("userId", gymCustomer.getUserId())
                .execute() > 0);
    }

    @Override
    public boolean updateActiveStatus(String userId, boolean isActive) {
        String sql = "UPDATE gym_customer SET isActive = :isActive WHERE userID = :userId";
        return jdbi.withHandle(handle -> handle.createUpdate(sql)
                .bind("isActive", isActive ? 1 : 0)
                .bind("userId", userId)
                .execute() > 0);
    }

    @Override
    public boolean deleteGymCustomer(String userId) {
        String sql = "DELETE FROM gym_customer WHERE userID = :userId";
        return jdbi.withHandle(handle -> handle.createUpdate(sql)
                .bind("userId", userId)
                .execute() > 0);
    }
}
