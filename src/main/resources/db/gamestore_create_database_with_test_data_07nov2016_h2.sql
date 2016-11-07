-- MySQL dump 10.13  Distrib 5.7.14, for Win64 (x86_64)
--
-- Host: localhost    Database: gamestore
-- ------------------------------------------------------
-- Server version	5.7.14-log
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO,NO_KEY_OPTIONS,NO_TABLE_OPTIONS,ANSI' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table application
--

DROP TABLE IF EXISTS application;
CREATE TABLE application (
  id int(11) NOT NULL auto_increment,
  package varchar(300) NOT NULL,
  name varchar(40) NOT NULL,
  given_name varchar(40) NOT NULL,
  description varchar(300) DEFAULT NULL,
  file_path varchar(500) NOT NULL,
  image_128_path varchar(500) DEFAULT NULL,
  image_512_path varchar(500) DEFAULT NULL,
  category varchar(45) NOT NULL,
  download_num int(11) DEFAULT '0',
  time_uploaded timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY id_application_UNIQUE (id)
);

--
-- Dumping data for table application
--

INSERT INTO application VALUES (25,'play.blitsard','Ocean Online','Ocean Age','Play forever','play.blitsard\\Ocean Online\\best_rpg.zip','play.blitsard\\Ocean Online\\best_rpg_128.jpg','play.blitsard\\Ocean Online\\best_rpg_512.jpg','GAMES',0,'2016-10-07 15:15:00');
INSERT INTO application VALUES (27,'pack.gtd','GTD','GTD','Get things done','pack.gtd\\GTD\\gtd.zip','pack.gtd\\GTD\\gtd_128.jpg','pack.gtd\\GTD\\gtd_512.jpg','LIFESTYLE',0,'2016-10-07 15:15:54');
INSERT INTO application VALUES (36,'read.pdf','PDRfer','PDF','Reader','read.pdf\\PDRfer\\pdf.zip','read.pdf\\PDRfer\\pdf_128.jpg','read.pdf\\PDRfer\\pdf_512.jpg','SCHOOL',0,'2016-10-11 12:12:00');
INSERT INTO application VALUES (39,'gamez.sonic','Sonic 3D','Sonic','The Hedgedog','gamez.sonic\\Sonic 3D\\sonic.zip','gamez.sonic\\Sonic 3D\\sonic_128.png','gamez.sonic\\Sonic 3D\\sonic_512.png','GAMES',1,'2016-10-12 15:14:50');
INSERT INTO application VALUES (49,'org.art.paint','Painter','Drawer','Some description here - Some description here - Some description here - Some description here - Some description here - Some description here - Some description here - Some description here - Some description here - Some description here - Some description here ','org.art.paint\\Painter\\paint.zip','org.art.paint\\Painter\\paint_128.jpg','org.art.paint\\Painter\\paint_512.jpg','GRAPHICS',0,'2016-10-14 14:24:00');
INSERT INTO application VALUES (52,'com.fitness.studio','Activity Tracker','Tracker','dfgdfgdf','com.fitness.studio\\Activity Tracker\\activity_tracker.zip','com.fitness.studio\\Activity Tracker\\activity_128_128.jpg','com.fitness.studio\\Activity Tracker\\activity_512_512.jpg','HEALTH',2,'2016-10-17 11:07:53');
INSERT INTO application VALUES (53,'org.exclusivecuisine','Top 101 Recipes','Recipes','Cooking','org.exclusivecuisine\\Top 101 Recipes\\best_recipes.zip','org.exclusivecuisine\\Top 101 Recipes\\best_recipes_128.jpg','org.exclusivecuisine\\Top 101 Recipes\\best_recipes_512.jpg','HOME',2,'2016-10-17 14:56:40');
INSERT INTO application VALUES (54,'muzatron','Gold Player','Player','Music hoerer','muzatron\\Gold Player\\music_player.zip',NULL,NULL,'MULTIMEDIA',0,'2016-10-17 15:02:05');
INSERT INTO application VALUES (55,'haus.makerz','Husband For An Hour','Mann','El hombre hermoso','haus.makerz\\Husband For An Hour\\husband.zip','haus.makerz\\Husband For An Hour\\husband_128.jpg',NULL,'HOME',0,'2016-10-17 15:04:20');
INSERT INTO application VALUES (56,'talk.zkaib','Happy Chatt','UPdated Chat','Never stop talking','talk.zkaib\\Happy Chatt\\happy_chat.zip',NULL,'talk.zkaib\\Happy Chatt\\happy_chat_512.gif','COMMUNICATIONS',3,'2016-10-17 15:06:15');
INSERT INTO application VALUES (58,'org.strokova','superjet app','Super Jett','Taking off','org.strokova\\superjetapp\\superjetappzip.zip','org.strokova\\superjetapp\\calm_128_128.jpg','org.strokova\\superjetapp\\calm_512_512.jpg','GAMES',0,'2016-10-20 13:26:50');

