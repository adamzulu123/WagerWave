CREATE TABLE Users
(
    id              INT AUTO_INCREMENT      NOT NULL,
    first_name      VARCHAR(255)            NOT NULL,
    last_name       VARCHAR(255)            NOT NULL,
    password        VARCHAR(255)            NOT NULL,
    email           VARCHAR(255)            NOT NULL UNIQUE ,
    created_at      TIMESTAMP DEFAULT NOW() NULL,
    birthdate       DATE NOT NULL,           -- Dodanie kolumny birthdate
    verified_status BIT DEFAULT 0 NULL,   -- Zmiana z TINYINT na BIT
    CONSTRAINT `PRIMARY` PRIMARY KEY (id)
);
