create or replace PACKAGE TECHNICIANS_PACKAGE AS
    PROCEDURE update_notes_visits(v_visit_id IN CHAR, v_note IN VARCHAR2);
    PROCEDURE update_items(v_tech_id IN CHAR, v_screws IN NUMBER, v_bolts IN NUMBER, v_fiber_optic IN NUMBER);
    PROCEDURE update_tools(v_tech_id IN CHAR, v_fusion_splicer IN CHAR, v_drill IN CHAR);
    FUNCTION getFunctioningTools(v_tech_id IN CHAR) RETURN VARCHAR2;
END TECHNICIANS_PACKAGE;
/
create or replace PACKAGE BODY TECHNICIANS_PACKAGE AS
    PROCEDURE update_notes_visits(v_visit_id IN CHAR, v_note IN VARCHAR2) 
    AS
    BEGIN
        UPDATE visits SET notes = v_note WHERE visit_id = v_visit_id;
    END update_notes_visits;

    PROCEDURE update_items(v_tech_id IN CHAR, v_screws IN NUMBER, v_bolts IN NUMBER, v_fiber_optic IN NUMBER)
    AS
        vamount NUMBER;
        number_too_large EXCEPTION;
        PRAGMA EXCEPTION_INIT(number_too_large, -20002);
    BEGIN
        SELECT amount INTO vamount FROM equipment_items WHERE emp_id = v_tech_id AND name = 'SCREWS';
        IF (vamount - v_screws) < 0 THEN
            RAISE number_too_large;
        END IF;
        SELECT amount INTO vamount FROM equipment_items WHERE emp_id = v_tech_id AND name = 'BOLTS';
        IF (vamount - v_bolts) < 0 THEN
            RAISE number_too_large;
        END IF;
        SELECT amount INTO vamount FROM equipment_items WHERE emp_id = v_tech_id AND name = 'FIBER_OPTIC (1 ft)';
        IF (vamount - v_fiber_optic) < 0 THEN
            RAISE number_too_large;
        END IF;
        
        UPDATE equipment_items SET amount = amount - v_screws WHERE emp_id = v_tech_id AND name = 'SCREWS';
        UPDATE equipment_items SET amount = amount - v_bolts WHERE emp_id = v_tech_id AND name = 'BOLTS';
        UPDATE equipment_items SET amount = amount - v_fiber_optic WHERE emp_id = v_tech_id AND name = 'FIBER_OPTIC (1 ft)';
    END update_items;

    PROCEDURE update_tools(v_tech_id IN CHAR, v_fusion_splicer IN CHAR, v_drill IN CHAR)
    AS
    BEGIN
        UPDATE equipment_tools SET ISFUNCTIONING = v_fusion_splicer WHERE emp_id = v_tech_id AND name = 'FUSION_SPLICER';
        UPDATE equipment_tools SET ISFUNCTIONING = v_drill WHERE emp_id = v_tech_id AND name = 'DRILL';
    END update_tools;

    FUNCTION getFunctioningTools(v_tech_id IN CHAR) RETURN VARCHAR2
    AS
        TYPE TOOL_ARRAY IS VARRAY(50) OF VARCHAR(30);
        tools TOOL_ARRAY;
        i NUMBER;
        toReturn VARCHAR2(2000) := '';        
    BEGIN
        SELECT name BULK COLLECT INTO tools FROM equipment_tools WHERE emp_id = v_tech_id AND isFunctioning = '1';
        FOR i IN 1..(tools.COUNT) LOOP
            toReturn := toReturn || tools(i) || ',';
        END LOOP;
        return SUBSTR(toReturn,1,LENGTH(toReturn) - 1);
    END;
END TECHNICIANS_PACKAGE;
/
create or replace TRIGGER AFTER_EQUIPMENT_ITEMS_UPDATE
AFTER
UPDATE
ON EQUIPMENT_ITEMS
FOR EACH ROW
BEGIN
    IF (:OLD.NAME = 'SCREWS' AND :NEW.AMOUNT < 50) THEN
        INSERT INTO equipment_orders VALUES (equipment_order_id.NEXTVAL, :OLD.EMP_ID, :OLD.NAME, 200, SYSDATE);
    ELSIF (:OLD.NAME = 'BOLTS' AND :NEW.AMOUNT < 50) THEN
        INSERT INTO equipment_orders VALUES (equipment_order_id.NEXTVAL,:OLD.EMP_ID, :OLD.NAME, 200, SYSDATE);
    ELSIF (:OLD.NAME = 'FIBER_OPTIC (1 ft)' AND :NEW.AMOUNT < 50) THEN
        INSERT INTO equipment_orders VALUES (equipment_order_id.NEXTVAL,:OLD.EMP_ID, :OLD.NAME, 75, SYSDATE);
    END IF;
END;
/
create or replace TRIGGER AFTER_EQUIPMENT_TOOLS_UPDATE
AFTER
UPDATE
ON EQUIPMENT_TOOLS
FOR EACH ROW
BEGIN
    IF (:NEW.isFunctioning = '0') THEN
        INSERT INTO equipment_orders VALUES (equipment_order_id.NEXTVAL,:OLD.EMP_ID, :OLD.NAME, 1, SYSDATE);
    END IF;
END;
