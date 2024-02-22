-- MySQL dump 10.13  Distrib 8.0.34, for Win64 (x86_64)
--
-- Host: localhost    Database: apirest
-- ------------------------------------------------------
-- Server version	8.0.35

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
  `celular` varchar(20) NOT NULL,
  `direccion` varchar(100) NOT NULL,
  `email` varchar(80) DEFAULT NULL,
  `fecha_creacion` date DEFAULT NULL,
  `fecha_modificacion` date DEFAULT NULL,
  `nombres` varchar(50) NOT NULL,
  `observaciones` varchar(255) DEFAULT NULL,
  `pais` varchar(30) DEFAULT NULL,
  `usuario_creador` varchar(25) DEFAULT NULL,
  `usuario_modificador` varchar(25) DEFAULT NULL,
  PRIMARY KEY (`id_cliente`),
  UNIQUE KEY `UK_trfs6xemfuo1u29blh0jw3ekl` (`cedula`)
) ENGINE=InnoDB AUTO_INCREMENT=79 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cliente`
--

LOCK TABLES `cliente` WRITE;
/*!40000 ALTER TABLE `cliente` DISABLE KEYS */;
INSERT INTO `cliente` VALUES (1,'Carabali','66850016','3126081880','calle 54b 49d 05 piso 3',NULL,'2023-10-27','2023-11-08','Angela','Barrio cordoba reservado - SF',NULL,'maelito','blandon'),(2,'Rojas','1151949825','3229162350','cra 1e #59-163',NULL,'2023-10-27','2023-11-08','Felipe ','SF',NULL,'maelito','blandon'),(3,'Sepulveda','1144197339','3242782517','calle 35 30 27',NULL,'2023-10-27','2023-11-08','Jean','Hermano julieth patino - Fiadora - Julieth y mama de julieth',NULL,'maelito','blandon'),(4,'Botero','31528564','3172421575','calle 67 5 37',NULL,'2023-10-27','2023-11-08','Luz dary','Barrio brisas del guabito - Fiadora julieth patino - mama julieth patino',NULL,'maelito','blandon'),(5,'Gonzalez castro','1010062500','3163760956','calle 57 37 26 vallado','NN','2023-10-27',NULL,'Gaby vanessa','Amiga karen y julieth - Fiadora julieth patino','Colombia','maelito',NULL),(6,'Patino','1144159377','3164085216','calle 35 30 25 león 13','NN','2023-10-27',NULL,'Julieth','Fiadora karen gonzalez','Colombia','maelito',NULL),(7,'Nunez','29113754','3148460489','cra 1a9 73a 65','NN','2023-10-27',NULL,'Erika','Hermana lorenzo - Fiadora maría de los angeles - 1006050371 - cel 3103908434 - dir cra 1a9 73a 65','Colombia','maelito',NULL),(8,'Valencia','1107044344','3206876546','cra 26p 54 29','NN','2023-10-27',NULL,'Ronaldo','Cunado de karen gonzalez - SF','Colombia','maelito',NULL),(9,'Prueba','1','3111111111','compartir es vivir ',NULL,'2023-10-30','2023-11-22','Prueba','Prueba',NULL,'maelito','blandon'),(10,'Betancourt','1144064081','3156164561','calle 12b 40a 24','NN','2023-10-30',NULL,'Jaime','Fiadora Andrea londono-cc 1115183040 - celular 3113793867 - dir calle 14 37a 49 torre d 301 condominio san diego cristobal colon','Colombia','maelito',NULL),(11,'Osorio','29582969','3187402940','cra 36 5b1 36 apto 204','NN','2023-10-30',NULL,'Jakelin','Edif america 80 barrio san Fernando - Fiadora - leidy tatiana cuadros - cc 1007694463 - celular 3168566915 - dir cra 65 13b-125 bosques del limonar','Colombia','maelito',NULL),(12,'Viafara','67028776','3117676893','cra 93 42 93',NULL,'2023-10-30','2023-11-09','Katherine','',NULL,'maelito','blandon'),(13,'David','1085276989','3162851808','cra 22 12 95','NN','2023-10-30',NULL,'Eynar camilo','','Colombia','maelito',NULL),(14,'Valencia','94402551','3206743423','calle 61b 1hbis 40','NN','2023-10-30',NULL,'Ricardo','Esposo roselly -','Colombia','maelito',NULL),(15,'Mosquera','66960509','3104530365','vive donde la suegra','NN','2023-10-30',NULL,'Roselly','Se cambio de casa y no se sabe donde vive','Colombia','maelito',NULL),(16,'Vargas','26386421','3122070802','calle 15 22a 16','NN','2023-10-30',NULL,'Zully','Moroso reee','Colombia','maelito',NULL),(17,'Acosta','1143987657','3225981503','cra 26g 86 18','NN','2023-10-30',NULL,'Junior','','Colombia','maelito',NULL),(18,'Vallecilla','38644147','3164280120','no registra','NN','2023-10-30',NULL,'Vivian','No registra','Colombia','maelito',NULL),(19,'Gonzalez','31305143','3167564377','calle67n 1a 11-15','NN','2023-10-30',NULL,'Lisbeth','Sf','Colombia','maelito',NULL),(20,'Zambrano','1107070727','3106341121','cra 26g12 73b 59 marroco','NN','2023-10-30',NULL,'Jessica','Esposa de David salcedo','Colombia','maelito',NULL),(21,'Moreno','1143989341','3112652348','cra 26g5 86-16 san marcos','NN','2023-10-30',NULL,'Natalia','Fiador - jorge moreno - cc 1130625464 - cel 3152573349 - cra 7t bis 72 37','Colombia','maelito',NULL),(22,'Gonzalez','1111805885','3117396187','cra 26 56a 66 n floresta',NULL,'2023-10-30','2023-11-22','Wendy','Hermana de karen clinica colombia',NULL,'maelito','blandon'),(23,'Yarce','1144053206','3002083821','calle 18 44a 10','NN','2023-10-30',NULL,'Daniela','En la usa','Colombia','maelito',NULL),(24,'Hurtado','6228420','3108383025','calle 46 10 09','NN','2023-10-30',NULL,'Marcel','Sf','Colombia','maelito',NULL),(25,'Hernandez','1143936925','3158288824','cra 26 g10 73b 45 marroquí 2','NN','2023-10-30',NULL,'Lessdy','Sf','Colombia','maelito',NULL),(26,'Graterol','3857677','3128705319','cra 26 33f 71 santa fe','NN','2023-10-30',NULL,'Hector','Fiador - maría del mar','Colombia','maelito',NULL),(27,'Vivas','1006055175','3012915079','cra 1 a9 73a 65',NULL,'2023-10-30','2023-11-07','Lorenzo','Fiador - milena arismendi  - cc 6673933- cel  3229714386 dir CRA 7h #70-35',NULL,'maelito','blandon'),(28,'Salcedo','6229155','3205510345','calle 55 1 94 bloque 7 apt 404','NN','2023-10-30',NULL,'David','Sf','Colombia','maelito',NULL),(29,'Buitrago','1144157641','3164903048','cra 1d 62 04 chimi','NN','2023-10-31',NULL,'Juan david','Sf','Colombia','maelito',NULL),(30,'Vinasco','31930954','3172196114','calle 15a 74 30 brisas limonar','NN','2023-10-31',NULL,'Consuelo','','Colombia','maelito',NULL),(31,'Castillo','59675494','3043919297','cra 27 5b-10','NN','2023-11-01',NULL,'Idia','SF','Colombia','maelito',NULL),(32,'Taibel','1130642885','3137045817','calle 15 37 68 colibri','NN','2023-11-01',NULL,'Hermes','Poblado campestre - Fiador -  es el papá datos en el correo','Colombia','maelito',NULL),(33,'Flor','1107509524','3148321761','cra 26d 124-10','NN','2023-11-01',NULL,'Diana','','Colombia','maelito',NULL),(34,'Calero','1107528451','3177557782','san Antonio','NN','2023-11-01',NULL,'Aranza','Sf','Colombia','maelito',NULL),(35,'Luango','1107051725','4611232921','España','NN','2023-11-02',NULL,'Fransua','Sf','Colombia','blandon',NULL),(36,'Calero','1005706836','3182362717','san Antonio',NULL,'2023-11-02','2023-11-09','Maria del mar','Sf',NULL,'blandon','blandon'),(37,'Garcia','1144188641','1317809610','medellin','NN','2023-11-02',NULL,'Santiago','Sf','Colombia','blandon',NULL),(38,'Caicedo','31915252','3155150777','puerto tejada','NN','2023-11-09',NULL,'Ruby','Sf','Colombia','blandon',NULL),(39,'Calero','1143849576','7818347644','inglaterra','NN','2023-11-09',NULL,'Brian','Sf','Colombia','blandon',NULL),(47,'Gonzales','1148445049','3164167175','cra 27 56a 66','NN','2023-11-09',NULL,'Karen','Sf','Colombia','blandon',NULL),(48,'Enriquez','1130669503','3006722225','la isla',NULL,'2023-11-17','2023-11-23','Juan manuel','Sf ',NULL,'blandon','maelito'),(49,'Medina','1143945503','3166954761','Kra 29 # 39 - 25','NN','2023-11-29',NULL,'Fabian','Sf','Colombia','blandon',NULL),(50,'Guerrero','1144163255','3168672475','yumbo','NN','2023-12-01',NULL,'Leidy','Sf','Colombia','blandon',NULL),(51,'Gonzales','29784912','3162925815','calle 7 28-39','NN','2023-12-04',NULL,'Ana milena','Sf','Colombia','blandon',NULL),(52,'Yarpaz','1006011925','3145462609','CARRERA 1A NORTE #81- 21','NN','2023-12-04',NULL,'Diego estarlin','Fiadora vanessa Gonzalez','Colombia','blandon',NULL),(53,'Vivas','4640656','3122600524','Cra 1 a 5E #73 a 52 calimio nt',NULL,'2023-12-05','2023-12-05','Harold','Papa de lorenzo - fiador lorenzo',NULL,'blandon','blandon'),(54,'Ortegon','1130667927','1316254905','cra 45 # 18-32','NN','2023-12-16',NULL,'Claudia','Daniela yarce','Colombia','blandon',NULL),(55,'Arismendi','6673933','3229714386','carrera 22  #36 -89 santa fe','NN','2023-12-17',NULL,'Milena','Ramón segura c.c 94523830 telefono 3155149479 carrera 22  #36 -89 santa fe esposo - senora del trabajo de maría del mar','Colombia','blandon',NULL),(56,'Franco','1107075284','3162987978','Diagonal 51 oeste # 10 - 105 U','NN','2024-01-09',NULL,'Vicky yulieth','Fiadora - ruby','Colombia','blandon',NULL),(57,'Marin Ortiz','1143874050','3186926913','DIRECCION/ CRA 4 ·1-02 SAN ANT','NN','2024-01-10',NULL,'David','FIADORJUAN PABLO ORDOÑEZ PADILLA10059338753165648739CALLE 15 OE ·3-25 BELLAVISTA','Colombia','blandon',NULL),(58,'Salcedo luna','67005799','3238695459','av 2c norte 39-42 vipasa',NULL,'2024-01-22','2024-02-09','Luz Stella','David salcedo',NULL,'blandon','blandon'),(59,'Prueba','2','3333333333','calit',NULL,'2024-02-09','2024-02-21','Prueba','Sf',NULL,'blandon','blandon');
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
  `fecha_credito` date NOT NULL,
  `saldo_credito` double NOT NULL,
  `usuario_creador_credito` varchar(20) NOT NULL,
  `valor_credito` double NOT NULL,
  `id_cliente` int DEFAULT NULL,
  `id_estado_credito` int DEFAULT NULL,
  `id_modalidad` int DEFAULT NULL,
  PRIMARY KEY (`id_credito`),
  KEY `FK2wsrvlr3jsdvvdqrnp1svagxj` (`id_cliente`),
  KEY `FKim0k2ihsr31nbd9675smh1tto` (`id_estado_credito`),
  KEY `FKaa13hx1lk1fqm7sxy8ohj4eqk` (`id_modalidad`),
  CONSTRAINT `FK2wsrvlr3jsdvvdqrnp1svagxj` FOREIGN KEY (`id_cliente`) REFERENCES `cliente` (`id_cliente`),
  CONSTRAINT `FKaa13hx1lk1fqm7sxy8ohj4eqk` FOREIGN KEY (`id_modalidad`) REFERENCES `modalidad` (`id_modalidad`),
  CONSTRAINT `FKim0k2ihsr31nbd9675smh1tto` FOREIGN KEY (`id_estado_credito`) REFERENCES `estado_credito` (`id_estado_credito`)
) ENGINE=InnoDB AUTO_INCREMENT=123 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;



--
-- Table structure for table `estado_credito`
--

DROP TABLE IF EXISTS `estado_credito`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `estado_credito` (
  `id_estado_credito` int NOT NULL AUTO_INCREMENT,
  `descripcion` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id_estado_credito`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `estado_credito`
--

LOCK TABLES `estado_credito` WRITE;
/*!40000 ALTER TABLE `estado_credito` DISABLE KEYS */;
INSERT INTO `estado_credito` VALUES (1,'Activo'),(2,'Pagado'),(3,'Anulado');
/*!40000 ALTER TABLE `estado_credito` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `modalidad`
--

DROP TABLE IF EXISTS `modalidad`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `modalidad` (
  `id_modalidad` int NOT NULL AUTO_INCREMENT,
  `description` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id_modalidad`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `modalidad`
--

LOCK TABLES `modalidad` WRITE;
/*!40000 ALTER TABLE `modalidad` DISABLE KEYS */;
INSERT INTO `modalidad` VALUES (1,'mensual'),(2,'quincenal');
/*!40000 ALTER TABLE `modalidad` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roles` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(15) DEFAULT NULL,
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
  `country` varchar(25) DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `firstname` varchar(30) NOT NULL,
  `lastname` varchar(30) NOT NULL,
  `password` varchar(255) DEFAULT NULL,
  `username` varchar(25) NOT NULL,
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
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-02-22  8:34:38
