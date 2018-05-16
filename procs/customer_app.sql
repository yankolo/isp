CREATE OR REPLACE PACKAGE CUSTOMER_PACKAGE AS
    FUNCTION checkIfCustomer(cname IN VARCHAR2, pass IN RAW) RETURN CHAR;
    FUNCTION getSalt(name IN VARCHAR2) RETURN VARCHAR2;
    PROCEDURE upgradeing(cname IN VARCHAR2, int_package IN CHAR);
    PROCEDURE changePassword(identity IN VARCHAR2, saltCode IN VARCHAR2, hashCode IN RAW);
    PROCEDURE addRequest(videntity IN VARCHAR2, v_service_id IN CHAR, vdescribe IN VARCHAR2);
END CUSTOMER_PACKAGE;
/
create or replace PACKAGE BODY CUSTOMER_PACKAGE AS
    FUNCTION getSalt(name IN VARCHAR2) RETURN VARCHAR2
    AS
        saltCode VARCHAR2(32);
    BEGIN
        SELECT salt INTO saltCode FROM Customer_Login WHERE username = name;
        RETURN saltCode;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            RETURN NULL;
    END;

    PROCEDURE upgradeing(cname IN VARCHAR2, int_package IN CHAR)
    AS
    BEGIN
        UPDATE Customers SET package_id = int_package WHERE username = cname;
    END;

    FUNCTION checkIfCustomer(cname IN VARCHAR2, pass IN RAW) RETURN CHAR
    AS
        hashCode RAW(256);
        userCode VARCHAR(30);
    BEGIN
        SELECT username INTO userCode FROM Customer_Login WHERE username = cname
        AND pass = hash;

        RETURN userCode;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            RETURN NULL;
    END;

    PROCEDURE changePassword(identity IN VARCHAR2, saltCode IN VARCHAR2, hashCode IN RAW)
    AS
    BEGIN
        UPDATE Customer_Login SET salt = saltCode, hash = hashCode
        WHERE username = identity;
        COMMIT;
    END;

    PROCEDURE addRequest(videntity IN VARCHAR2, v_service_id IN CHAR, vdescribe IN VARCHAR2)
    AS
    BEGIN
        INSERT INTO SERVICE_REQUEST(request_id, username, service_id, done, description) VALUES (request_id.NEXTVAL, videntity, v_service_id, 0, vdescribe);
    END;
END CUSTOMER_PACKAGE;