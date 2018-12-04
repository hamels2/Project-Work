-- MySQL dump 10.16  Distrib 10.1.31-MariaDB, for Win32 (AMD64)
--
-- Host: localhost    Database: courtside
-- ------------------------------------------------------
-- Server version	10.1.31-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `courts`
--

DROP TABLE IF EXISTS `courts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `courts` (
  `idcourts` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` text NOT NULL,
  `latitude` decimal(10,6) DEFAULT NULL,
  `longitude` decimal(10,6) DEFAULT NULL,
  `image_url` text,
  `rating` decimal(10,0) unsigned NOT NULL,
  PRIMARY KEY (`idcourts`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `courts`
--

LOCK TABLES `courts` WRITE;
/*!40000 ALTER TABLE `courts` DISABLE KEYS */;
INSERT INTO `courts` VALUES (1,'k',90.000000,90.000000,NULL,4),(2,'ill',90.000000,180.000000,NULL,5),(3,'here',51.503364,-0.127625,NULL,4),(7,'ruckard p',43.251170,-79.923752,'',4),(8,'ruckard park',43.260900,-79.919200,'out.jpg',5);
/*!40000 ALTER TABLE `courts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reviews`
--

DROP TABLE IF EXISTS `reviews`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `reviews` (
  `idreviews` int(11) NOT NULL AUTO_INCREMENT,
  `review` text NOT NULL,
  `rating` decimal(10,0) NOT NULL,
  `idusers` varchar(45) NOT NULL,
  `idcourts` int(11) unsigned DEFAULT NULL,
  PRIMARY KEY (`idreviews`),
  KEY `court_idx` (`idcourts`),
  KEY `user_idx` (`idusers`),
  CONSTRAINT `court` FOREIGN KEY (`idcourts`) REFERENCES `courts` (`idcourts`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `user` FOREIGN KEY (`idusers`) REFERENCES `users` (`email`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reviews`
--

LOCK TABLES `reviews` WRITE;
/*!40000 ALTER TABLE `reviews` DISABLE KEYS */;
INSERT INTO `reviews` VALUES (1,'cool',4,'sam2hamel@gmail.com',3),(2,'it\'s good',3,'sam2hamel@gmail.com',NULL),(3,'cool',3,'sam2hamel@gmail.com',NULL),(4,'review',2,'sam2hamel@gmail.com',NULL),(5,'',3,'sam2hamel@gmail.com',1),(6,'great place',4,'sam2hamel@gmail.com',7),(7,'great place',4,'sam2hamel@gmail.com',7),(8,'great courts',5,'sam2hamel@gmail.com',8);
/*!40000 ALTER TABLE `reviews` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `name` text NOT NULL,
  `password` varchar(45) NOT NULL,
  `dob` date NOT NULL,
  `gender` enum('male','female','non-binary') NOT NULL,
  `email` varchar(45) NOT NULL,
  PRIMARY KEY (`email`),
  UNIQUE KEY `email_UNIQUE` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES ('sam','12345','2018-04-16','male','sam2hamel@gmail.com');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-04-09 14:55:28
