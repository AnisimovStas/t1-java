-- liquibase formatted sql

-- changeset anisimov_sa:2-1
CREATE SEQUENCE IF NOT EXISTS account_seq START WITH 1 INCREMENT BY 50;

-- changeset anisimov_sa:2-2
CREATE TABLE IF NOT EXISTS  account
(
    id BIGINT NOT NULL,
    client_id BIGINT,
    account_type VARCHAR(255) CHECK(account_type IN ('CREDIT', 'DEBIT')),
    balance DECIMAL(19, 2),
    CONSTRAINT pk_account PRIMARY KEY (id),
    CONSTRAINT fk_account_client FOREIGN KEY (client_id) REFERENCES client(id)
);