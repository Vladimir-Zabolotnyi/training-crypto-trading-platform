CREATE TABLE user_wallet (
    id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    user_id INT NOT NULL UNIQUE,
    # DECIMAL(11,2). Syntax analysis:
    # 11 is the precision - ten billion minus one max; 2 is the accuracy - 99 coins max.
    money_balance DECIMAL(11,2) NOT NULL DEFAULT 0.0,
    cryptocurrency_balance DECIMAL(11,2) NOT NULL DEFAULT 0.0,
    FOREIGN KEY (user_id) REFERENCES user(id)
);