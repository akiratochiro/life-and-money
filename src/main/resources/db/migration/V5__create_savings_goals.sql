CREATE TABLE savings_goals (
                               id            BIGSERIAL     PRIMARY KEY,
                               user_id       BIGINT        NOT NULL,
                               name          VARCHAR(100)  NOT NULL,
                               target_amount NUMERIC(19,2) NOT NULL,
                               deadline      DATE          NOT NULL,
                               created_at    TIMESTAMPTZ   NOT NULL DEFAULT now(),
                               CONSTRAINT fk_savings_goals_user
                                   FOREIGN KEY (user_id) REFERENCES users (id),
                               CONSTRAINT ck_savings_goals_target_amount_positive
                                   CHECK (target_amount > 0)
);

CREATE INDEX idx_savings_goals_user_id
    ON savings_goals (user_id);

ALTER TABLE transactions ADD COLUMN goal_id BIGINT;

ALTER TABLE transactions
    ADD CONSTRAINT fk_transactions_goal
        FOREIGN KEY (goal_id) REFERENCES savings_goals (id) ON DELETE SET NULL;

CREATE INDEX idx_transactions_goal_id
    ON transactions (goal_id);