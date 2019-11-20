CREATE DATABASE registry_usa
    WITH OWNER = postgres
    ENCODING = 'UTF8'
    TABLESPACE = pg_default
-- LC_COLLATE = 'English_United States.UTF8'
-- LC_CTYPE = 'English_United States.UTF8'
    TEMPLATE = template0
    LC_COLLATE = 'English_United States.1252'
    LC_CTYPE = 'English_United States.1252'
    CONNECTION LIMIT = -1;
GRANT CONNECT, TEMPORARY ON DATABASE textsaver TO public;
GRANT ALL ON DATABASE textsaver TO postgres WITH GRANT OPTION;

-- -----------------------------------------------------------------------------------------------------------------
-- DROP DATABASE registry_USA;
-- -----------------------------------------------------------------------------------------------------------------
