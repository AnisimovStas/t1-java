-- liquibase formatted sql

-- changeset anisimov_sa:5-1
CREATE SEQUENCE IF NOT EXISTS data_source_error_log_seq START WITH 1 INCREMENT BY 50;

-- changeset anisimov_sa:5-2
CREATE TABLE IF NOT EXISTS data_source_error_log
(
    id BIGINT NOT NULL,
    message TEXT,
    stacktrace TEXT,
    signature VARCHAR(255),
    CONSTRAINT pk_data_source_error PRIMARY KEY (id)
);
