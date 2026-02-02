-- ============================================
-- FLIPFIT DATABASE SCHEMA
-- ============================================
-- This file sets up the entire database for
-- our Flipfit gym management app.
--
-- To run this, open your terminal and type:
--   mysql -u root -p < flipfit_schema.sql
--
-- Or just copy-paste into MySQL Workbench!
-- ============================================

-- creating our database
CREATE DATABASE IF NOT EXISTS flipfit_db;
USE flipfit_db;

-- ============================================
-- DROPPING OLD TABLES (if you need a fresh start)
-- ============================================
-- Uncomment these lines if you want to rebuild everything
-- DROP TABLE IF EXISTS waitlist;
-- DROP TABLE IF EXISTS booking;
-- DROP TABLE IF EXISTS slot;
-- DROP TABLE IF EXISTS gym_center;
-- DROP TABLE IF EXISTS gym_owner;
-- DROP TABLE IF EXISTS gym_customer;
-- DROP TABLE IF EXISTS gym_admin;
-- DROP TABLE IF EXISTS user;
-- DROP TABLE IF EXISTS city;

-- ============================================
-- USER TABLE
-- ============================================
-- This is the base table for all users - admins,
-- gym owners, and customers. The common stuff
-- like name, email, password goes here.

CREATE TABLE IF NOT EXISTS user (
    user_id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    phone_number VARCHAR(15),
    city VARCHAR(50),
    role ENUM('ADMIN', 'GYM_OWNER', 'GYM_CUSTOMER') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    -- These indexes make searching faster
    INDEX idx_email (email),
    INDEX idx_role (role)
);

-- ============================================
-- GYM OWNER TABLE
-- ============================================
-- Extra info for gym owners - their PAN, GST,
-- and whether an admin has verified them yet.

CREATE TABLE IF NOT EXISTS gym_owner (
    user_id VARCHAR(50) PRIMARY KEY,
    pan_number VARCHAR(10) NOT NULL,
    gst_number VARCHAR(15) NOT NULL,
    is_verified BOOLEAN DEFAULT FALSE,
    verified_at TIMESTAMP NULL,
    
    -- Links back to the user table
    FOREIGN KEY (user_id) REFERENCES user(user_id) 
        ON DELETE CASCADE ON UPDATE CASCADE
);

-- ============================================
-- GYM CUSTOMER TABLE
-- ============================================
-- Extra info for customers - when they joined
-- and if their account is active.

CREATE TABLE IF NOT EXISTS gym_customer (
    user_id VARCHAR(50) PRIMARY KEY,
    registration_date DATE NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    
    FOREIGN KEY (user_id) REFERENCES user(user_id) 
        ON DELETE CASCADE ON UPDATE CASCADE
);

-- ============================================
-- GYM ADMIN TABLE
-- ============================================
-- Admins are pretty simple - they just need
-- the base user info. This table links them.

CREATE TABLE IF NOT EXISTS gym_admin (
    user_id VARCHAR(50) PRIMARY KEY,
    
    FOREIGN KEY (user_id) REFERENCES user(user_id) 
        ON DELETE CASCADE ON UPDATE CASCADE
);

-- ============================================
-- GYM CENTER TABLE
-- ============================================
-- All the gym locations! Each gym belongs to
-- an owner and has a city and capacity.

CREATE TABLE IF NOT EXISTS gym_center (
    center_id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    address VARCHAR(500) NOT NULL,
    city_id VARCHAR(50) NOT NULL,
    owner_id VARCHAR(50) NOT NULL,
    total_capacity INT NOT NULL DEFAULT 0,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    -- Every gym must have an owner
    FOREIGN KEY (owner_id) REFERENCES gym_owner(user_id) 
        ON DELETE CASCADE ON UPDATE CASCADE,
    
    INDEX idx_owner (owner_id),
    INDEX idx_city (city_id),
    INDEX idx_active (is_active)
);

-- ============================================
-- SLOT TABLE
-- ============================================
-- Time slots at each gym. Each slot has a
-- start time, end time, and max capacity.

CREATE TABLE IF NOT EXISTS slot (
    slot_id VARCHAR(50) PRIMARY KEY,
    center_id VARCHAR(50) NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    max_capacity INT NOT NULL DEFAULT 10,
    current_bookings INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (center_id) REFERENCES gym_center(center_id) 
        ON DELETE CASCADE ON UPDATE CASCADE,
    
    INDEX idx_center (center_id),
    INDEX idx_times (start_time, end_time),
    
    -- Make sure end time is after start time!
    CHECK (end_time > start_time),
    CHECK (current_bookings >= 0),
    CHECK (current_bookings <= max_capacity)
);