--
-- Table structure for table user
--
DROP TABLE IF EXISTS user;
CREATE TABLE user (
  id int(11) NOT NULL auto_increment,
  username varchar(45) NOT NULL,
  password varchar(60) NOT NULL,
  role varchar(30) NOT NULL DEFAULT 'USER',
  PRIMARY KEY (id),
  UNIQUE KEY id_user_UNIQUE (id),
  UNIQUE KEY username_UNIQUE (username)
);

--
-- Dumping data for table user
--

INSERT INTO user VALUES (4,'userenc','$2a$10$UPcekmipgW03RwBc486KFeaDqoXVfsEdBIcMHnzB1e6T1YUVB/90i','USER');
INSERT INTO user VALUES (6,'user3','$2a$10$tImNDyfcDQbsE0lck/czvuAk0f9/ozCvJC37up05vDwXHiVuABdAu','DEVELOPER');
INSERT INTO user VALUES (7,'user','$2a$10$z7.C2HNtC./6/eyLuZ3HgupWnt20xjD/8zRGjV76B7br9Hsb1gch.','DEVELOPER');
INSERT INTO user VALUES (8,'usertest','$2a$10$kLAup4vOrffMK8IPeSYjDO6SVGgdMt/3Eka9HTbmu1.KvxAR1.tUa','USER');
INSERT INTO user VALUES (9,'userkii','$2a$10$d2uo2z/E4JAh.Ffs3z/qiO/rFYkNJV1o3IeyejnfRSsVAgIllaAfW','USER');
INSERT INTO user VALUES (10,'userqw','$2a$10$i6Sbi4uIrji3urKa./JxYuMYV4ywVGv7kmhfh63CAPr1ZjHJpF/5C','DEVELOPER');
INSERT INTO user VALUES (11,'userr','$2a$10$ntuuGBcAV4jCnGxQU.lem.k5jwVjFgVcsK9N3uZGOWbkG4Ztyodw2','USER');
INSERT INTO user VALUES (12,'usert','$2a$10$JsF34C1KeRVBd7c.7V1DReA9GEMlglPnhrExFJmlreyUKbDCXZZNS','DEVELOPER');
INSERT INTO user VALUES (13,'userty','$2a$10$67OJMjk0A6u3lQulW6PlpOZL4YbL4DubrPW8lxGxwiip4ZFHNJusq','DEVELOPER');
INSERT INTO user VALUES (14,'usero','$2a$10$PPWzc5ib4W4Iptfq1ibY7.pnEylaCJiATyyW7BGdnlCv6NL9uT4Wq','USER');
INSERT INTO user VALUES (15,'sefs','$2a$10$dtZEpb3RA3Srjg2rXNYPv.panZemi6s2D0pdI/8XschLPnQf.MWiW','DEVELOPER');
INSERT INTO user VALUES (17,'uio','$2a$10$TDp3vSxRWZHmRCDjlOo9heo1NtbkPGSNicKA9JiIHPYo19CZkrNo6','DEVELOPER');
INSERT INTO user VALUES (18,'apr','$2a$10$g.tgFllBFcZY1K/R0t8rYO8ZubalSnhQr.2bxM.90rtf4JVXXXc7C','USER');

--
-- Table structure for table user_application
--

DROP TABLE IF EXISTS user_application;
CREATE TABLE user_application (
  user_id int(11) NOT NULL auto_increment,
  application_id int(11) NOT NULL,
  PRIMARY KEY (user_id,application_id),
  CONSTRAINT app_id FOREIGN KEY (application_id) REFERENCES application (id) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT user_id FOREIGN KEY (user_id) REFERENCES user (id) ON UPDATE NO ACTION
);

--
-- Dumping data for table user_application
--

INSERT INTO user_application VALUES (7,25);
INSERT INTO user_application VALUES (7,27);
INSERT INTO user_application VALUES (7,36);
INSERT INTO user_application VALUES (7,39);
INSERT INTO user_application VALUES (7,49);
INSERT INTO user_application VALUES (7,52);
INSERT INTO user_application VALUES (7,53);
INSERT INTO user_application VALUES (7,54);
INSERT INTO user_application VALUES (7,55);
INSERT INTO user_application VALUES (7,56);
INSERT INTO user_application VALUES (7,58);
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-11-07 12:28:40
