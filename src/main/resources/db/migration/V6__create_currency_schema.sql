CREATE TABLE currency (
    id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    bank_currency BOOLEAN NOT NULL,
    name VARCHAR(30) NOT NULL ,
    acronym VARCHAR(20) NOT NULL
);
