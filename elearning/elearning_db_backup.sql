-- MySQL dump 10.13  Distrib 8.0.43, for Win64 (x86_64)
--
-- Host: localhost    Database: elearning_db
-- ------------------------------------------------------
-- Server version	8.0.43

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `chapter`
--

DROP TABLE IF EXISTS `chapter`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `chapter` (
  `id` int NOT NULL,
  `chapter1id` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `chapter1name` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `chapter2id` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `chapter2name` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `chapter3id` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `chapter3name` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `chapter4id` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `chapter4name` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `chapter5id` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `chapter5name` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `chapter6id` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `chapter6name` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `chapter7id` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `chapter7name` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `chapter8id` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `chapter8name` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `coursename` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chapter`
--

LOCK TABLES `chapter` WRITE;
/*!40000 ALTER TABLE `chapter` DISABLE KEYS */;
INSERT INTO `chapter` VALUES (19,'zzzzz','chapter 20','','','','','','','','','','','','','','','cours test'),(31,'https://www.youtube.com/watch?v=TjDa4QorBTg','11','','','','','','','','','','','','','','','cours Math');
/*!40000 ALTER TABLE `chapter` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `comment`
--

DROP TABLE IF EXISTS `comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comment` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `content` varchar(1000) COLLATE utf8mb4_general_ci NOT NULL,
  `created_at` datetime NOT NULL,
  `rating` int DEFAULT NULL,
  `course_id` int NOT NULL,
  `parent_id` bigint DEFAULT NULL,
  `professor_id` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `user_id` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKdsub2q6m6519rpas8b075fr7m` (`course_id`),
  KEY `FKde3rfu96lep00br5ov0mdieyt` (`parent_id`),
  KEY `FKfy1bwjan4lb9714eb2ov4xx63` (`professor_id`),
  KEY `FK8kcum44fvpupyw6f5baccx25c` (`user_id`),
  CONSTRAINT `FK8kcum44fvpupyw6f5baccx25c` FOREIGN KEY (`user_id`) REFERENCES `user` (`email`),
  CONSTRAINT `FKde3rfu96lep00br5ov0mdieyt` FOREIGN KEY (`parent_id`) REFERENCES `comment` (`id`),
  CONSTRAINT `FKfy1bwjan4lb9714eb2ov4xx63` FOREIGN KEY (`professor_id`) REFERENCES `professor` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comment`
--

LOCK TABLES `comment` WRITE;
/*!40000 ALTER TABLE `comment` DISABLE KEYS */;
INSERT INTO `comment` VALUES (1,'bonjour \nj\'ai un qestion','2025-10-23 20:34:21',0,16,NULL,NULL,'bayrem.bayrem@gmail.com'),(2,'oui cest qoi voter question\n','2025-10-29 18:14:52',0,16,NULL,NULL,'med@gmail.com');
/*!40000 ALTER TABLE `comment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `course`
--

DROP TABLE IF EXISTS `course`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `course` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `courseid` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `coursename` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `coursetype` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `description` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `enrolledcount` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `enrolleddate` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `instructorinstitution` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `instructorname` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `is_premuim` bit(1) NOT NULL,
  `language` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `skilllevel` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `websiteurl` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `youtubeurl` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `is_premium` bit(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `course`
--

LOCK TABLES `course` WRITE;
/*!40000 ALTER TABLE `course` DISABLE KEYS */;
INSERT INTO `course` VALUES (16,'mbge5kIVCvkh','cours Math','Youtube','zzzz','0','2025-06-12','aaaaaa','cours test ',_binary '\0','English','','cccaaaaaaaaaaaaaa','',_binary '\0'),(18,'sffOVdMQBXHL','cours test 30','Youtube','AA','0','2025-06-12','medmedoussema','cours ',_binary '','English','Advanced','','',_binary '\0'),(20,'l96SWxaSU2sH','Cours English ','Youtube','aa','0','2025-06-11','medmedoussema','medmedoussema',_binary '','English','Intermediate','','',_binary '\0'),(22,'AuM5XsIylYcD','cours python ','Youtube','aaa','0','2025-06-14','aaa','aaaa',_binary '\0','English','Advanced','','',_binary '\0');
/*!40000 ALTER TABLE `course` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `enrollment`
--

DROP TABLE IF EXISTS `enrollment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `enrollment` (
  `id` int NOT NULL,
  `courseid` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `coursename` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `coursetype` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `description` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `enrolledcount` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `enrolleddate` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `enrolleduserid` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `enrolledusername` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `enrolledusertype` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `instructorinstitution` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `instructorname` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `language` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `skilllevel` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `websiteurl` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `youtubeurl` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `enrollment`
--

LOCK TABLES `enrollment` WRITE;
/*!40000 ALTER TABLE `enrollment` DISABLE KEYS */;
INSERT INTO `enrollment` VALUES (2,'8oWxrP8SOOjY','cours test ','Youtube','aaaa','0','2025-06-21','CFAaV4SIQh2e','prof','PROFESSOR','medmedoussema','cours test ','Hindi','Basic','',''),(5,'8oWxrP8SOOjY','cours test ','Youtube','aaaa','0','2025-06-22','CFAaV4SIQh2e','prof','PROFESSOR','medmedoussema','cours test ','Hindisssss','Basic','',''),(6,'8oWxrP8SOOjY','cours test ','Youtube','aaaa','0','2025-06-22','CFAaV4SIQh2e','prof','PROFESSOR','medmedoussema','cours test ','Hindisssss','Basic','',''),(7,'8oWxrP8SOOjY','cours test ','Youtube','aaaa','0','2025-06-22','CFAaV4SIQh2e','prof','PROFESSOR','medmedoussema','cours test ','Hindisssss','Basic','',''),(8,'8oWxrP8SOOjY','cours test ','Youtube','aaaa','0','2025-06-22','CFAaV4SIQh2e','prof','PROFESSOR','medmedoussema','cours test ','Hindisssss','Basic','',''),(9,'8oWxrP8SOOjY','cours test ','Youtube','aaaa','0','2025-06-22','CFAaV4SIQh2e','prof','PROFESSOR','medmedoussema','cours test ','Hindisssss','Basic','',''),(10,'8oWxrP8SOOjY','cours test ','Youtube','aaaa','0','2025-06-22','CFAaV4SIQh2e','prof','PROFESSOR','medmedoussema','cours test ','Hindisssss','Basic','',''),(11,'xJfjAE7PjSWs','cours test2 ','Youtube','aaaa','0','2025-06-22','CFAaV4SIQh2e','prof','PROFESSOR','aaaaaa','zzaa','Français','','aaa',''),(12,'8oWxrP8SOOjY','cours test ','Youtube','aaaa','0','2025-06-22','CFAaV4SIQh2e','prof','PROFESSOR','medmedoussema','cours test ','Hindisssss','Basic','',''),(21,'mbge5kIVCvkh','cours Math','Youtube','zzzz','0','2025-06-22','yn01v1aobfss','med@gmail.com','USER','aaaaaa','cours test ','English','','cccaaaaaaaaaaaaaa',''),(23,'sffOVdMQBXHL','cours test 30','Youtube','AA','0','2025-06-23','skvccfQPOmGF','USER','USER','medmedoussema','cours ','English','Advanced','',''),(24,'mbge5kIVCvkh','cours Math','Youtube','zzzz','0','2025-06-23','skvccfQPOmGF','USER','USER','aaaaaa','cours test ','English','','cccaaaaaaaaaaaaaa',''),(25,'AuM5XsIylYcD','cours python ','Youtube','aaa','0','2025-06-23','skvccfQPOmGF','USER','USER','aaa','aaaa','English','Advanced','',''),(26,'mbge5kIVCvkh','cours Math','Youtube','zzzz','0','2025-10-23','UUd3DpANhKXF','bayrem','USER','aaaaaa','cours test ','English','','cccaaaaaaaaaaaaaa',''),(27,'l96SWxaSU2sH','Cours English ','Youtube','aa','0','2025-10-23','UUd3DpANhKXF','bayrem','USER','medmedoussema','medmedoussema','English','Intermediate','',''),(28,'sffOVdMQBXHL','cours test 30','Youtube','AA','0','2025-10-23','UUd3DpANhKXF','bayrem','USER','medmedoussema','cours ','English','Advanced','',''),(29,'mbge5kIVCvkh','cours Math','Youtube','zzzz','0','2025-10-29','empty','tor','USER','aaaaaa','cours test ','English','','cccaaaaaaaaaaaaaa',''),(30,'mbge5kIVCvkh','cours Math','Youtube','zzzz','0','2025-10-31',NULL,NULL,'ADMIN','aaaaaa','cours test ','English','','cccaaaaaaaaaaaaaa','');
/*!40000 ALTER TABLE `enrollment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hibernate_sequence`
--

DROP TABLE IF EXISTS `hibernate_sequence`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hibernate_sequence` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hibernate_sequence`
--

LOCK TABLES `hibernate_sequence` WRITE;
/*!40000 ALTER TABLE `hibernate_sequence` DISABLE KEYS */;
INSERT INTO `hibernate_sequence` VALUES (32);
/*!40000 ALTER TABLE `hibernate_sequence` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `message`
--

DROP TABLE IF EXISTS `message`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `message` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `content` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `timestamp` datetime NOT NULL,
  `receiver_professor_id` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `receiver_user_id` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `sender_professor_id` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `sender_user_id` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK2a0c0vix8ownyrfd8veq3pawq` (`receiver_professor_id`),
  KEY `FKh4xy93vijopygnpjqbe0c9iew` (`receiver_user_id`),
  KEY `FK3en5pjvr0o75mkiw9tw0sto2m` (`sender_professor_id`),
  KEY `FK80flimpheqbm2ex5r6ng1iodk` (`sender_user_id`),
  CONSTRAINT `FK2a0c0vix8ownyrfd8veq3pawq` FOREIGN KEY (`receiver_professor_id`) REFERENCES `professor` (`email`),
  CONSTRAINT `FK3en5pjvr0o75mkiw9tw0sto2m` FOREIGN KEY (`sender_professor_id`) REFERENCES `professor` (`email`),
  CONSTRAINT `FK80flimpheqbm2ex5r6ng1iodk` FOREIGN KEY (`sender_user_id`) REFERENCES `user` (`email`),
  CONSTRAINT `FKh4xy93vijopygnpjqbe0c9iew` FOREIGN KEY (`receiver_user_id`) REFERENCES `user` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `message`
--

LOCK TABLES `message` WRITE;
/*!40000 ALTER TABLE `message` DISABLE KEYS */;
INSERT INTO `message` VALUES (1,'AAAA','2025-06-22 23:46:26','prof@gmail.com',NULL,NULL,'med@gmail.com'),(2,'AAAA','2025-06-22 23:46:29','prof@gmail.com',NULL,NULL,'med@gmail.com'),(3,'monsieur','2025-10-23 20:59:44','prof@gmail.com',NULL,NULL,'bayrem.bayrem@gmail.com'),(4,'hello','2025-10-29 09:52:15','prof@gmail.com',NULL,NULL,'rouatorkhani15@gmail.com'),(5,'hello','2025-10-29 11:04:41','prof@gmail.com',NULL,NULL,'med@gmail.com'),(6,'monsieur','2025-10-29 16:57:54',NULL,'bayrem.bayrem@gmail.com',NULL,NULL),(7,'ouiii','2025-10-29 16:57:59',NULL,'bayrem.bayrem@gmail.com',NULL,NULL),(8,'ouii','2025-10-29 17:14:26',NULL,'bayrem.bayrem@gmail.com',NULL,NULL),(9,'good morgen','2025-10-31 15:40:53','gadour.gadour@gmail.com',NULL,NULL,NULL),(10,'...','2025-10-31 15:41:00','gadour.gadour@gmail.com',NULL,NULL,NULL);
/*!40000 ALTER TABLE `message` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `option`
--

DROP TABLE IF EXISTS `option`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `option` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `content` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `correct` bit(1) NOT NULL,
  `question_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKgtlhwmagte7l2ssfsgw47x9ka` (`question_id`),
  CONSTRAINT `FK_option_question` FOREIGN KEY (`question_id`) REFERENCES `question` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `option`
--

LOCK TABLES `option` WRITE;
/*!40000 ALTER TABLE `option` DISABLE KEYS */;
INSERT INTO `option` VALUES (1,'a ',_binary '',1),(2,'c',_binary '\0',1),(3,'d ',_binary '\0',1),(4,'a',_binary '\0',1),(5,'c',_binary '',2),(6,'c',_binary '\0',2),(7,'d ',_binary '\0',2),(8,'f',_binary '\0',2),(9,'a',_binary '',3),(10,'c',_binary '\0',3),(11,'d ',_binary '\0',3),(12,'f',_binary '\0',3),(13,'',_binary '\0',3);
/*!40000 ALTER TABLE `option` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payment`
--

DROP TABLE IF EXISTS `payment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payment` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `amount` double DEFAULT NULL,
  `card_number` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `card_type` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `expiry_date` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `payment_method` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `receipt_date` date DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payment`
--

LOCK TABLES `payment` WRITE;
/*!40000 ALTER TABLE `payment` DISABLE KEYS */;
INSERT INTO `payment` VALUES (1,1,'11111111111111111','mastercard','2025-09','credit','2025-06-24'),(2,3,'111111111111111','visa','2025-05','paypal','2025-06-19'),(3,111,'1234577','amex','2025-09','credit','2025-05-27'),(4,19,'1225555556521','amex','0015-02','bank','2026-02-15'),(5,19,'1225555556521','amex','0015-02','bank','2026-02-15');
/*!40000 ALTER TABLE `payment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `professor`
--

DROP TABLE IF EXISTS `professor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `professor` (
  `email` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `degreecompleted` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `department` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `experience` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `gender` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `institutionname` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `mobile` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `password` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `professorid` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `professorname` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `status` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `role` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `professor`
--

LOCK TABLES `professor` WRITE;
/*!40000 ALTER TABLE `professor` DISABLE KEYS */;
INSERT INTO `professor` VALUES ('gadour.gadour@gmail.com','5','python','4','Male','esprit','1111111111','$2a$10$AMDOAYcaqnau8vhO0btC/O/2zuOsJ8meRlBhrgPfW8BnfZLO9mC2.','FeMvszxsD1SF','gadour','false','Professor'),('prof@gmail.com','aaaa','info','3','Male','prof@gmail.com','1234545662','$2b$10$69ZXibNJiYGPRXyQGcKSc.EX8.RDzTpdp/gIfmjJ5Ib9YiLMqCa7C','bNhHk7d0pkfu','prof','1',NULL);
/*!40000 ALTER TABLE `professor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `question`
--

DROP TABLE IF EXISTS `question`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `question` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `content` varchar(1000) NOT NULL,
  `created_at` datetime NOT NULL,
  `course_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_question_course` (`course_id`),
  CONSTRAINT `FK_question_course` FOREIGN KEY (`course_id`) REFERENCES `course` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `question`
--

LOCK TABLES `question` WRITE;
/*!40000 ALTER TABLE `question` DISABLE KEYS */;
INSERT INTO `question` VALUES (1,'ddddd','2025-11-05 15:55:45',16),(2,'aaa','2025-11-05 15:57:13',16),(3,'bbbbb','2025-11-05 16:05:03',16),(4,'ddddd','2025-11-05 16:53:41',16),(5,'ddddd','2025-11-05 20:08:24',16),(6,'ddddd','2025-11-05 20:17:46',16),(7,'ddddd','2025-11-05 20:21:23',16),(8,'ddddd','2025-11-05 20:22:21',18),(9,'ddddd','2025-11-05 20:46:22',16),(10,'ddddd','2025-11-05 20:51:04',16);
/*!40000 ALTER TABLE `question` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `question_backup`
--

DROP TABLE IF EXISTS `question_backup`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `question_backup` (
  `id` bigint NOT NULL DEFAULT '0',
  `content` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `created_at` datetime NOT NULL,
  `course_id` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `question_backup`
--

LOCK TABLES `question_backup` WRITE;
/*!40000 ALTER TABLE `question_backup` DISABLE KEYS */;
INSERT INTO `question_backup` VALUES (1,'donner une reponse ','2025-06-22 15:12:40',16),(2,'donner une reponse 2','2025-06-22 16:01:28',18),(3,'donner une reponse Q3 ','2025-06-22 21:07:42',20),(4,'ddddd','2025-10-29 16:53:54',20),(5,'ddddd','2025-10-29 16:53:55',20),(6,'ddddd','2025-10-29 16:53:56',20),(7,'ddddd','2025-10-29 16:53:56',20),(8,'ddddd','2025-10-29 16:54:02',20),(9,'ddddd','2025-10-29 16:54:03',20),(10,'ddddd','2025-10-29 16:54:03',20),(11,'11','2025-11-02 18:55:17',22),(12,'11','2025-11-02 18:55:18',22),(13,'11','2025-11-02 18:55:19',22),(14,'11','2025-11-02 18:55:19',22),(15,'11','2025-11-02 18:55:19',22),(16,'11','2025-11-02 18:55:20',22),(17,'11','2025-11-02 18:55:20',22),(18,'11','2025-11-02 18:55:20',22),(19,'11','2025-11-02 18:55:20',22),(20,'11','2025-11-02 18:55:25',22),(21,'11','2025-11-02 18:55:26',22),(22,'11','2025-11-02 18:55:26',22),(23,'11','2025-11-02 18:55:27',22),(24,'11','2025-11-02 18:55:27',22),(25,'11','2025-11-02 18:55:27',22),(26,'11','2025-11-02 18:55:27',22),(27,'11','2025-11-02 18:55:27',22),(28,'11','2025-11-02 18:55:29',22),(29,'11','2025-11-02 19:09:46',22),(30,'11','2025-11-02 19:09:47',22),(31,'11','2025-11-02 19:09:47',22),(32,'11','2025-11-02 19:09:47',22),(33,'11','2025-11-02 19:09:47',22),(34,'11','2025-11-02 19:09:47',22),(35,'11','2025-11-02 19:18:33',22),(36,'11','2025-11-02 20:14:05',22),(37,'aa','2025-11-02 20:28:52',22),(38,'aa','2025-11-02 20:28:53',22),(39,'ddddd','2025-11-04 10:06:24',22),(40,'ddddd','2025-11-04 10:06:25',22),(41,'ddddd','2025-11-04 10:06:25',22),(42,'ddddd','2025-11-04 10:06:25',22),(43,'ddddd','2025-11-04 10:06:26',22),(44,'ddddd','2025-11-04 10:06:26',22),(45,'ddddd','2025-11-04 10:06:26',22),(46,'ddddd','2025-11-04 10:07:02',22),(47,'ddddd','2025-11-04 10:07:11',22),(48,'ddddd','2025-11-04 10:10:20',22),(49,'ddddd','2025-11-04 10:10:40',22);
/*!40000 ALTER TABLE `question_backup` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `email` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `address` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `gender` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `is_premuim` bit(1) NOT NULL,
  `mobile` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `password` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `profession` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `role` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `userid` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `username` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES ('admin@gmail.com','aaaaa','',_binary '','2873668489','$2b$10$O1NMHh2j5yLZZSbbkCZpgeUAy/eJWM2Dnpv14PopDRA9Kne3P7oAW','user','Admin','aRmVQ4roqZl3','med@gmail.com'),('bayrem.bayrem@gmail.com','dddddc','',_binary '','1111111111','$2a$10$JM74TGhx3iOwrkA5xKps9ufDdZ8B2XilW9ZjYyys9445wqnm4GKXe','44555','User','UUd3DpANhKXF','bayrem'),('raki.raki@gmail.com','Am walperloh 11 Schmalkalden 98574','',_binary '','5510313412','$2a$10$7TAz.JEBIsvwFbAa5kiLeee2Ug1Hu4ss03B41NXxyqT/UfNz/Q6k2','aaab','User','hYe4FITUXdkM','raki'),('rouatorkhani15@gmail.com','Am walperloh 11 Schmalkalden 98574','femme',_binary '','5510313412','$2b$10$eIa3XTkmBZbesg7BxR9bx.x5spYU46m8ziY5zTIrUcb/GR/gsKckG','aaa','User','empty','tor'),('safa.safa@gmail.com','Am walperloh 11 Schmalkalden 98574','',_binary '','1255247755','$2a$10$zDUW83r4PlHWWZm2YtaYeOYDdWMSj1ZsORjnSljYonuTv9.htPWzO','math','User','EeElP5jpImjn','safa'),('test.test@gmail.com','Am walperloh 11 Schmalkalden 98574','',_binary '\0','1111111111','$2a$10$yZEYkL6MHAvGv3pKZ5/aDuHglMo9e4JQxVFhGYHv0oDU0oSUnL8e6','aaab','User','XIEnTXaTEZip','test'),('usrer@gmail.com','tunis','',_binary '','2873668489','$2a$10$JLuowOwsjMVIqCXvt2Q2tezyIJql66hpYlndiHYhI13nCFooms5/2','apprenant','User','skvccfQPOmGF','USER');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `wishlist`
--

DROP TABLE IF EXISTS `wishlist`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `wishlist` (
  `coursename` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `courseid` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `coursetype` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `description` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `enrolledcount` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `instructorinstitution` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `instructorname` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `language` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `likeduser` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `likedusertype` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `skilllevel` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `websiteurl` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`coursename`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `wishlist`
--

LOCK TABLES `wishlist` WRITE;
/*!40000 ALTER TABLE `wishlist` DISABLE KEYS */;
INSERT INTO `wishlist` VALUES ('cours test 30','sffOVdMQBXHL','Youtube','AA','0','medmedoussema','cours ','English','bayrem.bayrem@gmail.com','USER','Advanced',''),('cours test2 ','xJfjAE7PjSWs','Youtube','aaaa','0','aaaaaa','zzaa','Français','prof@gmail.com','PROFESSOR','','aaa');
/*!40000 ALTER TABLE `wishlist` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-11-05 20:56:16
