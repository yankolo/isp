-- Created by Vertabelo (http://vertabelo.com)
-- Last modification date: 2018-05-10 01:22:31.563

-- tables
-- Table: Customers
CREATE TABLE Customers (
    username varchar2(30)  NOT NULL,
    name varchar2(100)  NOT NULL,
    address varchar2(100)  NOT NULL,
    phone_number char(10)  NULL,
    email_address varchar2(100)  NULL,
    package_id char(4)  NOT NULL,
    CONSTRAINT Customers_pk PRIMARY KEY (username)
) ;

-- Table: Department
CREATE TABLE Department (
    department_id char(4)  NOT NULL,
    name varchar2(30)  NOT NULL,
    CONSTRAINT Department_pk PRIMARY KEY (department_id)
) ;

-- Table: EQUIPMENT
CREATE TABLE EQUIPMENT (
    name varchar2(30)  NOT NULL,
    cost number(7,2)  NOT NULL,
    manufacturer varchar2(30)  NOT NULL,
    CONSTRAINT EQUIPMENT_pk PRIMARY KEY (name)
) ;

-- Table: Employees
CREATE TABLE Employees (
    "number" char(4)  NOT NULL,
    name varchar2(30)  NOT NULL,
    phone char(10)  NOT NULL,
    email varchar2(100)  NOT NULL,
    department_id char(4)  NOT NULL,
    CONSTRAINT Employees_pk PRIMARY KEY ("number")
) ;

-- Table: Extra_Features
CREATE TABLE Extra_Features (
    feature_id char(4)  NOT NULL,
    name varchar2(30)  NOT NULL,
    description varchar2(500)  NOT NULL,
    CONSTRAINT Extra_Features_pk PRIMARY KEY (feature_id)
) ;

-- Table: Internet_Packages
CREATE TABLE Internet_Packages (
    package_id char(4)  NOT NULL,
    name varchar2(30)  NOT NULL,
    description varchar2(500)  NOT NULL,
    cost number(6,2)  NOT NULL,
    up number(6,2)  NOT NULL,
    down number(6,2)  NOT NULL,
    bandwidth number(6,2)  NOT NULL,
    overage number(6,2)  NOT NULL,
    CONSTRAINT Internet_Packages_pk PRIMARY KEY (package_id)
) ;

CREATE INDEX package_cost_index 
on Internet_Packages 
(cost ASC)
;

-- Table: Invoice
CREATE TABLE Invoice (
    invoice_id char(8)  NOT NULL,
    username varchar2(30)  NOT NULL,
    cost_plan number(6,2)  NOT NULL,
    overage number(6,2)  NOT NULL,
    other_services number(6,2)  NOT NULL,
    GST number(6,2)  NOT NULL,
    QST number(6,2)  NOT NULL,
    previous_balance number(6,2)  NOT NULL,
    new_balance number(6,2)  NOT NULL,
    total number(8,2)  NOT NULL,
    due_date date  NOT NULL,
    start_invoicing_period date  NOT NULL,
    end_invoicing_period date  NOT NULL,
    CONSTRAINT Invoice_pk PRIMARY KEY (invoice_id)
) ;

CREATE INDEX start_invoicing_index 
on Invoice 
(start_invoicing_period ASC)
;

CREATE INDEX end_invoicing_index 
on Invoice 
(end_invoicing_period ASC)
;

-- Table: Packages_Features
CREATE TABLE Packages_Features (
    packages_id char(4)  NOT NULL,
    feature_id char(4)  NOT NULL,
    CONSTRAINT Packages_Features_pk PRIMARY KEY (packages_id,feature_id)
) ;

-- Table: Payments
CREATE TABLE Payments (
    payment_id char(8)  NOT NULL,
    username varchar2(30)  NOT NULL,
    receive_date date  NOT NULL,
    amount number(6,2)  NOT NULL,
    CONSTRAINT Payments_pk PRIMARY KEY (payment_id)
) ;

CREATE INDEX Payments_username_index 
on Payments 
(username ASC)
;

-- Table: Purchases
CREATE TABLE Purchases (
    purchase_id char(8)  NOT NULL,
    username varchar2(30)  NOT NULL,
    service_id char(4)  NOT NULL,
    date_of_order date  NOT NULL,
    CONSTRAINT Purchases_pk PRIMARY KEY (purchase_id)
) ;

CREATE INDEX service_id_index 
on Purchases 
(service_id ASC)
;

CREATE INDEX username_index 
on Purchases 
(username ASC)
;

-- Table: Services
CREATE TABLE Services (
    service_id char(4)  NOT NULL,
    name varchar2(50)  NOT NULL,
    cost number(6,2)  NOT NULL,
    CONSTRAINT Services_pk PRIMARY KEY (service_id)
) ;

-- Table: Usage
CREATE TABLE Usage (
    username varchar2(30)  NOT NULL,
    day date  NOT NULL,
    down number(6,2)  NOT NULL,
    up number(6,2)  NOT NULL,
    CONSTRAINT Usage_pk PRIMARY KEY (username,day)
) ;

-- Table: Visits
CREATE TABLE Visits (
    visit_id char(8)  NOT NULL,
    username varchar2(30)  NOT NULL,
    technician_id char(4)  NOT NULL,
    "start" date  NOT NULL,
    end date  NOT NULL,
    notes varchar2(500)  NULL,
    CONSTRAINT Visits_pk PRIMARY KEY (visit_id)
) ;

CREATE INDEX start_date_index 
on Visits 
("start" ASC)
;

CREATE INDEX end_date_index 
on Visits 
(end ASC)
;

-- Table: calls_billing
CREATE TABLE calls_billing (
    call_id char(8)  NOT NULL,
    username varchar2(30)  NOT NULL,
    representative_id char(4)  NOT NULL,
    "date" date  NOT NULL,
    details varchar2(500)  NOT NULL,
    CONSTRAINT calls_billing_pk PRIMARY KEY (call_id)
) ;

CREATE INDEX calls_billing_emp_id_index 
on calls_billing 
(representative_id ASC)
;

-- Table: calls_sales
CREATE TABLE calls_sales (
    call_id char(8)  NOT NULL,
    username varchar2(30)  NULL,
    representative_id char(4)  NOT NULL,
    "date" date  NOT NULL,
    sale char(1)  NOT NULL,
    CONSTRAINT calls_sales_pk PRIMARY KEY (call_id)
) ;

CREATE INDEX calls_sales_emp_id_index 
on calls_sales 
(representative_id ASC)
;

-- Table: calls_tech
CREATE TABLE calls_tech (
    call_id char(8)  NOT NULL,
    username varchar2(30)  NOT NULL,
    representative_id char(4)  NOT NULL,
    "date" date  NOT NULL,
    details varchar2(500)  NOT NULL,
    problem varchar2(500)  NOT NULL,
    CONSTRAINT calls_tech_pk PRIMARY KEY (call_id)
) ;

CREATE INDEX calls_tech_emp_id_index 
on calls_tech 
(representative_id ASC)
;

-- Table: customer_login
CREATE TABLE customer_login (
    username varchar2(30)  NOT NULL,
    salt varchar2(32)  NOT NULL,
    hash raw(265)  NOT NULL,
    CONSTRAINT customer_login_pk PRIMARY KEY (username)
) ;

-- Table: equipment_items
CREATE TABLE equipment_items (
    emp_id char(4)  NOT NULL,
    name varchar2(30)  NOT NULL,
    amount number(4,0)  NOT NULL,
    CONSTRAINT equipment_items_pk PRIMARY KEY (emp_id,name)
) ;

-- Table: equipment_orders
CREATE TABLE equipment_orders (
    order_id char(8)  NOT NULL,
    emp_id char(4)  NOT NULL,
    name varchar2(30)  NOT NULL,
    amount number(4,0)  NOT NULL,
    order_date date  NOT NULL,
    CONSTRAINT equipment_orders_pk PRIMARY KEY (order_id)
) ;

-- Table: equipment_tools
CREATE TABLE equipment_tools (
    emp_id char(4)  NOT NULL,
    name varchar2(30)  NOT NULL,
    isFunctioning char(1)  DEFAULT 1 NOT NULL,
    CONSTRAINT equipment_tools_pk PRIMARY KEY (emp_id,name)
) ;

-- Table: sales_login
CREATE TABLE sales_login (
    username varchar2(30)  NOT NULL,
    salt varchar2(32)  NOT NULL,
    hash raw(265)  NOT NULL,
    "number" char(4)  NOT NULL,
    CONSTRAINT sales_login_pk PRIMARY KEY (username)
) ;

-- Table: service_request
CREATE TABLE service_request (
    request_id char(4)  NOT NULL,
    username varchar2(30)  NOT NULL,
    service_id char(4)  NOT NULL,
    emp_number char(4)  NULL,
    done char(1)  NOT NULL,
    description varchar2(500)  NOT NULL,
    CONSTRAINT service_request_pk PRIMARY KEY (request_id)
) ;

CREATE INDEX service_request_username_index 
on service_request 
(username ASC)
;

-- Table: technicians_login
CREATE TABLE technicians_login (
    username varchar2(30)  NOT NULL,
    salt varchar2(32)  NOT NULL,
    hash raw(265)  NOT NULL,
    "number" char(4)  NOT NULL,
    CONSTRAINT technicians_login_pk PRIMARY KEY (username)
) ;

-- foreign keys
-- Reference: Customers_Internet_Packages (table: Customers)
ALTER TABLE Customers ADD CONSTRAINT Customers_Internet_Packages
    FOREIGN KEY (package_id)
    REFERENCES Internet_Packages (package_id);

-- Reference: Customers_Usage (table: Usage)
ALTER TABLE Usage ADD CONSTRAINT Customers_Usage
    FOREIGN KEY (username)
    REFERENCES Customers (username);

-- Reference: Customers_customer_login (table: customer_login)
ALTER TABLE customer_login ADD CONSTRAINT Customers_customer_login
    FOREIGN KEY (username)
    REFERENCES Customers (username);

-- Reference: EQUIPMENT_equipment_items (table: equipment_items)
ALTER TABLE equipment_items ADD CONSTRAINT EQUIPMENT_equipment_items
    FOREIGN KEY (name)
    REFERENCES EQUIPMENT (name);

-- Reference: EQUIPMENT_equipment_orders (table: equipment_orders)
ALTER TABLE equipment_orders ADD CONSTRAINT EQUIPMENT_equipment_orders
    FOREIGN KEY (name)
    REFERENCES EQUIPMENT (name);

-- Reference: EQUIPMENT_equipment_tools (table: equipment_tools)
ALTER TABLE equipment_tools ADD CONSTRAINT EQUIPMENT_equipment_tools
    FOREIGN KEY (name)
    REFERENCES EQUIPMENT (name);

-- Reference: Employees_Department (table: Employees)
ALTER TABLE Employees ADD CONSTRAINT Employees_Department
    FOREIGN KEY (department_id)
    REFERENCES Department (department_id);

-- Reference: Employees_service_request (table: service_request)
ALTER TABLE service_request ADD CONSTRAINT Employees_service_request
    FOREIGN KEY (emp_number)
    REFERENCES Employees ("number");

-- Reference: Employees_technicians_login (table: technicians_login)
ALTER TABLE technicians_login ADD CONSTRAINT Employees_technicians_login
    FOREIGN KEY ("number")
    REFERENCES Employees ("number");

-- Reference: Features_Packages (table: Packages_Features)
ALTER TABLE Packages_Features ADD CONSTRAINT Features_Packages
    FOREIGN KEY (feature_id)
    REFERENCES Extra_Features (feature_id)
    ON DELETE CASCADE;

-- Reference: Invoice_Customers (table: Invoice)
ALTER TABLE Invoice ADD CONSTRAINT Invoice_Customers
    FOREIGN KEY (username)
    REFERENCES Customers (username);

-- Reference: Packages_Features (table: Packages_Features)
ALTER TABLE Packages_Features ADD CONSTRAINT Packages_Features
    FOREIGN KEY (packages_id)
    REFERENCES Internet_Packages (package_id)
    ON DELETE CASCADE;

-- Reference: Payments_Customers (table: Payments)
ALTER TABLE Payments ADD CONSTRAINT Payments_Customers
    FOREIGN KEY (username)
    REFERENCES Customers (username);

-- Reference: Purchases_Customers (table: Purchases)
ALTER TABLE Purchases ADD CONSTRAINT Purchases_Customers
    FOREIGN KEY (username)
    REFERENCES Customers (username);

-- Reference: Purchases_Services (table: Purchases)
ALTER TABLE Purchases ADD CONSTRAINT Purchases_Services
    FOREIGN KEY (service_id)
    REFERENCES Services (service_id);

-- Reference: Table_28_Customers (table: service_request)
ALTER TABLE service_request ADD CONSTRAINT Table_28_Customers
    FOREIGN KEY (username)
    REFERENCES Customers (username);

-- Reference: Table_28_Services (table: service_request)
ALTER TABLE service_request ADD CONSTRAINT Table_28_Services
    FOREIGN KEY (service_id)
    REFERENCES Services (service_id);

-- Reference: Visits_Customers (table: Visits)
ALTER TABLE Visits ADD CONSTRAINT Visits_Customers
    FOREIGN KEY (username)
    REFERENCES Customers (username);

-- Reference: Visits_Employees (table: Visits)
ALTER TABLE Visits ADD CONSTRAINT Visits_Employees
    FOREIGN KEY (technician_id)
    REFERENCES Employees ("number");

-- Reference: calls_billing_Customers (table: calls_billing)
ALTER TABLE calls_billing ADD CONSTRAINT calls_billing_Customers
    FOREIGN KEY (username)
    REFERENCES Customers (username);

-- Reference: calls_billing_Employees (table: calls_billing)
ALTER TABLE calls_billing ADD CONSTRAINT calls_billing_Employees
    FOREIGN KEY (representative_id)
    REFERENCES Employees ("number");

-- Reference: calls_sales_Customers (table: calls_sales)
ALTER TABLE calls_sales ADD CONSTRAINT calls_sales_Customers
    FOREIGN KEY (username)
    REFERENCES Customers (username);

-- Reference: calls_sales_Employees (table: calls_sales)
ALTER TABLE calls_sales ADD CONSTRAINT calls_sales_Employees
    FOREIGN KEY (representative_id)
    REFERENCES Employees ("number");

-- Reference: calls_tech_Customers (table: calls_tech)
ALTER TABLE calls_tech ADD CONSTRAINT calls_tech_Customers
    FOREIGN KEY (username)
    REFERENCES Customers (username);

-- Reference: calls_tech_Employees (table: calls_tech)
ALTER TABLE calls_tech ADD CONSTRAINT calls_tech_Employees
    FOREIGN KEY (representative_id)
    REFERENCES Employees ("number");

-- Reference: equipment_items_Employees (table: equipment_items)
ALTER TABLE equipment_items ADD CONSTRAINT equipment_items_Employees
    FOREIGN KEY (emp_id)
    REFERENCES Employees ("number");

-- Reference: equipment_orders_Employees (table: equipment_orders)
ALTER TABLE equipment_orders ADD CONSTRAINT equipment_orders_Employees
    FOREIGN KEY (emp_id)
    REFERENCES Employees ("number");

-- Reference: equipment_tools_Employees (table: equipment_tools)
ALTER TABLE equipment_tools ADD CONSTRAINT equipment_tools_Employees
    FOREIGN KEY (emp_id)
    REFERENCES Employees ("number");

-- Reference: sales_login_Employees (table: sales_login)
ALTER TABLE sales_login ADD CONSTRAINT sales_login_Employees
    FOREIGN KEY ("number")
    REFERENCES Employees ("number");

-- sequences
-- Sequence: calls_billing_id
CREATE SEQUENCE calls_billing_id
      INCREMENT BY 1
      MINVALUE 10000001
      MAXVALUE 99999999
      START WITH 10000001
      NOCACHE
      NOCYCLE;

-- Sequence: calls_sales_id
CREATE SEQUENCE calls_sales_id
      INCREMENT BY 1
      MINVALUE 10000001
      MAXVALUE 99999999
      START WITH 10000001
      NOCACHE
      NOCYCLE;

-- Sequence: calls_tech_id
CREATE SEQUENCE calls_tech_id
      INCREMENT BY 1
      MINVALUE 10000001
      MAXVALUE 99999999
      START WITH 10000001
      NOCACHE
      NOCYCLE;

-- Sequence: department_id
CREATE SEQUENCE department_id
      INCREMENT BY 1
      MINVALUE 1001
      MAXVALUE 9999
      START WITH 1001
      NOCACHE
      NOCYCLE;

-- Sequence: employee_id
CREATE SEQUENCE employee_id
      INCREMENT BY 1
      MINVALUE 1001
      MAXVALUE 9999
      START WITH 1001
      NOCACHE
      NOCYCLE;

-- Sequence: equipment_order_id
CREATE SEQUENCE equipment_order_id
      INCREMENT BY 1
      MINVALUE 10000001
      MAXVALUE 99999999
      START WITH 10000001
      NOCACHE
      NOCYCLE;

-- Sequence: feature_id
CREATE SEQUENCE feature_id
      INCREMENT BY 1
      MINVALUE 1001
      MAXVALUE 9999
      START WITH 1001
      NOCACHE
      NOCYCLE;

-- Sequence: invoice_id
CREATE SEQUENCE invoice_id
      INCREMENT BY 1
      MINVALUE 10000001
      MAXVALUE 99999999
      START WITH 10000001
      NOCACHE
      NOCYCLE;

-- Sequence: package_id
CREATE SEQUENCE package_id
      INCREMENT BY 1
      MINVALUE 1001
      MAXVALUE 9999
      START WITH 1001
      NOCACHE
      NOCYCLE;

-- Sequence: payment_id
CREATE SEQUENCE payment_id
      INCREMENT BY 1
      MINVALUE 10000001
      MAXVALUE 99999999
      START WITH 10000001
      NOCACHE
      NOCYCLE;

-- Sequence: purchases_id
CREATE SEQUENCE purchases_id
      INCREMENT BY 1
      MINVALUE 10000001
      MAXVALUE 99999999
      START WITH 10000001
      NOCACHE
      NOCYCLE;

-- Sequence: request_id
CREATE SEQUENCE request_id
      INCREMENT BY 1
      MINVALUE 1001
      MAXVALUE 9999
      START WITH 1001
      NOCACHE
      NOCYCLE;

-- Sequence: service_id
CREATE SEQUENCE service_id
      INCREMENT BY 1
      MINVALUE 1001
      MAXVALUE 9999
      START WITH 1001
      NOCACHE
      NOCYCLE;

-- Sequence: visit_id
CREATE SEQUENCE visit_id
      INCREMENT BY 1
      MINVALUE 10000001
      MAXVALUE 99999999
      START WITH 10000001
      NOCACHE
      NOCYCLE;

-- End of file.

