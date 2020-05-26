-- MySQL dump 10.13  Distrib 5.7.30, for Linux (x86_64)
--
-- Host: localhost    Database: dreimt
-- ------------------------------------------------------
-- Server version	5.7.30-0ubuntu0.18.04.1

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
  `articleAbstract` varchar(10000) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `authors` varchar(2000) COLLATE utf8mb4_unicode_ci NOT NULL,
  `title` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`pubmedId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `cmap_result`
--

DROP TABLE IF EXISTS `cmap_result`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cmap_result` (
  `caseType` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `numPerm` int(11) NOT NULL,
  `referenceType` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `id` char(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK4g4549cdbruc56dfa00bpc4t4` FOREIGN KEY (`id`) REFERENCES `work` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `cmap_result_geneset`
--

DROP TABLE IF EXISTS `cmap_result_geneset`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cmap_result_geneset` (
  `geneSetType` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `id` char(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FKr3begj73oj3094nvwh4fyls2x` FOREIGN KEY (`id`) REFERENCES `cmap_result` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `cmap_result_geneset_drug_interactions`
--

DROP TABLE IF EXISTS `cmap_result_geneset_drug_interactions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cmap_result_geneset_drug_interactions` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `fdr` double NOT NULL,
  `tau` double NOT NULL,
  `cmapResultId` char(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `drugId` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK86pu1bk7qsvr1gwbpq5h5jqxe` (`cmapResultId`,`drugId`),
  KEY `FKq0xksuxwxxbt1fc62wr9m3luw` (`drugId`),
  CONSTRAINT `FK_cmap_result_cmap_result_geneset_drug_interactions` FOREIGN KEY (`cmapResultId`) REFERENCES `cmap_result` (`id`),
  CONSTRAINT `FKq0xksuxwxxbt1fc62wr9m3luw` FOREIGN KEY (`drugId`) REFERENCES `drug` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `cmap_result_geneset_genes`
--

DROP TABLE IF EXISTS `cmap_result_geneset_genes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cmap_result_geneset_genes` (
  `id` char(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `gene` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`,`gene`),
  KEY `FKltfo5kbf8c4975oi4ghd5of04` (`gene`),
  CONSTRAINT `FK1o4oy2by01v9hdkfxuf2mpwak` FOREIGN KEY (`id`) REFERENCES `cmap_result_geneset` (`id`),
  CONSTRAINT `FKltfo5kbf8c4975oi4ghd5of04` FOREIGN KEY (`gene`) REFERENCES `genes` (`gene`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `cmap_result_updown`
--

DROP TABLE IF EXISTS `cmap_result_updown`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cmap_result_updown` (
  `id` char(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FKh8hc4swhsq5nwd7p0x7mie9mr` FOREIGN KEY (`id`) REFERENCES `cmap_result` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `cmap_result_updown_drug_interactions`
--

DROP TABLE IF EXISTS `cmap_result_updown_drug_interactions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cmap_result_updown_drug_interactions` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `downFdr` double NOT NULL,
  `tau` double NOT NULL,
  `upFdr` double NOT NULL,
  `cmapResultId` char(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `drugId` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKra602d31ub2gyfnlh93cno1ln` (`cmapResultId`,`drugId`),
  KEY `FK4ei64etv5i0bhxc3xrw4eo95v` (`drugId`),
  CONSTRAINT `FK4ei64etv5i0bhxc3xrw4eo95v` FOREIGN KEY (`drugId`) REFERENCES `drug` (`id`),
  CONSTRAINT `FK_cmap_result_cmap_result_updown_drug_interactions` FOREIGN KEY (`cmapResultId`) REFERENCES `cmap_result` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `cmap_result_updown_genes_down`
--

DROP TABLE IF EXISTS `cmap_result_updown_genes_down`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cmap_result_updown_genes_down` (
  `id` char(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `gene` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`,`gene`),
  KEY `FK1hgsd09spkic8c7lebpp51ooy` (`gene`),
  CONSTRAINT `FK1hgsd09spkic8c7lebpp51ooy` FOREIGN KEY (`gene`) REFERENCES `genes` (`gene`),
  CONSTRAINT `FK2fs0vpayqwe4stwikebhsydrx` FOREIGN KEY (`id`) REFERENCES `cmap_result_updown` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `cmap_result_updown_genes_up`
--

DROP TABLE IF EXISTS `cmap_result_updown_genes_up`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cmap_result_updown_genes_up` (
  `id` char(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `gene` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`,`gene`),
  KEY `FK80eacrqmor0sv03o687d5e3mx` (`gene`),
  CONSTRAINT `FK80eacrqmor0sv03o687d5e3mx` FOREIGN KEY (`gene`) REFERENCES `genes` (`gene`),
  CONSTRAINT `FKn5v342ka37lsiaiuk6m2jl660` FOREIGN KEY (`id`) REFERENCES `cmap_result_updown` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `database_versions`
--

DROP TABLE IF EXISTS `database_versions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `database_versions` (
  `version` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `current` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`version`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `dreimt_information`
--

DROP TABLE IF EXISTS `dreimt_information`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dreimt_information` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `tauThreshold` double NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `drug`
--

DROP TABLE IF EXISTS `drug`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `drug` (
  `id` int(11) NOT NULL,
  `commonName` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `dss` double DEFAULT NULL,
  `sourceDb` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `sourceName` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `drug_moa`
--

DROP TABLE IF EXISTS `drug_moa`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `drug_moa` (
  `id` int(11) NOT NULL,
  `moa` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  UNIQUE KEY `UKaqnj5i6li679pqcyyvvh3ku4l` (`id`,`moa`),
  CONSTRAINT `FKdmc4tbc6rifac45vq4pman8ln` FOREIGN KEY (`id`) REFERENCES `drug` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `drug_signature_interaction`
--

DROP TABLE IF EXISTS `drug_signature_interaction`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `drug_signature_interaction` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cellTypeAEffect` int(11) DEFAULT NULL,
  `cellTypeBEffect` int(11) DEFAULT NULL,
  `downFdr` double DEFAULT NULL,
  `interactionType` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `tau` double NOT NULL,
  `upFdr` double DEFAULT NULL,
  `drugId` int(11) DEFAULT NULL,
  `signature` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  KEY `IDXd7ua29ve0t7njdgk9grfjgco8` (`tau`),
  KEY `IDXm0dqgruedycvnd9qmgd0uofv` (`upFdr`),
  KEY `IDX1yof1axgl78po6ybou471t469` (`downFdr`),
  KEY `FKqv24iupckh17q8jhr5hnkaemu` (`drugId`),
  KEY `FKnsnnq5179w6clhenc6o6h8oov` (`signature`),
  CONSTRAINT `FKnsnnq5179w6clhenc6o6h8oov` FOREIGN KEY (`signature`) REFERENCES `signature` (`signatureName`),
  CONSTRAINT `FKqv24iupckh17q8jhr5hnkaemu` FOREIGN KEY (`drugId`) REFERENCES `drug` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `drug_target_genes`
--

DROP TABLE IF EXISTS `drug_target_genes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `drug_target_genes` (
  `id` int(11) NOT NULL,
  `gene` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`,`gene`),
  KEY `FKh74f9x833fssvhb68d0tsho5i` (`gene`),
  CONSTRAINT `FKh74f9x833fssvhb68d0tsho5i` FOREIGN KEY (`gene`) REFERENCES `genes` (`gene`),
  CONSTRAINT `FKo1a2ki6iv2kn5dtf2gi9gmiqk` FOREIGN KEY (`id`) REFERENCES `drug` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `full_drug_signature_interaction`
--

DROP TABLE IF EXISTS `full_drug_signature_interaction`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `full_drug_signature_interaction` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cellTypeAEffect` int(11) DEFAULT NULL,
  `cellTypeBEffect` int(11) DEFAULT NULL,
  `downFdr` double DEFAULT NULL,
  `drugCommonName` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `drugDss` double DEFAULT NULL,
  `drugMoa` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `drugSourceDb` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `drugSourceName` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `drugStatus` int(11) DEFAULT NULL,
  `drugTargetGenes` varchar(450) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `interactionType` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `signatureCellSubTypeA` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `signatureCellSubTypeB` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `signatureCellTypeA` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `signatureCellTypeB` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `signatureDisease` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `signatureDiseaseA` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `signatureDiseaseB` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `signatureExperimentalDesign` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `signatureLocalisationA` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `signatureLocalisationB` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `signatureName` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `signatureOrganism` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `signatureSourceDb` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `signatureSourceDbUrl` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `signatureStateA` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `signatureStateB` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `signatureTreatmentA` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `signatureTreatmentB` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `signatureType` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `tau` double NOT NULL,
  `upFdr` double DEFAULT NULL,
  `signatureArticlePubmedId` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `IDX4hkkniqx8jx0t74qcpqco6dit` (`tau`),
  KEY `IDXl2qoayrbw9v7uacqregtqpc3q` (`upFdr`),
  KEY `IDXbc2mh9iwhk7eb2erlf57k1w8w` (`downFdr`),
  KEY `IDXjeyb6tx2m8w82apv673e6x4gy` (`drugCommonName`),
  KEY `IDXkua6pu0gkbql9gbcy9dddg5is` (`drugSourceName`),
  KEY `IDX1508ma2bjjx77uahdqt6tqnp0` (`drugMoa`),
  KEY `IDXbrd94o0ah3jrh5erhp81lkv3m` (`drugDss`),
  KEY `IDXab8o6290f312y62e07v4y5r00` (`signatureName`),
  KEY `IDXc51wckwyvrmt1dny9fl4pnw9f` (`signatureType`),
  KEY `IDXffpxt44kp8ch04udpvr06l4f9` (`signatureExperimentalDesign`),
  KEY `IDXcfolo4fcjm3mteqv5cuae81c5` (`signatureOrganism`),
  KEY `IDXmhy6pg1dvekcc075ww20vsu20` (`signatureSourceDb`),
  KEY `IDXk9opwyikpvq3rae10815y3xau` (`signatureCellTypeA`),
  KEY `IDXghdkqt1s647y0d2prw8uwct37` (`signatureCellTypeB`),
  KEY `FKboll38t85j69hdb7uf9c5gvh4` (`signatureArticlePubmedId`),
  CONSTRAINT `FKboll38t85j69hdb7uf9c5gvh4` FOREIGN KEY (`signatureArticlePubmedId`) REFERENCES `article_metadata` (`pubmedId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `genes`
--

DROP TABLE IF EXISTS `genes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `genes` (
  `gene` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `universe` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`gene`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `jaccard_result`
--

DROP TABLE IF EXISTS `jaccard_result`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jaccard_result` (
  `cellSubType1` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `cellSubType2` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `cellType1` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `cellType2` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `cellTypeOrSubType1` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `cellTypeOrSubType2` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `disease` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `experimentalDesign` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `onlyUniverseGenes` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `organism` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `signatureSourceDb` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `id` char(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK3vfegxa9spa1bjuc4jpsppm7y` FOREIGN KEY (`id`) REFERENCES `work` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
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
  `sourceComparisonType` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `targetComparisonType` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `jaccardResultId` char(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `targetSignature` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_jaccard_result_jaccard_result_gene_overlap` (`jaccardResultId`),
  KEY `FK9v13mymrqag29wybo2seqqha` (`targetSignature`),
  CONSTRAINT `FK9v13mymrqag29wybo2seqqha` FOREIGN KEY (`targetSignature`) REFERENCES `signature` (`signatureName`),
  CONSTRAINT `FK_jaccard_result_jaccard_result_gene_overlap` FOREIGN KEY (`jaccardResultId`) REFERENCES `jaccard_result` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `jaccard_result_geneset`
--

DROP TABLE IF EXISTS `jaccard_result_geneset`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jaccard_result_geneset` (
  `geneSetType` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `id` char(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK53ivtawgjjl8y4fckfpx3nqh7` FOREIGN KEY (`id`) REFERENCES `jaccard_result` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `jaccard_result_geneset_genes`
--

DROP TABLE IF EXISTS `jaccard_result_geneset_genes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jaccard_result_geneset_genes` (
  `id` char(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `gene` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`,`gene`),
  KEY `FKfgyc1oowp7gum79vwp260vji3` (`gene`),
  CONSTRAINT `FKfgyc1oowp7gum79vwp260vji3` FOREIGN KEY (`gene`) REFERENCES `genes` (`gene`),
  CONSTRAINT `FKjer3ebsimnsgrgy0kh6w6lopp` FOREIGN KEY (`id`) REFERENCES `jaccard_result_geneset` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `jaccard_result_updown`
--

DROP TABLE IF EXISTS `jaccard_result_updown`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jaccard_result_updown` (
  `id` char(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK2qw1852kipvjllj1971khxfp6` FOREIGN KEY (`id`) REFERENCES `jaccard_result` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `jaccard_result_updown_genes_down`
--

DROP TABLE IF EXISTS `jaccard_result_updown_genes_down`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jaccard_result_updown_genes_down` (
  `id` char(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `gene` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`,`gene`),
  KEY `FKoayt235ixruva0lg854ruentf` (`gene`),
  CONSTRAINT `FKbdlbeqcqqp7g7b2mje31y5hxa` FOREIGN KEY (`id`) REFERENCES `jaccard_result_updown` (`id`),
  CONSTRAINT `FKoayt235ixruva0lg854ruentf` FOREIGN KEY (`gene`) REFERENCES `genes` (`gene`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `jaccard_result_updown_genes_up`
--

DROP TABLE IF EXISTS `jaccard_result_updown_genes_up`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jaccard_result_updown_genes_up` (
  `id` char(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `gene` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`,`gene`),
  KEY `FKt6ni80wrxgncluma3qeuswudk` (`gene`),
  CONSTRAINT `FK3ersliln9hql4ucng052bkcue` FOREIGN KEY (`id`) REFERENCES `jaccard_result_updown` (`id`),
  CONSTRAINT `FKt6ni80wrxgncluma3qeuswudk` FOREIGN KEY (`gene`) REFERENCES `genes` (`gene`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `precalculated_example`
--

DROP TABLE IF EXISTS `precalculated_example`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `precalculated_example` (
  `resultType` varchar(31) COLLATE utf8mb4_unicode_ci NOT NULL,
  `work` char(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `reference` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `url` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`work`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `precalculated_example_cmap`
--

DROP TABLE IF EXISTS `precalculated_example_cmap`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `precalculated_example_cmap` (
  `work` char(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`work`),
  CONSTRAINT `FKny0lp9b7k9uh7kes939q97nsa` FOREIGN KEY (`work`) REFERENCES `precalculated_example` (`work`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `precalculated_example_cmap_geneset`
--

DROP TABLE IF EXISTS `precalculated_example_cmap_geneset`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `precalculated_example_cmap_geneset` (
  `work` char(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`work`),
  CONSTRAINT `FK4ni99igxnjyg6ic71n3qu6age` FOREIGN KEY (`work`) REFERENCES `precalculated_example_cmap` (`work`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `precalculated_example_cmap_updown`
--

DROP TABLE IF EXISTS `precalculated_example_cmap_updown`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `precalculated_example_cmap_updown` (
  `work` char(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`work`),
  CONSTRAINT `FKdlqn7c6jcxsmm62osft9259ni` FOREIGN KEY (`work`) REFERENCES `precalculated_example_cmap` (`work`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `precalculated_example_jaccard`
--

DROP TABLE IF EXISTS `precalculated_example_jaccard`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `precalculated_example_jaccard` (
  `work` char(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`work`),
  CONSTRAINT `FK7bfnnquu5tq1nph6jckfliats` FOREIGN KEY (`work`) REFERENCES `precalculated_example` (`work`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `precalculated_example_jaccard_geneset`
--

DROP TABLE IF EXISTS `precalculated_example_jaccard_geneset`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `precalculated_example_jaccard_geneset` (
  `work` char(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`work`),
  CONSTRAINT `FKtbs89pqf0ghlsyg6de9l11sp7` FOREIGN KEY (`work`) REFERENCES `precalculated_example_jaccard` (`work`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `precalculated_example_jaccard_updown`
--

DROP TABLE IF EXISTS `precalculated_example_jaccard_updown`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `precalculated_example_jaccard_updown` (
  `work` char(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`work`),
  CONSTRAINT `FK20qmw6b5j0g1ur6n1l3ngsa4o` FOREIGN KEY (`work`) REFERENCES `precalculated_example_jaccard` (`work`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `signature`
--

DROP TABLE IF EXISTS `signature`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `signature` (
  `signatureType` varchar(31) COLLATE utf8mb4_unicode_ci NOT NULL,
  `signatureName` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `experimentalDesign` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `localisationA` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `localisationB` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `organism` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `sourceDb` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `sourceDbUrl` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `stateA` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `stateB` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `article_pubmedId` int(11) DEFAULT NULL,
  PRIMARY KEY (`signatureName`),
  KEY `FKob3uvde5er8oiayu91rowlcgm` (`article_pubmedId`),
  CONSTRAINT `FKob3uvde5er8oiayu91rowlcgm` FOREIGN KEY (`article_pubmedId`) REFERENCES `article_metadata` (`pubmedId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `signature_cell_subtype_a`
--

DROP TABLE IF EXISTS `signature_cell_subtype_a`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `signature_cell_subtype_a` (
  `signatureName` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `cellSubType` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`signatureName`,`cellSubType`),
  CONSTRAINT `FKdw9fhtdt3vj45vjc453jl7ovh` FOREIGN KEY (`signatureName`) REFERENCES `signature` (`signatureName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `signature_cell_subtype_b`
--

DROP TABLE IF EXISTS `signature_cell_subtype_b`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `signature_cell_subtype_b` (
  `signatureName` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `cellSubType` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`signatureName`,`cellSubType`),
  CONSTRAINT `FKm67vnhg2rbgdctv8gh6055y8l` FOREIGN KEY (`signatureName`) REFERENCES `signature` (`signatureName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `signature_cell_type_a`
--

DROP TABLE IF EXISTS `signature_cell_type_a`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `signature_cell_type_a` (
  `signatureName` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `cellType` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`signatureName`,`cellType`),
  CONSTRAINT `FKav0ta8w7h6p10gfa7uddvfxl5` FOREIGN KEY (`signatureName`) REFERENCES `signature` (`signatureName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `signature_cell_type_b`
--

DROP TABLE IF EXISTS `signature_cell_type_b`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `signature_cell_type_b` (
  `signatureName` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `cellType` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`signatureName`,`cellType`),
  CONSTRAINT `FKd2cpam0rgflkr9yhsj7cwfihj` FOREIGN KEY (`signatureName`) REFERENCES `signature` (`signatureName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `signature_disease`
--

DROP TABLE IF EXISTS `signature_disease`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `signature_disease` (
  `signatureName` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `disease` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  UNIQUE KEY `UKmeecl07ojxsu7h7abkomqrojg` (`signatureName`,`disease`),
  CONSTRAINT `FK2qk99ln9qpe1ypmv8uvrd1lvu` FOREIGN KEY (`signatureName`) REFERENCES `signature` (`signatureName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `signature_disease_a`
--

DROP TABLE IF EXISTS `signature_disease_a`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `signature_disease_a` (
  `signatureName` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `diseaseA` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  UNIQUE KEY `UK5l7cxcamgachdlhftt5w8up4c` (`signatureName`,`diseaseA`),
  CONSTRAINT `FK9kg6rjl4mqi5jvuhq2xeo8727` FOREIGN KEY (`signatureName`) REFERENCES `signature` (`signatureName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `signature_disease_b`
--

DROP TABLE IF EXISTS `signature_disease_b`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `signature_disease_b` (
  `signatureName` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `diseaseB` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  UNIQUE KEY `UKs688b13os4hgm5xyuf8qe4u7k` (`signatureName`,`diseaseB`),
  CONSTRAINT `FKkqqdynv8wv5wdvh1cg65dracp` FOREIGN KEY (`signatureName`) REFERENCES `signature` (`signatureName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `signature_geneset_genes`
--

DROP TABLE IF EXISTS `signature_geneset_genes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `signature_geneset_genes` (
  `signature` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `gene` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`signature`,`gene`),
  KEY `FKrddj16bdtfbejiox2tvxvimif` (`gene`),
  CONSTRAINT `FKphu5u29cwlhygq5ihbasc7bsj` FOREIGN KEY (`signature`) REFERENCES `signature` (`signatureName`),
  CONSTRAINT `FKrddj16bdtfbejiox2tvxvimif` FOREIGN KEY (`gene`) REFERENCES `genes` (`gene`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `signature_treatment_a`
--

DROP TABLE IF EXISTS `signature_treatment_a`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `signature_treatment_a` (
  `signatureName` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `treatmentA` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  UNIQUE KEY `UK9re42ueptpwx3pvwhoin2yifj` (`signatureName`,`treatmentA`),
  CONSTRAINT `FK7lrm33u484vu6bmyp2fhf4nyb` FOREIGN KEY (`signatureName`) REFERENCES `signature` (`signatureName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `signature_treatment_b`
--

DROP TABLE IF EXISTS `signature_treatment_b`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `signature_treatment_b` (
  `signatureName` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `treatmentB` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  UNIQUE KEY `UKnq7bvll14xl4mqclcwbjp4llk` (`signatureName`,`treatmentB`),
  CONSTRAINT `FKdpc2sb6wbjo937ium3vbimv7v` FOREIGN KEY (`signatureName`) REFERENCES `signature` (`signatureName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `signature_updown_genes`
--

DROP TABLE IF EXISTS `signature_updown_genes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `signature_updown_genes` (
  `gene` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `signature` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `type` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`gene`,`signature`),
  KEY `FKeyhnrum6i9llm8m55ncrh3avi` (`signature`),
  CONSTRAINT `FK71w1mvo2s9n99b51fmy9jwkdh` FOREIGN KEY (`gene`) REFERENCES `genes` (`gene`),
  CONSTRAINT `FKeyhnrum6i9llm8m55ncrh3avi` FOREIGN KEY (`signature`) REFERENCES `signature` (`signatureName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `work`
--

DROP TABLE IF EXISTS `work`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `work` (
  `resultType` varchar(31) COLLATE utf8mb4_unicode_ci NOT NULL,
  `id` char(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `resultReference` varchar(1023) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `creationDateTime` datetime NOT NULL,
  `failureCause` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `finishingDateTime` datetime DEFAULT NULL,
  `schedulingDateTime` datetime DEFAULT NULL,
  `startDateTime` datetime DEFAULT NULL,
  `status` varchar(9) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `work_step`
--

DROP TABLE IF EXISTS `work_step`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `work_step` (
  `stepOrder` int(11) NOT NULL,
  `workId` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `progress` double DEFAULT NULL,
  PRIMARY KEY (`stepOrder`,`workId`),
  KEY `FK_work_work_step` (`workId`),
  CONSTRAINT `FK_work_work_step` FOREIGN KEY (`workId`) REFERENCES `work` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-05-28  9:52:34
