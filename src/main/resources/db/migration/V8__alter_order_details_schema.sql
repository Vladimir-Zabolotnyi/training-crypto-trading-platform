ALTER TABLE order_details
    DROP COLUMN order_type,
    DROP COLUMN cryptocurrency_price,
    DROP COLUMN cryptocurrency_amount,
    ADD sell_currency_id INT NOT NULL,
    ADD buy_currency_id INT NOT NULL,
    ADD sell_currency_amount DECIMAL(11,2) NOT NULL,
    ADD buy_currency_amount DECIMAL(11,2) NOT NULL,
    ADD FOREIGN KEY (sell_currency_id) REFERENCES currency(id),
    ADD FOREIGN KEY (buy_currency_id) REFERENCES currency(id);