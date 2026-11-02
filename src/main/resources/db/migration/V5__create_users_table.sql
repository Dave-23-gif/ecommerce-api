CREATE TABLE users (
                       id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,

                       full_name VARCHAR(120) NOT NULL,
                       email VARCHAR(254) NOT NULL,
                       password_hash VARCHAR(255) NOT NULL,

                       role VARCHAR(20) NOT NULL DEFAULT 'CUSTOMER',
                       enabled BOOLEAN NOT NULL DEFAULT TRUE,

                       created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

                       CONSTRAINT chk_users_role
                           CHECK (role IN ('CUSTOMER', 'ADMIN'))
);

CREATE UNIQUE INDEX uk_users_email
    ON users (LOWER(email));

CREATE INDEX idx_users_role
    ON users (role);