
--Insert statements for: DEPARTMENT
Insert into DEPARTMENT (DEPARTMENT_ID,NAME) values (DEPARTMENT_ID.NEXTVAL,'tech_support');
Insert into DEPARTMENT (DEPARTMENT_ID,NAME) values (DEPARTMENT_ID.NEXTVAL,'sales');
Insert into DEPARTMENT (DEPARTMENT_ID,NAME) values (DEPARTMENT_ID.NEXTVAL,'billing');
Insert into DEPARTMENT (DEPARTMENT_ID,NAME) values (DEPARTMENT_ID.NEXTVAL,'technicians');

--Insert statements for: EMPLOYEES
Insert into EMPLOYEES ("number",NAME,PHONE,EMAIL,DEPARTMENT_ID) values (EMPLOYEE_ID.NEXTVAL,'Andrew','6666666666','andrew@dawsoncollege.qc.ca','1001');
Insert into EMPLOYEES ("number",NAME,PHONE,EMAIL,DEPARTMENT_ID) values (EMPLOYEE_ID.NEXTVAL,'Jaya','7777777777','jaya@dawsoncollege.qc.ca','1002');
Insert into EMPLOYEES ("number",NAME,PHONE,EMAIL,DEPARTMENT_ID) values (EMPLOYEE_ID.NEXTVAL,'Myles','8888888888','myles@dawsoncollege.qc.ca','1003');
Insert into EMPLOYEES ("number",NAME,PHONE,EMAIL,DEPARTMENT_ID) values (EMPLOYEE_ID.NEXTVAL,'Nagi','9999999999','nagi@dawsoncollege.qc.ca','1004');
Insert into EMPLOYEES ("number",NAME,PHONE,EMAIL,DEPARTMENT_ID) values (EMPLOYEE_ID.NEXTVAL,'Josh','9999999999','josh@dawsoncollege.qc.ca','1004');
Insert into EMPLOYEES ("number",NAME,PHONE,EMAIL,DEPARTMENT_ID) values (EMPLOYEE_ID.NEXTVAL,'Bob','9999999999','bob@dawsoncollege.qc.ca','1004');

--Insert statements for: SALES_LOGIN
Insert into SALES_LOGIN (USERNAME,SALT,HASH,"number") values ('test','h7kb5jkqkl9l7qf5327qi90fb8','31323334353668376B62356A6B716B6C396C3771663533323771693930666238','1004');

--Insert statements for: TECHNICIANS_LOGIN
Insert into TECHNICIANS_LOGIN (USERNAME,SALT,HASH,"number") values ('test','h7kb5jkqkl9l7qf5327qi90fb8','31323334353668376B62356A6B716B6C396C3771663533323771693930666238','1004');
Insert into TECHNICIANS_LOGIN (USERNAME,SALT,HASH,"number") values ('yanik123','h7kb5jkqkl9l7qf5327qi90fb8','31323334353668376B62356A6B716B6C396C3771663533323771693930666238','1001');

--Insert statements for: INTERNET_PACKAGES
Insert into INTERNET_PACKAGES (PACKAGE_ID,NAME,DESCRIPTION,COST,UP,DOWN,BANDWIDTH,OVERAGE) values (PACKAGE_ID.NEXTVAL,'Essential plus','All the essentials',42.95,10,20,30,2);
Insert into INTERNET_PACKAGES (PACKAGE_ID,NAME,DESCRIPTION,COST,UP,DOWN,BANDWIDTH,OVERAGE) values (PACKAGE_ID.NEXTVAL,'Fibe 15','Fibe internet with 15Mbps Down',49.95,10,15,50,1.5);
Insert into INTERNET_PACKAGES (PACKAGE_ID,NAME,DESCRIPTION,COST,UP,DOWN,BANDWIDTH,OVERAGE) values (PACKAGE_ID.NEXTVAL,'Fibe 50','Fibe internet with 50Mbps Down',59.95,10,50,300,1);
Insert into INTERNET_PACKAGES (PACKAGE_ID,NAME,DESCRIPTION,COST,UP,DOWN,BANDWIDTH,OVERAGE) values (PACKAGE_ID.NEXTVAL,'Fibe 150','Fibe intenet ewith 150Mbps Down',64.95,150,150,600,1);

--Insert statements for: EXTRA_FEATURES
Insert into EXTRA_FEATURES (FEATURE_ID,NAME,DESCRIPTION) values ('1002','Cookie','You get free cookies');
Insert into EXTRA_FEATURES (FEATURE_ID,NAME,DESCRIPTION) values ('1001','Telephone','The user also gets unlimited landline calls.');

--Insert statements for: PACKAGES_FEATURES
Insert into PACKAGES_FEATURES (PACKAGES_ID,FEATURE_ID) values ('1001','1001');

