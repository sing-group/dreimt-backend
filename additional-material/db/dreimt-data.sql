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
-- Dumping data for table `article_metadata`
--

LOCK TABLES `article_metadata` WRITE;
/*!40000 ALTER TABLE `article_metadata` DISABLE KEYS */;
INSERT INTO `article_metadata` VALUES (1001,'The abstract of this paper: it can be extremely long','Fulano HM, Mengano IJ','A Random Paper'),(1002,'The abstract of this second paper: it can be extremely long','Fulano HM, Mengano IJ, Tomar V','Another Random Paper'),(1003,NULL,'Vamos X, Tomar Y','A Paper Without Abstract');
/*!40000 ALTER TABLE `article_metadata` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `cmap_result`
--

LOCK TABLES `cmap_result` WRITE;
/*!40000 ALTER TABLE `cmap_result` DISABLE KEYS */;
/*!40000 ALTER TABLE `cmap_result` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `cmap_result_drug_interactions`
--

LOCK TABLES `cmap_result_drug_interactions` WRITE;
/*!40000 ALTER TABLE `cmap_result_drug_interactions` DISABLE KEYS */;
/*!40000 ALTER TABLE `cmap_result_drug_interactions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `cmap_result_geneset`
--

LOCK TABLES `cmap_result_geneset` WRITE;
/*!40000 ALTER TABLE `cmap_result_geneset` DISABLE KEYS */;
/*!40000 ALTER TABLE `cmap_result_geneset` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `cmap_result_geneset_genes`
--

LOCK TABLES `cmap_result_geneset_genes` WRITE;
/*!40000 ALTER TABLE `cmap_result_geneset_genes` DISABLE KEYS */;
/*!40000 ALTER TABLE `cmap_result_geneset_genes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `cmap_result_updown`
--

LOCK TABLES `cmap_result_updown` WRITE;
/*!40000 ALTER TABLE `cmap_result_updown` DISABLE KEYS */;
/*!40000 ALTER TABLE `cmap_result_updown` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `cmap_result_updown_genes_down`
--

LOCK TABLES `cmap_result_updown_genes_down` WRITE;
/*!40000 ALTER TABLE `cmap_result_updown_genes_down` DISABLE KEYS */;
/*!40000 ALTER TABLE `cmap_result_updown_genes_down` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `cmap_result_updown_genes_up`
--

LOCK TABLES `cmap_result_updown_genes_up` WRITE;
/*!40000 ALTER TABLE `cmap_result_updown_genes_up` DISABLE KEYS */;
/*!40000 ALTER TABLE `cmap_result_updown_genes_up` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `drug`
--

LOCK TABLES `drug` WRITE;
/*!40000 ALTER TABLE `drug` DISABLE KEYS */;
INSERT INTO `drug` VALUES (1,'Aspirin','Lincs','BRD-4654'),(2,'Ibuprofen','GDSC','IBR-0001'),(3,'acetylsalicylic acid','Lincs','TTT-2244'),(4,'Ebastel','GDSC','EB-306');
/*!40000 ALTER TABLE `drug` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `drug_signature_interaction`
--

LOCK TABLES `drug_signature_interaction` WRITE;
/*!40000 ALTER TABLE `drug_signature_interaction` DISABLE KEYS */;
INSERT INTO `drug_signature_interaction` VALUES (1,0.04,0.05,2,1,'Signature 1'),(2,0.04,0.05,1.9,1,'Signature 2'),(3,0.031,0.02,1,2,'Signature 3'),(4,0.011,0.01,0.5,2,'Signature 3'),(5,0.001,0.001,0.1,3,'Signature 6'),(6,0.001,0.001,-1,4,'Signature 6');
/*!40000 ALTER TABLE `drug_signature_interaction` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `genes`
--

LOCK TABLES `genes` WRITE;
/*!40000 ALTER TABLE `genes` DISABLE KEYS */;
INSERT INTO `genes` VALUES ('A','T'),('B','T'),('C','T'),('D','T'),('E','T'),('F','T'),('G','T'),('H','T'),('I','T'),('J','T'),('K','T'),('L','T'),('M','T'),('N','T'),('O','T'),('P','T'),('Q','F'),('R','F'),('S','F'),('T','F');
/*!40000 ALTER TABLE `genes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `jaccard_result`
--

LOCK TABLES `jaccard_result` WRITE;
/*!40000 ALTER TABLE `jaccard_result` DISABLE KEYS */;
/*!40000 ALTER TABLE `jaccard_result` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `jaccard_result_gene_overlap`
--

LOCK TABLES `jaccard_result_gene_overlap` WRITE;
/*!40000 ALTER TABLE `jaccard_result_gene_overlap` DISABLE KEYS */;
/*!40000 ALTER TABLE `jaccard_result_gene_overlap` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `jaccard_result_geneset`
--

LOCK TABLES `jaccard_result_geneset` WRITE;
/*!40000 ALTER TABLE `jaccard_result_geneset` DISABLE KEYS */;
/*!40000 ALTER TABLE `jaccard_result_geneset` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `jaccard_result_geneset_genes`
--

LOCK TABLES `jaccard_result_geneset_genes` WRITE;
/*!40000 ALTER TABLE `jaccard_result_geneset_genes` DISABLE KEYS */;
/*!40000 ALTER TABLE `jaccard_result_geneset_genes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `jaccard_result_updown`
--

LOCK TABLES `jaccard_result_updown` WRITE;
/*!40000 ALTER TABLE `jaccard_result_updown` DISABLE KEYS */;
/*!40000 ALTER TABLE `jaccard_result_updown` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `jaccard_result_updown_genes_down`
--

LOCK TABLES `jaccard_result_updown_genes_down` WRITE;
/*!40000 ALTER TABLE `jaccard_result_updown_genes_down` DISABLE KEYS */;
/*!40000 ALTER TABLE `jaccard_result_updown_genes_down` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `jaccard_result_updown_genes_up`
--

LOCK TABLES `jaccard_result_updown_genes_up` WRITE;
/*!40000 ALTER TABLE `jaccard_result_updown_genes_up` DISABLE KEYS */;
/*!40000 ALTER TABLE `jaccard_result_updown_genes_up` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `signature`
--

LOCK TABLES `signature` WRITE;
/*!40000 ALTER TABLE `signature` DISABLE KEYS */;
INSERT INTO `signature` VALUES ('UPDOWN','Signature 1','Catarro','IN_VIVO','Homo sapiens','C7',1001),('GENESET','Signature 2','Ébola','EX_VIVO','Homo sapiens','C7',1002),('UPDOWN','Signature 3','Meningitis','IN_VIVO','Homo sapiens','C7',1001),('GENESET','Signature 4','VIH','EX_VIVO','Homo sapiens','C7',1002),('GENESET','Signature 5','Ébola','IN_VIVO','Homo sapiens','Selected',1003),('UPDOWN','Signature 6','Titulitis','EX_VIVO','Mus musculus','Selected',1003);
/*!40000 ALTER TABLE `signature` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `signature_cell_type_a`
--

LOCK TABLES `signature_cell_type_a` WRITE;
/*!40000 ALTER TABLE `signature_cell_type_a` DISABLE KEYS */;
INSERT INTO `signature_cell_type_a` VALUES ('Signature 1','Type_1A'),('Signature 1','Type_1B'),('Signature 2','Type_1B'),('Signature 3','Type_2A'),('Signature 4','Type_2B'),('Signature 5','Type_2A'),('Signature 5','Type_2B'),('Signature 6','Type_3');
/*!40000 ALTER TABLE `signature_cell_type_a` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `signature_cell_type_b`
--

LOCK TABLES `signature_cell_type_b` WRITE;
/*!40000 ALTER TABLE `signature_cell_type_b` DISABLE KEYS */;
INSERT INTO `signature_cell_type_b` VALUES ('Signature 1','Type_2A'),('Signature 2','Type_2B'),('Signature 3','Type_4A'),('Signature 4','Type_4B'),('Signature 5','Type_5B'),('Signature 6','Type_1');
/*!40000 ALTER TABLE `signature_cell_type_b` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `signature_geneset_genes`
--

LOCK TABLES `signature_geneset_genes` WRITE;
/*!40000 ALTER TABLE `signature_geneset_genes` DISABLE KEYS */;
INSERT INTO `signature_geneset_genes` VALUES ('Signature 2','A'),('Signature 2','B'),('Signature 2','C'),('Signature 2','D'),('Signature 2','E'),('Signature 2','F'),('Signature 2','G'),('Signature 2','H'),('Signature 2','I'),('Signature 2','J'),('Signature 4','K'),('Signature 4','L'),('Signature 4','M'),('Signature 4','N'),('Signature 4','O'),('Signature 4','P'),('Signature 4','Q');
/*!40000 ALTER TABLE `signature_geneset_genes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `signature_updown_genes`
--

LOCK TABLES `signature_updown_genes` WRITE;
/*!40000 ALTER TABLE `signature_updown_genes` DISABLE KEYS */;
INSERT INTO `signature_updown_genes` VALUES ('UP','Signature 1','A'),('UP','Signature 1','B'),('UP','Signature 1','C'),('UP','Signature 1','D'),('DOWN','Signature 1','E'),('DOWN','Signature 1','F'),('DOWN','Signature 1','G'),('DOWN','Signature 1','H');
/*!40000 ALTER TABLE `signature_updown_genes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES ('hlfernandez','b49f846960fcac513453cf712926d327','USER');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `work`
--

LOCK TABLES `work` WRITE;
/*!40000 ALTER TABLE `work` DISABLE KEYS */;
/*!40000 ALTER TABLE `work` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `work_step`
--

LOCK TABLES `work_step` WRITE;
/*!40000 ALTER TABLE `work_step` DISABLE KEYS */;
/*!40000 ALTER TABLE `work_step` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-11-25 15:34:49
