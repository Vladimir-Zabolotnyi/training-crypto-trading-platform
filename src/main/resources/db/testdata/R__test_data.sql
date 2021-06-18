SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE order_details;
TRUNCATE wallet;
TRUNCATE role;
TRUNCATE user;
TRUNCATE audit_trail;
SET FOREIGN_KEY_CHECKS = 1;


INSERT INTO role(id,name)
VALUES (1, 'USER');
INSERT INTO role(id,name)
VALUES (2,'ADMIN');

INSERT INTO user(id, name, login, password,role_id)
VALUES (1, 'Jack', 'Jacklog', '$2a$10$cc9PZ/P.BJre5fGkZ1K/ROhc7HNskwrNB2yymrXTmombBlui5Q012',1);
INSERT INTO user(id, name, login, password,role_id)
VALUES (2, 'Vova', 'Vovalog', '$2a$10$tPmrzgBhgpm6KZq3EvOn5ud/oXc4x0y1h9jxIDhwc.UglMuEJf8ue',1);
INSERT INTO user(id, name, login, password,role_id)
VALUES (3, 'Vlad', 'Vladlog', '$2a$10$BBP0NZDJG45hqP6z2xsHVu1TMRNTDvNFbpBUdIP.qN2N0XGsxWFFC',2);

INSERT INTO currency(id,basic,bank_currency,name,acronym)
VALUES (1,true,true,'USA_dollar','USD');
INSERT INTO currency(id,basic,bank_currency,name,acronym)
VALUES (2,false,true,'UA_hryvnia','UAH');
INSERT INTO currency(id,basic,bank_currency,name,acronym)
VALUES (3,false,false,'Bitcoin','BTC');
INSERT INTO currency(id,basic,bank_currency,name,acronym)
VALUES (4,false,false,'Ethereum','ETH');
INSERT INTO currency(id,basic,bank_currency,name,acronym)
VALUES (5,false,false,'Tron','TRX');
INSERT INTO currency(id,basic,bank_currency,name,acronym)
VALUES (6,false,false,'Ripple','XRP');


INSERT INTO wallet VALUES
(1, 1, 1, 100),
(2,1,2,50),
(3,1,3,200),
(4,2,1,300),
(5,2,2,15);

INSERT INTO order_details(id,creation_date,user_id,order_status,order_type,cryptocurrency_price,cryptocurrency_amount)
VALUES (1, '2021-06-04 13:28:39',1,'CREATED','SELL',100,5);
INSERT INTO order_details(id,creation_date,user_id,order_status,order_type,cryptocurrency_price,cryptocurrency_amount)
VALUES (2, '2021-06-04 13:28:39',1,'CREATED','SELL',25,12);
INSERT INTO order_details(id,creation_date,user_id,order_status,order_type,cryptocurrency_price,cryptocurrency_amount)
VALUES (3, '2021-06-04 14:23:39',1,'FULFILLED','SELL',32,5);
INSERT INTO order_details(id,creation_date,user_id,order_status,order_type,cryptocurrency_price,cryptocurrency_amount)
VALUES (4, '2021-06-04 15:28:39',1,'CANCELLED','SELL',56,9);
INSERT INTO order_details(id,creation_date,user_id,order_status,order_type,cryptocurrency_price,cryptocurrency_amount)
VALUES (5, '2021-06-13 22:28:39',2,'CREATED','SELL',66,2);
INSERT INTO order_details(id,creation_date,user_id,order_status,order_type,cryptocurrency_price,cryptocurrency_amount)
VALUES (6, '2021-06-16 09:28:39',2,'CREATED','SELL',29,8);

INSERT INTO order_details(id,creation_date,user_id,order_status,order_type,cryptocurrency_price,cryptocurrency_amount)
VALUES (7, '2021-06-08 15:28:39',1,'CREATED','BUY',110,4);
INSERT INTO order_details(id,creation_date,user_id,order_status,order_type,cryptocurrency_price,cryptocurrency_amount)
VALUES (8, '2021-06-08 15:28:39',1,'CREATED','BUY',145,77);
INSERT INTO order_details(id,creation_date,user_id,order_status,order_type,cryptocurrency_price,cryptocurrency_amount)
VALUES (9, '2021-06-10 02:28:39',1,'FULFILLED','BUY',123,7);
INSERT INTO order_details(id,creation_date,user_id,order_status,order_type,cryptocurrency_price,cryptocurrency_amount)
VALUES (10, '2021-06-10 21:28:39',1,'CANCELLED','BUY',75,10);
INSERT INTO order_details(id,creation_date,user_id,order_status,order_type,cryptocurrency_price,cryptocurrency_amount)
VALUES (11, '2021-06-12 15:28:39',2,'CREATED','BUY',220,24);
INSERT INTO order_details(id,creation_date,user_id,order_status,order_type,cryptocurrency_price,cryptocurrency_amount)
VALUES (12, '2021-06-13 02:28:39',2,'CREATED','BUY',456,12);



INSERT INTO audit_trail(id,date,user_id,description)
VALUES (1, '2021-06-04 13:28:39', '1', 'User created the order (id: 1)');
INSERT INTO audit_trail(id,date,user_id,description)
VALUES (2, '2021-06-04 13:28:39', '1', 'User created the order (id: 2)');
INSERT INTO audit_trail(id,date,user_id,description)
VALUES (3, '2021-06-04 13:28:39', '1', 'User created the order (id: 3)');
INSERT INTO audit_trail(id,date,user_id,description)
VALUES (4, '2021-06-04 13:33:39', '2', 'User created the order (id: 4)');
INSERT INTO audit_trail(id,date,user_id,description)
VALUES (5, '2021-06-04 14:23:39', '1', 'User fulfilled the order (id: 4)');
INSERT INTO audit_trail(id,date,user_id,description)
VALUES (6, '2021-06-04 15:28:39', '1', 'User cancelled the order (id: 3)');