-- ============================================
-- BOOKING TABLE
-- ============================================
-- When a customer books a slot, it goes here.
-- We track the date, status, and prevent
-- double-booking the same slot.

CREATE TABLE IF NOT EXISTS booking (
    booking_id VARCHAR(50) PRIMARY KEY,
    user_id VARCHAR(50) NOT NULL,
    slot_id VARCHAR(50) NOT NULL,
    booking_date DATE NOT NULL,
    status ENUM('CONFIRMED', 'CANCELLED', 'PENDING', 'WAITLISTED') NOT NULL DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (user_id) REFERENCES user(user_id) 
        ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (slot_id) REFERENCES slot(slot_id) 
        ON DELETE CASCADE ON UPDATE CASCADE,
    
    INDEX idx_user (user_id),
    INDEX idx_slot (slot_id),
    INDEX idx_date (booking_date),
    INDEX idx_status (status),
    
    -- One person can't book the same slot twice on the same day
    UNIQUE KEY uk_user_slot_date (user_id, slot_id, booking_date)
);

-- ============================================
-- WAITLIST TABLE
-- ============================================
-- When a slot is full, customers can join
-- the waitlist. We track their position in line.

CREATE TABLE IF NOT EXISTS waitlist (
    waitlist_id VARCHAR(50) PRIMARY KEY,
    user_id VARCHAR(50) NOT NULL,
    slot_id VARCHAR(50) NOT NULL,
    request_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    position INT NOT NULL,
    status ENUM('WAITING', 'NOTIFIED', 'EXPIRED', 'CONVERTED') NOT NULL DEFAULT 'WAITING',
    
    FOREIGN KEY (user_id) REFERENCES user(user_id) 
        ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (slot_id) REFERENCES slot(slot_id) 
        ON DELETE CASCADE ON UPDATE CASCADE,
    
    INDEX idx_slot_position (slot_id, position),
    
    -- Can't join the same waitlist twice
    UNIQUE KEY uk_user_slot (user_id, slot_id)
);

-- ============================================
-- CITY TABLE
-- ============================================
-- All the cities where Flipfit operates.
-- We pre-load these with the major cities.

CREATE TABLE IF NOT EXISTS city (
    city_id VARCHAR(50) PRIMARY KEY,
    city_name VARCHAR(100) NOT NULL UNIQUE,
    state VARCHAR(100),
    is_active BOOLEAN DEFAULT TRUE
);

-- ============================================
-- LET'S ADD SOME DEFAULT DATA!
-- ============================================

-- Add the cities we operate in
INSERT IGNORE INTO city (city_id, city_name, state, is_active) VALUES
('BLR', 'Bangalore', 'Karnataka', TRUE),
('MUM', 'Mumbai', 'Maharashtra', TRUE),
('DEL', 'Delhi', 'Delhi', TRUE),
('CHN', 'Chennai', 'Tamil Nadu', TRUE),
('HYD', 'Hyderabad', 'Telangana', TRUE),
('PUN', 'Pune', 'Maharashtra', TRUE),
('KOL', 'Kolkata', 'West Bengal', TRUE),
('AMD', 'Ahmedabad', 'Gujarat', TRUE),
('JAI', 'Jaipur', 'Rajasthan', TRUE),
('LKO', 'Lucknow', 'Uttar Pradesh', TRUE);

-- Create a default admin account
-- Login: admin@flipfit.com / admin123
INSERT IGNORE INTO user (user_id, name, email, password, phone_number, city, role) VALUES
('ADMIN001', 'System Admin', 'admin@flipfit.com', 'admin123', '9999999999', 'Bangalore', 'ADMIN');

INSERT IGNORE INTO gym_admin (user_id) VALUES ('ADMIN001');

-- ============================================
-- HELPFUL VIEWS
-- ============================================
-- These make it easier to query common data
-- without writing complex JOINs every time.

-- All gym owners with their full details
CREATE OR REPLACE VIEW v_gym_owners AS
SELECT 
    u.user_id,
    u.name,
    u.email,
    u.phone_number,
    u.city,
    o.pan_number,
    o.gst_number,
    o.is_verified,
    o.verified_at
FROM user u
JOIN gym_owner o ON u.user_id = o.user_id
WHERE u.role = 'GYM_OWNER';

-- All customers with their details
CREATE OR REPLACE VIEW v_gym_customers AS
SELECT 
    u.user_id,
    u.name,
    u.email,
    u.phone_number,
    u.city,
    c.registration_date,
    c.is_active
FROM user u
JOIN gym_customer c ON u.user_id = c.user_id
WHERE u.role = 'GYM_CUSTOMER';

