-- MySQL dump 10.13  Distrib 5.7.14, for Win64 (x86_64)
--
-- Host: localhost    Database: gamestore
-- ------------------------------------------------------
-- Server version	5.7.14-log

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
-- Table structure for table `application`
--

DROP TABLE IF EXISTS `application`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `application` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `package` varchar(300) NOT NULL,
  `name` varchar(40) NOT NULL,
  `given_name` varchar(40) NOT NULL,
  `description` varchar(300) DEFAULT NULL,
  `file_path` varchar(500) NOT NULL,
  `image_128_path` varchar(500) DEFAULT NULL,
  `image_512_path` varchar(500) DEFAULT NULL,
  `category` varchar(45) NOT NULL,
  `download_num` int(11) DEFAULT '0',
  `time_uploaded` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=59 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `application`
--

LOCK TABLES `application` WRITE;
/*!40000 ALTER TABLE `application` DISABLE KEYS */;
INSERT INTO `application` VALUES (25,'play.blitsard','Ocean Online','Ocean Age','Play forever','play.blitsard\\Ocean Online\\best_rpg.zip','play.blitsard\\Ocean Online\\best_rpg_128.jpg','play.blitsard\\Ocean Online\\best_rpg_512.jpg','GAMES',0,'2016-10-07 15:15:00'),(27,'pack.gtd','GTD','GTD','Get things done','pack.gtd\\GTD\\gtd.zip','pack.gtd\\GTD\\gtd_128.jpg','pack.gtd\\GTD\\gtd_512.jpg','LIFESTYLE',0,'2016-10-07 15:15:54'),(36,'read.pdf','PDRfer','PDF','Reader','read.pdf\\PDRfer\\pdf.zip','read.pdf\\PDRfer\\pdf_128.jpg','read.pdf\\PDRfer\\pdf_512.jpg','SCHOOL',0,'2016-10-11 12:12:00'),(39,'gamez.sonic','Sonic 3D','Sonic','The Hedgedog','gamez.sonic\\Sonic 3D\\sonic.zip','gamez.sonic\\Sonic 3D\\sonic_128.png','gamez.sonic\\Sonic 3D\\sonic_512.png','GAMES',1,'2016-10-12 15:14:50'),(49,'org.art.paint','Painter','Drawer','Some description here - Some description here - Some description here - Some description here - Some description here - Some description here - Some description here - Some description here - Some description here - Some description here - Some description here ','org.art.paint\\Painter\\paint.zip','org.art.paint\\Painter\\paint_128.jpg','org.art.paint\\Painter\\paint_512.jpg','GRAPHICS',0,'2016-10-14 14:24:00'),(52,'com.fitness.studio','Activity Tracker','New Tracker','Super good tracker','com.fitness.studio\\Activity Tracker\\activity_tracker.zip','com.fitness.studio\\Activity Tracker\\activity_128_128.jpg','com.fitness.studio\\Activity Tracker\\activity_512_512.jpg','LIFESTYLE',2,'2016-10-17 11:07:53'),(53,'org.exclusivecuisine','Top 101 Recipes','Recipes','Cooking','org.exclusivecuisine\\Top 101 Recipes\\best_recipes.zip','org.exclusivecuisine\\Top 101 Recipes\\best_recipes_128.jpg','org.exclusivecuisine\\Top 101 Recipes\\best_recipes_512.jpg','HOME',2,'2016-10-17 14:56:40'),(54,'muzatron','Gold Player','Player','Music hoerer','muzatron\\Gold Player\\music_player.zip',NULL,NULL,'MULTIMEDIA',0,'2016-10-17 15:02:05'),(55,'haus.makerz','Husband For An Hour','Mann','El hombre hermoso','haus.makerz\\Husband For An Hour\\husband.zip','haus.makerz\\Husband For An Hour\\husband_128.jpg',NULL,'HOME',0,'2016-10-17 15:04:20'),(56,'talk.zkaib','Happy Chatt','UPdated Chat','Never stop talking','talk.zkaib\\Happy Chatt\\happy_chat.zip',NULL,'talk.zkaib\\Happy Chatt\\happy_chat_512.gif','COMMUNICATIONS',3,'2016-10-17 15:06:15');
/*!40000 ALTER TABLE `application` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(45) NOT NULL,
  `password` varchar(60) NOT NULL,
  `role` varchar(30) NOT NULL DEFAULT 'USER',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `username_UNIQUE` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (4,'userenc','$2a$10$UPcekmipgW03RwBc486KFeaDqoXVfsEdBIcMHnzB1e6T1YUVB/90i','USER'),(6,'user3','$2a$10$tImNDyfcDQbsE0lck/czvuAk0f9/ozCvJC37up05vDwXHiVuABdAu','DEVELOPER'),(7,'user','$2a$10$z7.C2HNtC./6/eyLuZ3HgupWnt20xjD/8zRGjV76B7br9Hsb1gch.','DEVELOPER'),(8,'usertest','$2a$10$kLAup4vOrffMK8IPeSYjDO6SVGgdMt/3Eka9HTbmu1.KvxAR1.tUa','USER'),(9,'userkii','$2a$10$d2uo2z/E4JAh.Ffs3z/qiO/rFYkNJV1o3IeyejnfRSsVAgIllaAfW','USER'),(10,'userqw','$2a$10$i6Sbi4uIrji3urKa./JxYuMYV4ywVGv7kmhfh63CAPr1ZjHJpF/5C','DEVELOPER'),(11,'userr','$2a$10$ntuuGBcAV4jCnGxQU.lem.k5jwVjFgVcsK9N3uZGOWbkG4Ztyodw2','USER'),(12,'usert','$2a$10$JsF34C1KeRVBd7c.7V1DReA9GEMlglPnhrExFJmlreyUKbDCXZZNS','DEVELOPER'),(13,'userty','$2a$10$67OJMjk0A6u3lQulW6PlpOZL4YbL4DubrPW8lxGxwiip4ZFHNJusq','DEVELOPER'),(14,'usero','$2a$10$PPWzc5ib4W4Iptfq1ibY7.pnEylaCJiATyyW7BGdnlCv6NL9uT4Wq','USER'),(15,'sefs','$2a$10$dtZEpb3RA3Srjg2rXNYPv.panZemi6s2D0pdI/8XschLPnQf.MWiW','DEVELOPER'),(17,'uio','$2a$10$TDp3vSxRWZHmRCDjlOo9heo1NtbkPGSNicKA9JiIHPYo19CZkrNo6','DEVELOPER');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_application`
--

DROP TABLE IF EXISTS `user_application`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_application` (
  `user_id` int(11) NOT NULL,
  `application_id` int(11) NOT NULL,
  PRIMARY KEY (`user_id`,`application_id`),
  KEY `app_id_idx` (`application_id`),
  CONSTRAINT `app_id` FOREIGN KEY (`application_id`) REFERENCES `application` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_application`
--

LOCK TABLES `user_application` WRITE;
/*!40000 ALTER TABLE `user_application` DISABLE KEYS */;
INSERT INTO `user_application` VALUES (7,25),(7,27),(7,36),(7,39),(7,49),(7,52),(7,53),(7,54),(7,55),(7,56);
/*!40000 ALTER TABLE `user_application` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-11-03 11:07:57
