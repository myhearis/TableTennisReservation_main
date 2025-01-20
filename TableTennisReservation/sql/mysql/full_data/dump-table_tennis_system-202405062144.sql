-- MySQL dump 10.13  Distrib 8.1.0, for Win64 (x86_64)
--
-- Host: 192.168.234.80    Database: table_tennis_system
-- ------------------------------------------------------
-- Server version	8.0.31

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `BALL_TABLE`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `BALL_TABLE` (
  `GUID` varchar(50) NOT NULL COMMENT 'ID主键',
  `IMG_PATH` varchar(200) DEFAULT NULL COMMENT '图片路径',
  `CODE` varchar(20) DEFAULT NULL COMMENT '球桌编码(唯一)',
  `ADDRESS` varchar(100) DEFAULT NULL COMMENT '地址位置',
  `DESCRIPTION` varchar(200) DEFAULT NULL COMMENT '描述',
  `CREATE_DATE` varchar(20) DEFAULT NULL COMMENT '创建日期',
  `CREATE_USER` varchar(100) DEFAULT NULL COMMENT '创建用户（用户名）',
  `UPDATE_DATE` varchar(20) DEFAULT NULL COMMENT '更新日期',
  `UPDATE_USER` varchar(100) DEFAULT NULL COMMENT '更新用户（用户名）',
  `STATUS` int DEFAULT NULL COMMENT '是否可用 1 可用，0 不可用',
  `START_TIME` varchar(8) DEFAULT NULL COMMENT '球桌当天开放时间，24小时制：HH:mm:ss',
  `END_TIME` varchar(8) DEFAULT NULL COMMENT '球桌当天结束时间，24小时制：HH:mm:ss',
  `BRAND` varchar(50) DEFAULT NULL COMMENT '球桌品牌',
  `FLOOR_MATERIAL` varchar(50) DEFAULT NULL COMMENT '地面材质：如黑色地胶，瓷砖，木制',
  `QUALITY_RATING` varchar(50) DEFAULT NULL COMMENT '球桌完好程度：完美，优良，破损',
  `PRICE` decimal(10,0) DEFAULT NULL COMMENT '球桌每小时单价',
  PRIMARY KEY (`GUID`),
  UNIQUE KEY `CODE` (`CODE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `BALL_TABLE`
--

LOCK TABLES `BALL_TABLE` WRITE;
/*!40000 ALTER TABLE `BALL_TABLE` DISABLE KEYS */;
INSERT INTO `BALL_TABLE` VALUES ('21b2daf3-4e6a-46f8-b309-56871d8fa1f9','2024/02/20/cf282d55-8930-4a4a-8db3-4f458991823a.jpg','4','体育馆1楼4号室','尊享版球桌1','2024-02-20 23:18:34','3ccbec15-588d-4821-8df4-4c3c66930d73','2024-03-17 16:44:23','52d4c537-2310-47f0-bbca-dff919bccfa2',1,'09:00:00','21:00:00','双鱼','瓷砖','良好',15),('585fda38-5ab6-47ab-b9f5-d4dc9c9ebea6','2024/02/20/c2b2f09d-04f7-4802-8e46-43fa37d2cf5f.jpg','3','体育馆1楼3号室','豪华版球桌2','2024-02-20 23:17:49','3ccbec15-588d-4821-8df4-4c3c66930d73','2024-03-16 11:15:55','52d4c537-2310-47f0-bbca-dff919bccfa2',1,NULL,NULL,'红双喜','灰色地胶','良好',45),('58790181-1fa3-40bf-8060-c155d50a3de1','2024/02/27/9ab2c9f5-eca7-450a-801f-dc627e4c5b7b.jpg','8','体育馆4楼1室','体育馆4楼1室','2024-02-27 13:58:38','3ccbec15-588d-4821-8df4-4c3c66930d73','2024-03-16 11:16:05','52d4c537-2310-47f0-bbca-dff919bccfa2',1,'13:57:00','18:02:00','多尼克','灰色地胶','良好',35),('7de4a2ed-8db0-437c-b544-442d495e5aac','2024/02/20/2b5e8b19-17aa-4dfa-82e8-6fa565d939a1.png','7','体育馆2楼3号室','标准版球桌3','2024-02-20 23:20:54','3ccbec15-588d-4821-8df4-4c3c66930d73','2024-03-17 16:54:48','52d4c537-2310-47f0-bbca-dff919bccfa2',1,'09:00:00','20:57:00','双鱼','红色地胶','良好',25),('9d135721-7ef7-4862-9d5e-932f7c42cff5','2024/02/20/8203db54-eae7-4133-b189-13cd401da524.jpg','1','体育馆1楼1号室','豪华版球桌1','2024-02-20 23:16:31','3ccbec15-588d-4821-8df4-4c3c66930d73','2024-04-17 21:32:36','3ccbec15-588d-4821-8df4-4c3c66930d73',1,'09:00:00','23:49:00','红双喜','黑色地胶','良好',40),('ba9df0a1-5c0d-4535-82d4-fba3593aabf1','2024/02/20/e34dbab8-a5fd-40d5-832d-9330d1353b96.png','6','体育馆2楼2号室','标准版球桌2','2024-02-20 23:20:07','3ccbec15-588d-4821-8df4-4c3c66930d73','2024-03-31 10:27:04','3ccbec15-588d-4821-8df4-4c3c66930d73',1,'10:30:00','16:31:00','双鱼','木制','良好',16),('bf900744-6962-44c8-bdca-1c52ab52a81f','2024/02/28/1c7fd252-772f-4d60-8062-cfbe55d46edc.png','2','体育馆1楼2号室','豪华版球桌2','2024-02-20 23:17:29','3ccbec15-588d-4821-8df4-4c3c66930d73','2024-04-16 22:29:16','3ccbec15-588d-4821-8df4-4c3c66930d73',0,'00:00:00','23:50:00','双鱼','黑色地胶','良好',15),('ceb47b28-bd42-45b7-adec-5e55f6f92add','2024/02/20/dce72b64-6ef8-4242-9828-5b874f01b95b.jpg','5','体育馆2楼1号室','标准版球桌1','2024-02-20 23:19:16','3ccbec15-588d-4821-8df4-4c3c66930d73','2024-03-16 11:16:35','52d4c537-2310-47f0-bbca-dff919bccfa2',1,NULL,NULL,'挺拔','红色地胶','良好',15),('e3f0e7de-6dbd-4ceb-b726-88e5aeb734d4','pingpong.jpg','9','体育馆4楼3室','体育馆4楼3室','2024-04-16 23:12:39','3ccbec15-588d-4821-8df4-4c3c66930d73','2024-05-03 23:59:18','3ccbec15-588d-4821-8df4-4c3c66930d73',1,'00:02:00','23:59:00','双鱼','木制','良好',14);
/*!40000 ALTER TABLE `BALL_TABLE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CANDIDATE_RESERVE`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `CANDIDATE_RESERVE` (
  `GUID` varchar(50) NOT NULL COMMENT 'ID主键',
  `USER_ID` varchar(50) DEFAULT NULL COMMENT '用户ID',
  `TABLE_ID` varchar(50) DEFAULT NULL COMMENT '球桌ID',
  `START_DATE` varchar(20) DEFAULT NULL COMMENT '开始时间',
  `USE_TIME` int DEFAULT NULL COMMENT '预订时长，单位(小时)',
  `STATUS` int DEFAULT NULL COMMENT '候补状态：0 候补中，1 候补成功',
  `TABLE_CODE` varchar(20) DEFAULT NULL COMMENT '球桌编号',
  `PROCESS_START_DATE` varchar(10) GENERATED ALWAYS AS (substr(`START_DATE`,1,10)) VIRTUAL,
  `CREATE_DATE` varchar(20) DEFAULT NULL COMMENT '创建日期',
  `PAY_AMT` decimal(10,2) DEFAULT NULL COMMENT '支付金额',
  `OUT_TRADE_NO` varchar(100) DEFAULT NULL COMMENT '商户订单号',
  `TRADE_NO` varchar(100) DEFAULT NULL COMMENT '支付宝订单交易号',
  `USER_NAME` varchar(100) DEFAULT NULL COMMENT '用户名',
  PRIMARY KEY (`GUID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CANDIDATE_RESERVE`
--

LOCK TABLES `CANDIDATE_RESERVE` WRITE;
/*!40000 ALTER TABLE `CANDIDATE_RESERVE` DISABLE KEYS */;
INSERT INTO `CANDIDATE_RESERVE` (`GUID`, `USER_ID`, `TABLE_ID`, `START_DATE`, `USE_TIME`, `STATUS`, `TABLE_CODE`, `CREATE_DATE`, `PAY_AMT`, `OUT_TRADE_NO`, `TRADE_NO`, `USER_NAME`) VALUES ('18f26e43-20cd-47a4-a948-a2c64c3e0d17','52d4c537-2310-47f0-bbca-dff919bccfa2','9d135721-7ef7-4862-9d5e-932f7c42cff5','2024-04-15 11:00:00',3,1,'1','2024-04-14 22:33:25',60.00,'202404142233063871060109161950669918','2024041422001410020502688911','abc'),('207fd2f2-ba86-4881-97cf-3dfb750aaa07','3ccbec15-588d-4821-8df4-4c3c66930d73','7de4a2ed-8db0-437c-b544-442d495e5aac','2024-03-31 16:00:00',2,1,'7','2024-03-31 11:32:40',50.00,'2024033111321913519608052361796595167','2024033122001410020502533031',NULL),('2993cddf-40f1-47fd-b81a-93679bedddd1','52d4c537-2310-47f0-bbca-dff919bccfa2','7de4a2ed-8db0-437c-b544-442d495e5aac','2024-03-31 15:00:00',2,1,'7','2024-03-31 11:07:36',50.00,'202403311107207901060109161796595167','2024033122001410020502531662',NULL),('bdab9c2b-1554-478e-828c-5b9e510812bd','52d4c537-2310-47f0-bbca-dff919bccfa2','9d135721-7ef7-4862-9d5e-932f7c42cff5','2024-04-18 12:00:00',9,1,'1','2024-04-17 21:40:46',360.00,'202404172140246671060109161950669918','2024041722001410020502724901',NULL);
/*!40000 ALTER TABLE `CANDIDATE_RESERVE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CAUSUER`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `CAUSUER` (
  `GUID` varchar(50) NOT NULL COMMENT 'ID主键',
  `USER_NAME` varchar(100) DEFAULT NULL COMMENT '用户名，不能重复',
  `PASSWORD` varchar(100) DEFAULT NULL COMMENT '密码',
  `IMG_PATH` varchar(200) DEFAULT NULL COMMENT '用户图片路径',
  `MOBILE_PHONE` varchar(20) DEFAULT NULL COMMENT '手机号',
  `GENDER` int DEFAULT NULL COMMENT '性别 0 女，1 男',
  `AGE` int DEFAULT NULL COMMENT '年龄',
  `CREATE_DATE` varchar(20) DEFAULT NULL COMMENT '创建日期',
  `CREATE_USER` varchar(100) DEFAULT NULL COMMENT '创建用户（用户名）',
  `UPDATE_DATE` varchar(20) DEFAULT NULL COMMENT '更新日期',
  `UPDATE_USER` varchar(100) DEFAULT NULL COMMENT '更新用户（用户名）',
  `STATUS` int DEFAULT NULL COMMENT '是否可用 1 可用，0 不可用',
  `EMAIL` varchar(100) DEFAULT NULL COMMENT '用户邮箱',
  `ROLE_ID` varchar(50) DEFAULT NULL COMMENT '角色ID',
  PRIMARY KEY (`GUID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CAUSUER`
--

LOCK TABLES `CAUSUER` WRITE;
/*!40000 ALTER TABLE `CAUSUER` DISABLE KEYS */;
INSERT INTO `CAUSUER` VALUES ('2d64dd54-854f-4705-86af-8858dbf2b2b4','hello','123456','2024/03/15/3d0d01ae-3ef0-4a17-8cb5-715f42a52e45.jpg','',0,0,'2023-11-27 10:04:47',NULL,'2023-11-27 10:04:47',NULL,1,'','normal'),('3ccbec15-588d-4821-8df4-4c3c66930d73','admin','123456','2024/05/03/439dcd78-451d-43fb-a10a-f0470763b177.jpg','137689872642',0,21,'2024-01-13 22:03:29',NULL,NULL,NULL,1,'1602820115@qq.com','admin'),('3d217382-fcc1-4dc0-96ba-eb8b18eef968','testuser','123456','admin.png',NULL,NULL,NULL,'2024-03-13 17:17:08',NULL,'2024-03-13 17:17:08',NULL,1,NULL,'normal'),('52d4c537-2310-47f0-bbca-dff919bccfa2','abc','123456','2024/04/15/267f523b-add0-48ea-a024-58253ed33193.png','13471519267',1,22,'2024-02-21 21:47:30',NULL,'2024-02-21 21:47:30',NULL,1,'1602820115@qq.com','normal'),('55aaf87d-ecac-4139-b817-d012efd7df28','world','123456','admin.png',NULL,NULL,NULL,'2024-04-16 15:20:05',NULL,'2024-04-16 15:20:05',NULL,1,NULL,NULL),('9e9cd87d-5b02-40de-8a1f-6ae41b4705a6','kf','123456','admin.png',NULL,NULL,NULL,'2024-02-24 21:40:33',NULL,'2024-02-24 21:40:33',NULL,1,NULL,'customer'),('be018254-dc81-4e75-826b-a485b8e8b03d','su','123456','admin.png',NULL,0,0,'2024-01-13 22:01:24',NULL,'2024-01-13 22:01:24',NULL,1,NULL,'customer');
/*!40000 ALTER TABLE `CAUSUER` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `MATCH_INFO`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `MATCH_INFO` (
  `GUID` varchar(50) NOT NULL COMMENT '主键ID',
  `BILL_RESERVE_ID` varchar(50) DEFAULT NULL COMMENT '对应主预订单的id',
  `BILL_USER_ID` varchar(50) DEFAULT NULL COMMENT '主单用户ID',
  `BILL_USER_NAME` varchar(100) DEFAULT NULL COMMENT '主单用户名',
  `BILL_USER_OUT_TRADE_NO` varchar(100) DEFAULT NULL COMMENT '主单用户商户订单号',
  `BILL_USER_TRADE_NO` varchar(100) DEFAULT NULL COMMENT '主单用户支付交易号',
  `MATCH_USER_ID` varchar(50) DEFAULT NULL COMMENT '匹配用户ID',
  `MATCH_USER_NAME` varchar(100) DEFAULT NULL COMMENT '匹配用户名',
  `MATCH_USER_OUT_TRADE_NO` varchar(100) DEFAULT NULL COMMENT '匹配用户商户订单号',
  `MATCH_USER_TRADE_NO` varchar(100) DEFAULT NULL COMMENT '匹配用户支付交易号',
  `LEVEL` int DEFAULT NULL COMMENT '技术水平: 1 初级、2 中级、3 高级',
  `BILL_REMARK` varchar(200) DEFAULT NULL COMMENT '主单用户备注描述',
  `TABLE_ID` varchar(50) DEFAULT NULL COMMENT '球桌ID',
  `TABLE_CODE` varchar(20) DEFAULT NULL COMMENT '球桌编码',
  `START_DATE` varchar(20) DEFAULT NULL COMMENT '开始时间',
  `USE_TIME` int DEFAULT NULL COMMENT '预订时长，单位(小时)',
  `STATUS` int DEFAULT NULL COMMENT '匹配单状态： 0 正在匹配  1 预匹配，等待主单用户同意 2 匹配成功',
  `CREATE_DATE` varchar(20) DEFAULT NULL COMMENT '创建日期',
  `CREATE_USER` varchar(100) DEFAULT NULL COMMENT '创建用户（用户名）',
  `UPDATE_DATE` varchar(20) DEFAULT NULL COMMENT '更新日期',
  `UPDATE_USER` varchar(100) DEFAULT NULL COMMENT '更新用户（用户名）',
  `PROCESS_START_DATE` varchar(10) GENERATED ALWAYS AS (substr(`START_DATE`,1,10)) VIRTUAL,
  `MATCH_USER_NEED_PAY_AMT` decimal(10,0) DEFAULT NULL COMMENT '匹配用户应支付金额',
  `BILL_USER_NEED_PAY_AMT` decimal(10,0) DEFAULT NULL COMMENT '主单用户应支付金额',
  PRIMARY KEY (`GUID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `MATCH_INFO`
--

LOCK TABLES `MATCH_INFO` WRITE;
/*!40000 ALTER TABLE `MATCH_INFO` DISABLE KEYS */;
INSERT INTO `MATCH_INFO` (`GUID`, `BILL_RESERVE_ID`, `BILL_USER_ID`, `BILL_USER_NAME`, `BILL_USER_OUT_TRADE_NO`, `BILL_USER_TRADE_NO`, `MATCH_USER_ID`, `MATCH_USER_NAME`, `MATCH_USER_OUT_TRADE_NO`, `MATCH_USER_TRADE_NO`, `LEVEL`, `BILL_REMARK`, `TABLE_ID`, `TABLE_CODE`, `START_DATE`, `USE_TIME`, `STATUS`, `CREATE_DATE`, `CREATE_USER`, `UPDATE_DATE`, `UPDATE_USER`, `MATCH_USER_NEED_PAY_AMT`, `BILL_USER_NEED_PAY_AMT`) VALUES ('2935ed11-0252-4ab4-bfe6-0022d9bb8f97','93b788eb-e5e6-4c25-8ea5-ebcdb928d981','3ccbec15-588d-4821-8df4-4c3c66930d73','admin','202403302024484071960805236','2024033022001410020502529089','3ccbec15-588d-4821-8df4-4c3c66930d73','admin','2024041314324032719608052361950669918','2024041322001410020502672554',3,'','9d135721-7ef7-4862-9d5e-932f7c42cff5','1','2024-04-01 16:00:00',5,1,'2024-03-30 20:25:34','3ccbec15-588d-4821-8df4-4c3c66930d73','2024-04-13 14:33:02','3ccbec15-588d-4821-8df4-4c3c66930d73',50,50),('9bad00a1-85db-4917-accb-b8e5bd334d24','36e25d30-a227-4baa-b35b-375c0ee4792e','3ccbec15-588d-4821-8df4-4c3c66930d73','admin','202404171847429521960805236','2024041722001410020502724900',NULL,NULL,NULL,NULL,2,'ceshi','9d135721-7ef7-4862-9d5e-932f7c42cff5','1','2024-04-18 11:00:00',2,0,'2024-04-17 18:48:27','3ccbec15-588d-4821-8df4-4c3c66930d73','2024-04-17 18:49:26','52d4c537-2310-47f0-bbca-dff919bccfa2',40,40),('aba9eda1-da76-488c-8ab5-5cc3d615d538','33d78a07-658e-4648-9de6-54ae3e5bff4d','52d4c537-2310-47f0-bbca-dff919bccfa2','abc','202404142233063871060109161950669918','2024041422001410020502688911','3ccbec15-588d-4821-8df4-4c3c66930d73','admin','2024041521482447419608052361950669918','2024041522001410020502702200',NULL,NULL,'9d135721-7ef7-4862-9d5e-932f7c42cff5','1','2024-04-15 11:00:00',3,2,'2024-04-14 23:01:24','52d4c537-2310-47f0-bbca-dff919bccfa2','2024-04-15 21:48:40','3ccbec15-588d-4821-8df4-4c3c66930d73',30,30),('abee51f1-3f45-4f71-8f68-71f51a115a18','7f4acea1-b52a-443f-a163-837bbc129b5e','3ccbec15-588d-4821-8df4-4c3c66930d73','admin','202403311026028301960805236','2024033122001410020502526152','52d4c537-2310-47f0-bbca-dff919bccfa2','abc','202403311028172821060109161950669918','2024033122001410020502530266',3,'测试','9d135721-7ef7-4862-9d5e-932f7c42cff5','1','2024-03-31 11:00:00',3,2,'2024-03-31 10:27:21','3ccbec15-588d-4821-8df4-4c3c66930d73','2024-03-31 10:28:35','52d4c537-2310-47f0-bbca-dff919bccfa2',30,30),('b6cf9d80-077d-4d5d-b4eb-04fe201c42b2','98f4d254-46d4-4d00-8d1e-0ebf654e5ce9','3ccbec15-588d-4821-8df4-4c3c66930d73','admin','202404181342165171960805236','2024041822001410020502732810','52d4c537-2310-47f0-bbca-dff919bccfa2','abc','20240418135819513106010916150687963','2024041822001410020502736943',2,'测试匹配','21b2daf3-4e6a-46f8-b309-56871d8fa1f9','4','2024-04-18 14:00:00',5,2,'2024-04-18 13:44:12','3ccbec15-588d-4821-8df4-4c3c66930d73','2024-04-18 13:58:40','52d4c537-2310-47f0-bbca-dff919bccfa2',38,38);
/*!40000 ALTER TABLE `MATCH_INFO` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `MENU`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `MENU` (
  `GUID` varchar(50) NOT NULL COMMENT 'ID主键',
  `PARENT_ID` varchar(50) DEFAULT NULL COMMENT '父节点ID',
  `IS_LEAF` int DEFAULT NULL COMMENT '是否为叶子节点 0 否 1 是',
  `NAME` varchar(100) DEFAULT NULL COMMENT '菜单名称',
  `CODE` varchar(50) DEFAULT NULL COMMENT '菜单编码',
  `SORT` int DEFAULT NULL COMMENT '排序字段(数值越小，优先级越高)',
  `TYPE` int DEFAULT NULL COMMENT '类型 1: HEADR  菜单头 2: ITEM 菜单项',
  `URL` varchar(100) DEFAULT NULL COMMENT '请求路径',
  PRIMARY KEY (`GUID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `MENU`
--

LOCK TABLES `MENU` WRITE;
/*!40000 ALTER TABLE `MENU` DISABLE KEYS */;
INSERT INTO `MENU` VALUES ('001','',0,'预订','001',1,1,''),('001001','001',1,'场馆预订','001001',NULL,2,'/dashboard/'),('002','',0,'信息管理','002',3,1,''),('002001','002',1,'场地管理','002001',NULL,2,'/ballTable/manager'),('002002','002',1,'用户信息管理','002002',NULL,2,'/user/manager'),('002003','002',1,'订单管理','002003',NULL,2,'/orders/manager'),('003','',0,'球友匹配板','003',5,1,''),('003001','003',1,'球友匹配','003001',NULL,2,'/playerMatching/'),('003002','003',1,'我的发布','003002',NULL,2,'/playerMatching/user'),('003003','002',1,'我的候补','002004',NULL,2,'/candidateReserve/candidateReserveManager'),('004','',0,'消息管理','004',10,1,''),('004001','004',1,'消息','004001',NULL,2,'/message/'),('004002','004',1,'客服反馈','004002',NULL,2,'/message/customerService'),('004003','004',1,'最近沟通','004003',NULL,2,'/userChat/'),('004004','004',0,'推送全体消息','004004',5,2,'/message/pushAllUserMessage'),('005','',0,'菜单管理','005',10,1,''),('005001','005',1,'菜单配置','005001',3,2,'/menu/'),('005002','005',1,'角色菜单授权','005002',10,2,'/menu/roleMenu');
/*!40000 ALTER TABLE `MENU` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `MESSAGE`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `MESSAGE` (
  `GUID` varchar(50) NOT NULL COMMENT 'ID主键',
  `ORIGIN_ID` varchar(50) DEFAULT NULL COMMENT '发送者ID',
  `ORIGIN_NAME` varchar(50) DEFAULT NULL COMMENT '发送者名称',
  `TARGET_ID` varchar(50) DEFAULT NULL COMMENT '接收者ID',
  `TARGET_NAME` varchar(50) DEFAULT NULL COMMENT '接收者名称',
  `TYPE` int DEFAULT NULL COMMENT '消息类型：1 单点消息 2 全体消息',
  `CATEGORY` int DEFAULT NULL COMMENT '消息类别：\r\n1 系统消息 \r\n2 用户消息 \r\n3 用户请求连接 \r\n4 拒绝连接\r\n5 确认连接\r\n6 用户聊天消息\r\n7 系统公告',
  `VALUE` varchar(300) DEFAULT NULL COMMENT '消息内容',
  `CREATE_TIME` varchar(20) DEFAULT NULL COMMENT '创建时间',
  `IS_READ` int DEFAULT NULL COMMENT '消息是否已读  0 未读  1 已读',
  `PROCESS_CREATE_DATE` varchar(10) GENERATED ALWAYS AS (substr(`CREATE_TIME`,1,10)) VIRTUAL,
  PRIMARY KEY (`GUID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `MESSAGE`
--

LOCK TABLES `MESSAGE` WRITE;
/*!40000 ALTER TABLE `MESSAGE` DISABLE KEYS */;
INSERT INTO `MESSAGE` (`GUID`, `ORIGIN_ID`, `ORIGIN_NAME`, `TARGET_ID`, `TARGET_NAME`, `TYPE`, `CATEGORY`, `VALUE`, `CREATE_TIME`, `IS_READ`) VALUES ('012afd5b-d975-432c-8059-22cfc8ee2e92','system','system','3ccbec15-588d-4821-8df4-4c3c66930d73',NULL,1,1,'[预订成功]\n您已成功预订7号球桌。\n使用时间为：2024-03-31 16:00:00\n使用时长(小时)：2','2024-03-31 11:41:25',0),('06b5b28d-398f-4156-84d3-096db6b19063','system','system',NULL,NULL,2,1,'测试公告3','2024-05-03 23:11:38',NULL),('07c27fd7-bdfe-4196-b247-0c2c301dc06e',NULL,NULL,NULL,NULL,2,1,'发发发',NULL,NULL),('09bd1f99-172e-475f-8531-9b3e3938e743','system','system','3ccbec15-588d-4821-8df4-4c3c66930d73',NULL,1,1,'您已成功取消订单，退款金额为[75.00]','2024-03-28 00:20:49',0),('0b9322c7-d341-49ab-9dcc-6657f171cda0','system','system','3ccbec15-588d-4821-8df4-4c3c66930d73',NULL,1,1,'[预订成功]\n您已成功预订1号球桌。\n使用时间为：2024-03-23 10:00:00\n使用时长(小时)：6','2024-03-23 21:21:15',1),('0c73a433-261e-48b6-aa1f-e4b91f301a61','system','system','3ccbec15-588d-4821-8df4-4c3c66930d73',NULL,1,1,'[预订成功]\n您已成功预订7号球桌。\n使用时间为：2024-04-18 11:00:00\n使用时长(小时)：2','2024-04-17 19:15:03',0),('113e07cd-aa51-42f6-8831-e3891aff1c11','system','system','3ccbec15-588d-4821-8df4-4c3c66930d73',NULL,1,1,'您已成功取消订单，退款金额为[100.00]','2024-04-14 21:57:51',0),('12f5d829-1a09-4613-8593-8d540db8ea09','system','system','3ccbec15-588d-4821-8df4-4c3c66930d73',NULL,1,1,'[预订成功]\n您已成功预订4号球桌。\n使用时间为：2024-03-29 14:00:00\n使用时长(小时)：3','2024-03-29 11:15:00',0),('158e1ad7-ec58-45da-9778-91228abd52dd','system','system','3ccbec15-588d-4821-8df4-4c3c66930d73',NULL,1,1,'[预订成功]\n您已成功预订2号球桌。\n使用时间为：2024-03-28 16:00:00\n使用时长(小时)：5','2024-03-28 00:20:08',0),('16e15ae9-e1fb-45da-8281-07637285be66','3ccbec15-588d-4821-8df4-4c3c66930d73','admin','52d4c537-2310-47f0-bbca-dff919bccfa2','abc',1,6,'你好','2024-04-18 14:05:42',0),('1711a4b4-0e0a-40db-bd5f-d0a1b80d2993','system','system','3ccbec15-588d-4821-8df4-4c3c66930d73',NULL,1,1,'您已匹配球友成功，差价[60]已退回您的支付宝账户!','2024-03-25 21:34:33',1),('196701e4-cf2e-4e78-84e2-b688234f23b5','system','system','3ccbec15-588d-4821-8df4-4c3c66930d73',NULL,1,1,'您已拒绝来自用户【abc】的匹配请求，差价[60]已退回匹配用户的支付宝账户!','2024-03-25 21:17:02',1),('197928ff-eebc-42d9-9f6b-2e194129ab85','system','system','3ccbec15-588d-4821-8df4-4c3c66930d73',NULL,1,1,'您已成功取消订单，退款金额为[80.00]','2024-04-14 21:47:56',0),('1e881867-80e3-47a1-9970-908371464cca','system','system','3ccbec15-588d-4821-8df4-4c3c66930d73',NULL,1,1,'您已成功取消订单，退款金额为[45.00]','2024-03-29 11:13:16',0),('1ef2b056-363c-44f8-aac0-8b7af042e571','system','system','3ccbec15-588d-4821-8df4-4c3c66930d73',NULL,1,1,'您已成功取消订单，退款金额为[20.00]','2024-03-30 19:33:07',0),('2121d0e5-4933-44bb-bb7d-ee4ad63d422d','system','system','52d4c537-2310-47f0-bbca-dff919bccfa2',NULL,1,1,'您已成功取消订单，退款金额为[38.00]','2024-03-30 20:35:11',0),('22ef2aa3-a554-4831-8c24-915a322f67ca','system','system','52d4c537-2310-47f0-bbca-dff919bccfa2',NULL,1,1,'[预订成功]\n您已成功预订1号球桌。\n使用时间为：2024-03-28 11:00:00\n使用时长(小时)：3','2024-03-27 23:46:45',0),('26d58aee-e4b3-42a0-8ed2-c2d434fb0508','system','system','3ccbec15-588d-4821-8df4-4c3c66930d73',NULL,1,1,'[预订成功]\n您已成功预订1号球桌。\n使用时间为：2024-04-01 16:00:00\n使用时长(小时)：5','2024-03-30 20:25:14',0),('290eb31d-15b3-4641-8105-a7da97b5f2e0','52d4c537-2310-47f0-bbca-dff919bccfa2','abc','undefined','undefined',1,6,'haha','2024-03-22 14:09:12',0),('2bbaf7cd-902e-4f7a-b9cc-99ef26ddcda8','system','system','52d4c537-2310-47f0-bbca-dff919bccfa2',NULL,1,1,'您已匹配球友成功，差价[38]已退回您的支付宝账户!','2024-03-27 15:45:07',0),('2bc76cce-d006-410b-a293-6ba342e60a5d','system','system','3ccbec15-588d-4821-8df4-4c3c66930d73',NULL,1,1,'[预订成功]\n您已成功预订1号球桌。\n使用时间为：2024-03-22 10:00:00\n使用时长(小时)：1','2024-03-22 21:20:18',1),('2d6b26a9-ab5d-43be-a3ee-4f0d8579332a','system','system','52d4c537-2310-47f0-bbca-dff919bccfa2',NULL,1,1,'候补成功!','2024-03-29 11:15:40',0),('2f78e47b-289c-4bcd-84dd-54d8f866a36b','system','system','3ccbec15-588d-4821-8df4-4c3c66930d73',NULL,1,1,'[预订成功]\n您已成功预订1号球桌。\n使用时间为：2024-03-30 20:00:00\n使用时长(小时)：1','2024-03-30 19:31:55',0),('3020099b-c8fd-4ac6-aa98-bccb3b1e9310','system','system','3ccbec15-588d-4821-8df4-4c3c66930d73',NULL,1,1,'[预订成功]\n您已成功预订1号球桌。\n使用时间为：2024-04-18 11:00:00\n使用时长(小时)：2','2024-04-17 18:48:06',0),('3057cf71-9fc7-4f13-9a55-a6fd143de6bb','system','system','3ccbec15-588d-4821-8df4-4c3c66930d73',NULL,1,1,'[预订成功]\n您已成功预订1号球桌。\n使用时间为：2024-03-30 16:00:00\n使用时长(小时)：3','2024-03-30 11:19:33',0),('33c10bb9-0c9f-4f0f-8ed6-daf1f4639f5c','system','system','3ccbec15-588d-4821-8df4-4c3c66930d73',NULL,1,1,'[预订成功]\n您已成功预订7号球桌。\n使用时间为：2024-03-30 10:00:00\n使用时长(小时)：3','2024-03-29 11:47:39',0),('36141b11-8512-41f1-a8b7-b257463c9235','system','system','3ccbec15-588d-4821-8df4-4c3c66930d73',NULL,1,1,'您的拼友abc取消了订单，将恢复您的匹配拼桌球友，请知悉','2024-04-17 18:50:32',0),('36e475e9-fb62-43bc-bffc-1abf6b043e68','system','system','2d64dd54-854f-4705-86af-8858dbf2b2b4',NULL,1,1,'[预订成功]\n您已成功预订1号球桌。\n使用时间为：2024-03-15 11:00:00\n使用时长(小时)：2','2024-03-15 11:33:51',0),('38cb874b-b0cd-4e9a-adf2-002c9ce4d0c3','system','system','3ccbec15-588d-4821-8df4-4c3c66930d73',NULL,1,1,'[预订成功]\n您已成功预订1号球桌。\n使用时间为：2024-03-28 12:00:00\n使用时长(小时)：1','2024-03-28 00:20:49',0),('3beec9e7-063f-4541-83fa-7b926db20476','system','system','52d4c537-2310-47f0-bbca-dff919bccfa2',NULL,1,1,'[预订成功]\n您已成功预订7号球桌。\n使用时间为：2024-03-28 11:00:00\n使用时长(小时)：4','2024-03-27 16:09:33',0),('3c6af611-1024-4c4c-ab9e-de3f8f3eba14','system','system','52d4c537-2310-47f0-bbca-dff919bccfa2',NULL,1,1,'候补成功!','2024-03-31 11:26:05',0),('3cf480f9-13db-4541-9164-a37dcb06786f','system','system','3ccbec15-588d-4821-8df4-4c3c66930d73',NULL,1,1,'您已成功取消订单，退款金额为[25.00]','2024-03-31 11:26:06',0),('3f6656fb-c515-4420-846d-431898ec1f98','system','system','3ccbec15-588d-4821-8df4-4c3c66930d73',NULL,1,1,'[预订成功]\n您已成功预订1号球桌。\n使用时间为：2024-04-15 10:00:00\n使用时长(小时)：6','2024-04-14 22:36:33',0),('40b2838b-d3c5-4791-9297-c0d644432ab3','system','system','52d4c537-2310-47f0-bbca-dff919bccfa2',NULL,1,1,'候补成功!','2024-03-29 10:53:20',0),('40f42cd9-95a9-46b1-a5a7-4359d3dc0687','52d4c537-2310-47f0-bbca-dff919bccfa2','abc','3d217382-fcc1-4dc0-96ba-eb8b18eef968','testuser',1,6,'haha','2024-03-21 23:00:45',0),('43f7d01f-38ea-462e-9357-b3b306424059','system','system','3ccbec15-588d-4821-8df4-4c3c66930d73',NULL,1,1,'[预订成功]\n您已成功预订2号球桌。\n使用时间为：2024-03-28 11:00:00\n使用时长(小时)：2','2024-03-30 20:29:30',0),('45033f00-4aea-45e3-abcf-a70b3154a0a7','system','system','52d4c537-2310-47f0-bbca-dff919bccfa2',NULL,1,1,'[预订成功]\n您已成功预订1号球桌。\n使用时间为：2024-03-28 17:00:00\n使用时长(小时)：3','2024-03-28 00:01:19',0),('467842eb-5413-4894-aa6a-6e882101d4a4','system','system','2d64dd54-854f-4705-86af-8858dbf2b2b4',NULL,1,1,'[预订成功]\n您已成功预订1号球桌。\n使用时间为：2024-03-14 11:00:00\n使用时长(小时)：4','2024-03-14 11:40:58',0),('46ce7031-f835-4ba8-839f-3dfa140bfa06','system','system','52d4c537-2310-47f0-bbca-dff919bccfa2',NULL,1,1,'[预订成功]\n您已成功预订4号球桌。\n使用时间为：2024-03-25 09:00:00\n使用时长(小时)：2','2024-03-23 12:16:47',1),('487a7fba-2df7-4338-8c0b-6da3a7f13287','system','system','3ccbec15-588d-4821-8df4-4c3c66930d73',NULL,1,1,'[预订成功]\n您已成功预订1号球桌。\n使用时间为：2024-04-17 10:00:00\n使用时长(小时)：4','2024-04-14 21:39:47',0),('48ecde4a-b425-4425-8074-7433d26d7766','system','system','3ccbec15-588d-4821-8df4-4c3c66930d73',NULL,1,1,'[预订成功]\n您已成功预订4号球桌。\n使用时间为：2024-03-27 11:00:00\n使用时长(小时)：3','2024-03-27 10:37:23',0),('4e4f02c7-858a-4eed-9d60-3fc576636618','3d217382-fcc1-4dc0-96ba-eb8b18eef968','testuser','2d64dd54-854f-4705-86af-8858dbf2b2b4','hello',1,6,'cwe','2024-03-14 12:00:41',0),('4f06f9d2-d40d-4f5f-aa27-65d8c8c5d179','system','system','3ccbec15-588d-4821-8df4-4c3c66930d73',NULL,1,1,'[预订成功]\n您已成功预订1号球桌。\n使用时间为：2024-03-25 15:00:00\n使用时长(小时)：5','2024-03-25 21:32:44',0),('507cd92a-0f5d-46a5-904e-e14497f64bd6','system','system','52d4c537-2310-47f0-bbca-dff919bccfa2',NULL,1,1,'您已成功取消订单，退款金额为[8.00]','2024-04-17 18:25:20',0),('50fc5360-b9ed-487c-8bfb-4c18de7a4bee','system','system','3ccbec15-588d-4821-8df4-4c3c66930d73',NULL,1,1,'您已成功取消订单，退款金额为[30.00]','2024-03-27 23:02:56',0),('516da61c-34ca-4fda-8379-189de6b3231c','system','system','52d4c537-2310-47f0-bbca-dff919bccfa2',NULL,1,1,'[预订成功]\n您已成功预订1号球桌。\n使用时间为：2024-03-27 11:00:00\n使用时长(小时)：4','2024-03-27 22:25:10',0),('51a8b7fe-5200-4612-b5b6-e50b0146759e','system','system','3ccbec15-588d-4821-8df4-4c3c66930d73',NULL,1,1,'[预订成功]\n您已成功预订2号球桌。\n使用时间为：2024-03-28 11:00:00\n使用时长(小时)：2','2024-03-30 20:34:08',0),('52352829-280e-4365-9c8b-cfc4eb24bb7a','system','system','3ccbec15-588d-4821-8df4-4c3c66930d73',NULL,1,1,'您已成功取消订单，退款金额为[50.00]','2024-03-27 22:10:26',0),('53710d31-e686-4e24-8f39-b3788d3a48fc','system','system','3ccbec15-588d-4821-8df4-4c3c66930d73',NULL,1,1,'[预订成功]\n您已成功预订1号球桌。\n使用时间为：2024-04-14 11:00:00\n使用时长(小时)：3','2024-04-14 21:38:50',0),('5570c731-6242-4635-aae1-f65f0fe5d7b3','system','system','3ccbec15-588d-4821-8df4-4c3c66930d73',NULL,1,1,'[预订成功]\n您已成功预订1号球桌。\n使用时间为：2024-03-28 11:00:00\n使用时长(小时)：7','2024-03-25 21:57:53',1),('55da6f30-4eab-4b20-af2f-04ccd23b7bcd','system','system',NULL,NULL,2,7,'五一劳动节开放时间：5月1-3日进行闭馆，5月4日以后正常营业！','2024-05-04 00:27:10',NULL),('56b397b0-a82a-4b23-a67c-7daa5db6d4f0','system','system','3ccbec15-588d-4821-8df4-4c3c66930d73',NULL,1,1,'[预订成功]\n您已成功预订4号球桌。\n使用时间为：2024-03-29 12:00:00\n使用时长(小时)：4','2024-03-29 10:36:06',0),('57cd0374-e7d3-414b-937e-0f32f0497188','system','system','52d4c537-2310-47f0-bbca-dff919bccfa2',NULL,1,1,'[预订成功]\n您已成功预订4号球桌。\n使用时间为：2024-03-19 11:00:00\n使用时长(小时)：4','2024-03-19 19:09:39',1),('585193d5-fba6-4fac-8d13-68b085b666c3','system','system','3ccbec15-588d-4821-8df4-4c3c66930d73',NULL,1,1,'您已成功取消订单，退款金额为[80.00]','2024-04-17 21:42:12',0),('59f2971c-7783-4473-a806-ac902760fc8d','system','system','52d4c537-2310-47f0-bbca-dff919bccfa2',NULL,1,1,'您已成功取消订单，退款金额为[60.00]','2024-03-27 23:48:13',0),('5c928c07-dacb-410e-b1e0-a8f57331fc8b','system','system','52d4c537-2310-47f0-bbca-dff919bccfa2',NULL,1,1,'[预订成功]\n您已成功预订1号球桌。\n使用时间为：2024-02-28 13:00:00\n使用时长(小时)：2','2024-03-01 15:10:42',1),('5e199130-e0f9-414c-89c0-36a3f6c5432a','system','system','3ccbec15-588d-4821-8df4-4c3c66930d73',NULL,1,1,'[预订成功]\n您已成功预订1号球桌。\n使用时间为：2024-03-31 11:00:00\n使用时长(小时)：3','2024-03-31 10:26:34',1),('5f1b7813-af17-4e76-8997-f080baed02f9','system','system','3ccbec15-588d-4821-8df4-4c3c66930d73',NULL,1,1,'候补成功!','2024-03-28 00:20:49',0),('60339214-3afc-43cc-8dc8-49e1872535e5','system','system','3ccbec15-588d-4821-8df4-4c3c66930d73',NULL,1,1,'候补成功!','2024-03-01 15:10:42',1),('6160e115-3770-4e98-b784-6151c8965618','system','system','52d4c537-2310-47f0-bbca-dff919bccfa2',NULL,1,1,'[预订成功]\n您已成功预订2号球桌。\n使用时间为：2024-03-28 11:00:00\n使用时长(小时)：4','2024-03-27 22:31:15',0),('62772218-5c79-4bb3-b1fd-33bcd5923dcd','system','system','3ccbec15-588d-4821-8df4-4c3c66930d73',NULL,1,1,'[预订成功]\n您已成功预订1号球桌。\n使用时间为：2024-04-15 12:00:00\n使用时长(小时)：6','2024-04-14 22:06:43',0),('627de3d9-f0b5-41b8-8bc7-bf8ced8c54c3','system','system','3ccbec15-588d-4821-8df4-4c3c66930d73',NULL,1,1,'[预订成功]\n您已成功预订8号球桌。\n使用时间为:2024-03-01 10:00:00','2024-03-01 15:07:58',1),('63ef7e88-4157-42bc-ba42-eb24d8271a92','system','system','52d4c537-2310-47f0-bbca-dff919bccfa2',NULL,1,1,'[预订成功]\n您已成功预订4号球桌。\n使用时间为：2024-03-23 09:00:00\n使用时长(小时)：1','2024-03-23 12:30:24',1),('64a5067c-f94d-45be-8b30-b7240b55b9eb','system','system','3d217382-fcc1-4dc0-96ba-eb8b18eef968',NULL,1,1,'[预订成功]\n您已成功预订8号球桌。\n使用时间为：2024-03-13 11:00:00\n使用时长(小时)：1','2024-03-13 17:19:05',0),('6552a6b0-5af0-4310-9059-cc302d230e56','system','system','52d4c537-2310-47f0-bbca-dff919bccfa2',NULL,1,1,'您的拼友admin取消了订单，将恢复您的匹配拼桌球友，请知悉','2024-03-27 16:16:38',0),('656282ca-2c4b-4a7f-b730-467d3842cf13','system','system','3ccbec15-588d-4821-8df4-4c3c66930d73',NULL,1,1,'[预订成功]\n您已成功预订1号球桌。\n使用时间为：2024-04-15 10:00:00\n使用时长(小时)：5','2024-04-14 22:42:02',0),('656e08e5-f97c-4ff8-bf55-bac75e8ebb34','system','system','3ccbec15-588d-4821-8df4-4c3c66930d73',NULL,1,1,'[预订成功]\n您已成功预订1号球桌。\n使用时间为：2024-03-04 13:00:00\n使用时长(小时)：3','2024-03-01 23:46:47',1),('6691d29d-f05d-41ad-a7c0-b8d1cc1b5805','system','system','3ccbec15-588d-4821-8df4-4c3c66930d73',NULL,1,1,'您已成功取消订单，退款金额为[25.00]','2024-03-31 11:24:04',0),('676baf57-8d3c-4cf3-a73b-50e1db50125c','system','system','52d4c537-2310-47f0-bbca-dff919bccfa2',NULL,1,1,'[预订成功]\n您已成功预订7号球桌。\n使用时间为：2024-03-31 15:00:00\n使用时长(小时)：2','2024-03-31 11:26:05',0),('67ebef30-0c83-44a9-8a08-80b89cf3212c','system','system','52d4c537-2310-47f0-bbca-dff919bccfa2',NULL,1,1,'候补成功!','2024-04-17 21:49:32',0),('689731e8-94eb-4f26-aea0-adb3a99d9e02','system','system','3ccbec15-588d-4821-8df4-4c3c66930d73',NULL,1,1,'[预订成功]\n您已成功预订7号球桌。\n使用时间为：2024-03-22 11:00:00\n使用时长(小时)：2','2024-03-22 14:31:37',1),('6bdba072-97c5-4169-80b8-8b5f714fb826','system','system','52d4c537-2310-47f0-bbca-dff919bccfa2',NULL,1,1,'候补成功!','2024-03-28 11:19:54',0),('6cf3af87-7ba9-4264-bdab-969b6e480f2b','system','system','3ccbec15-588d-4821-8df4-4c3c66930d73',NULL,1,1,'您已成功取消订单，退款金额为[50.00]','2024-04-17 19:16:37',0),('6f1364db-a3de-4639-a21a-ddc3c07d1c1d','system','system','52d4c537-2310-47f0-bbca-dff919bccfa2',NULL,1,1,'您已成功取消订单，退款金额为[60.00]','2024-03-28 00:04:18',0),('71d14c87-36ac-45de-8f21-3ea17d420722','system','system','52d4c537-2310-47f0-bbca-dff919bccfa2',NULL,1,1,'候补成功!','2024-04-14 23:00:30',0),('73be0f80-8fa5-45a9-a1ae-05d15399937e',NULL,NULL,NULL,NULL,2,1,'吃饭的事',NULL,NULL),('75eb1342-61fe-4e46-8cba-4be13663843e','system','system','3ccbec15-588d-4821-8df4-4c3c66930d73',NULL,1,1,'[预订成功]\n您已成功预订7号球桌。\n使用时间为：2024-03-30 20:00:00\n使用时长(小时)：1','2024-03-30 19:14:19',0),('77bcaca7-423a-4885-b86a-7d26fcddd069','system','system','52d4c537-2310-47f0-bbca-dff919bccfa2',NULL,1,1,'您的匹配请求已撤回,金额【38】已退回您的账户','2024-04-18 13:51:30',0),('7b729148-2e4f-4e80-8f08-5abd0a61b262','system','system','3ccbec15-588d-4821-8df4-4c3c66930d73',NULL,1,1,'候补成功!','2024-03-30 20:35:11',0),('7c4a7e1c-7312-4b3d-9035-50bdfb385b49','system','system',NULL,NULL,2,1,'范德萨','2024-05-03 23:04:37',NULL),('7dd74ade-7a0b-43f4-b49f-5f9b513e3494','system','system','52d4c537-2310-47f0-bbca-dff919bccfa2',NULL,1,1,'[预订成功]\n您已成功预订2号球桌。\n使用时间为：2024-03-19 15:00:00\n使用时长(小时)：5','2024-03-19 18:18:53',1),('7f07e561-6a9f-459e-b266-1d7b00c6f5e0','system','system','52d4c537-2310-47f0-bbca-dff919bccfa2',NULL,1,1,'[预订成功]\n您已成功预订1号球桌。\n使用时间为：2024-04-15 11:00:00\n使用时长(小时)：3','2024-04-14 23:00:30',0),('821dfdae-7c42-4839-b206-f29dd9764ae3','system','system','52d4c537-2310-47f0-bbca-dff919bccfa2',NULL,1,1,'您已成功取消订单，退款金额为[60.00]','2024-03-27 23:33:01',0),('8354e51f-cc10-4fbc-82d4-959d3d313db3','system','system','3ccbec15-588d-4821-8df4-4c3c66930d73',NULL,1,1,'您已匹配球友成功，差价[70]已退回您的支付宝账户!','2024-03-25 22:04:58',1),('836b7224-5cf3-4017-9cad-68aaef73e294','system','system',NULL,NULL,2,1,'范德萨',NULL,NULL),('84aa3066-35ad-4f21-ac99-58b4da44d5fd','system','system','52d4c537-2310-47f0-bbca-dff919bccfa2',NULL,1,1,'[预订成功]\n您已成功预订1号球桌。\n使用时间为：2024-03-20 12:00:00\n使用时长(小时)：4','2024-03-20 00:10:28',1),('930be0ed-e28c-4a20-846f-6c58aeb92702','system','system','52d4c537-2310-47f0-bbca-dff919bccfa2',NULL,1,1,'您已成功取消订单，退款金额为[13.00]','2024-03-30 19:44:50',0),('93a3cf40-3df3-4875-90a2-971c1dca4934','system','system','52d4c537-2310-47f0-bbca-dff919bccfa2',NULL,1,1,'您已成功取消订单，退款金额为[38.00]','2024-03-30 20:34:08',0),('94670c3b-d34f-4840-9883-e155a0548d6b','system','system','3ccbec15-588d-4821-8df4-4c3c66930d73',NULL,1,1,'[预订成功]\n您已成功预订1号球桌。\n使用时间为：2024-04-18 15:00:00\n使用时长(小时)：3','2024-04-17 21:39:43',0),('94a51513-cf68-43cd-955a-8356b2bd3477','system','system','3d217382-fcc1-4dc0-96ba-eb8b18eef968',NULL,1,1,'[预订成功]\n您已成功预订2号球桌。\n使用时间为：2024-03-16 11:00:00\n使用时长(小时)：4','2024-03-13 17:22:01',0),('96f71f0f-22da-4465-8b0a-88215ea5d9f6','system','system','3ccbec15-588d-4821-8df4-4c3c66930d73',NULL,1,1,'[预订成功]\n您已成功预订4号球桌。\n使用时间为：2024-03-29 15:00:00\n使用时长(小时)：3','2024-03-29 11:00:55',0),('96ff1173-f5a4-4e5a-bc92-06a4c6ca4742','system','system','52d4c537-2310-47f0-bbca-dff919bccfa2',NULL,1,1,'您已成功取消订单，退款金额为[50.00]','2024-03-31 11:41:25',0),('97cd4044-cb6d-49a3-9fd0-cbf399258b83','system','system','52d4c537-2310-47f0-bbca-dff919bccfa2',NULL,1,1,'您已成功取消订单，退款金额为[38.00]','2024-03-30 20:29:30',0),('98948912-7e4b-449a-b03c-eecdbb91b891','system','system','52d4c537-2310-47f0-bbca-dff919bccfa2',NULL,1,1,'您的匹配请求已撤回,金额【38】已退回您的账户','2024-03-30 17:40:43',0),('9d2ae2f3-b6b9-451b-8ee4-967e554a8fd3','system','system','3ccbec15-588d-4821-8df4-4c3c66930d73',NULL,1,1,'候补成功!','2024-03-30 20:29:30',0),('9d4009b2-0fc2-4f34-9fa8-658d94a10bb4','system','system','3ccbec15-588d-4821-8df4-4c3c66930d73',NULL,1,1,'您已成功取消订单，退款金额为[null]','2024-03-28 11:19:54',0),('9e289336-3c38-47a1-973e-237d0040f3c0','system','system','52d4c537-2310-47f0-bbca-dff919bccfa2',NULL,1,1,'您的匹配请求已撤回,金额【38】已退回您的账户','2024-04-18 13:55:23',0),('a054ecd1-a743-4e30-9e97-c0c33517a270','system','system','52d4c537-2310-47f0-bbca-dff919bccfa2',NULL,1,1,'您的拼友admin取消了订单，将恢复您的匹配拼桌球友，请知悉','2024-03-27 23:11:44',0),('a07ecb21-02dd-4a8b-bc15-03f0a2f04922','system','system','52d4c537-2310-47f0-bbca-dff919bccfa2',NULL,1,1,'您的匹配请求已被主单用户[admin]拒绝,金额已退回您的账户','2024-03-25 21:17:02',1),('a0e71f77-2094-4e75-96dc-5e3fa58d6ae4','system','system','3ccbec15-588d-4821-8df4-4c3c66930d73',NULL,1,1,'[预订成功]\n您已成功预订2号球桌。\n使用时间为：2024-03-30 15:00:00\n使用时长(小时)：5','2024-03-30 00:23:03',0),('a0eac8e9-8ff4-4432-88e8-e59b32a3bf10','system','system','3ccbec15-588d-4821-8df4-4c3c66930d73',NULL,1,1,'您已成功取消订单，退款金额为[30.00]','2024-03-27 23:11:44',0),('a2770c38-61e2-467c-8aac-8ee8ea3b84d3','system','system','3ccbec15-588d-4821-8df4-4c3c66930d73',NULL,1,1,'您的拼友abc取消了订单，将恢复您的匹配拼桌球友，请知悉','2024-04-17 18:25:20',0),('a28bb032-a7b9-48ac-8b1d-a30bb04aac23','system','system','3ccbec15-588d-4821-8df4-4c3c66930d73',NULL,1,1,'[预订成功]\n您已成功预订2号球桌。\n使用时间为：2024-03-28 11:00:00\n使用时长(小时)：2','2024-03-30 20:35:11',0),('a3fa587d-966d-4183-8621-6400c13af960','system','system','3ccbec15-588d-4821-8df4-4c3c66930d73',NULL,1,1,'您已成功取消订单，退款金额为[45.00]','2024-03-29 10:53:20',0),('a5080eec-a732-44ad-89d2-b916075f3d75','system','system','3ccbec15-588d-4821-8df4-4c3c66930d73',NULL,1,1,'您的拼友abc取消了订单，将恢复您的匹配拼桌球友，请知悉','2024-03-30 20:29:30',0),('a849dd77-7176-4041-9dd6-4121d8ba1abf','system','system','3ccbec15-588d-4821-8df4-4c3c66930d73',NULL,1,1,'[预订成功]\n您已成功预订1号球桌。\n使用时间为：2024-03-06 10:00:00\n使用时长(小时)：5','2024-03-01 22:11:00',1),('aa343305-f6eb-40df-ab4c-3ee5fe82551e','system','system','3ccbec15-588d-4821-8df4-4c3c66930d73',NULL,1,1,'[预订成功]\n您已成功预订1号球桌。\n使用时间为：2024-04-15 10:00:00\n使用时长(小时)：5','2024-04-14 22:59:30',0),('af0f7e15-68e5-42f5-a543-d290fbd7c232','system','system','test',NULL,1,1,'测试保存系统消息','2024-03-01 14:17:07',0),('b020bc4a-1ee9-46de-8aaa-5b3467b24289','system','system','52d4c537-2310-47f0-bbca-dff919bccfa2',NULL,1,1,'候补成功!','2024-03-01 15:10:42',1),('b0c659d1-9244-4f2d-a065-3c6b550362b0','system','system','3ccbec15-588d-4821-8df4-4c3c66930d73',NULL,1,1,'[预订成功]\n您已成功预订4号球桌。\n使用时间为：2024-04-18 14:00:00\n使用时长(小时)：5','2024-04-18 13:42:45',1),('b52e5a5d-d18d-4306-abec-2bd2e540b376','system','system','3ccbec15-588d-4821-8df4-4c3c66930d73',NULL,1,1,'[预订成功]\n您已成功预订7号球桌。\n使用时间为：2024-03-29 17:00:00\n使用时长(小时)：3','2024-03-29 11:44:06',0),('b61b18c9-83ea-462d-b9f9-41f1b6e603b3','system','system',NULL,NULL,2,1,'官方说的发给大','2024-05-03 23:04:48',NULL),('b67035a4-9d48-44e7-b272-68ba86170bec','system','system','3ccbec15-588d-4821-8df4-4c3c66930d73',NULL,1,1,'您已成功取消订单，退款金额为[60.00]','2024-03-29 10:37:41',0),('b8f12488-bba8-4fcc-886b-a612b29fcf8f','system','system','52d4c537-2310-47f0-bbca-dff919bccfa2',NULL,1,1,'[预订成功]\n您已成功预订4号球桌。\n使用时间为：2024-03-27 14:00:00\n使用时长(小时)：5','2024-03-27 15:43:36',0),('bb78f815-17bf-4cc3-b536-5c8d57d31d1c','system','system','3ccbec15-588d-4821-8df4-4c3c66930d73',NULL,1,1,'[预订成功]\n您已成功预订1号球桌。\n使用时间为：2024-04-18 11:00:00\n使用时长(小时)：2','2024-04-17 21:31:34',0),('bc15d44f-390c-425a-88a7-5f12bd3276a6','system','system','3ccbec15-588d-4821-8df4-4c3c66930d73',NULL,1,1,'您已成功取消订单，退款金额为[30.00]','2024-03-27 22:38:06',0),('bc9cce89-7e1a-434b-8af3-58fc650e714e','system','system','3ccbec15-588d-4821-8df4-4c3c66930d73',NULL,1,1,'[预订成功]\n您已成功预订7号球桌。\n使用时间为：2024-03-31 16:00:00\n使用时长(小时)：1','2024-03-31 11:25:41',0),('bd5e37cd-dc24-4042-b52e-acba300ada61','system','system','3ccbec15-588d-4821-8df4-4c3c66930d73',NULL,1,1,'候补成功!','2024-03-30 20:34:08',0),('bf296e43-354b-4cfa-a99a-da7af76aee8c','system','system','3ccbec15-588d-4821-8df4-4c3c66930d73',NULL,1,1,'您已成功取消订单，退款金额为[120.00]','2024-04-14 22:37:31',0),('c35129ef-9e62-4a11-844b-7da48f485dbd',NULL,NULL,NULL,NULL,2,1,'发大水发',NULL,NULL),('c53b8d92-70a9-4e8c-b45f-6f5211c3b469','system','system','3ccbec15-588d-4821-8df4-4c3c66930d73',NULL,1,1,'[预订成功]\n您已成功预订1号球桌。\n使用时间为：2024-02-28 10:00:00\n使用时长(小时)：1','2024-03-01 15:10:42',1),('c88ae3c7-84fb-4f01-869e-cd606fbe5742','system','system','3ccbec15-588d-4821-8df4-4c3c66930d73',NULL,1,1,'候补成功!','2024-03-31 11:41:25',0),('cbfab552-a65e-4db0-87e9-53e254dcf80b','system','system','3ccbec15-588d-4821-8df4-4c3c66930d73',NULL,1,1,'您已成功取消订单，退款金额为[75.00]','2024-03-31 11:08:07',0),('cccd6627-00ed-44a7-8bb9-1f1552fe56ad','system','system','3ccbec15-588d-4821-8df4-4c3c66930d73',NULL,1,1,'您已成功取消订单，退款金额为[15.00]','2024-04-14 21:53:49',0),('cd1ce97b-00e2-40af-bdfe-27be1e82e131','system','system','52d4c537-2310-47f0-bbca-dff919bccfa2',NULL,1,1,'[预订成功]\n您已成功预订1号球桌。\n使用时间为：2024-03-28 16:00:00\n使用时长(小时)：1','2024-03-29 10:53:20',0),('ce273063-b88e-44c0-abb3-6b409121d58c','system','system','2d64dd54-854f-4705-86af-8858dbf2b2b4',NULL,1,1,'[预订成功]\n您已成功预订1号球桌。\n使用时间为：2024-03-14 15:00:00\n使用时长(小时)：1','2024-03-14 11:56:24',0),('ceb53b67-b1b6-44d6-90dc-5f3443235773','system','system','3ccbec15-588d-4821-8df4-4c3c66930d73',NULL,1,1,'[预订成功]\n您已成功预订1号球桌。\n使用时间为：2024-04-16 12:00:00\n使用时长(小时)：4','2024-04-15 21:46:52',0),('cf7541a4-902e-4831-b6af-1efba6a4f14e','system','system','3ccbec15-588d-4821-8df4-4c3c66930d73',NULL,1,1,'[预订成功]\n您已成功预订1号球桌。\n使用时间为：2024-03-25 12:00:00\n使用时长(小时)：3','2024-03-25 21:28:07',0),('d25d71b5-6c81-40f3-a1dd-01a19a9a42a3','system','system','3ccbec15-588d-4821-8df4-4c3c66930d73',NULL,1,1,'[预订成功]\n您已成功预订2号球桌。\n使用时间为：2024-04-15 10:00:00\n使用时长(小时)：1','2024-04-14 21:52:54',0),('d2693365-c9e6-4282-bef8-399488113a9c','system','system','3ccbec15-588d-4821-8df4-4c3c66930d73',NULL,1,1,'您已成功取消订单，退款金额为[100.00]','2024-04-14 23:00:09',0),('d2f9768a-0019-4ca3-a4e3-b7c791593f6b','system','system','3ccbec15-588d-4821-8df4-4c3c66930d73',NULL,1,1,'您已成功取消订单，退款金额为[120.00]','2024-04-14 22:33:56',0),('dba229fe-63fd-4b0b-81b9-fc4ad92dc02e','system','system','3ccbec15-588d-4821-8df4-4c3c66930d73',NULL,1,1,'[预订成功]\n您已成功预订4号球桌。\n使用时间为：2024-03-29 12:00:00\n使用时长(小时)：3','2024-03-29 10:53:05',0),('dc3c6db6-fa30-4418-b3dc-509bacca5710','system','system','52d4c537-2310-47f0-bbca-dff919bccfa2',NULL,1,1,'[预订成功]\n您已成功预订4号球桌。\n使用时间为：2024-03-29 14:00:00\n使用时长(小时)：2','2024-03-29 11:15:40',0),('dd9ee5e7-d3dd-41d7-a1f2-d03db5be15fd','system','system','3ccbec15-588d-4821-8df4-4c3c66930d73',NULL,1,1,'[预订成功]\n您已成功预订8号球桌。\n使用时间为：2024-04-17 16:00:00\n使用时长(小时)：1','2024-04-17 14:14:27',0),('de8d92af-4f5e-43b4-ae13-e98fe4ebf971','system','system','52d4c537-2310-47f0-bbca-dff919bccfa2',NULL,1,1,'[预订成功]\n您已成功预订2号球桌。\n使用时间为：2024-03-19 10:00:00\n使用时长(小时)：1','2024-03-19 17:41:30',1),('deda4a88-baea-435f-ba6a-8ab54c6c125c','system','system','3ccbec15-588d-4821-8df4-4c3c66930d73',NULL,1,1,'[预订成功]\n您已成功预订4号球桌。\n使用时间为：2024-03-29 10:00:00\n使用时长(小时)：3','2024-03-29 11:00:22',0),('e1dfa2e3-8ea7-418e-9a5b-ba5795286dea','system','system','52d4c537-2310-47f0-bbca-dff919bccfa2',NULL,1,1,'[预订成功]\n您已成功预订4号球桌。\n使用时间为：2024-03-23 13:00:00\n使用时长(小时)：3','2024-03-23 12:11:36',1),('e3c88c34-bccf-459e-9a90-0db8ba53350d','system','system','3ccbec15-588d-4821-8df4-4c3c66930d73',NULL,1,1,'[预订成功]\n您已成功预订4号球桌。\n使用时间为：2024-03-29 16:00:00\n使用时长(小时)：4','2024-03-29 10:55:33',0),('e44ec1c1-1284-4bc6-9973-2cb736c2b8d3','system','system','52d4c537-2310-47f0-bbca-dff919bccfa2',NULL,1,1,'您已成功取消订单，退款金额为[360.00]','2024-04-17 22:17:44',0),('e55c5ad4-398e-4e15-93ac-4bb0a44b7c1a','system','system','3ccbec15-588d-4821-8df4-4c3c66930d73',NULL,1,1,'[预订成功]\n您已成功预订4号球桌。\n使用时间为：2024-04-19 11:00:00\n使用时长(小时)：1','2024-04-17 17:58:07',0),('e75bf620-7caa-4e7c-aa96-1b387dd00f63','system','system','3ccbec15-588d-4821-8df4-4c3c66930d73',NULL,1,1,'您已成功取消订单，退款金额为[120.00]','2024-04-17 21:49:32',0),('e856f9a5-6fdc-44e3-a598-f57b43d614cf','system','system',NULL,NULL,2,1,'测试公告','2024-05-03 23:01:31',NULL),('e86093db-695a-4a86-913c-5bb0e9e872df','system','system','52d4c537-2310-47f0-bbca-dff919bccfa2',NULL,1,1,'[预订成功]\n您已成功预订1号球桌。\n使用时间为：2024-03-28 16:00:00\n使用时长(小时)：2','2024-03-28 11:19:54',0),('eba99bf5-0367-4386-90d4-a33499c5e0ed','fdsaf','苏小白','fdsafdsagf','李白',2,1,'你好，我是测试消息','2024-03-01 13:09:44',0),('ecafb6bc-1fc4-41e8-831f-055ad5dc6652','52d4c537-2310-47f0-bbca-dff919bccfa2','abc','3d217382-fcc1-4dc0-96ba-eb8b18eef968','testuser',1,6,'fff','2024-03-20 00:12:57',0),('ed9c0eb4-df42-4866-9bf1-9c409b8d34f9','system','system','3ccbec15-588d-4821-8df4-4c3c66930d73',NULL,1,1,'[预订成功]\n您已成功预订7号球桌。\n使用时间为：2024-03-31 16:00:00\n使用时长(小时)：3','2024-03-31 11:05:32',0),('eeef8868-91ed-4588-9184-6890969f5b62','system','system','3ccbec15-588d-4821-8df4-4c3c66930d73',NULL,1,1,'您已成功取消订单，退款金额为[100.00]','2024-04-14 22:42:36',0),('f484987c-5bc1-4160-9d9a-8b6d3a55792d','system','system',NULL,NULL,2,1,'测试公告2','2024-05-03 23:03:16',NULL),('f4e9bf9c-ee1b-4927-b7ae-55195915ffdf','system','system','52d4c537-2310-47f0-bbca-dff919bccfa2',NULL,1,1,'您已成功取消订单，退款金额为[40.00]','2024-04-17 18:50:32',0),('f4f34983-cf79-4863-9da3-7c7bb832e9e9','system','system','3ccbec15-588d-4821-8df4-4c3c66930d73',NULL,1,1,'[预订成功]\n您已成功预订1号球桌。\n使用时间为：2024-04-15 10:00:00\n使用时长(小时)：5','2024-04-14 21:57:23',0),('f6b0769b-133e-44df-a0f1-bfe36ff57ca4','system','system','3ccbec15-588d-4821-8df4-4c3c66930d73',NULL,1,1,'您的拼友abc取消了订单，将恢复您的匹配拼桌球友，请知悉','2024-03-30 19:44:50',0),('f9aff13c-507b-4ca9-b62f-4872a8d8ad8b','system','system','52d4c537-2310-47f0-bbca-dff919bccfa2',NULL,1,1,'[预订成功]\n您已成功预订1号球桌。\n使用时间为：2024-04-18 12:00:00\n使用时长(小时)：9','2024-04-17 21:49:32',0),('fb7850ef-be8a-4571-b129-3dae1ed93018','system','system','3ccbec15-588d-4821-8df4-4c3c66930d73',NULL,1,1,'[预订成功]\n您已成功预订7号球桌。\n使用时间为：2024-03-31 16:00:00\n使用时长(小时)：1','2024-03-31 11:12:35',0),('fbff4b41-e296-40bc-ad78-bbe0e4d29ae2','system','system','3ccbec15-588d-4821-8df4-4c3c66930d73',NULL,1,1,'您已成功取消订单，退款金额为[45.00]','2024-03-29 11:15:40',0),('fcf1c077-8e4d-42c7-87c7-6473978e469a','system','system','3ccbec15-588d-4821-8df4-4c3c66930d73',NULL,1,1,'您已成功取消订单，退款金额为[60.00]','2024-03-29 10:57:22',0),('fd33f68b-fd26-4d40-96d8-26b92aaced64','system','system','3ccbec15-588d-4821-8df4-4c3c66930d73',NULL,1,1,'[预订成功]\n您已成功预订2号球桌。\n使用时间为：2024-03-28 16:00:00\n使用时长(小时)：4','2024-03-28 00:11:03',0),('grewtrewtrewt','origin_id_value','origin_name_value','target_id_value','target_name_value',2,1,'这是一条全体消息，乒乓球馆正式开业!','2024-03-01 14:17:07',0);
/*!40000 ALTER TABLE `MESSAGE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `RESERVE`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `RESERVE` (
  `USER_ID` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '用户id',
  `TABLE_ID` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '球桌id',
  `START_DATE` varchar(20) DEFAULT NULL COMMENT '开始时间',
  `USE_TIME` int DEFAULT NULL COMMENT '预订时长，单位(小时)',
  `PAY_AMT` decimal(10,2) DEFAULT NULL COMMENT '支付金额',
  `BILL_ID` varchar(50) DEFAULT NULL COMMENT '预订主单ID，用于球友匹配相关功能,多个预订单会属于同一个主单',
  `RESERVE_STATUS` int DEFAULT NULL COMMENT '预订状态. -3 预订失败，0 预订成功，待付款，1 待匹配球友拼桌， 3 付款成功，待使用 4:已使用 6 预订取消 9 预订过期 ',
  `CREATE_DATE` varchar(20) DEFAULT NULL COMMENT '创建日期',
  `CREATE_USER` varchar(100) DEFAULT NULL COMMENT '创建用户（用户名）',
  `UPDATE_DATE` varchar(20) DEFAULT NULL COMMENT '更新日期',
  `UPDATE_USER` varchar(100) DEFAULT NULL COMMENT '更新用户（用户名）',
  `guid` varchar(50) NOT NULL COMMENT '预订单主键',
  `TABLE_CODE` varchar(20) DEFAULT NULL COMMENT '球桌编号',
  `PROCESS_START_DATE` varchar(10) GENERATED ALWAYS AS (substr(`START_DATE`,1,10)) VIRTUAL,
  `USER_NAME` varchar(100) DEFAULT NULL COMMENT '用户名，不能重复',
  `OUT_TRADE_NO` varchar(100) DEFAULT NULL COMMENT '商户订单号',
  `TRADE_NO` varchar(100) DEFAULT NULL COMMENT '支付宝订单交易号',
  `MATCH_USER_MATCH_INFO_ID` varchar(50) DEFAULT NULL COMMENT '匹配用户的匹配单id,匹配用户使用，主单用户留空',
  `IS_DELETED` int DEFAULT NULL COMMENT '逻辑删除： 0 未删除 1 删除',
  PRIMARY KEY (`guid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `RESERVE`
--

LOCK TABLES `RESERVE` WRITE;
/*!40000 ALTER TABLE `RESERVE` DISABLE KEYS */;
INSERT INTO `RESERVE` (`USER_ID`, `TABLE_ID`, `START_DATE`, `USE_TIME`, `PAY_AMT`, `BILL_ID`, `RESERVE_STATUS`, `CREATE_DATE`, `CREATE_USER`, `UPDATE_DATE`, `UPDATE_USER`, `guid`, `TABLE_CODE`, `USER_NAME`, `OUT_TRADE_NO`, `TRADE_NO`, `MATCH_USER_MATCH_INFO_ID`, `IS_DELETED`) VALUES ('3ccbec15-588d-4821-8df4-4c3c66930d73','9d135721-7ef7-4862-9d5e-932f7c42cff5','2024-04-14 11:00:00',3,60.00,NULL,4,'2024-04-14 21:38:50','3ccbec15-588d-4821-8df4-4c3c66930d73','2024-04-14 21:38:50','3ccbec15-588d-4821-8df4-4c3c66930d73','01673acc-a73d-4021-a4ae-ef69439b26de','1','admin','202404142138260061960805236','2024041422001410020502687070',NULL,NULL),('3ccbec15-588d-4821-8df4-4c3c66930d73','7de4a2ed-8db0-437c-b544-442d495e5aac','2024-03-31 16:00:00',2,50.00,NULL,4,'2024-03-31 11:41:25','52d4c537-2310-47f0-bbca-dff919bccfa2','2024-03-31 11:41:25','52d4c537-2310-47f0-bbca-dff919bccfa2','31e65052-ea0f-4a6a-a8df-70ae355c13e6','7','abc','2024033111321913519608052361796595167','2024033122001410020502533031',NULL,NULL),('52d4c537-2310-47f0-bbca-dff919bccfa2','9d135721-7ef7-4862-9d5e-932f7c42cff5','2024-04-15 11:00:00',3,60.00,NULL,1,'2024-04-14 23:00:14','3ccbec15-588d-4821-8df4-4c3c66930d73','2024-04-14 23:00:14','3ccbec15-588d-4821-8df4-4c3c66930d73','33d78a07-658e-4648-9de6-54ae3e5bff4d','1','abc','202404142233063871060109161950669918','2024041422001410020502688911',NULL,NULL),('3ccbec15-588d-4821-8df4-4c3c66930d73','7de4a2ed-8db0-437c-b544-442d495e5aac','2024-03-29 17:00:00',3,75.00,NULL,4,'2024-03-29 11:44:06','3ccbec15-588d-4821-8df4-4c3c66930d73','2024-03-29 11:44:06','3ccbec15-588d-4821-8df4-4c3c66930d73','405372f1-185b-4d74-ae6f-1fbfdb35389c','7','admin','202403291143479841960805236','2024032922001410020502501617',NULL,NULL),('3ccbec15-588d-4821-8df4-4c3c66930d73','7de4a2ed-8db0-437c-b544-442d495e5aac','2024-03-31 16:00:00',1,25.00,NULL,6,'2024-03-31 11:12:35','3ccbec15-588d-4821-8df4-4c3c66930d73','2024-03-31 11:12:35','3ccbec15-588d-4821-8df4-4c3c66930d73','427b052b-3148-44e1-9324-43eeb3ecdec4','7','admin','202403311112175841960805236','2024033122001410020502531663',NULL,NULL),('3ccbec15-588d-4821-8df4-4c3c66930d73','9d135721-7ef7-4862-9d5e-932f7c42cff5','2024-04-15 10:00:00',5,100.00,NULL,6,'2024-04-14 22:59:30','3ccbec15-588d-4821-8df4-4c3c66930d73','2024-04-14 22:59:30','3ccbec15-588d-4821-8df4-4c3c66930d73','4e8e725f-867b-4d74-998e-c79ad09d4c7a','1','admin','202404142259114231960805236','2024041422001410020502691766',NULL,NULL),('3ccbec15-588d-4821-8df4-4c3c66930d73','9d135721-7ef7-4862-9d5e-932f7c42cff5','2024-04-16 12:00:00',4,80.00,NULL,4,'2024-04-15 21:46:52','3ccbec15-588d-4821-8df4-4c3c66930d73','2024-04-15 21:46:52','3ccbec15-588d-4821-8df4-4c3c66930d73','4e949960-0ee1-4147-9b39-991f86984e6c','1','admin','202404152146301431960805236','2024041522001410020502697775',NULL,NULL),('52d4c537-2310-47f0-bbca-dff919bccfa2','9d135721-7ef7-4862-9d5e-932f7c42cff5','2024-04-18 12:00:00',9,360.00,NULL,6,'2024-04-17 21:49:32','3ccbec15-588d-4821-8df4-4c3c66930d73','2024-04-17 21:49:32','3ccbec15-588d-4821-8df4-4c3c66930d73','4fc47db3-c1fc-45bc-85fd-61c47fcd76e8','1','admin','202404172140246671060109161950669918','2024041722001410020502724901',NULL,NULL),('3ccbec15-588d-4821-8df4-4c3c66930d73','7de4a2ed-8db0-437c-b544-442d495e5aac','2024-03-31 16:00:00',3,75.00,NULL,6,'2024-03-31 11:05:32','3ccbec15-588d-4821-8df4-4c3c66930d73','2024-03-31 11:05:32','3ccbec15-588d-4821-8df4-4c3c66930d73','55b094d4-ab6b-4220-82ed-df7ed80f74ba','7','admin','202403311105056741960805236','2024033122001410020502523574',NULL,NULL),('52d4c537-2310-47f0-bbca-dff919bccfa2','9d135721-7ef7-4862-9d5e-932f7c42cff5','2024-04-18 11:00:00',2,40.00,NULL,6,'2024-04-17 18:49:45','3ccbec15-588d-4821-8df4-4c3c66930d73','2024-04-17 18:49:45','3ccbec15-588d-4821-8df4-4c3c66930d73','64329b67-586b-4292-941a-ae13f8301673','1','abc','202404171849057731060109161950669918','2024041722001410020502729866',NULL,NULL),('3ccbec15-588d-4821-8df4-4c3c66930d73','9d135721-7ef7-4862-9d5e-932f7c42cff5','2024-04-15 12:00:00',6,120.00,NULL,6,'2024-04-14 22:06:43','3ccbec15-588d-4821-8df4-4c3c66930d73','2024-04-14 22:06:43','3ccbec15-588d-4821-8df4-4c3c66930d73','64d633a0-bd7b-4529-b040-1da66bd84ce6','1','admin','202404142206237031960805236','2024041422001410020502688910',NULL,NULL),('52d4c537-2310-47f0-bbca-dff919bccfa2','bf900744-6962-44c8-bdca-1c52ab52a81f','2024-03-30 21:00:00',5,38.00,NULL,3,'2024-03-30 20:26:00','3ccbec15-588d-4821-8df4-4c3c66930d73','2024-03-30 20:26:00','3ccbec15-588d-4821-8df4-4c3c66930d73','66d407e8-9df3-4975-976f-317c4d9f0623','2','abc','20240330201958532106010916140747281','2024033022001410020502527346','24e0abc3-6866-45b8-b161-c59afc74f133',NULL),('3ccbec15-588d-4821-8df4-4c3c66930d73','9d135721-7ef7-4862-9d5e-932f7c42cff5','2024-04-15 11:00:00',3,30.00,NULL,4,'2024-04-15 21:49:14','52d4c537-2310-47f0-bbca-dff919bccfa2','2024-04-15 21:49:14','52d4c537-2310-47f0-bbca-dff919bccfa2','699a963e-83ba-40c2-9626-e6ae086c2fc8','1','admin','2024041521482447419608052361950669918','2024041522001410020502702200','aba9eda1-da76-488c-8ab5-5cc3d615d538',NULL),('3ccbec15-588d-4821-8df4-4c3c66930d73','21b2daf3-4e6a-46f8-b309-56871d8fa1f9','2024-03-29 10:00:00',3,45.00,NULL,4,'2024-03-29 11:00:22','3ccbec15-588d-4821-8df4-4c3c66930d73','2024-03-29 11:00:22','3ccbec15-588d-4821-8df4-4c3c66930d73','6dc74a75-ba0a-486c-b192-6d517ec4b0ed','4','admin','202403291100030131960805236','2024032922001410020502497786',NULL,NULL),('52d4c537-2310-47f0-bbca-dff919bccfa2','7de4a2ed-8db0-437c-b544-442d495e5aac','2024-03-31 15:00:00',2,50.00,NULL,6,'2024-03-31 11:26:05','3ccbec15-588d-4821-8df4-4c3c66930d73','2024-03-31 11:26:05','3ccbec15-588d-4821-8df4-4c3c66930d73','7125b260-9a62-4eda-a4a2-43f1f465a2e9','7','admin','202403311107207901060109161796595167','2024033122001410020502531662',NULL,NULL),('3ccbec15-588d-4821-8df4-4c3c66930d73','9d135721-7ef7-4862-9d5e-932f7c42cff5','2024-04-17 10:00:00',4,80.00,NULL,6,'2024-04-14 21:39:46','3ccbec15-588d-4821-8df4-4c3c66930d73','2024-04-14 21:39:46','3ccbec15-588d-4821-8df4-4c3c66930d73','722d6063-35e6-4ba2-b0c7-b43e519ffff5','1','admin','202404142139246361960805236','2024041422001410020502690643',NULL,NULL),('3ccbec15-588d-4821-8df4-4c3c66930d73','9d135721-7ef7-4862-9d5e-932f7c42cff5','2024-03-30 16:00:00',3,60.00,NULL,4,'2024-03-30 11:19:33','3ccbec15-588d-4821-8df4-4c3c66930d73','2024-03-30 11:19:33','3ccbec15-588d-4821-8df4-4c3c66930d73','78993357-f812-48cd-b22b-ff0612b1ee15','1','admin','202403301119005531960805236','2024033022001410020502509436',NULL,NULL),('3ccbec15-588d-4821-8df4-4c3c66930d73','9d135721-7ef7-4862-9d5e-932f7c42cff5','2024-03-31 12:00:00',3,60.00,NULL,1,'2024-03-31 10:26:34','3ccbec15-588d-4821-8df4-4c3c66930d73','2024-03-31 10:26:34','3ccbec15-588d-4821-8df4-4c3c66930d73','7f4acea1-b52a-443f-a163-837bbc129b5e','1','admin','202403311026028301960805236','2024033122001410020502526152',NULL,NULL),('3ccbec15-588d-4821-8df4-4c3c66930d73','7de4a2ed-8db0-437c-b544-442d495e5aac','2024-03-30 10:00:00',3,75.00,NULL,4,'2024-03-29 11:47:39','3ccbec15-588d-4821-8df4-4c3c66930d73','2024-03-29 11:47:39','3ccbec15-588d-4821-8df4-4c3c66930d73','84fcf1e8-4532-482d-8a05-eeac1b5a89aa','7','admin','202403291147190951960805236','2024032922001410020502507909',NULL,NULL),('52d4c537-2310-47f0-bbca-dff919bccfa2','9d135721-7ef7-4862-9d5e-932f7c42cff5','2024-03-28 16:00:00',2,70.00,NULL,3,'2024-03-28 11:19:54','3ccbec15-588d-4821-8df4-4c3c66930d73','2024-03-28 11:19:54','3ccbec15-588d-4821-8df4-4c3c66930d73','86406abb-d17a-43a9-92f8-5607b23a1283','1','admin',NULL,NULL,NULL,NULL),('3ccbec15-588d-4821-8df4-4c3c66930d73','bf900744-6962-44c8-bdca-1c52ab52a81f','2024-03-28 11:00:00',2,NULL,NULL,4,'2024-03-30 20:29:30','52d4c537-2310-47f0-bbca-dff919bccfa2','2024-03-30 20:29:30','52d4c537-2310-47f0-bbca-dff919bccfa2','8705f6c3-e240-42b3-9e4e-7a6637c9e572','2','abc',NULL,NULL,NULL,NULL),('3ccbec15-588d-4821-8df4-4c3c66930d73','9d135721-7ef7-4862-9d5e-932f7c42cff5','2024-03-30 20:00:00',1,20.00,NULL,6,'2024-03-30 19:31:55','3ccbec15-588d-4821-8df4-4c3c66930d73','2024-03-30 19:31:55','3ccbec15-588d-4821-8df4-4c3c66930d73','8ba4a46f-7ea6-4d76-87d3-ea8fec7a8be1','1','admin','202403301931076241960805236','2024033022001410020502526150',NULL,NULL),('3ccbec15-588d-4821-8df4-4c3c66930d73','9d135721-7ef7-4862-9d5e-932f7c42cff5','2024-04-01 16:00:00',5,100.00,NULL,1,'2024-03-30 20:25:14','3ccbec15-588d-4821-8df4-4c3c66930d73','2024-03-30 20:25:14','3ccbec15-588d-4821-8df4-4c3c66930d73','93b788eb-e5e6-4c25-8ea5-ebcdb928d981','1','admin','202403302024484071960805236','2024033022001410020502529089',NULL,NULL),('3ccbec15-588d-4821-8df4-4c3c66930d73','bf900744-6962-44c8-bdca-1c52ab52a81f','2024-03-28 11:00:00',2,NULL,NULL,4,'2024-03-30 20:34:08','52d4c537-2310-47f0-bbca-dff919bccfa2','2024-03-30 20:34:08','52d4c537-2310-47f0-bbca-dff919bccfa2','97503458-51af-45e9-b2a6-3b54ffb10d56','2','abc',NULL,NULL,NULL,NULL),('3ccbec15-588d-4821-8df4-4c3c66930d73','21b2daf3-4e6a-46f8-b309-56871d8fa1f9','2024-04-18 14:00:00',5,75.00,NULL,4,'2024-04-18 13:42:45','3ccbec15-588d-4821-8df4-4c3c66930d73','2024-04-18 13:42:45','3ccbec15-588d-4821-8df4-4c3c66930d73','98f4d254-46d4-4d00-8d1e-0ebf654e5ce9','4','admin','202404181342165171960805236','2024041822001410020502732810',NULL,NULL),('52d4c537-2310-47f0-bbca-dff919bccfa2','9d135721-7ef7-4862-9d5e-932f7c42cff5','2024-03-31 11:00:00',3,30.00,NULL,4,'2024-03-31 10:29:01','3ccbec15-588d-4821-8df4-4c3c66930d73','2024-03-31 10:29:01','3ccbec15-588d-4821-8df4-4c3c66930d73','9d27831f-7d94-4fcc-9f72-29c9da42728d','1','abc','202403311028172821060109161950669918','2024033122001410020502530266','abee51f1-3f45-4f71-8f68-71f51a115a18',NULL),('3ccbec15-588d-4821-8df4-4c3c66930d73','21b2daf3-4e6a-46f8-b309-56871d8fa1f9','2024-03-29 15:00:00',3,45.00,NULL,6,'2024-03-29 11:00:55','3ccbec15-588d-4821-8df4-4c3c66930d73','2024-03-29 11:00:55','3ccbec15-588d-4821-8df4-4c3c66930d73','a2bee396-9e95-47c8-bb34-604b46eb53f3','4','admin','202403291100382791960805236','2024032922001410020502506105',NULL,NULL),('3ccbec15-588d-4821-8df4-4c3c66930d73','7de4a2ed-8db0-437c-b544-442d495e5aac','2024-03-31 16:00:00',1,25.00,NULL,6,'2024-03-31 11:25:41','3ccbec15-588d-4821-8df4-4c3c66930d73','2024-03-31 11:25:41','3ccbec15-588d-4821-8df4-4c3c66930d73','ac7cd88b-9458-4c1f-b745-fb62b412406a','7','admin','202403311125223551960805236','2024033122001410020502531664',NULL,NULL),('3ccbec15-588d-4821-8df4-4c3c66930d73','9d135721-7ef7-4862-9d5e-932f7c42cff5','2024-04-18 15:00:00',3,120.00,NULL,6,'2024-04-17 21:39:43','3ccbec15-588d-4821-8df4-4c3c66930d73','2024-04-17 21:39:43','3ccbec15-588d-4821-8df4-4c3c66930d73','b4028158-a468-4f82-a22f-e8a43928af7f','1','admin','202404172139047281960805236','2024041722001410020502729868',NULL,NULL),('52d4c537-2310-47f0-bbca-dff919bccfa2','21b2daf3-4e6a-46f8-b309-56871d8fa1f9','2024-03-29 14:00:00',2,NULL,NULL,3,'2024-03-29 11:15:40','3ccbec15-588d-4821-8df4-4c3c66930d73','2024-03-29 11:15:40','3ccbec15-588d-4821-8df4-4c3c66930d73','b7dcb608-44ff-48cb-a00c-cdfbe2c92960','4','admin',NULL,NULL,NULL,NULL),('52d4c537-2310-47f0-bbca-dff919bccfa2','21b2daf3-4e6a-46f8-b309-56871d8fa1f9','2024-04-18 14:00:00',5,38.00,NULL,3,'2024-04-18 13:59:53','3ccbec15-588d-4821-8df4-4c3c66930d73','2024-04-18 13:59:53','3ccbec15-588d-4821-8df4-4c3c66930d73','bb1dd690-94c9-4d3a-976c-5d0b3bfcb7c2','4','abc','20240418135819513106010916150687963','2024041822001410020502736943','b6cf9d80-077d-4d5d-b4eb-04fe201c42b2',NULL),('3ccbec15-588d-4821-8df4-4c3c66930d73','9d135721-7ef7-4862-9d5e-932f7c42cff5','2024-04-15 10:00:00',6,120.00,NULL,6,'2024-04-14 22:36:33','3ccbec15-588d-4821-8df4-4c3c66930d73','2024-04-14 22:36:33','3ccbec15-588d-4821-8df4-4c3c66930d73','c2d4dc27-4823-4a47-86ca-4257048f4c5c','1','admin','202404142236186381960805236','2024041422001410020502690644',NULL,NULL),('3ccbec15-588d-4821-8df4-4c3c66930d73','9d135721-7ef7-4862-9d5e-932f7c42cff5','2024-04-18 11:00:00',2,80.00,NULL,6,'2024-04-17 21:31:34','3ccbec15-588d-4821-8df4-4c3c66930d73','2024-04-17 21:31:34','3ccbec15-588d-4821-8df4-4c3c66930d73','c57dffce-6543-4b9d-a50e-f2a28338df33','1','admin','202404172130560461960805236','2024041722001410020502735447',NULL,NULL),('3ccbec15-588d-4821-8df4-4c3c66930d73','21b2daf3-4e6a-46f8-b309-56871d8fa1f9','2024-03-29 12:00:00',3,45.00,NULL,6,'2024-03-29 10:53:05','3ccbec15-588d-4821-8df4-4c3c66930d73','2024-03-29 10:53:05','3ccbec15-588d-4821-8df4-4c3c66930d73','d4810c6e-b5f5-4637-87de-99ad49732e3d','4','admin','202403291052477361960805236','2024032922001410020502499081',NULL,NULL),('3ccbec15-588d-4821-8df4-4c3c66930d73','9d135721-7ef7-4862-9d5e-932f7c42cff5','2024-04-15 10:00:00',5,100.00,NULL,6,'2024-04-14 21:57:23','3ccbec15-588d-4821-8df4-4c3c66930d73','2024-04-14 21:57:23','3ccbec15-588d-4821-8df4-4c3c66930d73','d9ea9725-2c26-426f-a926-d527d2127588','1','admin','202404142157036211960805236','2024041422001410020502697773',NULL,NULL),('3ccbec15-588d-4821-8df4-4c3c66930d73','7de4a2ed-8db0-437c-b544-442d495e5aac','2024-04-18 11:00:00',2,50.00,NULL,6,'2024-04-17 19:15:03','3ccbec15-588d-4821-8df4-4c3c66930d73','2024-04-17 19:15:03','3ccbec15-588d-4821-8df4-4c3c66930d73','dcacf9c2-767d-4958-8ff7-793b3fc34944','7','admin','202404171914184331960805236','2024041722001410020502729867',NULL,NULL),('3ccbec15-588d-4821-8df4-4c3c66930d73','bf900744-6962-44c8-bdca-1c52ab52a81f','2024-04-15 10:00:00',1,15.00,NULL,6,'2024-04-14 21:52:54','3ccbec15-588d-4821-8df4-4c3c66930d73','2024-04-14 21:52:54','3ccbec15-588d-4821-8df4-4c3c66930d73','e0a5c5a9-fbb4-4a13-8f07-0bbe23c63f03','2','admin','202404142152334471960805236','2024041422001410020502697772',NULL,NULL),('52d4c537-2310-47f0-bbca-dff919bccfa2','9d135721-7ef7-4862-9d5e-932f7c42cff5','2024-03-28 16:00:00',1,NULL,NULL,3,'2024-03-29 10:53:20','3ccbec15-588d-4821-8df4-4c3c66930d73','2024-03-29 10:53:20','3ccbec15-588d-4821-8df4-4c3c66930d73','f21b73db-a51b-4206-9708-32f99b1bb239','1','admin',NULL,NULL,NULL,NULL),('3ccbec15-588d-4821-8df4-4c3c66930d73','9d135721-7ef7-4862-9d5e-932f7c42cff5','2024-03-28 12:00:00',1,50.00,NULL,4,'2024-03-28 00:20:49','3ccbec15-588d-4821-8df4-4c3c66930d73','2024-03-28 00:20:49','3ccbec15-588d-4821-8df4-4c3c66930d73','f4a95762-e6df-4749-b389-978d5878a845','1','admin',NULL,NULL,NULL,NULL),('3ccbec15-588d-4821-8df4-4c3c66930d73','9d135721-7ef7-4862-9d5e-932f7c42cff5','2024-03-28 15:00:00',4,68.00,NULL,6,'2024-03-28 00:20:49','3ccbec15-588d-4821-8df4-4c3c66930d73','2024-03-28 00:20:49','3ccbec15-588d-4821-8df4-4c3c66930d73','f4af1762-e6df-4749-b389-978d5878a845','1','admin',NULL,NULL,NULL,NULL),('3ccbec15-588d-4821-8df4-4c3c66930d73','58790181-1fa3-40bf-8060-c155d50a3de1','2024-04-17 16:00:00',1,35.00,NULL,4,'2024-04-17 14:14:27','3ccbec15-588d-4821-8df4-4c3c66930d73','2024-04-17 14:14:27','3ccbec15-588d-4821-8df4-4c3c66930d73','f5003727-a76f-425e-9613-b1bf8dedfdd1','8','admin','202404171413368961960805236','2024041722001410020502726720',NULL,NULL),('52d4c537-2310-47f0-bbca-dff919bccfa2','21b2daf3-4e6a-46f8-b309-56871d8fa1f9','2024-04-19 11:00:00',1,8.00,NULL,6,'2024-04-17 18:14:14','3ccbec15-588d-4821-8df4-4c3c66930d73','2024-04-17 18:14:14','3ccbec15-588d-4821-8df4-4c3c66930d73','f9e06be7-e3fb-4d52-b878-90ae74dc350d','4','abc','20240417181113943106010916150687963','2024041722001410020502732807',NULL,NULL);
/*!40000 ALTER TABLE `RESERVE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ROLE`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ROLE` (
  `GUID` varchar(50) NOT NULL COMMENT 'ID主键',
  `NAME` varchar(100) DEFAULT NULL COMMENT '角色名称',
  `REMARK` varchar(200) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`GUID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ROLE`
--

LOCK TABLES `ROLE` WRITE;
/*!40000 ALTER TABLE `ROLE` DISABLE KEYS */;
INSERT INTO `ROLE` VALUES ('admin','管理员','测试管理员'),('customer','客服','客服角色'),('normal','普通会员','普通用户角色');
/*!40000 ALTER TABLE `ROLE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ROLE_MENU`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ROLE_MENU` (
  `GUID` varchar(50) NOT NULL COMMENT 'ID主键',
  `ROLE_ID` varchar(100) DEFAULT NULL COMMENT '角色ID',
  `MENU_ID` varchar(100) DEFAULT NULL COMMENT '菜单ID',
  PRIMARY KEY (`GUID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ROLE_MENU`
--

LOCK TABLES `ROLE_MENU` WRITE;
/*!40000 ALTER TABLE `ROLE_MENU` DISABLE KEYS */;
INSERT INTO `ROLE_MENU` VALUES ('0d39110d-7544-44bb-b389-3fb6a534d8ec','normal','004003'),('13d88c2e-65a9-4a26-81fc-4b4e261ff449','admin','002002'),('1df85930-b14c-4dff-9525-2973e91d2cbc','admin','001'),('21fcfd4b-805c-471d-b8d7-fbf17cf75762','normal','004'),('2f6542bd-efaf-4713-8fa3-162a8429b91e','normal','003'),('3067bc82-9557-4e5f-852a-cc82cc793c2f','normal','003001'),('313da660-f8e4-47da-b826-49ac61bed292','normal','004002'),('318a9a1b-0fd4-42a9-8d81-22373c5bee5c','admin','004'),('3cc5c7bf-87ab-4a84-abff-4a3c4561314b','admin','002001'),('4148f42a-e00f-42a7-ba80-205b34289e41','normal','003002'),('44583935-8e5c-4552-a565-45553b84e2bd','admin','004001'),('49839790-2cbb-4434-a4af-a9d5b8d73fac','admin','003001'),('5648359a-bbde-49d1-b5fa-8f7efdd8b54b','normal','002003'),('5b29edf6-1aac-4d20-bdfa-e1cd6d6ca802','admin','004004'),('634b5aa4-c58b-48bc-883e-6291f24a3d8a','admin','004003'),('6ea1cbae-cb5e-4ae3-a139-e007bbb7ad39','normal','002'),('71808ba9-65e8-45c3-8843-796f790bc6d1','normal','004001'),('779625d6-521e-4322-b53b-1cea80ce0884','normal','002002'),('7cd8b9a2-77aa-4fc6-b509-b3fe54d8a4c6','normal','003003'),('81b66199-68cb-40cc-94dc-20bc03370219','admin','002003'),('8e03bca5-4829-4e9b-aa7a-e196d491dce4','admin','003002'),('b0f91b6d-6926-41da-84ea-4343d6912315','admin','004002'),('b5f470e9-d263-4e6a-8f9a-b796d8d98a7d','normal','001001'),('bbc3ddfa-bc6e-4976-ab70-41b2b60da85b','admin','005'),('c397a07e-cc8e-4ea4-bc8e-cbafb6b1e7f6','admin','003'),('c409b130-bdcf-48df-9991-6a94c20980de','admin','005001'),('dda46436-c2ac-4cb7-841e-d9e5252af7c6','admin','001001'),('ddcc7c16-2d96-4808-b8d9-1da4a130b433','admin','002'),('e1e5b94d-82a2-4d06-855a-b222c4e59053','normal','001'),('e6bc7471-a5cf-4a05-be5c-1c2e980b61bf','admin','005002'),('ee62cc76-a186-45cb-b777-58666bfd19d3','admin','003003'),('fbc66233-cba6-4ada-968f-f0bd617b4bd3','customer','004002'),('ffe01e38-e727-41fd-9fb0-631efdeadc2d','customer','004');
/*!40000 ALTER TABLE `ROLE_MENU` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'table_tennis_system'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-05-06 21:44:23
