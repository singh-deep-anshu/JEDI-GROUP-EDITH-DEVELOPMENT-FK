package com.flipfit.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * A handy collection of database helper methods.
 * 
 * This class has all the little utilities you'll need when
 * working with the database - things like closing resources
 * properly, generating IDs, and handling transactions.
 * 
 * None of these need an instance, just call them like:
 *   DBUtils.generateBookingId()
 *   DBUtils.closeAll(conn, stmt, rs)
 * 
 * @author Flipfit Team
 */
public class DBUtils {
    
    /**
     * Closes a ResultSet without throwing errors everywhere.
     * Safe to call even if rs is null.
     */
    public static void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                System.err.println("[DBUtils] Couldn't close ResultSet: " + e.getMessage());
            }
        }
    }
    
    /**
     * Closes a Statement safely.
     */
    public static void closeStatement(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                System.err.println("[DBUtils] Couldn't close Statement: " + e.getMessage());
            }
        }
    }
    
    /**
     * Closes a PreparedStatement safely.
     * (Same as closeStatement but the name makes the code clearer)
     */
    public static void closePreparedStatement(PreparedStatement pstmt) {
        if (pstmt != null) {
            try {
                pstmt.close();
            } catch (SQLException e) {
                System.err.println("[DBUtils] Couldn't close PreparedStatement: " + e.getMessage());
            }
        }
    }
    
    /**
     * Closes everything in one go.
     * Pass null for anything you don't need to close.
     */
    public static void closeAll(Connection conn, Statement stmt, ResultSet rs) {
        closeResultSet(rs);
        closeStatement(stmt);
        if (conn != null) {
            DBConnection.closeConnection(conn);
        }
    }
    
    /**
     * Closes statement and result set, but leaves the connection open.
     * Useful when you're running multiple queries.
     */
    public static void closeStatementAndResultSet(Statement stmt, ResultSet rs) {
        closeResultSet(rs);
        closeStatement(stmt);
    }
    
    /**
     * Runs a SELECT query and gives you the results.
     * Remember: you need to close the statement when done!
     */
    public static ResultSet executeQuery(Connection conn, String sql) throws SQLException {
        Statement stmt = conn.createStatement();
        return stmt.executeQuery(sql);
    }
    
    /**
     * Runs an INSERT, UPDATE, or DELETE query.
     * Returns how many rows were affected.
     */
    public static int executeUpdate(Connection conn, String sql) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            return stmt.executeUpdate(sql);
        }
    }
    
    // ========================================
    // ID Generators
    // ========================================
    // These create unique IDs for our database records.
    // They use timestamp + random number so they're always unique.
    
    /**
     * Creates a unique ID with a custom prefix.
     * Example: generateId("USR") might return "USR17145632451234"
     */
    public static String generateId(String prefix) {
        long timestamp = System.currentTimeMillis();
        int random = (int)(Math.random() * 10000);
        return prefix + timestamp + String.format("%04d", random);
    }
    
    /**
     * Creates a user ID based on their role.
     * ADM for admins, OWN for gym owners, CUS for customers.
     */
    public static String generateUserId(String role) {
        String prefix;
        switch (role.toUpperCase()) {
            case "ADMIN":
                prefix = "ADM";
                break;
            case "GYM_OWNER":
                prefix = "OWN";
                break;
            case "GYM_CUSTOMER":
                prefix = "CUS";
                break;
            default:
                prefix = "USR";
        }
        return generateId(prefix);
    }
    
    /**
     * Creates a booking ID like "BKG17145632451234"
     */
    public static String generateBookingId() {
        return generateId("BKG");
    }
    
    /**
     * Creates a slot ID like "SLT17145632451234"
     */
    public static String generateSlotId() {
        return generateId("SLT");
    }
    
    /**
     * Creates a gym center ID like "GYM17145632451234"
     */
    public static String generateCenterId() {
        return generateId("GYM");
    }
    
    /**
     * Creates a waitlist ID like "WTL17145632451234"
     */
    public static String generateWaitlistId() {
        return generateId("WTL");
    }
    
    // ========================================
    // Database Helpers
    // ========================================
    
    /**
     * Escapes quotes so they don't break your SQL.
     * 
     * BUT PLEASE: use PreparedStatement instead whenever possible!
     * This is just here for edge cases.
     */
    public static String escapeSQL(String input) {
        if (input == null) return null;
        return input.replace("'", "''")
                    .replace("\\", "\\\\");
    }
    
    /**
     * Checks if a table exists in the database.
     * Handy before running CREATE TABLE or for debugging.
     */
    public static boolean tableExists(Connection conn, String tableName) {
        String sql = "SELECT 1 FROM information_schema.tables WHERE table_schema = DATABASE() AND table_name = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, tableName);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.err.println("[DBUtils] Error checking if table exists: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Counts how many rows are in a table.
     * Returns -1 if something goes wrong.
     */
    public static int getRowCount(Connection conn, String tableName) {
        String sql = "SELECT COUNT(*) FROM " + tableName;
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("[DBUtils] Couldn't count rows: " + e.getMessage());
        }
        return -1;
    }
    
    // ========================================
    // Transaction Helpers
    // ========================================
    // Use these when you need multiple queries to succeed or fail together.
    
    /**
     * Starts a transaction.
     * After this, nothing is saved until you call commit.
     */
    public static void beginTransaction(Connection conn) throws SQLException {
        conn.setAutoCommit(false);
    }
    
    /**
     * Saves everything since beginTransaction was called.
     */
    public static void commitTransaction(Connection conn) throws SQLException {
        conn.commit();
        conn.setAutoCommit(true);
    }
    
    /**
     * This won't throw errors, making it safe to call in catch blocks.
     */
    public static void rollbackTransaction(Connection conn) {
        try {
            if (conn != null && !conn.getAutoCommit()) {
                conn.rollback();
                conn.setAutoCommit(true);
                System.out.println("[DBUtils] Transaction rolled back");
            }
        } catch (SQLException e) {
            System.err.println("[DBUtils] Rollback failed: " + e.getMessage());
        }
    }
}
