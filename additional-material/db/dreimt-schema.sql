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
-- Table structure for table `cmap_result`
--

DROP TABLE IF EXISTS `cmap_result`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cmap_result` (
  `maxPvalue` double NOT NULL,
  `numPerm` int(11) NOT NULL,
  `id` char(36) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK4g4549cdbruc56dfa00bpc4t4` FOREIGN KEY (`id`) REFERENCES `work` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `cmap_result_drug_interactions`
--

DROP TABLE IF EXISTS `cmap_result_drug_interactions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cmap_result_drug_interactions` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `fdr` double DEFAULT NULL,
  `pValue` double DEFAULT NULL,
  `tes` double DEFAULT NULL,
  `cmapResultId` char(36) NOT NULL,
  `drugId` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKe8t7f8fwbquttecdrfy41vi7d` (`cmapResultId`,`drugId`),
  KEY `FK7s3cw6jwjf5lwv6kxbk5lu3vy` (`drugId`),
  CONSTRAINT `FK7s3cw6jwjf5lwv6kxbk5lu3vy` FOREIGN KEY (`drugId`) REFERENCES `drug` (`id`),
  CONSTRAINT `FK_cmap_result_cmap_result_drug_interactions` FOREIGN KEY (`cmapResultId`) REFERENCES `cmap_result` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `cmap_result_geneset`
--

DROP TABLE IF EXISTS `cmap_result_geneset`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cmap_result_geneset` (
  `id` char(36) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FKr3begj73oj3094nvwh4fyls2x` FOREIGN KEY (`id`) REFERENCES `cmap_result` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `cmap_result_geneset_genes`
--

DROP TABLE IF EXISTS `cmap_result_geneset_genes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cmap_result_geneset_genes` (
  `id` char(36) NOT NULL,
  `gene` varchar(255) NOT NULL,
  PRIMARY KEY (`id`,`gene`),
  KEY `FKltfo5kbf8c4975oi4ghd5of04` (`gene`),
  CONSTRAINT `FK1o4oy2by01v9hdkfxuf2mpwak` FOREIGN KEY (`id`) REFERENCES `cmap_result_geneset` (`id`),
  CONSTRAINT `FKltfo5kbf8c4975oi4ghd5of04` FOREIGN KEY (`gene`) REFERENCES `genes` (`gene`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `cmap_result_updown`
--

DROP TABLE IF EXISTS `cmap_result_updown`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cmap_result_updown` (
  `id` char(36) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FKh8hc4swhsq5nwd7p0x7mie9mr` FOREIGN KEY (`id`) REFERENCES `cmap_result` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `cmap_result_updown_genes_down`
--

DROP TABLE IF EXISTS `cmap_result_updown_genes_down`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cmap_result_updown_genes_down` (
  `id` char(36) NOT NULL,
  `gene` varchar(255) NOT NULL,
  PRIMARY KEY (`id`,`gene`),
  KEY `FK1hgsd09spkic8c7lebpp51ooy` (`gene`),
  CONSTRAINT `FK1hgsd09spkic8c7lebpp51ooy` FOREIGN KEY (`gene`) REFERENCES `genes` (`gene`),
  CONSTRAINT `FK2fs0vpayqwe4stwikebhsydrx` FOREIGN KEY (`id`) REFERENCES `cmap_result_updown` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `cmap_result_updown_genes_up`
--

DROP TABLE IF EXISTS `cmap_result_updown_genes_up`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cmap_result_updown_genes_up` (
  `id` char(36) NOT NULL,
  `gene` varchar(255) NOT NULL,
  PRIMARY KEY (`id`,`gene`),
  KEY `FK80eacrqmor0sv03o687d5e3mx` (`gene`),
  CONSTRAINT `FK80eacrqmor0sv03o687d5e3mx` FOREIGN KEY (`gene`) REFERENCES `genes` (`gene`),
  CONSTRAINT `FKn5v342ka37lsiaiuk6m2jl660` FOREIGN KEY (`id`) REFERENCES `cmap_result_updown` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `drug`
--

DROP TABLE IF EXISTS `drug`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `drug` (
  `id` int(11) NOT NULL,
  `commonName` varchar(255) DEFAULT NULL,
  `sourceDb` varchar(255) DEFAULT NULL,
  `sourceName` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
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
  `fdr` double DEFAULT NULL,
  `pValue` double DEFAULT NULL,
  `tes` double DEFAULT NULL,
  `drugId` int(11) DEFAULT NULL,
  `signature` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKqv24iupckh17q8jhr5hnkaemu` (`drugId`),
  KEY `FKnsnnq5179w6clhenc6o6h8oov` (`signature`),
  CONSTRAINT `FKnsnnq5179w6clhenc6o6h8oov` FOREIGN KEY (`signature`) REFERENCES `signature` (`signatureName`),
  CONSTRAINT `FKqv24iupckh17q8jhr5hnkaemu` FOREIGN KEY (`drugId`) REFERENCES `drug` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
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
-- Table structure for table `jaccard_result`
--

DROP TABLE IF EXISTS `jaccard_result`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jaccard_result` (
  `id` char(36) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK3vfegxa9spa1bjuc4jpsppm7y` FOREIGN KEY (`id`) REFERENCES `work` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `jaccard_result_gene_overlap`
--

DROP TABLE IF EXISTS `jaccard_result_gene_overlap`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jaccard_result_gene_overlap` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `fdr` double DEFAULT NULL,
  `jaccard` double DEFAULT NULL,
  `pValue` double DEFAULT NULL,
  `sourceComparisonType` varchar(255) DEFAULT NULL,
  `targetComparisonType` varchar(255) DEFAULT NULL,
  `jaccardResultId` char(36) NOT NULL,
  `targetSignature` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKmp57c7n17poxtq4gjrftsi28r` (`jaccardResultId`,`targetSignature`,`targetComparisonType`,`sourceComparisonType`),
  KEY `FK9v13mymrqag29wybo2seqqha` (`targetSignature`),
  CONSTRAINT `FK9v13mymrqag29wybo2seqqha` FOREIGN KEY (`targetSignature`) REFERENCES `signature` (`signatureName`),
  CONSTRAINT `FK_jaccard_result_jaccard_result_gene_overlap` FOREIGN KEY (`jaccardResultId`) REFERENCES `jaccard_result` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `jaccard_result_geneset`
--

DROP TABLE IF EXISTS `jaccard_result_geneset`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jaccard_result_geneset` (
  `id` char(36) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK53ivtawgjjl8y4fckfpx3nqh7` FOREIGN KEY (`id`) REFERENCES `jaccard_result` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `jaccard_result_geneset_genes`
--

DROP TABLE IF EXISTS `jaccard_result_geneset_genes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jaccard_result_geneset_genes` (
  `id` char(36) NOT NULL,
  `gene` varchar(255) NOT NULL,
  PRIMARY KEY (`id`,`gene`),
  KEY `FKfgyc1oowp7gum79vwp260vji3` (`gene`),
  CONSTRAINT `FKfgyc1oowp7gum79vwp260vji3` FOREIGN KEY (`gene`) REFERENCES `genes` (`gene`),
  CONSTRAINT `FKjer3ebsimnsgrgy0kh6w6lopp` FOREIGN KEY (`id`) REFERENCES `jaccard_result_geneset` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `jaccard_result_updown`
--

DROP TABLE IF EXISTS `jaccard_result_updown`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jaccard_result_updown` (
  `id` char(36) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK2qw1852kipvjllj1971khxfp6` FOREIGN KEY (`id`) REFERENCES `jaccard_result` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `jaccard_result_updown_genes_down`
--

DROP TABLE IF EXISTS `jaccard_result_updown_genes_down`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jaccard_result_updown_genes_down` (
  `id` char(36) NOT NULL,
  `gene` varchar(255) NOT NULL,
  PRIMARY KEY (`id`,`gene`),
  KEY `FKoayt235ixruva0lg854ruentf` (`gene`),
  CONSTRAINT `FKbdlbeqcqqp7g7b2mje31y5hxa` FOREIGN KEY (`id`) REFERENCES `jaccard_result_updown` (`id`),
  CONSTRAINT `FKoayt235ixruva0lg854ruentf` FOREIGN KEY (`gene`) REFERENCES `genes` (`gene`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `jaccard_result_updown_genes_up`
--

DROP TABLE IF EXISTS `jaccard_result_updown_genes_up`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jaccard_result_updown_genes_up` (
  `id` char(36) NOT NULL,
  `gene` varchar(255) NOT NULL,
  PRIMARY KEY (`id`,`gene`),
  KEY `FKt6ni80wrxgncluma3qeuswudk` (`gene`),
  CONSTRAINT `FK3ersliln9hql4ucng052bkcue` FOREIGN KEY (`id`) REFERENCES `jaccard_result_updown` (`id`),
  CONSTRAINT `FKt6ni80wrxgncluma3qeuswudk` FOREIGN KEY (`gene`) REFERENCES `genes` (`gene`)
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `work`
--

DROP TABLE IF EXISTS `work`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `work` (
  `resultType` varchar(31) NOT NULL,
  `id` char(36) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `resultReference` varchar(1023) DEFAULT NULL,
  `creationDateTime` datetime NOT NULL,
  `failureCause` varchar(255) DEFAULT NULL,
  `finishingDateTime` datetime DEFAULT NULL,
  `schedulingDateTime` datetime DEFAULT NULL,
  `startDateTime` datetime DEFAULT NULL,
  `status` varchar(9) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `work_step`
--

DROP TABLE IF EXISTS `work_step`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `work_step` (
  `stepOrder` int(11) NOT NULL,
  `workId` varchar(255) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `progress` double DEFAULT NULL,
  PRIMARY KEY (`stepOrder`,`workId`),
  KEY `FK_work_work_step` (`workId`),
  CONSTRAINT `FK_work_work_step` FOREIGN KEY (`workId`) REFERENCES `work` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-11-25 15:34:49
