-- liquibase formatted sql

-- changeset anisimov_sa:3-1
INSERT INTO account (id, client_id, account_type, balance)
VALUES
    (1, 1, 'CREDIT', 1000.00),
    (2, 1, 'DEBIT', 500.00),
    (3, 2, 'CREDIT', 2000.00),
    (4, 3, 'DEBIT', 1000.00),
    (5, 2, 'DEBIT', 1500.00),
    (6, 3, 'CREDIT', 3000.00),
    (7, 4, 'DEBIT', 700.00),
    (8, 4, 'CREDIT', 1200.00),
    (9, 5, 'CREDIT', 2500.00),
    (10, 5, 'DEBIT', 900.00);
