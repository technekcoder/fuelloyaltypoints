DROP TABLE IF EXISTS `LOYALTY_CARDS`;
DROP TABLE IF EXISTS `LOYALTY_LEDGER`;
DROP TABLE IF EXISTS `LOYALTY_TRNS`;
DROP TABLE IF EXISTS `MOBILE_MONEY`;
DROP TABLE IF EXISTS `CUSTOMERS`;
DROP TABLE IF EXISTS `FUEL_PRICES`;
DROP TABLE IF EXISTS `MOBILE_USERS`;
DROP TABLE IF EXISTS `spmobileusers`;
DROP TABLE IF EXISTS `spusers`;
DROP TABLE IF EXISTS `spuroles`;
CREATE TABLE `LOYALTY_CARDS` (
		`SERIAL_NUMBER` int NOT NULL AUTO_INCREMENT, 
		`CARD_NUMBER` varchar(200),
		`CARD_NAME` varchar(60), 
		`LEDGER_NUMBER` varchar(20) NOT NULL, 
		`CARD_ADDRESS` varchar(250), 
		`COUNTY` varchar(16), 
		`ID_NUMBER` varchar(16), 
		`TELEPHONE` varchar(16), 
		`EMAIL` varchar(60), 
		`ISSUE_DATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, 
		`BIRTH_DATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, 
		`GENDER` CHAR(1) DEFAULT 'M', 
		`CHECK_OPTION` CHAR(1) DEFAULT 'N', 
		`CARD_TYPE` varchar(3) DEFAULT 'NRM', 
		`CARD_STATUS` varchar(3) DEFAULT 'ACT', 
		`PIN_CHECK` varchar(16), 
		`PASS_WORD` varchar(16), 
		`DISABLED` CHAR(1) DEFAULT 'N', 
		`DISABLE_DATE` TIMESTAMP, 
		`LAST_ACTIVITY` TIMESTAMP, 
		`FISCAL_YEAR` double(4,0) DEFAULT 2020, 
		`FISCAL_MONTH` double(2,0) DEFAULT 11, 
		`RUN_CHECK` varchar(3) DEFAULT '*', 
		`OPENING` double(20,0) DEFAULT 0, 
		`DEBIT` double(20,0) DEFAULT 0, 
		`CREDIT` double(20,0) DEFAULT 0, 
		`CLOSING` double(20,0) DEFAULT 0, 
		`REDEEMED` double(20,0) DEFAULT 0, 
		`CONFIGURATION` varchar(16) DEFAULT 'CONFIGURATION', 
		`DATE_CREATED` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, 
		`CREATED_BY` varchar(16), 
		`DATE_MODIFIED` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, 
		`MODIFIED_BY` varchar(16), 
		`EXPORT_INDICATOR` CHAR(1) DEFAULT 'N', 
		`DATE_EXPORTED` TIMESTAMP, 
		`EXPORTED_BY` varchar(50), 
		`REFERENCE_NUMBER` varchar(50),
		 PRIMARY KEY (`SERIAL_NUMBER`,`CARD_NUMBER`),
  UNIQUE KEY `CARD_NUMBER_UNIQUE` (`CARD_NUMBER`)
	)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;
	
