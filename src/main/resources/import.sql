INSERT INTO roles(name, version) VALUES ('ROLE_USER', 0);
INSERT INTO roles(name, version) VALUES ('ROLE_ADMIN', 0);
INSERT INTO roles(name, version) VALUES ('MANAGER', 0);

INSERT INTO users(blocked, email, login, password, phone, version) VALUES (false, 'admin@gmail.com', 'root', '$2a$10$FWbR.MFYu5cjeQ6arxWqsu6rIofiJoHEshLJ2MCj/nGNfa3/fcrhy', '987654321 ', 1);
INSERT INTO users_roles(user_id, role_id) VALUES ((SELECT user_id FROM users WHERE users.login='root' LIMIT 1), (SELECT role_id FROM roles WHERE roles.name='ROLE_ADMIN' LIMIT 1));

INSERT INTO categories(name, version) VALUES ('rolls', 0);
INSERT INTO categories(name, version) VALUES ('breads', 0);