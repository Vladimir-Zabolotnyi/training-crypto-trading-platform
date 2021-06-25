CREATE TABLE audit_trail (
    id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    user_id INT NOT NULL,
    description VARCHAR(200) NOT NULL ,
    FOREIGN KEY (user_id) REFERENCES user(id)
);