CREATE TABLE `LOYALTY_LEDGER` (
		`SERIAL_NUMBER` int NOT NULL AUTO_INCREMENT, 
		`CARD_NO` varchar(50), 
		`CARD_TYPE` varchar(3) DEFAULT 'LOY', 
		`LEDGER_NUMBER` varchar(25) NOT NULL, 
		`TRANS_DATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, 
		`TRANS_TYPE` varchar(3) NOT NULL, 
		`ORDER_NO` int NOT NULL, 
		`ORDER_TYPE` varchar(3) NOT NULL, 
		`DESCRIPTION` varchar(50), 
		`ORDER_DATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, 
		`FISCAL_YEAR` int, 
		`FISCAL_MONTH` int, 
		`POINTS_AWARDED` double(20,2) DEFAULT 0.00, 
		`POINTS_REDEEMED` double(20,2) DEFAULT 0.00, 
		`DATE_CREATED` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, 
		`CREATED_BY` varchar(25), 
		`DATE_MODIFIED` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, 
		`MODIFIED_BY` varchar(25), 
		`APPROVAL_DATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, 
		`APPROVED` CHAR(1), 
		`APPROVED_BY` varchar(30),
				 PRIMARY KEY (`SERIAL_NUMBER`)
	)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

	

CREATE TABLE `LOYALTY_TRNS` (
		`SERIAL_NUMBER` int NOT NULL AUTO_INCREMENT, 
		`CARD_NO` varchar(50), 
		`CARD_TYPE` varchar(3) DEFAULT 'LOY', 
		`LEDGER_NUMBER` varchar(25) NOT NULL, 
		`TRANS_DATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, 
		`TRANS_TYPE` varchar(3) NOT NULL, 
		`ORDER_NO` BIGINT NOT NULL, 
		`ORDER_TYPE` varchar(3) NOT NULL, 
		`DESCRIPTION` varchar(50), 
		`ORDER_DATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, 
		`FISCAL_YEAR` int, 
		`FISCAL_MONTH` int, 
		`POINTS_AWARDED` double(20,2) DEFAULT 0.00, 
		`POINTS_REDEEMED` double(20,2) DEFAULT 0.00, 
		`DATE_CREATED` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, 
		`CREATED_BY` varchar(25), 
		`DATE_MODIFIED` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, 
		`MODIFIED_BY` varchar(25), 
		`APPROVAL_DATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, 
		`APPROVED` CHAR(1), 
		`APPROVED_BY` varchar(30),
		PRIMARY KEY (`SERIAL_NUMBER`)
	)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

	

CREATE TABLE `MOBILE_MONEY` (
		`SERIAL_NUMBER` int NOT NULL AUTO_INCREMENT, 
		`BILLREFNUMBER` varchar(200), 
		`BUSINESSSHORTCODE` varchar(200), 
		`INVOICENUMBER` varchar(200), 
		`MSISDN` varchar(20), 
		`ORGACCOUNTBALANCE` varchar(50), 
		`THIRDPARTYTRANSID` varchar(100), 
		`TRANSAMOUNT` varchar(50), 
		`TRANSID` varchar(200), 
		`TRANSTIME` varchar(50), 
		`TRANSTYPE` varchar(50), 
		`CUSTOMERFIRSTNAME` varchar(50), 
		`CUSTOMERSURNAME` varchar(50), 
		`CUSTOMERLASTNAME` varchar(50), 
		`POST_FLAG1` varchar(3) DEFAULT 'NEW', 
		`POST_FLAG2` varchar(3) DEFAULT 'NEW', 
		`POST_FLAG3` varchar(3) DEFAULT 'NEW', 
		`LEDGER_NUMBER` varchar(50), 
		`LEDGER_NAMES` varchar(100) DEFAULT 'NON', 
		`COMPANY` varchar(100) DEFAULT 'NON', 
		`SMS_FLAG` varchar(100) DEFAULT 'NEW', 
		`SMSMSG_ID` varchar(100) DEFAULT '0', 
		`SMSCLIENT_ID` varchar(100) DEFAULT '0', 
		`SMSERROR_CODE` varchar(100) DEFAULT '0', 
		`SMSERROR_DESC` varchar(200) DEFAULT '0', 
		`SMSACTION_ID` varchar(100) DEFAULT '0', 
		`SMSRESPONSE` varchar(100) DEFAULT '0', 
		`MESSAGE_SENT` varchar(500) DEFAULT 'NONE',
		PRIMARY KEY (`SERIAL_NUMBER`)
	)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

	

