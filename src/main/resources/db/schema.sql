CREATE TABLE IF NOT EXISTS currencies
(
    id        SERIAL PRIMARY KEY,
    code      VARCHAR NOT NULL UNIQUE,
    full_name VARCHAR NOT NULL,
    sign      VARCHAR NOT NULL
);

CREATE UNIQUE INDEX idx_code ON currencies (code);

CREATE TABLE IF NOT EXISTS exchange_rates
(
    id                 SERIAL PRIMARY KEY,
    base_currency_id   INTEGER        NOT NULL,
    target_currency_id INTEGER        NOT NULL,
    rate               DECIMAL(18, 8) NOT NULL,
    FOREIGN KEY (base_currency_id) REFERENCES currencies (id),
    FOREIGN KEY (target_currency_id) REFERENCES currencies (id)
);

CREATE UNIQUE INDEX idx_base_target ON exchange_rates (base_currency_id, target_currency_id);