--Insert statements for: CUSTOMERS
Insert into CUSTOMERS (USERNAME,NAME,ADDRESS,PHONE_NUMBER,EMAIL_ADDRESS,PACKAGE_ID) values ('test','sdsad sad sadsa','dawson college','4443332222','hello@gmail.com','1001');
Insert into CUSTOMERS (USERNAME,NAME,ADDRESS,PHONE_NUMBER,EMAIL_ADDRESS,PACKAGE_ID) values ('sadsadsad','asdsad sadsad','1123 sadsad','1232312312','asd@sadm2.com','1003');
Insert into CUSTOMERS (USERNAME,NAME,ADDRESS,PHONE_NUMBER,EMAIL_ADDRESS,PACKAGE_ID) values ('yanik123','Yanik Kolomatski','Dawson College','5555555555','yanik@dawsoncollege.qc.ca','1001');
Insert into CUSTOMERS (USERNAME,NAME,ADDRESS,PHONE_NUMBER,EMAIL_ADDRESS,PACKAGE_ID) values ('test1','asdsasad sadsa','133 sadsadsad','1231231234','sad@sd.com','1001');

--Insert statements for: CALLS_BILLING
Insert into CALLS_BILLING (CALL_ID,USERNAME,REPRESENTATIVE_ID,"date",DETAILS) values (CALLS_BILLING_ID.NEXTVAL,'sadsadsad','1004',to_date('09-MAY-18','DD-MON-RR'),'Customer wanted more money');
Insert into CALLS_BILLING (CALL_ID,USERNAME,REPRESENTATIVE_ID,"date",DETAILS) values (CALLS_BILLING_ID.NEXTVAL,'yanik123','1004',to_date('09-MAY-18','DD-MON-RR'),'Customer did not have enough money in the bank');

--Insert statements for: CALLS_SALES
Insert into CALLS_SALES (CALL_ID,USERNAME,REPRESENTATIVE_ID,"date",SALE) values (CALLS_SALES_ID.NEXTVAL,'sadsadsad','1004',to_date('09-MAY-18','DD-MON-RR'),'1');
Insert into CALLS_SALES (CALL_ID,USERNAME,REPRESENTATIVE_ID,"date",SALE) values (CALLS_SALES_ID.NEXTVAL,'yanik123','1004',to_date('09-MAY-18','DD-MON-RR'),'0');

--Insert statements for: CALLS_TECH
Insert into CALLS_TECH (CALL_ID,USERNAME,REPRESENTATIVE_ID,"date", DETAILS, PROBLEM) values (CALLS_TECH_ID.NEXTVAL,'sadsadsad','1004',to_date('09-MAY-18','DD-MON-RR'),'router unplugged', 'router not working');
Insert into CALLS_TECH (CALL_ID,USERNAME,REPRESENTATIVE_ID,"date",DETAILS, PROBLEM) values (CALLS_TECH_ID.NEXTVAL,'yanik123','1004',to_date('09-MAY-18','DD-MON-RR'),'Router overheat', 'House blew up!');

--Insert statements for: USAGE
Insert into USAGE (USERNAME,DAY,DOWN,UP) values ('yanik123',to_date('26-DEC-17','DD-MON-RR'),30,5);
Insert into USAGE (USERNAME,DAY,DOWN,UP) values ('yanik123',to_date('27-DEC-17','DD-MON-RR'),40,2);
Insert into USAGE (USERNAME,DAY,DOWN,UP) values ('yanik123',to_date('28-DEC-17','DD-MON-RR'),20,1);
Insert into USAGE (USERNAME,DAY,DOWN,UP) values ('yanik123',to_date('29-DEC-17','DD-MON-RR'),40,2);
Insert into USAGE (USERNAME,DAY,DOWN,UP) values ('yanik123',to_date('30-DEC-17','DD-MON-RR'),30,10);

--Insert statements for: INVOICE
Insert into INVOICE (INVOICE_ID,USERNAME,COST_PLAN,OVERAGE,OTHER_SERVICES,GST,QST,PREVIOUS_BALANCE,NEW_BALANCE,TOTAL,DUE_DATE,START_INVOICING_PERIOD,END_INVOICING_PERIOD) values (INVOICE_ID.NEXTVAL,'yanik123',35,1.5,10,2.33,4.64,500,446.53,25.6,to_date('31-DEC-17','DD-MON-RR'),to_date('28-DEC-17','DD-MON-RR'),to_date('30-DEC-17','DD-MON-RR'));
Insert into INVOICE (INVOICE_ID,USERNAME,COST_PLAN,OVERAGE,OTHER_SERVICES,GST,QST,PREVIOUS_BALANCE,NEW_BALANCE,TOTAL,DUE_DATE,START_INVOICING_PERIOD,END_INVOICING_PERIOD) values (INVOICE_ID.NEXTVAL,'yanik123',35,1.5,10,2.33,4.64,564,256.7,23.2,to_date('30-DEC-17','DD-MON-RR'),to_date('25-DEC-17','DD-MON-RR'),to_date('27-DEC-17','DD-MON-RR'));

