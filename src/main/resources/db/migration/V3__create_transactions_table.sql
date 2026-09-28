CREATE TABLE transactions (
    id               BIGSERIAL     PRIMARY KEY,
    user_id          BIGINT        NOT NULL,
    category_id      BIGINT        NOT NULL,
    amount           NUMERIC(19,2) NOT NULL,
    transaction_date DATE          NOT NULL,
    description      VARCHAR(255),
    created_at       TIMESTAMPTZ   NOT NULL DEFAULT now(),
    updated_at       TIMESTAMPTZ   NOT NULL DEFAULT now(),
    CONSTRAINT fk_transactions_user
        FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_transactions_category
        FOREIGN KEY (category_id) REFERENCES categories (id),
    CONSTRAINT ck_transactions_amount_positive
        CHECK (amount > 0)
);

CREATE INDEX idx_transactions_user_date
    ON transactions (user_id, transaction_date);

CREATE INDEX idx_transactions_category_id
    ON transactions (category_id);