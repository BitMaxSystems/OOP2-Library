-- To be executed by a root user in the database.
CREATE SCHEMA oop_library;
CREATE USER app_user INDETIFIED BY 'oop2Libpassword';
GRANT REFERENCES, SELECT, UPDATE, ALTER, CREATE, INSERT, DROP, DELETE ON oop_library.* to app_user;