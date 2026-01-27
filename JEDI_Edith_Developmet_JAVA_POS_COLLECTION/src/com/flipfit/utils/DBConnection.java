package com.flipfit.utils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * This is our main database connection class.
 * 
 * It handles all the messy stuff like loading config files,
 * connecting to MySQL, and making sure we don't leave connections
 * hanging around.
 * 
 * How to use it:
 *   Connection conn = DBConnection.getConnection();
 *   // do your database stuff here
 *   DBConnection.closeConnection(conn);
 * 
 * Or if you just want one connection throughout your app:
 *   Connection conn = DBConnection.getSingletonConnection();
 * 
 * @author Flipfit Team
 */
public class DBConnection {
    
    // These are our fallback values in case db.properties is missing
    private static final String DEFAULT_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DEFAULT_URL = "jdbc:mysql://localhost:3306/flipfit_db";
    private static final String DEFAULT_USERNAME = "root";
    private static final String DEFAULT_PASSWORD = "root";
    
    // Where we look for the config file
    private static final String PROPERTIES_FILE = "db.properties";
    
    // We'll store our loaded settings here
    private static Properties properties = null;
    private static boolean driverLoaded = false;
    
    // For apps that just need one connection
    private static Connection singletonConnection = null;
    
    // Load everything when the class is first used
    static {
        loadProperties();
        loadDriver();
    }
    
    /**
     * Tries to load our database settings from db.properties.
     * If it can't find the file, no worries - we'll just use defaults.
     */
    private static void loadProperties() {
        properties = new Properties();
        
        // First, try loading from the classpath
        try (InputStream input = DBConnection.class.getClassLoader()
                .getResourceAsStream(PROPERTIES_FILE)) {
            
            if (input != null) {
                properties.load(input);
                System.out.println("[DB] Found and loaded " + PROPERTIES_FILE);
            } else {
                // Maybe it's in the same package as this class?
                try (InputStream packageInput = DBConnection.class
                        .getResourceAsStream(PROPERTIES_FILE)) {
                    
                    if (packageInput != null) {
                        properties.load(packageInput);
                        System.out.println("[DB] Loaded config from package folder");
                    } else {
                        System.out.println("[DB] Couldn't find " + PROPERTIES_FILE + ", using defaults");
                        setDefaultProperties();
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("[DB] Error reading config: " + e.getMessage());
            setDefaultProperties();
        }
    }
    
    /**
     * Sets up our default connection values.
     * These work for a typical local MySQL setup.
     */
    private static void setDefaultProperties() {
        properties.setProperty("db.driver", DEFAULT_DRIVER);
        properties.setProperty("db.url", DEFAULT_URL);
        properties.setProperty("db.username", DEFAULT_USERNAME);
        properties.setProperty("db.password", DEFAULT_PASSWORD);
    }
    
    /**
     * Loads the MySQL JDBC driver so we can actually connect.
     * Without this, nothing works!
     */
    private static void loadDriver() {
        if (driverLoaded) return;
        
        String driver = properties.getProperty("db.driver", DEFAULT_DRIVER);
        try {
            Class.forName(driver);
            driverLoaded = true;
            System.out.println("[DB] MySQL driver loaded successfully!");
        } catch (ClassNotFoundException e) {
            System.err.println("[DB] MySQL driver not found!");
            System.err.println("[DB] You need to add mysql-connector-java to your project.");
            System.err.println("[DB] Grab it from: https://dev.mysql.com/downloads/connector/j/");
        }
    }
    
    /**
     * Gives you a fresh new database connection.
     * Don't forget to close it when you're done!
     */
    public static Connection getConnection() throws SQLException {
        if (!driverLoaded) {
            loadDriver();
        }
        
        String url = properties.getProperty("db.url", DEFAULT_URL);
        String username = properties.getProperty("db.username", DEFAULT_USERNAME);
        String password = properties.getProperty("db.password", DEFAULT_PASSWORD);
        
        Connection connection = DriverManager.getConnection(url, username, password);
        System.out.println("[DB] Connected successfully!");
        return connection;
    }
    
    /**
     * Same as above but with custom credentials.
     * Handy if you need to connect to a different database.
     */
    public static Connection getConnection(String url, String username, String password) throws SQLException {
        if (!driverLoaded) {
            loadDriver();
        }
        return DriverManager.getConnection(url, username, password);
    }
    
    /**
     * Returns the same connection every time.
     * Great for simple apps where you don't need multiple connections.
     * Creates a new one if the old one was closed.
     */
    public static Connection getSingletonConnection() throws SQLException {
        if (singletonConnection == null || singletonConnection.isClosed()) {
            singletonConnection = getConnection();
        }
        return singletonConnection;
    }
    
    /**
     * Closes a connection safely.
     * Won't throw errors if the connection is already closed or null.
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                    System.out.println("[DB] Connection closed");
                }
            } catch (SQLException e) {
                System.err.println("[DB] Problem closing connection: " + e.getMessage());
            }
        }
    }
    
    /**
     * Closes our singleton connection.
     * Call this before your app shuts down.
     */
    public static void closeSingletonConnection() {
        closeConnection(singletonConnection);
        singletonConnection = null;
    }
    
    /**
     * Quick way to check if everything is working.
     * Returns true if we can connect to the database.
     */
    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            boolean works = conn != null && conn.isValid(5);
            if (works) {
                System.out.println("[DB] Connection test passed! We're good to go.");
            }
            return works;
        } catch (SQLException e) {
            System.err.println("[DB] Connection test failed: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Returns the database URL we're using.
     */
    public static String getDatabaseUrl() {
        return properties.getProperty("db.url", DEFAULT_URL);
    }
    
    /**
     * Returns just the database name.
     */
    public static String getDatabaseName() {
        return properties.getProperty("db.name", "flipfit_db");
    }
    
    /**
     * Reloads the config file.
     * Useful if you changed db.properties while the app is running.
     */
    public static void reloadProperties() {
        loadProperties();
    }
    
    /**
     * Run this to test your database connection!
     * Just execute: java com.flipfit.utils.DBConnection
     */
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("  Flipfit Database Connection Tester");
        System.out.println("========================================");
        System.out.println();
        System.out.println("Database URL: " + getDatabaseUrl());
        System.out.println("Testing connection...");
        System.out.println();
        
        if (testConnection()) {
            System.out.println();
            System.out.println("SUCCESS! Your database is ready to go!");
        } else {
            System.out.println();
            System.out.println("FAILED! Here's what you can try:");
            System.out.println("  1. Make sure MySQL is running");
            System.out.println("  2. Check your username and password in db.properties");
            System.out.println("  3. Run flipfit_schema.sql to create the database");
            System.out.println("  4. Make sure mysql-connector-java is in your classpath");
        }
    }
}
