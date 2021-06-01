CREATE TABLE order_details (
    id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    creation_date DATETIME NOT NULL,
    user_id INT NOT NULL,
    status ENUM('created','canceled','fulfilled') NOT NULL,
    order_type ENUM('buy','sell') NOT NULL,
    cryptocurrency_price DECIMAL(11,2) NOT NULL,
    cryptocurrency_amount DECIMAL(11,2) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user(id)
);