CREATE TABLE `MOBILE_USERS` (
		`ID` int NOT NULL AUTO_INCREMENT, 
		`USERNAME` varchar(25), 
		`PASSWORD` varchar(300), 
		`NAME` varchar(250), 
		`ID_NUMBER` varchar(25), 
		`BRANCH_CODE` varchar(25), 
		`CATEGORY` varchar(25), 
		`IMEI` varchar(25), 
		`FIRST_TIME` CHAR(1) DEFAULT 'N', 
		`DAY_OPENED` varchar(1) DEFAULT 'N', 
		`MOBILE_NUMBER` varchar(20), 
		`RESIDENCE` varchar(50), 
		`ENABLE_USER` CHAR(1) DEFAULT 'N', 
		`APP_FUEL_PASSWORD` varchar(250), 
		`APP_ORD_PASSWORD` varchar(250), 
		`APP_POS_PASSWORD` varchar(250), 
		`ENABLE_USER_FUEL` CHAR(1), 
		`ENABLE_USER_ORD` CHAR(1), 
		`ENABLE_USER_POS` CHAR(1), 
		`FIRST_TIME_FUEL` varchar(1), 
		`FIRST_TIME_ORD` varchar(1), 
		`FIRST_TIME_POS` varchar(1),
		PRIMARY KEY (`ID`)
	)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

	
	
	CREATE TABLE `FUEL_PRICES` (
		`SERIAL_NUMBER` int NOT NULL AUTO_INCREMENT, 
		`TOWN` varchar(20), 
		`PMS` double(20,8) DEFAULT 0, 
		`AGO` double(20,8) DEFAULT 0, 
		`DPK` double(20,8) DEFAULT 0, 
		`PMS_BUY_TC` double(20,8) DEFAULT 0, 
		`AGO_BUY_TC` double(20,8) DEFAULT 0, 
		`DPK_BUY_TC` double(20,8) DEFAULT 0, 
		`PMS_SALE_TC` double(20,8) DEFAULT 0, 
		`AGO_SALE_TC` double(20,8) DEFAULT 0, 
		`DPK_SALE_TC` double(20,8) DEFAULT 0, 
		`PMS_BUY_LEVY` double(20,8) DEFAULT 0, 
		`AGO_BUY_LEVY` double(20,8) DEFAULT 0, 
		`DPK_BUY_LEVY` double(20,8) DEFAULT 0, 
		`PMS_SALE_LEVY` double(20,8) DEFAULT 0, 
		`AGO_SALE_LEVY` double(20,8) DEFAULT 0, 
		`DPK_SALE_LEVY` double(20,8) DEFAULT 0, 
		`PMS_LANDING_COST` double(20,8) DEFAULT 0, 
		`AGO_LANDING_COST` double(20,8) DEFAULT 0, 
		`DPK_LANDING_COST` double(20,8) DEFAULT 0, 
		`PMS_BUY_MARGIN` double(20,8) DEFAULT 0, 
		`AGO_BUY_MARGIN` double(20,8) DEFAULT 0, 
		`DPK_BUY_MARGIN` double(20,8) DEFAULT 0, 
		`AGO_BUY_PRICE` double(20,8) DEFAULT 0, 
		`DPK_BUY_PRICE` double(20,8) DEFAULT 0, 
		`PMS_BUY_PRICE` double(20,8) DEFAULT 0, 
		`AGO_SAD_COST` double(20,8) DEFAULT 0, 
		`DPK_SAD_COST` double(20,8) DEFAULT 0, 
		`PMS_SAD_COST` double(20,8) DEFAULT 0, 
		`AGO_GRM` double(20,8) DEFAULT 0, 
		`DPK_GRM` double(20,8) DEFAULT 0, 
		`PMS_GRM` double(20,8) DEFAULT 0, 
		`AGO_EXD` double(20,8) DEFAULT 0, 
		`DPK_EXD` double(20,8) DEFAULT 0, 
		`PMS_EXD` double(20,8) DEFAULT 0, 
		`AGO_VAT` double(20,8) DEFAULT 0, 
		`DPK_VAT` double(20,8) DEFAULT 0, 
		`PMS_VAT` double(20,8) DEFAULT 0, 
		`AGO_LEVIES` double(20,8) DEFAULT 0, 
		`DPK_LEVIES` double(20,8) DEFAULT 0, 
		`PMS_LEVIES` double(20,8) DEFAULT 0,
		PRIMARY KEY (`SERIAL_NUMBER`)
	)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

