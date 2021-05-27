CREATE TABLE user_wallet (
    id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    user_id INT NOT NULL,
    money_balance DECIMAL NOT NULL DEFAULT 0.0,
    cryptocurrency_balance DECIMAL NOT NULL DEFAULT 0.0,
    FOREIGN KEY (user_id) REFERENCES user(id)
);