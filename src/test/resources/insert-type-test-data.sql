DELETE FROM USERS;
DELETE FROM TYPES;

INSERT INTO TYPES (ID, NAME) VALUES (101, 'Cat');
INSERT INTO TYPES (ID, NAME) VALUES (102, 'Dog');
INSERT INTO USERS (ID, USERNAME, PASSWORD, ROLE) VALUES (101, 'admin', '$2a$12$LrWCy0SSCYNTovkxy3yau.4lvKKC3jMWC6HKmwF9F99jxLoKXaO/K', 'ADMIN');
INSERT INTO USERS (ID, USERNAME, PASSWORD, ROLE) VALUES (102, 'user', '$2a$12$6gjgTswiLOubRSn8xRYXeuKOEF/g1NPIGubJ90Hou9iC7c4du6Kim', 'USER');