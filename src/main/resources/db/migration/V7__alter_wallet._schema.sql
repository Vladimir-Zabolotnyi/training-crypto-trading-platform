ALTER TABLE wallet
    DROP COLUMN money_balance,
    DROP COLUMN cryptocurrency_balance,
    DROP FOREIGN KEY wallet_ibfk_1,
    DROP INDEX user_id,
    DROP COLUMN user_id,
    ADD user_id INT NOT NULL,
    ADD currency_id INT,
    ADD amount DECIMAL(11,2) NOT NULL DEFAULT 0.0,
    ADD FOREIGN KEY (user_id) REFERENCES user(id),
    ADD FOREIGN KEY (currency_id) REFERENCES currency (id);