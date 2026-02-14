-- MySQL dump 10.13  Distrib 9.5.0, for Win64 (x86_64)
--
-- Host: localhost    Database: db1
-- ------------------------------------------------------
-- Server version	9.5.0

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
-- Table structure for table `alias`
--

DROP TABLE IF EXISTS `alias`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `alias` (
  `anime_id` int DEFAULT NULL,
  `id` int NOT NULL AUTO_INCREMENT,
  `fname` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `anime_id_fk` (`anime_id`),
  KEY `idx_anime_id` (`anime_id`),
  CONSTRAINT `anime_id_fk` FOREIGN KEY (`anime_id`) REFERENCES `anime` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=241 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

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
  PRIMARY KEY (`id`),
  KEY `idx_originalName` (`originalName`),
  KEY `idx_foldername` (`foldername`)
) ENGINE=InnoDB AUTO_INCREMENT=9711 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `coment`
--

DROP TABLE IF EXISTS `coment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `coment` (
  `id` int DEFAULT NULL,
  `date` date DEFAULT NULL,
  `count` int DEFAULT NULL,
  `title` varchar(400) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `AI` int NOT NULL AUTO_INCREMENT,
  `up_check` int DEFAULT NULL,
  PRIMARY KEY (`AI`),
  KEY `title_index` (`title`),
  KEY `idx_date` (`date`),
  KEY `idx_count` (`count`)
) ENGINE=InnoDB AUTO_INCREMENT=59999 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `duplicatevideo`
--

DROP TABLE IF EXISTS `duplicatevideo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `duplicatevideo` (
  `videoId_search` int NOT NULL,
  `videoId_target` int NOT NULL,
  `similar` decimal(3,2) DEFAULT NULL,
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`videoId_search`,`videoId_target`),
  KEY `videoId_target` (`videoId_target`),
  KEY `idx_duplicatevideo` (`similar`),
  KEY `idx_videoId_target` (`videoId_target`),
  CONSTRAINT `duplicatevideo_ibfk_1` FOREIGN KEY (`videoId_search`) REFERENCES `video` (`video_id`) ON DELETE CASCADE,
  CONSTRAINT `duplicatevideo_ibfk_2` FOREIGN KEY (`videoId_target`) REFERENCES `video` (`video_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `excludeanime`
--

DROP TABLE IF EXISTS `excludeanime`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `excludeanime` (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_excludeanime` (`title`)
) ENGINE=InnoDB AUTO_INCREMENT=76456 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `extension`
--

