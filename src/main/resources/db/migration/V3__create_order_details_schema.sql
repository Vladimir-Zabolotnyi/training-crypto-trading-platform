CREATE TABLE order_details (
    id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    creation_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    user_id INT NOT NULL,
    order_status ENUM('CREATED','CANCELLED','FULFILLED') NOT NULL,
    order_type ENUM('BUY','SELL') NOT NULL,
    cryptocurrency_price DECIMAL(11,2) NOT NULL,
    cryptocurrency_amount DECIMAL(11,2) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user(id)
);