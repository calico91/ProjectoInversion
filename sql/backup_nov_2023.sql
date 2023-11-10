-- MySQL dump 10.13  Distrib 8.0.33, for Win64 (x86_64)
--
-- Host: localhost    Database: apirest
-- ------------------------------------------------------
-- Server version	8.0.33

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `cliente`
--

DROP TABLE IF EXISTS `cliente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cliente` (
  `id_cliente` int NOT NULL AUTO_INCREMENT,
  `apellidos` varchar(50) NOT NULL,
  `cedula` varchar(20) NOT NULL,
  `celular` varchar(10) NOT NULL,
  `direccion` varchar(100) NOT NULL,
  `email` varchar(100) DEFAULT NULL,
  `fecha_creacion` date DEFAULT NULL,
  `fecha_modificacion` date DEFAULT NULL,
  `nombres` varchar(50) NOT NULL,
  `observaciones` varchar(255) DEFAULT NULL,
  `pais` varchar(30) DEFAULT NULL,
  `usuario_creador` varchar(50) DEFAULT NULL,
  `usuario_modificador` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id_cliente`),
  UNIQUE KEY `UK_trfs6xemfuo1u29blh0jw3ekl` (`cedula`)
) ENGINE=InnoDB AUTO_INCREMENT=48 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cliente`
--

LOCK TABLES `cliente` WRITE;
/*!40000 ALTER TABLE `cliente` DISABLE KEYS */;
INSERT INTO `cliente` VALUES (1,'Carabali','66850016','3126081880','calle 54b 49d 05 piso 3',NULL,'2023-10-27','2023-11-08','Angela','Barrio cordoba reservado - SF',NULL,'maelito','blandon'),(2,'Rojas','1151949825','3229162350','cra 1e #59-163',NULL,'2023-10-27','2023-11-08','Felipe ','SF',NULL,'maelito','blandon'),(3,'Sepulveda','1144197339','3242782517','calle 35 30 27',NULL,'2023-10-27','2023-11-08','Jean','Hermano julieth patino - Fiadora - Julieth y mama de julieth',NULL,'maelito','blandon'),(4,'Botero','31528564','3172421575','calle 67 5 37',NULL,'2023-10-27','2023-11-08','Luz dary','Barrio brisas del guabito - Fiadora julieth patino - mama julieth patino',NULL,'maelito','blandon'),(5,'Gonzalez castro','1010062500','3163760956','calle 57 37 26 vallado','NN','2023-10-27',NULL,'Gaby vanessa','Amiga karen y julieth - Fiadora julieth patino','Colombia','maelito',NULL),(6,'Patino','1144159377','3164085216','calle 35 30 25 león 13','NN','2023-10-27',NULL,'Julieth','Fiadora karen gonzalez','Colombia','maelito',NULL),(7,'Nunez','29113754','3148460489','cra 1a9 73a 65','NN','2023-10-27',NULL,'Erika','Hermana lorenzo - Fiadora maría de los angeles - 1006050371 - cel 3103908434 - dir cra 1a9 73a 65','Colombia','maelito',NULL),(8,'Valencia','1107044344','3206876546','cra 26p 54 29','NN','2023-10-27',NULL,'Ronaldo','Cunado de karen gonzalez - SF','Colombia','maelito',NULL),(9,'Prueba','1','3111111111','pruebat',NULL,'2023-10-30','2023-11-01','Prueba','Prueba',NULL,'maelito','maelito'),(10,'Betancourt','1144064081','3156164561','calle 12b 40a 24','NN','2023-10-30',NULL,'Jaime','Fiadora Andrea londono-cc 1115183040 - celular 3113793867 - dir calle 14 37a 49 torre d 301 condominio san diego cristobal colon','Colombia','maelito',NULL),(11,'Osorio','29582969','3187402940','cra 36 5b1 36 apto 204','NN','2023-10-30',NULL,'Jakelin','Edif america 80 barrio san Fernando - Fiadora - leidy tatiana cuadros - cc 1007694463 - celular 3168566915 - dir cra 65 13b-125 bosques del limonar','Colombia','maelito',NULL),(12,'Viafara','67028776','3117676893','cra 93 42 93',NULL,'2023-10-30','2023-11-09','Katherine','',NULL,'maelito','blandon'),(13,'David','1085276989','3162851808','cra 22 12 95','NN','2023-10-30',NULL,'Eynar camilo','','Colombia','maelito',NULL),(14,'Valencia','94402551','3206743423','calle 61b 1hbis 40','NN','2023-10-30',NULL,'Ricardo','Esposo roselly -','Colombia','maelito',NULL),(15,'Mosquera','66960509','3104530365','vive donde la suegra','NN','2023-10-30',NULL,'Roselly','Se cambio de casa y no se sabe donde vive','Colombia','maelito',NULL),(16,'Vargas','26386421','3122070802','calle 15 22a 16','NN','2023-10-30',NULL,'Zully','Moroso reee','Colombia','maelito',NULL),(17,'Acosta','1143987657','3225981503','cra 26g 86 18','NN','2023-10-30',NULL,'Junior','','Colombia','maelito',NULL),(18,'Vallecilla','38644147','3164280120','no registra','NN','2023-10-30',NULL,'Vivian','No registra','Colombia','maelito',NULL),(19,'Gonzalez','31305143','3167564377','calle67n 1a 11-15','NN','2023-10-30',NULL,'Lisbeth','Sf','Colombia','maelito',NULL),(20,'Zambrano','1107070727','3106341121','cra 26g12 73b 59 marroco','NN','2023-10-30',NULL,'Jessica','Esposa de David salcedo','Colombia','maelito',NULL),(21,'Moreno','1143989341','3112652348','cra 26g5 86-16 san marcos','NN','2023-10-30',NULL,'Natalia','Fiador - jorge moreno - cc 1130625464 - cel 3152573349 - cra 7t bis 72 37','Colombia','maelito',NULL),(22,'Gonzalez','1111805885','3117396187','cra 26 56a 66 n floresta','NN','2023-10-30',NULL,'Wendy','Hermana de karen clínica colombia','Colombia','maelito',NULL),(23,'Yarce','1144053206','3002083821','calle 18 44a 10','NN','2023-10-30',NULL,'Daniela','En la usa','Colombia','maelito',NULL),(24,'Hurtado','6228420','3108383025','calle 46 10 09','NN','2023-10-30',NULL,'Marcel','Sf','Colombia','maelito',NULL),(25,'Hernandez','1143936925','3158288824','cra 26 g10 73b 45 marroquí 2','NN','2023-10-30',NULL,'Lessdy','Sf','Colombia','maelito',NULL),(26,'Graterol','3857677','3128705319','cra 26 33f 71 santa fe','NN','2023-10-30',NULL,'Hector','Fiador - maría del mar','Colombia','maelito',NULL),(27,'Vivas','1006055175','3012915079','cra 1 a9 73a 65',NULL,'2023-10-30','2023-11-07','Lorenzo','Fiador - milena arismendi  - cc 6673933- cel  3229714386 dir CRA 7h #70-35',NULL,'maelito','blandon'),(28,'Salcedo','6229155','3205510345','calle 55 1 94 bloque 7 apt 404','NN','2023-10-30',NULL,'David','Sf','Colombia','maelito',NULL),(29,'Buitrago','1144157641','3164903048','cra 1d 62 04 chimi','NN','2023-10-31',NULL,'Juan david','Sf','Colombia','maelito',NULL),(30,'Vinasco','31930954','3172196114','calle 15a 74 30 brisas limonar','NN','2023-10-31',NULL,'Consuelo','','Colombia','maelito',NULL),(31,'Castillo','59675494','3043919297','cra 27 5b-10','NN','2023-11-01',NULL,'Idia','SF','Colombia','maelito',NULL),(32,'Taibel','1130642885','3137045817','calle 15 37 68 colibri','NN','2023-11-01',NULL,'Hermes','Poblado campestre - Fiador -  es el papá datos en el correo','Colombia','maelito',NULL),(33,'Flor','1107509524','3148321761','cra 26d 124-10','NN','2023-11-01',NULL,'Diana','','Colombia','maelito',NULL),(34,'Calero','1107528451','3177557782','san Antonio','NN','2023-11-01',NULL,'Aranza','Sf','Colombia','maelito',NULL),(35,'Luango','1107051725','4611232921','España','NN','2023-11-02',NULL,'Fransua','Sf','Colombia','blandon',NULL),(36,'Calero','1005706836','3182362717','san Antonio',NULL,'2023-11-02','2023-11-09','Maria del mar','Sf',NULL,'blandon','blandon'),(37,'Garcia','1144188641','1317809610','medellin','NN','2023-11-02',NULL,'Santiago','Sf','Colombia','blandon',NULL),(38,'Caicedo','31915252','3155150777','puerto tejada','NN','2023-11-09',NULL,'Ruby','Sf','Colombia','blandon',NULL),(39,'Calero','1143849576','7818347644','inglaterra','NN','2023-11-09',NULL,'Brian','Sf','Colombia','blandon',NULL),(47,'Gonzales','1148445049','3164167175','cra 27 56a 66','NN','2023-11-09',NULL,'Karen','Sf','Colombia','blandon',NULL);
/*!40000 ALTER TABLE `cliente` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `credito`
--

DROP TABLE IF EXISTS `credito`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `credito` (
  `id_credito` int NOT NULL AUTO_INCREMENT,
  `estado_credito` varchar(255) NOT NULL,
  `fecha_credito` date NOT NULL,
  `usuario_creador_credito` varchar(255) NOT NULL,
  `id_cliente` int DEFAULT NULL,
  `valor_credito` double NOT NULL,
  PRIMARY KEY (`id_credito`),
  KEY `FK2wsrvlr3jsdvvdqrnp1svagxj` (`id_cliente`),
  CONSTRAINT `FK2wsrvlr3jsdvvdqrnp1svagxj` FOREIGN KEY (`id_cliente`) REFERENCES `cliente` (`id_cliente`)
) ENGINE=InnoDB AUTO_INCREMENT=51 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `credito`
--

LOCK TABLES `credito` WRITE;
/*!40000 ALTER TABLE `credito` DISABLE KEYS */;
INSERT INTO `credito` VALUES (2,'A','2023-10-24','maelito',1,600000),(3,'A','2023-10-09','maelito',2,1200000),(4,'A','2023-10-05','maelito',3,1000000),(5,'A','2023-10-04','maelito',4,600000),(6,'A','2023-10-17','maelito',5,300000),(7,'A','2023-07-05','maelito',6,1000000),(10,'A','2023-09-15','maelito',8,500000),(21,'A','2023-07-05','maelito',21,1500000),(22,'A','2023-01-01','maelito',9,1000000),(23,'A','2023-09-01','maelito',12,1200000),(25,'A','2023-09-01','maelito',13,400000),(26,'A','2023-08-01','maelito',26,1500000),(27,'A','2023-07-01','maelito',25,1000000),(28,'A','2023-06-30','maelito',34,1500000),(32,'A','2023-05-01','blandon',36,1000000),(33,'A','2023-08-01','blandon',23,1000000),(34,'A','2023-10-02','blandon',7,1000000),(35,'A','2023-10-03','blandon',29,600000),(36,'A','2023-11-07','blandon',27,1000000),(37,'A','2023-11-04','blandon',12,400000),(38,'A','2023-10-05','blandon',10,2000000),(39,'A','2023-09-05','blandon',17,300000),(41,'A','2023-05-06','blandon',20,1200000),(42,'A','2023-11-09','blandon',38,1000000),(43,'A','2023-11-09','blandon',38,2000000),(46,'A','2023-09-09','blandon',28,850000),(47,'A','2023-10-05','blandon',35,500000),(48,'A','2023-10-05','blandon',35,1000000),(50,'A','2023-10-05','blandon',35,200000);
/*!40000 ALTER TABLE `credito` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cuota_credito`
--

DROP TABLE IF EXISTS `cuota_credito`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cuota_credito` (
  `id_cuota_credito` int NOT NULL AUTO_INCREMENT,
  `couta_numero` int NOT NULL,
  `fecha_abono` date DEFAULT NULL,
  `fecha_cuota` date NOT NULL,
  `interes_porcentaje` double NOT NULL,
  `numero_cuotas` int NOT NULL,
  `valor_abonado` double DEFAULT NULL,
  `valor_capital` double NOT NULL,
  `valor_credito` double NOT NULL,
  `valor_cuota` double NOT NULL,
  `valor_interes` double NOT NULL,
  `id_credito` int NOT NULL,
  `tipo_abono` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id_cuota_credito`),
  KEY `FKqkddm4onlompx5m7xe3a23dq` (`id_credito`)
) ENGINE=InnoDB AUTO_INCREMENT=150 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cuota_credito`
--

LOCK TABLES `cuota_credito` WRITE;
/*!40000 ALTER TABLE `cuota_credito` DISABLE KEYS */;
INSERT INTO `cuota_credito` VALUES (2,1,NULL,'2023-11-24',7,3,NULL,200000,600000,243400,43400,2,NULL),(3,1,'2023-11-01','2023-11-01',7,10,184400,120000,1200000,184400,64400,3,NULL),(4,1,'2023-11-08','2023-11-05',7,5,272333,200000,1000000,272333,72333,4,NULL),(5,1,'2023-11-08','2023-11-04',7,5,163400,120000,600000,163400,43400,5,NULL),(6,1,NULL,'2023-11-17',7,3,NULL,100000,300000,121700,21700,6,NULL),(7,1,'2023-08-05','2023-08-05',7,7,213000,142857,1000000,215190,72333,7,NULL),(8,2,'2023-09-05','2023-09-05',7,7,213000,142857,1000000,212857,70000,7,NULL),(9,3,'2023-10-05','2023-10-05',7,7,213000,142857,1000000,212857,70000,7,NULL),(10,4,'2023-11-08','2023-11-05',7,7,212857,142857,1000000,212857,70000,7,NULL),(13,1,'2023-11-01','2023-10-15',7,2,285000,250000,500000,285000,35000,10,NULL),(56,2,NULL,'2023-12-01',7,10,NULL,120000,1200000,204000,84000,3,NULL),(57,1,'2023-11-01','2023-08-05',5,10,227500,150000,1500000,227500,77500,21,NULL),(58,2,'2023-11-01','2023-09-05',5,10,225000,150000,1500000,225000,75000,21,NULL),(59,3,'2023-11-01','2023-10-05',5,10,225000,150000,1500000,225000,75000,21,NULL),(60,4,'2023-11-07','2023-11-05',5,10,225000,150000,1500000,225000,75000,21,NULL),(63,1,'2023-11-01','2023-02-01',10,10,50000,0,1000000,203333,50000,22,NULL),(64,1,'2023-11-01','2023-03-01',10,10,200000,100000,1000000,200000,100000,22,NULL),(65,2,'2023-11-01','2023-04-01',10,10,100000,0,1000000,200000,100000,22,NULL),(66,2,'2023-11-01','2023-05-01',10,10,200000,100000,1000000,200000,100000,22,NULL),(67,2,NULL,'2023-11-15',7,2,NULL,250000,500000,285000,35000,10,NULL),(68,1,'2023-11-01','2023-10-01',5,6,260000,200000,1200000,260000,60000,23,NULL),(69,2,'2023-11-02','2023-11-01',5,6,260000,200000,1200000,260000,60000,23,NULL),(71,1,'2023-11-01','2023-10-01',7,1,28000,0,400000,428000,28000,25,NULL),(72,1,'2023-11-03','2023-11-01',7,1,28000,0,400000,428000,28000,25,NULL),(73,1,'2023-11-01','2023-09-01',7,10,258500,150000,1500000,258500,108500,26,NULL),(74,2,'2023-11-01','2023-10-01',7,10,255000,150000,1500000,255000,105000,26,NULL),(75,3,'2023-11-03','2023-11-01',7,10,255000,150000,1500000,255000,105000,26,NULL),(76,1,'2023-11-01','2023-08-01',7,5,272333,200000,1000000,272333,72333,27,NULL),(77,2,'2023-11-01','2023-09-01',7,5,70000,0,1000000,270000,70000,27,NULL),(78,2,'2023-11-01','2023-10-01',7,5,70000,0,1000000,270000,70000,27,NULL),(79,2,'2023-11-02','2023-11-01',7,5,270000,200000,1000000,270000,70000,27,NULL),(80,3,'2023-11-01','2023-06-01',10,10,50000,0,1000000,200000,50000,22,NULL),(81,3,'2023-11-09','2023-07-01',10,10,100000,0,1000000,200000,100000,22,'SI'),(82,1,'2023-11-01','2023-07-30',5,14,182143,107143,1500000,182143,75000,28,NULL),(83,2,'2023-11-01','2023-08-30',5,14,182142.85714285716,107142.85714285714,1500000,182142.85714285716,75000,28,NULL),(84,3,'2023-11-01','2023-09-30',5,14,182142.85714285716,107142.85714285714,1500000,182142.85714285716,75000,28,NULL),(85,4,'2023-11-03','2023-10-30',5,14,182142.85714285716,107142.85714285714,1500000,182142.85714285716,75000,28,NULL),(92,3,NULL,'2023-12-01',7,5,NULL,200000,1000000,270000,70000,27,NULL),(94,1,'2023-11-02','2023-06-01',5,7,194524,142857,1000000,194524,51667,32,NULL),(95,2,'2023-11-02','2023-07-01',5,7,192857.14285714287,142857.14285714287,1000000,192857.14285714287,50000,32,NULL),(96,3,'2023-11-02','2023-08-01',5,7,192857.14285714287,142857.14285714287,1000000,192857.14285714287,50000,32,NULL),(97,4,'2023-11-02','2023-09-01',5,7,192857.14285714287,142857.14285714287,1000000,192857.14285714287,50000,32,NULL),(98,5,'2023-11-02','2023-10-01',5,7,50000,0,1000000,192857.14285714287,50000,32,NULL),(99,5,'2023-11-08','2023-11-01',5,7,192857.14285714287,142857.14285714287,1000000,192857.14285714287,50000,32,NULL),(100,3,NULL,'2023-12-01',5,6,NULL,200000,1200000,260000,60000,23,NULL),(101,1,'2023-11-02','2023-09-01',7,5,272333,200000,1000000,272333,72333,33,NULL),(102,2,'2023-11-02','2023-10-01',7,5,270000,200000,1000000,270000,70000,33,NULL),(103,3,'2023-11-02','2023-11-01',7,5,270000,200000,1000000,270000,70000,33,NULL),(104,4,NULL,'2023-12-01',7,5,NULL,200000,1000000,270000,70000,33,NULL),(105,5,NULL,'2023-11-30',5,14,NULL,107142.85714285714,1500000,182142.85714285716,75000,28,NULL),(107,1,'2023-11-03','2023-11-02',7,5,272333,200000,1000000,272333,72333,34,NULL),(108,2,NULL,'2023-12-02',7,5,NULL,200000,1000000,270000,70000,34,NULL),(109,1,'2023-11-07','2023-11-03',5,4,181000,150000,600000,181000,31000,35,NULL),(110,4,NULL,'2023-12-01',7,10,NULL,150000,1500000,255000,105000,26,NULL),(111,1,NULL,'2023-12-01',7,1,NULL,400000,400000,428000,28000,25,NULL),(112,2,NULL,'2023-12-03',5,4,NULL,150000,600000,180000,30000,35,NULL),(113,5,NULL,'2023-12-05',5,10,NULL,150000,1500000,225000,75000,21,NULL),(114,6,NULL,'2023-12-01',5,7,NULL,142857.14285714287,1000000,192857.14285714287,50000,32,NULL),(115,2,NULL,'2023-12-05',7,5,NULL,200000,1000000,270000,70000,4,NULL),(116,5,NULL,'2023-12-05',7,7,NULL,142857.14285714287,1000000,212857.14285714287,70000,7,NULL),(117,2,NULL,'2023-12-04',7,5,NULL,120000,600000,162000,42000,5,NULL),(118,1,NULL,'2023-12-01',7,5,NULL,200000,1000000,256000,56000,36,NULL),(119,1,NULL,'2023-12-01',5,4,NULL,100000,400000,118000,18000,37,NULL),(120,1,'2023-11-09','2023-11-05',7,12,306667,166667,2000000,306667,140000,38,NULL),(121,1,'2023-11-08','2023-10-05',5,6,65000,50000,300000,65000,15000,39,NULL),(122,2,'2023-11-08','2023-11-05',5,6,65000,50000,300000,65000,15000,39,NULL),(123,3,NULL,'2023-12-05',5,6,NULL,50000,300000,65000,15000,39,NULL),(125,1,'2023-11-08','2023-06-06',5,12,160000,100000,1200000,160000,60000,41,NULL),(126,2,'2023-11-08','2023-07-06',5,12,160000,100000,1200000,160000,60000,41,NULL),(127,3,'2023-11-08','2023-08-06',5,12,160000,100000,1200000,160000,60000,41,NULL),(128,4,'2023-11-08','2023-09-06',5,12,160000,100000,1200000,160000,60000,41,NULL),(129,5,'2023-11-08','2023-10-06',5,12,160000,100000,1200000,160000,60000,41,NULL),(130,6,NULL,'2023-11-06',5,12,NULL,100000,1200000,160000,60000,41,NULL),(131,2,NULL,'2023-12-05',7,12,NULL,166666.66666666666,2000000,306666.6666666666,140000,38,NULL),(132,1,NULL,'2023-12-09',7,1,NULL,1000000,1000000,1070000,70000,42,NULL),(133,1,NULL,'2023-12-09',7,1,NULL,2000000,2000000,2140000,140000,43,NULL),(136,1,'2023-11-09','2023-10-09',5,6,184167,141667,850000,184167,42500,46,NULL),(137,2,NULL,'2023-11-09',5,6,NULL,141666.66666666666,850000,184166.66666666666,42500,46,NULL),(138,1,'2023-11-09','2023-11-05',5,1,25000,0,500000,525000,25000,47,NULL),(139,1,'2023-11-09','2023-11-05',7,1,70000,0,1000000,1070000,70000,48,NULL),(143,1,NULL,'2023-12-05',5,1,NULL,500000,500000,525000,25000,47,NULL),(144,1,NULL,'2023-12-05',7,1,NULL,1000000,1000000,1070000,70000,48,NULL),(145,1,'2023-11-09','2023-11-05',7,1,14000,0,200000,214000,14000,50,NULL),(146,1,NULL,'2023-12-05',7,1,NULL,200000,200000,214000,14000,50,NULL),(147,3,'2023-11-09','2023-08-01',10,10,100000,0,1000000,200000,100000,22,'SI'),(148,3,'2023-11-09','2023-09-01',10,10,200000,100000,1000000,200000,100000,22,'CN'),(149,4,NULL,'2023-10-01',10,10,NULL,100000,1000000,200000,100000,22,NULL);
/*!40000 ALTER TABLE `cuota_credito` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roles` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` VALUES (1,'ROLE_ADMIN'),(2,'ROLE_ADMIN'),(3,'ROLE_USER'),(4,'ROLE_ADMIN'),(5,'ROLE_ADMIN'),(6,'ROLE_USER');
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `country` varchar(255) DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `firstname` varchar(255) DEFAULT NULL,
  `lastname` varchar(255) NOT NULL,
  `password` varchar(255) DEFAULT NULL,
  `username` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_ob8kqyqqgmefl0aco34akdtpe` (`email`),
  UNIQUE KEY `UK_sb8bbouer5wak8vyiiy4pf2bx` (`username`),
  UNIQUE KEY `UKsb8bbouer5wak8vyiiy4pf2bx` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'Locombia','maelito@gmail.com','Maelito','Blandon','$2a$10$jAehpayzhvQwqEPkdHxoIemq/tMujfiTYDTs1vLv42VaDn.mVHQKi','maelito'),(3,'Locombia','blandon@gmail.com','Cristian','Blandon','$2a$10$BHCpFVvXCeVj2nCwlaF4uOAlWuzAaLYjmAaV3tlocIujVYHvyNBGq','blandon'),(11,'Locombia','quinio@gmail.com','Byron','Quinones','$2a$10$1mOoLIv3nDvRv.aXWBphE.qLVZFd8ON32WSsDRo6Vdy25mYZgy7wm','mumilon'),(13,'Locombia','qnio@gmail.com','Byron','Quinones','$2a$10$T6ycYMHcDXXw7TdHPQC0UOLJTsZf4ZG6vyCN7cBuJYdKz1FJdbCIa','mulon'),(14,'Locombia','nuevo@gmail.com','Byron','Quinones','$2a$10$jWD1.iytgWDwRjyoPaMZDO3wnahTR2YTO4RYu44USlSuGFSUhF896','user');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_roles`
--

DROP TABLE IF EXISTS `user_roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_roles` (
  `user_id` int NOT NULL,
  `role_id` bigint NOT NULL,
  PRIMARY KEY (`user_id`,`role_id`),
  KEY `FKh8ciramu9cc9q3qcqiv4ue8a6` (`role_id`),
  CONSTRAINT `FK55itppkw3i07do3h7qoclqd4k` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKh8ciramu9cc9q3qcqiv4ue8a6` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_roles`
--

LOCK TABLES `user_roles` WRITE;
/*!40000 ALTER TABLE `user_roles` DISABLE KEYS */;
INSERT INTO `user_roles` VALUES (1,1),(3,2),(3,3),(11,4),(13,5),(14,6);
/*!40000 ALTER TABLE `user_roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'apirest'
--

--
-- Dumping routines for database 'apirest'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-11-09 17:16:02