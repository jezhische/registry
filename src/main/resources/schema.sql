CREATE DATABASE registry_usa
    WITH OWNER = postgres
    ENCODING = 'UTF8'
    TABLESPACE = pg_default
--     to perform US collation
    TEMPLATE = template0
    LC_COLLATE = 'English_United States.1252'
    LC_CTYPE = 'English_United States.1252'
    CONNECTION LIMIT = -1;
GRANT CONNECT, TEMPORARY ON DATABASE textsaver TO public;
GRANT ALL ON DATABASE textsaver TO postgres WITH GRANT OPTION;
-- -----------------------------------------------------------------------------------------------------------------
-- DROP DATABASE registry_usa;
-- -----------------------------------------------------------------------------------------------------------------

-- CREATE DATABASE registry_canada
--     WITH OWNER = postgres
--     ENCODING = 'UTF8'
--     TABLESPACE = pg_default
--     TEMPLATE = template0
--     LC_COLLATE = 'English_United States.1252'
--     LC_CTYPE = 'English_United States.1252'
--     CONNECTION LIMIT = -1;
-- GRANT CONNECT, TEMPORARY ON DATABASE textsaver TO public;
-- GRANT ALL ON DATABASE textsaver TO postgres WITH GRANT OPTION;
-- -----------------------------------------------------------------------------------------------------------------
-- DROP DATABASE registry_canada;
-- -----------------------------------------------------------------------------------------------------------------

