-- liquibase formatted sql

-- changeset anisimov_sa:4-1
CREATE SEQUENCE IF NOT EXISTS transaction_seq START WITH 1 INCREMENT BY 50;

-- changeset anisimov_sa:4-2
CREATE TABLE IF NOT EXISTS transaction
(
    id BIGINT NOT NULL,
    account_id BIGINT,
    amount DECIMAL(19, 2),
    timestamp TIMESTAMP,
    CONSTRAINT pk_transaction PRIMARY KEY (id),
    CONSTRAINT fk_transaction_account FOREIGN KEY (account_id) REFERENCES account(id)
);

-- changeset anisimov_sa:4-3
INSERT INTO transaction(id, account_id, amount, timestamp)
VALUES
    (1, 1, 100.00, '2024-10-31 00:00:00.000'),
    (2, 2, 200.00, '2024-10-31 00:00:00.000'),
    (3, 2, -200.00, '2024-10-31 00:00:00.000'),
    (4, 3, 300.00, '2024-10-31 00:00:00.000');