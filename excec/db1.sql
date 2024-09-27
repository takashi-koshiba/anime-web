-- MySQL dump 10.13  Distrib 8.0.38, for Win64 (x86_64)
--
-- Host: localhost    Database: db1
-- ------------------------------------------------------
-- Server version	8.0.38

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
-- Current Database: `db1`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `db1` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `db1`;

--
-- Table structure for table `anime`
--

DROP TABLE IF EXISTS `anime`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `anime` (
  `id` int NOT NULL AUTO_INCREMENT,
  `originalName` varchar(300) DEFAULT NULL,
  `foldername` varchar(300) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2113 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `anime`
--

LOCK TABLES `anime` WRITE;
/*!40000 ALTER TABLE `anime` DISABLE KEYS */;
/*!40000 ALTER TABLE `anime` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jk_rownumber`
--

DROP TABLE IF EXISTS `jk_rownumber`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `jk_rownumber` (
  `video_id` int NOT NULL,
  `come_byte` int DEFAULT NULL,
  `video_time` time DEFAULT NULL,
  `hiduke` date DEFAULT '2000-01-01',
  PRIMARY KEY (`video_id`),
  CONSTRAINT `fk_rownumber` FOREIGN KEY (`video_id`) REFERENCES `video` (`video_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jk_rownumber`
--

LOCK TABLES `jk_rownumber` WRITE;
/*!40000 ALTER TABLE `jk_rownumber` DISABLE KEYS */;
/*!40000 ALTER TABLE `jk_rownumber` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `label`
--

DROP TABLE IF EXISTS `label`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `label` (
  `video_id` int DEFAULT NULL,
  `fname` int DEFAULT NULL,
  `score` decimal(5,3) DEFAULT NULL,
  `label` int DEFAULT NULL,
  KEY `video_id` (`video_id`),
  CONSTRAINT `label_ibfk_1` FOREIGN KEY (`video_id`) REFERENCES `video` (`video_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `label`
--

LOCK TABLES `label` WRITE;
/*!40000 ALTER TABLE `label` DISABLE KEYS */;
/*!40000 ALTER TABLE `label` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prefix`
--

DROP TABLE IF EXISTS `prefix`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `prefix` (
  `id` int NOT NULL AUTO_INCREMENT,
  `txt` char(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `txt` (`txt`)
) ENGINE=InnoDB AUTO_INCREMENT=73 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prefix`
--

LOCK TABLES `prefix` WRITE;
/*!40000 ALTER TABLE `prefix` DISABLE KEYS */;
INSERT INTO `prefix` VALUES (47,'Ａ'),(48,'Ｂ'),(49,'Ｃ'),(50,'Ｄ'),(51,'Ｅ'),(52,'Ｆ'),(53,'Ｇ'),(54,'Ｈ'),(55,'Ｉ'),(56,'Ｊ'),(57,'Ｋ'),(58,'Ｌ'),(59,'Ｍ'),(60,'Ｎ'),(61,'Ｏ'),(62,'Ｐ'),(63,'Ｑ'),(64,'Ｒ'),(65,'Ｓ'),(66,'Ｔ'),(67,'Ｕ'),(68,'Ｖ'),(69,'Ｗ'),(70,'Ｘ'),(71,'Ｙ'),(72,'Ｚ'),(1,'あ'),(2,'い'),(3,'う'),(4,'え'),(5,'お'),(6,'か'),(7,'き'),(8,'く'),(9,'け'),(10,'こ'),(11,'さ'),(12,'し'),(13,'す'),(14,'せ'),(15,'そ'),(16,'た'),(17,'ち'),(18,'つ'),(19,'て'),(20,'と'),(21,'な'),(22,'に'),(23,'ぬ'),(24,'ね'),(25,'の'),(26,'は'),(27,'ひ'),(28,'ふ'),(29,'へ'),(30,'ほ'),(31,'ま'),(32,'み'),(33,'む'),(34,'め'),(35,'も'),(36,'や'),(37,'ゆ'),(38,'よ'),(39,'ら'),(40,'り'),(41,'る'),(42,'れ'),(43,'ろ'),(44,'わ'),(45,'を'),(46,'ん');
/*!40000 ALTER TABLE `prefix` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ranked_anime`
--

DROP TABLE IF EXISTS `ranked_anime`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ranked_anime` (
  `anime_id` int DEFAULT NULL,
  `originalName` varchar(400) DEFAULT NULL,
  `foldername` varchar(400) DEFAULT NULL,
  `score` decimal(6,2) DEFAULT NULL,
  `txt` varchar(9000) DEFAULT NULL,
  `ranking` int DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ranked_anime`
--

LOCK TABLES `ranked_anime` WRITE;
/*!40000 ALTER TABLE `ranked_anime` DISABLE KEYS */;
/*!40000 ALTER TABLE `ranked_anime` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ranked_anime_season`
--

DROP TABLE IF EXISTS `ranked_anime_season`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ranked_anime_season` (
  `anime_id` int DEFAULT NULL,
  `year` int DEFAULT NULL,
  `season` int DEFAULT NULL,
  `all_ranking` int DEFAULT NULL,
  `score` decimal(6,2) DEFAULT NULL,
  `originalName` varchar(300) DEFAULT NULL,
  `folderName` varchar(300) DEFAULT NULL,
  `txt` varchar(9000) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ranked_anime_season`
--

LOCK TABLES `ranked_anime_season` WRITE;
/*!40000 ALTER TABLE `ranked_anime_season` DISABLE KEYS */;
/*!40000 ALTER TABLE `ranked_anime_season` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ranking`
--

DROP TABLE IF EXISTS `ranking`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ranking` (
  `all_ranking` int DEFAULT NULL,
  `anime_id` int DEFAULT NULL,
  `year` int DEFAULT NULL,
  `season` int DEFAULT NULL,
  `mediun_come_byte` int DEFAULT NULL,
  `T_count` int DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ranking`
--

LOCK TABLES `ranking` WRITE;
/*!40000 ALTER TABLE `ranking` DISABLE KEYS */;
/*!40000 ALTER TABLE `ranking` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `score`
--

DROP TABLE IF EXISTS `score`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `score` (
  `anime_id` int DEFAULT NULL,
  `score` decimal(6,2) DEFAULT NULL,
  `year` int DEFAULT NULL,
  `season` int DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `score`
--

LOCK TABLES `score` WRITE;
/*!40000 ALTER TABLE `score` DISABLE KEYS */;
/*!40000 ALTER TABLE `score` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `video`
--

DROP TABLE IF EXISTS `video`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `video` (
  `anime_id` int DEFAULT NULL,
  `video_id` int NOT NULL AUTO_INCREMENT,
  `fname` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`video_id`),
  UNIQUE KEY `fname` (`fname`),
  UNIQUE KEY `fname_2` (`fname`),
  KEY `fk_animeid` (`anime_id`),
  CONSTRAINT `fk_animeid` FOREIGN KEY (`anime_id`) REFERENCES `anime` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=7396 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `video`
--

LOCK TABLES `video` WRITE;
/*!40000 ALTER TABLE `video` DISABLE KEYS */;
/*!40000 ALTER TABLE `video` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `video_prog`
--

DROP TABLE IF EXISTS `video_prog`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `video_prog` (
  `video_id` int NOT NULL,
  `txt` varchar(9000) DEFAULT NULL,
  PRIMARY KEY (`video_id`),
  CONSTRAINT `fk_videoProg` FOREIGN KEY (`video_id`) REFERENCES `video` (`video_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `video_prog`
--

LOCK TABLES `video_prog` WRITE;
/*!40000 ALTER TABLE `video_prog` DISABLE KEYS */;
/*!40000 ALTER TABLE `video_prog` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-09-27 11:33:38