CREATE TABLE `CUSTOMERS` (
		`SERIAL_NUMBER` int NOT NULL AUTO_INCREMENT, 
		`LEDGER_NUMBER` varchar(16) NOT NULL, 
		`LEDGER_NAME` varchar(60), 
		`LEDGER_STATUS` varchar(3) DEFAULT 'ACT', 
		`FIRST_THREE` varchar(3), 
		`TELEPHONES` varchar(60), 
		`ADDRESS` varchar(500), 
		`TOWNCITY` varchar(60), 
		`EMAIL_ADDRESS` varchar(60), 
		`WEBSITE` varchar(60), 
		`CONTACT_PERSON` varchar(60), 
		`CONTACT_TITLE` varchar(60), 
		`CONTACT_PHONE` varchar(32), 
		`REPRESENTATIVE` varchar(16), 
		`BASE_LOCATION` varchar(16), 
		`DISCOUNT_RATE` double(20,6) DEFAULT 0, 
		`ON_CREDIT` CHAR(1) DEFAULT 'Y', 
		`CREDIT_LIMIT` double(20,2) DEFAULT 0, 
		`CREDIT_PERIOD` double(5,0) DEFAULT 0, 
		`DISTANCE` double(7,2) DEFAULT 0, 
		`TRANSPORT_RATE` double(20,2) DEFAULT 0, 
		`LEDGER_GROUP` varchar(3) DEFAULT 'GEN', 
		`SALES_AREA` varchar(3) DEFAULT 'N/A', 
		`VAT_NUMBER` varchar(16), 
		`PIN_CODE` varchar(16), 
		`DELIVERY_METHOD` varchar(3) DEFAULT 'N/A', 
		`DELIVERY_PERIOD` double(3,0) DEFAULT 0, 
		`PAYMENT_METHOD` varchar(3) DEFAULT 'N/A', 
		`LOYALTY_POINTS` double(20,0) DEFAULT 0, 
		`CREDIT_RATING` double(3,0) DEFAULT 10, 
		`PREF_PRIORITY` double(3,0) DEFAULT 10, 
		`COUNTRY_CODE` varchar(3) DEFAULT 'KEN', 
		`CATEGORY` varchar(3) DEFAULT 'N/A', 
		`CARD_NUMBER` varchar(18), 
		`ACCOUNT_NUMBER` varchar(16) DEFAULT '210100', 
		`CHECK_OPTION1` CHAR(1) DEFAULT 'N', 
		`CHECK_OPTION2` CHAR(1) DEFAULT 'N', 
		`PARENT_NUMBER` varchar(16), 
		`LAST_ACTIVITY` TIMESTAMP, 
		`ADDRESS_TO` varchar(500), 
		`CONFIGURATION` varchar(16) DEFAULT 'CONFIGURATION', 
		`DATE_MODIFIED` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, 
		`MODIFIED_BY` varchar(16), 
		`EXPORT_INDICATOR` CHAR(1) DEFAULT 'N', 
		`DATE_EXPORTED` TIMESTAMP, 
		`EXPORTED_BY` varchar(50), 
		`SYNC_STATUS` varchar(3) DEFAULT 'NEW', 
		`ID_NUMBER` int, 
		`RESTRICT_LOCATION` CHAR(1) DEFAULT 'N',
		PRIMARY KEY (`SERIAL_NUMBER`)
	)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

