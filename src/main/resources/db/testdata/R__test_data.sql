TRUNCATE user;
TRUNCATE user_wallet;

INSERT INTO user(id, name, login, password)
VALUES (1, 'Jack', 'Jacklog', '$2a$10$cc9PZ/P.BJre5fGkZ1K/ROhc7HNskwrNB2yymrXTmombBlui5Q012');
INSERT INTO user(id, name, login, password)
VALUES (2, 'Vova', 'Vovalog', '$2a$10$tPmrzgBhgpm6KZq3EvOn5ud/oXc4x0y1h9jxIDhwc.UglMuEJf8ue');

INSERT INTO user_wallet VALUES
(DEFAULT, 1, 228.33, 19),
(DEFAULT, 2, 728.33, 91.19);