--Insert statements for: SERVICES
Insert into SERVICES (SERVICE_ID,NAME,COST) values ('1001','Removal',0);
Insert into SERVICES (SERVICE_ID,NAME,COST) values ('1002','Upgrade',0);
Insert into SERVICES (SERVICE_ID,NAME,COST) values ('1003','Support',0);

--Insert statements for: PURCHASES
Insert into PURCHASES (purchase_id, username, service_id, date_of_order) values (PURCHASES_ID.NEXTVAL, 'yanik123', '1001', SYSDATE);

--Insert statements for: SERVICE_REQUEST      
Insert into SERVICE_REQUEST (REQUEST_ID,USERNAME,SERVICE_ID,EMP_NUMBER,DONE,DESCRIPTION) values (SERVICE_ID.NEXTVAL,'yanik123','1001',null,'0','test');
Insert into SERVICE_REQUEST (REQUEST_ID,USERNAME,SERVICE_ID,EMP_NUMBER,DONE,DESCRIPTION) values (SERVICE_ID.NEXTVAL,'yanik123','1001',null,'0','Please help me');
Insert into SERVICE_REQUEST (REQUEST_ID,USERNAME,SERVICE_ID,EMP_NUMBER,DONE,DESCRIPTION) values (SERVICE_ID.NEXTVAL,'yanik123','1001',null,'0','help me');
   
--Insert statements for: PAYMENTS
Insert into PAYMENTS (PAYMENT_ID, USERNAME, RECEIVE_DATE, AMOUNT) values (PAYMENT_ID.NEXTVAL, 'yanik123', SYSDATE, 50);

--Insert statements for: VISITS
Insert into VISITS (VISIT_ID,USERNAME,TECHNICIAN_ID,"start",END,NOTES) values (VISIT_ID.NEXTVAL,'test1','1004',to_date('12-DEC-18','DD-MON-RR'),to_date('12-DEC-18','DD-MON-RR'),null);
Insert into VISITS (VISIT_ID,USERNAME,TECHNICIAN_ID,"start",END,NOTES) values (VISIT_ID.NEXTVAL,'sadsadsad','1004',to_date('12-DEC-18','DD-MON-RR'),to_date('12-DEC-18','DD-MON-RR'),null);

--Insert statements for: EQUIPMENT
Insert into EQUIPMENT (NAME,COST,MANUFACTURER) values ('SCREWS',0.05,'MAN1');
Insert into EQUIPMENT (NAME,COST,MANUFACTURER) values ('BOLTS',0.05,'MAN1');
Insert into EQUIPMENT (NAME,COST,MANUFACTURER) values ('FIBER_OPTIC (1 ft)',2,'MAN2');
Insert into EQUIPMENT (NAME,COST,MANUFACTURER) values ('FUSION_SPLICER',2000,'MAN2');
Insert into EQUIPMENT (NAME,COST,MANUFACTURER) values ('DRILL',200,'MAN3');

--Insert statements for: EQUIPMENT_ITEMS
Insert into EQUIPMENT_ITEMS (EMP_ID,NAME,AMOUNT) values ('1004','SCREWS',200);
Insert into EQUIPMENT_ITEMS (EMP_ID,NAME,AMOUNT) values ('1004','BOLTS',200);
Insert into EQUIPMENT_ITEMS (EMP_ID,NAME,AMOUNT) values ('1004','FIBER_OPTIC (1 ft)',75);

--Insert statements for: EQUIPMENT_TOOLS
Insert into EQUIPMENT_TOOLS (EMP_ID,NAME,ISFUNCTIONING) values ('1004','FUSION_SPLICER','0');
Insert into EQUIPMENT_TOOLS (EMP_ID,NAME,ISFUNCTIONING) values ('1004','DRILL','1');
 
--Insert statements for: EQUIPMENT_ORDERS
Insert into EQUIPMENT_ORDERS (order_id,EMP_ID,NAME,AMOUNT,ORDER_DATE) values (equipment_order_id.NEXTVAL,'1004','FUSION_SPLICER',1,to_date('09-MAY-18','DD-MON-RR'));

--Insert statements for: CUSTOMER_LOGIN
Insert into CUSTOMER_LOGIN (USERNAME,SALT,HASH) values ('yanik123','30ufcug903m92sj7cam7ardr2l','68656C6C6F31333075666375673930336D3932736A3763616D3761726472326C');
Insert into CUSTOMER_LOGIN (USERNAME,SALT,HASH) values ('test','30ufcug903m92sj7cam7ardr2l','68656C6C6F31333075666375673930336D3932736A3763616D3761726472326C');
Insert into CUSTOMER_LOGIN (USERNAME,SALT,HASH) values ('test1','30ufcug903m92sj7cam7ardr2l','68656C6C6F31333075666375673930336D3932736A3763616D3761726472326C');