-- Gyms with their available slots
-- Handy for showing customers what's available!
CREATE OR REPLACE VIEW v_gym_slots AS
SELECT 
    gc.center_id,
    gc.name AS gym_name,
    gc.city_id,
    s.slot_id,
    s.start_time,
    s.end_time,
    s.max_capacity,
    s.current_bookings,
    (s.max_capacity - s.current_bookings) AS available_seats
FROM gym_center gc
JOIN slot s ON gc.center_id = s.center_id
WHERE gc.is_active = TRUE;

-- Booking details with all the info you'd want
CREATE OR REPLACE VIEW v_booking_details AS
SELECT 
    b.booking_id,
    b.booking_date,
    b.status,
    u.name AS customer_name,
    u.email AS customer_email,
    gc.name AS gym_name,
    s.start_time,
    s.end_time
FROM booking b
JOIN user u ON b.user_id = u.user_id
JOIN slot s ON b.slot_id = s.slot_id
JOIN gym_center gc ON s.center_id = gc.center_id;

-- ============================================
-- STORED PROCEDURES
-- ============================================
-- These handle common operations so you don't
-- have to write multiple queries every time.

-- Book a slot (handles full slots automatically)
DELIMITER //
CREATE PROCEDURE IF NOT EXISTS sp_book_slot(
    IN p_booking_id VARCHAR(50),
    IN p_user_id VARCHAR(50),
    IN p_slot_id VARCHAR(50),
    IN p_booking_date DATE,
    OUT p_result VARCHAR(50)
)
BEGIN
    DECLARE v_current_bookings INT;
    DECLARE v_max_capacity INT;
    
    -- Check how full the slot is
    SELECT current_bookings, max_capacity 
    INTO v_current_bookings, v_max_capacity
    FROM slot WHERE slot_id = p_slot_id;
    
    IF v_current_bookings < v_max_capacity THEN
        -- There's room! Book them in.
        INSERT INTO booking (booking_id, user_id, slot_id, booking_date, status)
        VALUES (p_booking_id, p_user_id, p_slot_id, p_booking_date, 'CONFIRMED');
        
        UPDATE slot SET current_bookings = current_bookings + 1 
        WHERE slot_id = p_slot_id;
        
        SET p_result = 'CONFIRMED';
    ELSE
        -- Slot is full - add to waitlist instead
        INSERT INTO booking (booking_id, user_id, slot_id, booking_date, status)
        VALUES (p_booking_id, p_user_id, p_slot_id, p_booking_date, 'WAITLISTED');
        
        SET p_result = 'WAITLISTED';
    END IF;
END //
DELIMITER ;

-- Cancel a booking (frees up the spot)
DELIMITER //
CREATE PROCEDURE IF NOT EXISTS sp_cancel_booking(
    IN p_booking_id VARCHAR(50),
    OUT p_result VARCHAR(50)
)
BEGIN
    DECLARE v_slot_id VARCHAR(50);
    DECLARE v_status VARCHAR(20);
    
    -- What's the current status?
    SELECT slot_id, status INTO v_slot_id, v_status
    FROM booking WHERE booking_id = p_booking_id;
    
    IF v_status = 'CONFIRMED' THEN
        -- They had a confirmed spot, free it up
        UPDATE booking SET status = 'CANCELLED' WHERE booking_id = p_booking_id;
        UPDATE slot SET current_bookings = current_bookings - 1 
        WHERE slot_id = v_slot_id AND current_bookings > 0;
        SET p_result = 'CANCELLED';
        
    ELSEIF v_status = 'WAITLISTED' THEN
        -- They were on the waitlist, just remove them
        UPDATE booking SET status = 'CANCELLED' WHERE booking_id = p_booking_id;
        SET p_result = 'CANCELLED';
        
    ELSE
        SET p_result = 'ALREADY_CANCELLED';
    END IF;
END //
DELIMITER ;

-- ============================================
-- TRIGGERS
-- ============================================
-- These run automatically when certain things happen.

-- Auto-generate user IDs if someone forgets to set one
DELIMITER //
CREATE TRIGGER IF NOT EXISTS trg_user_before_insert
BEFORE INSERT ON user
FOR EACH ROW
BEGIN
    IF NEW.user_id IS NULL OR NEW.user_id = '' THEN
        SET NEW.user_id = CONCAT(
            CASE NEW.role 
                WHEN 'ADMIN' THEN 'ADM'
                WHEN 'GYM_OWNER' THEN 'OWN'
                WHEN 'GYM_CUSTOMER' THEN 'CUS'
            END,
            UNIX_TIMESTAMP()
        );
    END IF;
END //
DELIMITER ;

-- ============================================
-- ALL DONE!
-- ============================================
SELECT '✓ Flipfit database is ready to go!' AS Status;
