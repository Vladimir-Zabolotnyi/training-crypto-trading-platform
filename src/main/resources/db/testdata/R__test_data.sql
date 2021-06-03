SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE order_details;
TRUNCATE wallet;
TRUNCATE user;
SET FOREIGN_KEY_CHECKS = 1;

INSERT INTO user(id, name, login, password)
VALUES (1, 'Jack', 'Jacklog', '$2a$10$cc9PZ/P.BJre5fGkZ1K/ROhc7HNskwrNB2yymrXTmombBlui5Q012');
INSERT INTO user(id, name, login, password)
VALUES (2, 'Vova', 'Vovalog', '$2a$10$tPmrzgBhgpm6KZq3EvOn5ud/oXc4x0y1h9jxIDhwc.UglMuEJf8ue');

INSERT INTO wallet VALUES
(1, 1, 228.13, 37),
(2, 2, 0.33, 0.0);