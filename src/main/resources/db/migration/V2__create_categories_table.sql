CREATE TABLE categories (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    name VARCHAR(100) NOT NULL,
    type VARCHAR(20) NOT NULL,
    archived BOOLEAN NOT NULL DEFAULT false,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    CONSTRAINT fk_categories_user
            FOREIGN KEY (user_id)
            REFERENCES users (id),
    CONSTRAINT ck_categories_type
            CHECK (type IN ('INCOME', 'EXPENSE', 'SAVING'))
);

CREATE INDEX idx_categories_user_id ON categories (user_id);

CREATE UNIQUE INDEX uk_categories_active
    ON categories (user_id, LOWER(name), type) WHERE archived = false;
