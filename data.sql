INSERT INTO roles (id, name) VALUES (1, 'ROLE_ADMIN');
INSERT INTO roles (id, name) VALUES (2, 'ROLE_MANAGER');
INSERT INTO roles (id, name) VALUES (3, 'ROLE_USER');

INSERT INTO users (id, first_name, last_name, email, password, role_id) VALUES (1, 'admin@coupit.com', 'Admin', 'Coupit', '$2a$10$RyY4bXtV3LKkDCutlUTYDOKd2AiJYZGp4Y7MPVdLzWzT1RX.JRZyG', 1);