DROP TABLE IF EXISTS `extension`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `extension` (
  `id` int NOT NULL AUTO_INCREMENT,
  `ex` varchar(20) DEFAULT NULL,
  `type` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=42 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

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
  `hiduke` datetime DEFAULT NULL,
  PRIMARY KEY (`video_id`),
  KEY `idx_jk_rownumber` (`come_byte`),
  KEY `idx_video_time` (`video_time`),
  KEY `idx_hiduke` (`hiduke`),
  CONSTRAINT `fk_rownumber` FOREIGN KEY (`video_id`) REFERENCES `video` (`video_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

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
  KEY `idx_video_id` (`video_id`),
  KEY `idx_label` (`label`),
  KEY `idx_label_video_id` (`video_id`,`label`),
  KEY `idx_score` (`score`),
  CONSTRAINT `label_ibfk_1` FOREIGN KEY (`video_id`) REFERENCES `video` (`video_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `linevecs`
--

DROP TABLE IF EXISTS `linevecs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `linevecs` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `strVecId` bigint unsigned NOT NULL,
  `vecId` bigint unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `lineVecs_fk1` (`strVecId`),
  KEY `idx_linevecs_vecId` (`vecId`),
  KEY `idx_linevecs_vecId_strVecId` (`vecId`,`strVecId`),
  KEY `idx_strVecId` (`strVecId`),
  KEY `idx_vecId` (`vecId`),
  CONSTRAINT `lineVecs_fk1` FOREIGN KEY (`strVecId`) REFERENCES `strvec` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=17124350 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `phash_video`
--

DROP TABLE IF EXISTS `phash_video`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `phash_video` (
  `video_id` int NOT NULL,
  `phash` binary(64) NOT NULL,
  PRIMARY KEY (`video_id`,`phash`),
  CONSTRAINT `phash_video_ibfk_1` FOREIGN KEY (`video_id`) REFERENCES `video` (`video_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `port_to_path`
--

DROP TABLE IF EXISTS `port_to_path`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `port_to_path` (
  `port` int DEFAULT NULL,
  `path` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

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
-- Table structure for table `progdoc`
--

DROP TABLE IF EXISTS `progdoc`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `progdoc` (
  `id` int NOT NULL AUTO_INCREMENT,
  `parent_id` int unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_parent_id` (`parent_id`),
  CONSTRAINT `progdoc_ibfk_1` FOREIGN KEY (`parent_id`) REFERENCES `progparent` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1426 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `proghash`
--

DROP TABLE IF EXISTS `proghash`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `proghash` (
  `doc_id` int NOT NULL,
  `hash_int` int NOT NULL,
  `gram_pos` mediumtext NOT NULL,
  KEY `idx_hash` (`hash_int`),
  KEY `proghash_ibfk_1` (`doc_id`),
  CONSTRAINT `proghash_ibfk_1` FOREIGN KEY (`doc_id`) REFERENCES `progdoc` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `progparent`
--

DROP TABLE IF EXISTS `progparent`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `progparent` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `video_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `progparent_ibfk_1` (`video_id`),
  CONSTRAINT `progparent_ibfk_1` FOREIGN KEY (`video_id`) REFERENCES `video_prog` (`video_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1425 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

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
  `ranking` int DEFAULT NULL,
  KEY `idx_ranking` (`ranking`),
  KEY `idx_score` (`score`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

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
  `txt` varchar(9000) DEFAULT NULL,
  KEY `idx_year_season` (`year`,`season`),
  KEY `idx_all_ranking` (`all_ranking`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

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
  `T_count` int DEFAULT NULL,
  KEY `idx_anime_id` (`anime_id`),
  KEY `idx_year_season` (`year`,`season`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `renban`
--

DROP TABLE IF EXISTS `renban`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `renban` (
  `alias` varchar(20) NOT NULL,
  PRIMARY KEY (`alias`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `score`
--

DROP TABLE IF EXISTS `score`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `score` (
  `anime_id` int NOT NULL,
  `score` decimal(6,2) DEFAULT NULL,
  `year` int NOT NULL,
  `season` int NOT NULL,
  PRIMARY KEY (`anime_id`,`year`,`season`),
  UNIQUE KEY `anime_id` (`anime_id`,`year`,`season`),
  KEY `idx_score` (`score`),
  KEY `idx_year_season` (`year`,`season`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `season`
--

DROP TABLE IF EXISTS `season`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `season` (
  `season` int DEFAULT NULL,
  `txt` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `strvec`
--

DROP TABLE IF EXISTS `strvec`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `strvec` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `vecParent_id` bigint unsigned NOT NULL,
  `rowNumber` int DEFAULT NULL,
  `cost` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `strVecLine_fk1` (`vecParent_id`),
  KEY `idx_strvec_id_cost` (`id`,`cost`),
  KEY `idx_vecParent_id` (`vecParent_id`),
  KEY `idx_rowNumber` (`rowNumber`),
  KEY `idx_cost` (`cost`),
  CONSTRAINT `strVecLine_fk1` FOREIGN KEY (`vecParent_id`) REFERENCES `strvecparent` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3818777 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `strvecparent`
--

DROP TABLE IF EXISTS `strvecparent`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `strvecparent` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `tableId` int NOT NULL,
  `parentId` int NOT NULL,
  `childId` bigint DEFAULT NULL,
  `childId_for_unique` bigint GENERATED ALWAYS AS (ifnull(`childId`,0)) STORED,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_table_parent_child` (`tableId`,`parentId`,`childId`),
  UNIQUE KEY `uniq_table_parent_child2` (`tableId`,`parentId`,`childId_for_unique`),
  KEY `idx_strvecparent_id_tableId` (`id`,`tableId`),
  KEY `idx_tableId` (`tableId`),
  KEY `idx_parentId` (`parentId`),
  KEY `idx_childId` (`childId`)
) ENGINE=InnoDB AUTO_INCREMENT=134442 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `table_id`
--

DROP TABLE IF EXISTS `table_id`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `table_id` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `name` char(30) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `type`
--

DROP TABLE IF EXISTS `type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `type` (
  `id` int NOT NULL AUTO_INCREMENT,
  `type` varchar(10) DEFAULT NULL,
  `fileType` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `type` (`type`)
) ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `up_user`
--

DROP TABLE IF EXISTS `up_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `up_user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `pw` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=66 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `upload_hash`
--

DROP TABLE IF EXISTS `upload_hash`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `upload_hash` (
  `alias` varchar(20) DEFAULT NULL,
  `hash` varchar(64) DEFAULT NULL,
  KEY `idx_hash` (`hash`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `uploadfile`
--

DROP TABLE IF EXISTS `uploadfile`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `uploadfile` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `fname` varchar(255) DEFAULT NULL,
  `lname` varchar(255) DEFAULT NULL,
  `alias` varchar(30) DEFAULT NULL,
  `extension` varchar(60) DEFAULT NULL,
  `hiduke` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `searchTxt` varchar(300) DEFAULT NULL,
  `mimeId` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_uploadfile_userid_alias` (`user_id`,`alias`),
  KEY `idx_uploadfile_mimeid` (`mimeId`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_hiduke` (`hiduke`),
  KEY `idx_mimeId` (`mimeId`)
) ENGINE=InnoDB AUTO_INCREMENT=140767 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

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
  `ext` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`video_id`),
  UNIQUE KEY `uni_video_ext` (`fname`,`ext`),
  KEY `fk_animeid` (`anime_id`),
  KEY `idx_video_id` (`video_id`),
  KEY `idx_anime_id_video_id` (`anime_id`,`video_id`),
  CONSTRAINT `fk_animeid` FOREIGN KEY (`anime_id`) REFERENCES `anime` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=63491 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `video_info`
--

DROP TABLE IF EXISTS `video_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `video_info` (
  `video_id` int DEFAULT NULL,
  `anime_id` int DEFAULT NULL,
  `fname` varchar(500) DEFAULT NULL,
  `score` decimal(5,3) DEFAULT NULL,
  `nocmframe` int DEFAULT NULL,
  `hiduke` datetime DEFAULT NULL,
  `video_time` time DEFAULT NULL,
  `ext` varchar(20) DEFAULT NULL,
  KEY `video_id` (`video_id`),
  KEY `idx_anime_id_video_id` (`anime_id`,`video_id`),
  KEY `idx_score` (`score`),
  KEY `idx_hiduke` (`hiduke`),
  KEY `idx_video_time` (`video_time`),
  CONSTRAINT `video_info_ibfk_1` FOREIGN KEY (`video_id`) REFERENCES `video` (`video_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

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
-- Table structure for table `videotitledoc`
--

DROP TABLE IF EXISTS `videotitledoc`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `videotitledoc` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `parent_id` int unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_parent_id` (`parent_id`),
  CONSTRAINT `videotitledoc_ibfk_1` FOREIGN KEY (`parent_id`) REFERENCES `videotitleparenthash` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=12624 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `videotitlehash`
--

DROP TABLE IF EXISTS `videotitlehash`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `videotitlehash` (
  `doc_id` int unsigned NOT NULL,
  `hash_int` int NOT NULL,
  `gram_pos` int NOT NULL,
  KEY `idx_hash_doc` (`hash_int`,`doc_id`),
  KEY `idx_hash` (`hash_int`),
  KEY `idx_doc_id` (`doc_id`),
  CONSTRAINT `videotitlehash_ibfk_1` FOREIGN KEY (`doc_id`) REFERENCES `videotitledoc` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `videotitleparenthash`
--

DROP TABLE IF EXISTS `videotitleparenthash`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `videotitleparenthash` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `anime_id` int NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `anime_id` (`anime_id`),
  KEY `idx_anime_id_table_id` (`anime_id`),
  CONSTRAINT `videotitleparenthash_ibfk_1` FOREIGN KEY (`anime_id`) REFERENCES `anime` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=6113 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `wordvec`
--

DROP TABLE IF EXISTS `wordvec`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `wordvec` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `cost` int NOT NULL,
  `vecAvg` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_vecParent_vecAvg_cost_id` (`vecAvg`,`cost`,`id`),
  KEY `idx_cost_vecAvg` (`cost`,`vecAvg`)
) ENGINE=InnoDB AUTO_INCREMENT=17124350 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-02-14 22:00:08
