-- MySQL dump 10.13  Distrib 5.7.24, for Linux (x86_64)
--
-- Host: localhost    Database: dreimt
-- ------------------------------------------------------
-- Server version	5.7.24-0ubuntu0.18.04.1

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
-- Table structure for table `article_metadata`
--

DROP TABLE IF EXISTS `article_metadata`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `article_metadata` (
  `pubmedId` int(11) NOT NULL,
  `articleAbstract` varchar(10000) DEFAULT NULL,
  `authors` varchar(2000) NOT NULL,
  `title` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`pubmedId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `drug`
--

DROP TABLE IF EXISTS `drug`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `drug` (
  `sourceDb` varchar(255) NOT NULL,
  `sourceName` varchar(255) NOT NULL,
  `commonName` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`sourceDb`,`sourceName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `drug_signature_interaction`
--

DROP TABLE IF EXISTS `drug_signature_interaction`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `drug_signature_interaction` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `fdr` double NOT NULL,
  `pValue` double NOT NULL,
  `tes` double NOT NULL,
  `drug_sourceDb` varchar(255) DEFAULT NULL,
  `drug_sourceName` varchar(255) DEFAULT NULL,
  `signature` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK7nvqgcvx6si97ktc4c0jq6ejk` (`drug_sourceDb`,`drug_sourceName`),
  KEY `FKnsnnq5179w6clhenc6o6h8oov` (`signature`),
  CONSTRAINT `FK7nvqgcvx6si97ktc4c0jq6ejk` FOREIGN KEY (`drug_sourceDb`, `drug_sourceName`) REFERENCES `drug` (`sourceDb`, `sourceName`),
  CONSTRAINT `FKnsnnq5179w6clhenc6o6h8oov` FOREIGN KEY (`signature`) REFERENCES `signature` (`signatureName`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `genes`
--

DROP TABLE IF EXISTS `genes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `genes` (
  `gene` varchar(255) NOT NULL,
  `universe` varchar(255) NOT NULL,
  PRIMARY KEY (`gene`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `signature`
--

DROP TABLE IF EXISTS `signature`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `signature` (
  `signatureType` varchar(31) NOT NULL,
  `signatureName` varchar(255) NOT NULL,
  `disease` varchar(255) DEFAULT NULL,
  `experimentalDesign` varchar(255) DEFAULT NULL,
  `organism` varchar(255) DEFAULT NULL,
  `sourceDb` varchar(255) DEFAULT NULL,
  `article_pubmedId` int(11) DEFAULT NULL,
  PRIMARY KEY (`signatureName`),
  KEY `FKob3uvde5er8oiayu91rowlcgm` (`article_pubmedId`),
  CONSTRAINT `FKob3uvde5er8oiayu91rowlcgm` FOREIGN KEY (`article_pubmedId`) REFERENCES `article_metadata` (`pubmedId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `signature_cell_type_a`
--

DROP TABLE IF EXISTS `signature_cell_type_a`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `signature_cell_type_a` (
  `signatureName` varchar(255) NOT NULL,
  `cellType` varchar(255) NOT NULL,
  PRIMARY KEY (`signatureName`,`cellType`),
  CONSTRAINT `FKav0ta8w7h6p10gfa7uddvfxl5` FOREIGN KEY (`signatureName`) REFERENCES `signature` (`signatureName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `signature_cell_type_b`
--

DROP TABLE IF EXISTS `signature_cell_type_b`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `signature_cell_type_b` (
  `signatureName` varchar(255) NOT NULL,
  `cellType` varchar(255) NOT NULL,
  PRIMARY KEY (`signatureName`,`cellType`),
  CONSTRAINT `FKd2cpam0rgflkr9yhsj7cwfihj` FOREIGN KEY (`signatureName`) REFERENCES `signature` (`signatureName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `signature_geneset_genes`
--

DROP TABLE IF EXISTS `signature_geneset_genes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `signature_geneset_genes` (
  `signature` varchar(255) NOT NULL,
  `gene` varchar(255) NOT NULL,
  PRIMARY KEY (`signature`,`gene`),
  KEY `FKrddj16bdtfbejiox2tvxvimif` (`gene`),
  CONSTRAINT `FKphu5u29cwlhygq5ihbasc7bsj` FOREIGN KEY (`signature`) REFERENCES `signature` (`signatureName`),
  CONSTRAINT `FKrddj16bdtfbejiox2tvxvimif` FOREIGN KEY (`gene`) REFERENCES `genes` (`gene`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `signature_updown_genes`
--

DROP TABLE IF EXISTS `signature_updown_genes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `signature_updown_genes` (
  `type` varchar(255) DEFAULT NULL,
  `signature` varchar(255) NOT NULL,
  `gene` varchar(255) NOT NULL,
  PRIMARY KEY (`signature`,`gene`),
  KEY `FK71w1mvo2s9n99b51fmy9jwkdh` (`gene`),
  CONSTRAINT `FK71w1mvo2s9n99b51fmy9jwkdh` FOREIGN KEY (`gene`) REFERENCES `genes` (`gene`),
  CONSTRAINT `FKeyhnrum6i9llm8m55ncrh3avi` FOREIGN KEY (`signature`) REFERENCES `signature` (`signatureName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `login` varchar(100) NOT NULL,
  `password` varchar(32) NOT NULL,
  `role` varchar(255) NOT NULL,
  PRIMARY KEY (`login`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-11-01 20:39:43
