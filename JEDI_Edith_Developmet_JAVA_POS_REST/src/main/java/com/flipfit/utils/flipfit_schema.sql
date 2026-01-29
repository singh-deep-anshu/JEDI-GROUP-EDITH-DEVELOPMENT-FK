CREATE DATABASE  IF NOT EXISTS `flipfit_schema` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `flipfit_schema`;
-- MySQL dump 10.13  Distrib 8.0.44, for macos15 (arm64)
--
-- Host: localhost    Database: flipfit_schema
-- ------------------------------------------------------
-- Server version	8.0.45

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `booking`
--

DROP TABLE IF EXISTS `booking`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `booking` (
  `bookingID` varchar(45) NOT NULL,
  `userID` varchar(45) NOT NULL,
  `slotID` varchar(45) NOT NULL,
  `bookingDate` datetime DEFAULT NULL,
  `status` enum('CONFIRMED','CANCELLED','PENDING','WAITLISTED') NOT NULL,
  PRIMARY KEY (`bookingID`),
  KEY `userID` (`userID`),
  KEY `slotID` (`slotID`),
  CONSTRAINT `booking_ibfk_1` FOREIGN KEY (`userID`) REFERENCES `user` (`userID`),
  CONSTRAINT `booking_ibfk_2` FOREIGN KEY (`slotID`) REFERENCES `slot` (`slotID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `booking`
--

LOCK TABLES `booking` WRITE;
/*!40000 ALTER TABLE `booking` DISABLE KEYS */;
/*!40000 ALTER TABLE `booking` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `gym_center`
--

DROP TABLE IF EXISTS `gym_center`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `gym_center` (
  `centerID` int NOT NULL,
  `name` varchar(45) NOT NULL,
  `address` varchar(45) NOT NULL,
  `cityID` varchar(45) NOT NULL,
  `ownerID` varchar(45) NOT NULL,
  `totalCapacity` int NOT NULL,
  `isActive` varchar(45) NOT NULL,
  PRIMARY KEY (`centerID`),
  KEY `ownerID` (`ownerID`),
  CONSTRAINT `gym_center_ibfk_1` FOREIGN KEY (`ownerID`) REFERENCES `gym_owner` (`userID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `gym_center`
--

LOCK TABLES `gym_center` WRITE;
/*!40000 ALTER TABLE `gym_center` DISABLE KEYS */;
/*!40000 ALTER TABLE `gym_center` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `gym_customer`
--

DROP TABLE IF EXISTS `gym_customer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `gym_customer` (
  `id` int NOT NULL AUTO_INCREMENT,
  `registrationDate` datetime NOT NULL,
  `isActive` tinyint NOT NULL,
  `userID` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `userID` (`userID`),
  CONSTRAINT `gym_customer_ibfk_1` FOREIGN KEY (`userID`) REFERENCES `user` (`userID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `gym_customer`
--

LOCK TABLES `gym_customer` WRITE;
/*!40000 ALTER TABLE `gym_customer` DISABLE KEYS */;
/*!40000 ALTER TABLE `gym_customer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `gym_owner`
--

DROP TABLE IF EXISTS `gym_owner`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `gym_owner` (
  `id` int NOT NULL AUTO_INCREMENT,
  `gstNumber` varchar(45) NOT NULL,
  `userID` varchar(45) NOT NULL,
  `panNumber` varchar(45) NOT NULL,
  `isVerified` tinyint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `panNumber_UNIQUE` (`panNumber`),
  KEY `userID` (`userID`),
  CONSTRAINT `gym_owner_ibfk_1` FOREIGN KEY (`userID`) REFERENCES `user` (`userID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `gym_owner`
--

LOCK TABLES `gym_owner` WRITE;
/*!40000 ALTER TABLE `gym_owner` DISABLE KEYS */;
/*!40000 ALTER TABLE `gym_owner` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `slot`
--

DROP TABLE IF EXISTS `slot`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `slot` (
  `slotID` varchar(16) NOT NULL,
  `centerID` varchar(45) NOT NULL,
  `startTime` datetime NOT NULL,
  `endTime` datetime NOT NULL,
  `maxCapacity` int NOT NULL,
  `currentBookings` int NOT NULL,
  PRIMARY KEY (`slotID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `slot`
--

LOCK TABLES `slot` WRITE;
/*!40000 ALTER TABLE `slot` DISABLE KEYS */;
/*!40000 ALTER TABLE `slot` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `userID` varchar(16) NOT NULL,
  `name` varchar(45) NOT NULL,
  `email` varchar(45) NOT NULL,
  `password` varchar(45) NOT NULL,
  `phoneNumber` varchar(45) NOT NULL,
  `city` varchar(45) NOT NULL,
  `role` enum('ADMIN','CUSTOMER','OWNER') NOT NULL,
  PRIMARY KEY (`userID`),
  UNIQUE KEY `email_UNIQUE` (`email`),
  UNIQUE KEY `phoneNumber_UNIQUE` (`phoneNumber`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-01-28 13:30:49
