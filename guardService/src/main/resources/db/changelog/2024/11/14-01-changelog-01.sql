-- liquibase formatted sql

-- changeset anisimov_sa:14-01-1
ALTER TABLE transaction
ADD COLUMN IF NOT EXISTS transaction_id BIGINT,
ADD COLUMN IF NOT EXISTS status VARCHAR(50);
ALTER TABLE transaction
ADD CONSTRAINT uk_transaction_transaction_id UNIQUE (transaction_id);

-- changeset anisimov_sa:14-01-2
ALTER TABLE account
ADD COLUMN IF NOT EXISTS account_id BIGINT,
ADD COLUMN IF NOT EXISTS frozen_amount DECIMAL(19, 2),
ADD COLUMN IF NOT EXISTS status VARCHAR(50);
ALTER TABLE account
ADD CONSTRAINT uk_account_account_id UNIQUE (account_id);

-- changeset anisimov_sa:14-01-3
ALTER TABLE client
ADD COLUMN IF NOT EXISTS client_id BIGINT;
ALTER TABLE client
ADD CONSTRAINT uk_client_client_id UNIQUE (client_id);