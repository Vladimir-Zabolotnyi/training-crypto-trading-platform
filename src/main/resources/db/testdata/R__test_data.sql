SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE order_details;
TRUNCATE wallet;
TRUNCATE role;
TRUNCATE user;
TRUNCATE audit_trail;
TRUNCATE currency;
SET FOREIGN_KEY_CHECKS = 1;


INSERT INTO role(id,name)
VALUES (1, 'USER');
INSERT INTO role(id,name)
VALUES (2,'ADMIN');
INSERT INTO role(id,name)
VALUES (3,'SYSTEM');

INSERT INTO user(id, name, login, password,role_id)
VALUES (1, 'Jack', 'Jacklog', '$2a$10$cc9PZ/P.BJre5fGkZ1K/ROhc7HNskwrNB2yymrXTmombBlui5Q012',1);
INSERT INTO user(id, name, login, password,role_id)
VALUES (2, 'Vova', 'Vovalog', '$2a$10$tPmrzgBhgpm6KZq3EvOn5ud/oXc4x0y1h9jxIDhwc.UglMuEJf8ue',1);
INSERT INTO user(id, name, login, password,role_id)
VALUES (3, 'Vlad', 'Vladlog', '$2a$10$BBP0NZDJG45hqP6z2xsHVu1TMRNTDvNFbpBUdIP.qN2N0XGsxWFFC',2);
INSERT INTO user(id, name, login, password,role_id)
VALUES (666, '.root', '', '', 3);

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


INSERT INTO wallet (id, user_id,currency_id,amount)
VALUES (1, 1, 1, 100);
INSERT INTO wallet (id, user_id,currency_id,amount)
VALUES (2,1,2,50);
INSERT INTO wallet (id, user_id,currency_id,amount)
VALUES (3,1,3,200);
INSERT INTO wallet (id, user_id,currency_id,amount)
VALUES (4,2,1,300);
INSERT INTO wallet (id, user_id,currency_id,amount)
VALUES (5,2,3,15);

INSERT INTO order_details(id, creation_date, user_id, order_status,
                          sell_currency_id, buy_currency_id, sell_currency_amount, buy_currency_amount)
VALUES (1, '2021-06-04 13:28:39',1,'CREATED',1,2,5,10);
INSERT INTO order_details(id, creation_date, user_id, order_status,
                          sell_currency_id, buy_currency_id, sell_currency_amount, buy_currency_amount)
VALUES (2, '2021-06-04 13:38:39',1,'CREATED',1,3,1100,4);
INSERT INTO order_details(id, creation_date, user_id, order_status,
                          sell_currency_id, buy_currency_id, sell_currency_amount, buy_currency_amount)
VALUES (3, '2021-06-04 13:38:39',1,'CANCELLED',3,6,20,400);
INSERT INTO order_details(id, creation_date, user_id, order_status,
                          sell_currency_id, buy_currency_id, sell_currency_amount, buy_currency_amount)
VALUES (4, '2021-06-04 13:38:39',1,'FULFILLED',1,4,34,400);
INSERT INTO order_details(id, creation_date, user_id, order_status,
                          sell_currency_id, buy_currency_id, sell_currency_amount, buy_currency_amount)
VALUES (5, '2021-06-04 13:28:39',2,'CREATED',1,3,2000,0.2);
INSERT INTO order_details(id, creation_date, user_id, order_status,
                          sell_currency_id, buy_currency_id, sell_currency_amount, buy_currency_amount)
VALUES (6, '2021-06-04 13:28:39',2,'CREATED',1,4,40,17);
INSERT INTO order_details(id, creation_date, user_id, order_status,
                          sell_currency_id, buy_currency_id, sell_currency_amount, buy_currency_amount)
VALUES (7, '2021-06-04 13:38:39',1,'CREATED',1,5,20,400);



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

