CREATE TABLE user
(
    id       INT         NOT NULL AUTO_INCREMENT,
    name     VARCHAR(20) NOT NULL,
    login    VARCHAR(20) NOT NULL,
    password VARCHAR(20) NOT NULL,
    PRIMARY KEY (id)
);
