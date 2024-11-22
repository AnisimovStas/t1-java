-- liquibase formatted sql

-- changeset anisimov_sa:1-1
INSERT INTO client (id, first_name, last_name, middle_name)
VALUES
    (1, 'Иван', 'Иванов', 'Иванович'),
    (2, 'Петр', 'Петров', 'Петрович'),
    (3, 'Сергей', 'Сергеев', 'Сергеевич'),
    (4, 'Александр', 'Александров', 'Александрович'),
    (5, 'Михаил', 'Михайлов', 'Михайлович'),
    (6, 'Николай', 'Николаев', 'Николаевич'),
    (7, 'Владимир', 'Владимиров', 'Владимирович'),
    (8, 'Дмитрий', 'Дмитриев', 'Дмитриевич'),
    (9, 'Андрей', 'Андреев', 'Андреевич'),
    (10, 'Алексей', 'Алексеев', 'Алексеевич');