CREATE TABLE `spmobileusers` (
  `uid` int NOT NULL AUTO_INCREMENT,
  `uname` varchar(45) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `email` varchar(45) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `pass` varchar(45) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  PRIMARY KEY (`uid`),
  UNIQUE KEY `email_UNIQUE` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;


CREATE TABLE `spusers` (
  `pass` varchar(100) NOT NULL,
  `uname` varchar(100) NOT NULL,
  `fname` varchar(100) NOT NULL,
  `lname` varchar(100) NOT NULL,
  `role` varchar(100) NOT NULL,
  `IdNo` varchar(100) NOT NULL,
  `PhoneNo` varchar(20) NOT NULL,
  `Email` varchar(200) NOT NULL,
  `Residence` varchar(200) NOT NULL,
  `Status` varchar(20) NOT NULL,
  PRIMARY KEY (`pass`),
  UNIQUE KEY `uname` (`uname`),
  UNIQUE KEY `pass_UNIQUE` (`pass`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


LOCK TABLES `spusers` WRITE;
INSERT INTO `spusers` VALUES ('gAx9BzrILChsIg8MZ/MdCnwuxxVj4YgEDHLiSQYGKxo=','Mod','Simba','Admin','Admin','2222222222','070000000','hello@simbapos.co.ke','Nairobi','Enabled'),('xIj9gYQSNMqV0sJXAFV+km4mBkan0/Zg6pFIsgappbc=','Vinni','Vincent','Owino','Admin','645646','464646','','','Enabled');
UNLOCK TABLES;


CREATE TABLE `spuroles` (
  `roleID` int NOT NULL AUTO_INCREMENT,
  `Role` varchar(100) NOT NULL,
  `ucSAdmin` varchar(20) NOT NULL,
  `ucAdmin` varchar(50) NOT NULL,
  `ucStore` varchar(10) NOT NULL,
  `ucAccounts` varchar(10) NOT NULL,
  `ucInventory` varchar(10) NOT NULL,
  `ucSettings` varchar(10) NOT NULL,
  `ucReports` varchar(10) NOT NULL,
  `ucBOreports` varchar(10) NOT NULL,
  `ucHR` varchar(10) NOT NULL,
  `ucPOS` varchar(10) NOT NULL,
  `ucPayments` varchar(10) NOT NULL,
  `ucVoid` varchar(10) NOT NULL,
  `ucCompliment` varchar(10) NOT NULL,
  `ucReverse` varchar(100) NOT NULL,
  `ucCreate` varchar(100) NOT NULL,
  `ucUpdate` varchar(10) NOT NULL,
  `ucDelete` varchar(10) NOT NULL,
  `ucOpenorders` varchar(10) NOT NULL,
  `ucStockin` varchar(10) NOT NULL,
  `ucMovestock` varchar(10) NOT NULL,
  `ucReconcile` varchar(10) NOT NULL,
  `ucManagestore` varchar(10) NOT NULL,
  `ucPayroll` varchar(10) NOT NULL,
  `ucManageroles` varchar(10) NOT NULL,
  `ucViewlogs` varchar(10) NOT NULL,
  `ucBulkdelete` varchar(20) NOT NULL,
  `ucShutdown` varchar(10) NOT NULL,
  `ucReturns` varchar(10) NOT NULL,
  `ucChangeprice` varchar(10) NOT NULL DEFAULT 'No',
  PRIMARY KEY (`roleID`),
  UNIQUE KEY `role` (`Role`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=latin1;


LOCK TABLES `spuroles` WRITE;
INSERT INTO `spuroles` VALUES (1,'Admin','Yes','Yes','Yes','Yes','Yes','Yes','Yes','Yes','Yes','Yes','Yes','Yes','Yes','Yes','Yes','Yes','Yes','Yes','Yes','Yes','Yes','Yes','No','No','Yes','Yes','Yes','Yes','Yes'),(2,'Waiter','No','No','No','No','No','No','No','No','No','Yes','No','No','No','No','No','No','No','No','No','No','No','No','No','No','No','No','No','No','No'),(3,'Chef','No','No','No','No','No','No','No','No','No','No','Yes','No','No','No','No','No','Yes','No','No','No','No','No','No','No','No','No','No','No','No'),(4,'Storekeeper','No','No','Yes','No','No','No','Yes','No','No','No','Yes','No','No','Yes','No','No','No','No','No','No','No','No','No','No','No','No','No','No','No'),(5,'Accountant','No','No','No','No','No','No','No','No','No','No','Yes','No','No','No','No','No','Yes','No','No','No','No','No','No','No','No','No','No','No','No'),(6,'Cashier','No','No','No','No','No','No','No','No','No','Yes','Yes','No','No','No','No','No','No','No','No','No','No','No','No','No','No','No','No','No','No'),(7,'Manager','Yes','Yes','Yes','Yes','No','Yes','Yes','Yes','Yes','Yes','Yes','Yes','Yes','Yes','Yes','Yes','Yes','No','No','No','No','No','No','No','No','No','Yes','Yes','No'),(8,'Supervisor','No','No','No','No','No','No','No','No','No','No','No','No','No','No','No','No','No','No','No','No','No','No','No','No','No','No','Yes','No','No');
UNLOCK TABLES;
