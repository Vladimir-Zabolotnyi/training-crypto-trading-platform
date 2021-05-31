CREATE TABLE wallet (
    id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    user_id INT NOT NULL UNIQUE,
    money_balance DECIMAL(11,2) NOT NULL DEFAULT 0.0,
    cryptocurrency_balance DECIMAL(11,2) NOT NULL DEFAULT 0.0,
    FOREIGN KEY (user_id) REFERENCES user(id)
);