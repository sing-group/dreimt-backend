-- MySQL dump 10.13  Distrib 5.7.23, for Linux (x86_64)
--
-- Host: localhost    Database: dreimt
-- ------------------------------------------------------
-- Server version	5.7.23-0ubuntu0.18.04.1

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
-- Dumping data for table `article_metadata`
--

LOCK TABLES `article_metadata` WRITE;
/*!40000 ALTER TABLE `article_metadata` DISABLE KEYS */;
INSERT INTO `article_metadata` VALUES (1001,'The abstract of this paper: it can be extremely long','Fulano HM, Mengano IJ','A Random Paper'),(1002,'The abstract of this second paper: it can be extremely long','Fulano HM, Mengano IJ, Tomar V','Another Random Paper'),(1003,NULL,'Vamos X, Tomar Y','A Paper Without Abstract');
/*!40000 ALTER TABLE `article_metadata` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `drug`
--

LOCK TABLES `drug` WRITE;
/*!40000 ALTER TABLE `drug` DISABLE KEYS */;
INSERT INTO `drug` VALUES ('GDSC','IBR-0001','Ibuprofen'),('Lincs','BRD-4654','Aspirin'),('Lincs','TTT-2244','acetylsalicylic acid');
/*!40000 ALTER TABLE `drug` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `drug_signature_interaction`
--

LOCK TABLES `drug_signature_interaction` WRITE;
/*!40000 ALTER TABLE `drug_signature_interaction` DISABLE KEYS */;
INSERT INTO `drug_signature_interaction` VALUES (1,0.002,0.0031,-2.335,'Lincs','BRD-4654','Signature 1'),(2,0.0006,0.0041,-2.345,'Lincs','BRD-4654','Signature 2'),(3,0.001,0.0042,-3.345,'Lincs','TTT-2244','Signature 3'),(4,0.002,0.003,-1.345,'GDSC','IBR-0001','Signature 3'),(5,0.002,0.0031,-2.335,'Lincs','BRD-4654','Signature 1'),(6,0.0006,0.0041,-2.345,'Lincs','BRD-4654','Signature 2'),(7,0.001,0.0042,-3.345,'Lincs','TTT-2244','Signature 3'),(8,0.002,0.003,-1.345,'GDSC','IBR-0001','Signature 3');
/*!40000 ALTER TABLE `drug_signature_interaction` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `signature`
--

LOCK TABLES `signature` WRITE;
/*!40000 ALTER TABLE `signature` DISABLE KEYS */;
INSERT INTO `signature` VALUES ('Signature 1','Type 1','Type 2','Catarro','IN_VIVO','Homo sapiens','C7',1001),('Signature 2','Type 1','Type 3','Meningitis','IN_VIVO','Homo sapiens','C7',1001),('Signature 3','Type 1','Type 2','Ébola','EX_VIVO','Homo sapiens','C7',1002),('Signature 4','Type 2','Type 3','VIH','EX_VIVO','Homo sapiens','C7',1002),('Signature 5','Type 2','Type 4','Ébola','IN_VIVO','Homo sapiens','Selected',1003),('Signature 6','Type 1','Type 4','Titulitis','EX_VIVO','Mus musculus','Selected',1003);
/*!40000 ALTER TABLE `signature` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `signature_gene`
--

LOCK TABLES `signature_gene` WRITE;
/*!40000 ALTER TABLE `signature_gene` DISABLE KEYS */;
INSERT INTO `signature_gene` VALUES ('GeneA','Signature 1','UP'),('GeneA','Signature 2','UP'),('GeneB','Signature 1','UP'),('GeneB','Signature 2','UP'),('GeneC','Signature 1','DOWN'),('GeneC','Signature 2','UP'),('GeneD','Signature 1','DOWN'),('GeneD','Signature 2','DOWN'),('GeneE','Signature 2','DOWN');
/*!40000 ALTER TABLE `signature_gene` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES ('hlfernandez','b49f846960fcac513453cf712926d327','USER');
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

-- Dump completed on 2018-10-09 17:26:08
