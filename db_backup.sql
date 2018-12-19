-- MySQL dump 10.13  Distrib 8.0.13, for Win64 (x86_64)
--
-- Host: localhost    Database: RPS
 SET NAMES utf8;

DROP TABLE IF EXISTS `stats`;
 SET character_set_client = utf8 ;
CREATE TABLE `stats` (
  `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
  `userID` int(6) unsigned NOT NULL,
  `wins` int(15) DEFAULT NULL,
  `losses` int(15) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `userID` (`userID`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

DROP TABLE IF EXISTS `user`;
 SET character_set_client = utf8 ;
CREATE TABLE `user` (
  `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(30) NOT NULL,
  `password` varchar(30) NOT NULL,
  `reg_date` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

-- Dump completed on 2018-12-19 10:26:10
