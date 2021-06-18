ALTER TABLE wallet
    DROP COLUMN money_balance,
    DROP COLUMN cryptocurrency_balance,
    DROP INDEX user_id,
    ADD currency_id INT,
    ADD amount DECIMAL(11,2) NOT NULL DEFAULT 0.0,
    ADD FOREIGN KEY (currency_id) REFERENCES role (id);