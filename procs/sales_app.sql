
CREATE OR REPLACE TYPE number_array IS VARRAY(1000) OF NUMBER;
/
CREATE OR REPLACE PACKAGE sales AS 
    FUNCTION get_possible_visits(day IN DATE) RETURN number_array;
    PROCEDURE register_visit(day IN DATE, hour IN NUMBER, username IN VARCHAR2);
    FUNCTION search_customer(in_username IN VARCHAR2) RETURN CHAR;
    PROCEDURE modify_user_package(in_username IN VARCHAR2, in_package_id CHAR);
    PROCEDURE record_sales_call(in_username IN VARCHAR2, emp_username IN CHAR);
END sales;
/
CREATE OR REPLACE PACKAGE BODY sales AS
    PROCEDURE verify_hour_availability(day IN DATE, hour IN NUMBER, emp_num OUT CHAR) 
    AS 
        TYPE id_array IS VARRAY(1000) OF CHAR(4);
        technician_ids id_array;
        
        TYPE date_array IS VARRAY(1000) OF DATE;
        dates date_array;
        
        i NUMBER(4);
        visits_on_currrent_hour NUMBER(4);
        
        beginning_of_day DATE;
        end_of_day DATE;
    BEGIN
        IF (hour >= 8 AND hour <= 18) THEN
            beginning_of_day := TO_DATE(TO_CHAR(day, 'dd/MON/yyyy') || ' ' || '00:00:00', 'dd/MON/yyyy hh24:mi:ss');
            end_of_day := TO_DATE(TO_CHAR(day, 'dd/MON/yyyy') || ' ' || '23:59:59', 'dd/MON/yyyy hh24:mi:ss');
            
            SELECT "number" BULK COLLECT INTO technician_ids FROM employees
                JOIN department USING (department_id)
            WHERE department.name = 'technicians';
        
            i := 0;
            FOR i IN 1 .. technician_ids.COUNT LOOP
                
                SELECT COUNT(*) INTO visits_on_currrent_hour FROM visits
                    WHERE (technician_id = technician_ids(i))
                    AND (hour = TO_NUMBER(TO_CHAR("start", 'hh24')))
                    AND ("start" BETWEEN beginning_of_day AND end_of_day);
                    
                IF (visits_on_currrent_hour = 0) THEN 
                    emp_num := technician_ids(i);
                    EXIT;
                END IF;
            END LOOP;
        END IF;
        
    END;
    
    FUNCTION get_possible_visits(day IN DATE) RETURN number_array
    AS
        no_available_hours EXCEPTION;
        PRAGMA EXCEPTION_INIT(no_available_hours, -20002);
    
        hour_array number_array := number_array();
        
        hour NUMBER(4);
        i NUMBER(4);
        
        emp_num CHAR(4);
    BEGIN
        i := 1;
        FOR hour IN 8 .. 18 LOOP
            emp_num := NULL;
            verify_hour_availability(day, hour, emp_num);
            
            if (emp_num IS NOT NULL) THEN
                hour_array.EXTEND;
                hour_array(i) := hour;
                i := i + 1;
            END IF;
        END LOOP;
        
        IF (hour_array.COUNT = 0) THEN
            RAISE no_available_hours;
        END IF;
        
        RETURN hour_array;
    END;
    
    PROCEDURE register_visit(day IN DATE, hour IN NUMBER, username IN VARCHAR2)
    AS
        hour_not_available EXCEPTION;
        PRAGMA EXCEPTION_INIT(hour_not_available, -20001);
        emp_num CHAR(4);
        
        start_time DATE;
        end_time DATE;
    BEGIN
        verify_hour_availability(day, hour, emp_num);
        
        IF (emp_num IS NULL) THEN
            RAISE hour_not_available;
        END IF;

        -- Set start to "day" with the "hour" passed from the parameters
        start_time := TO_DATE(TO_CHAR(day, 'dd/MON/yyyy') || ' ' || TO_CHAR(hour) || ':00:00', 'dd/MON/yyyy hh24:mi:ss');
        
        end_time := start_time  + 1/24;
        
        INSERT INTO visits VALUES (visit_id.NEXTVAL, username, emp_num, start_time, end_time, NULL);
    END;
    
    FUNCTION search_customer(in_username IN VARCHAR2) RETURN CHAR
    AS
        customer_count NUMBER(1);
    BEGIN
        SELECT COUNT(*) INTO customer_count FROM customers
            WHERE username = in_username;
            
        IF (customer_count = 0) THEN
            RETURN '0';
        ELSE
            RETURN '1';
        END IF;
    END;
    
    PROCEDURE modify_user_package(in_username IN VARCHAR2, in_package_id CHAR) 
    AS
    BEGIN
        UPDATE customers SET package_id = in_package_id 
            WHERE username = in_username;
    END;
    
    PROCEDURE record_sales_call(in_username IN VARCHAR2, emp_username IN CHAR)
    AS
        emp_number CHAR(4);
    BEGIN
        SELECT "number" INTO emp_number FROM sales_login
            WHERE username = emp_username;
            
        IF (in_username IS NULL) THEN
            INSERT INTO calls_sales VALUES (CALLS_SALES_ID.NEXTVAL, in_username, emp_number, SYSDATE, '0');
        ELSE
            INSERT INTO calls_sales VALUES (CALLS_SALES_ID.NEXTVAL, in_username, emp_number, SYSDATE, '1');
        END IF;
    END;
END sales;
