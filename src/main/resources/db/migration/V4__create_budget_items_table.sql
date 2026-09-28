CREATE TABLE budget_items (
                              id          BIGSERIAL     PRIMARY KEY,
                              user_id     BIGINT        NOT NULL,
                              category_id BIGINT        NOT NULL,
                              mode        VARCHAR(20)   NOT NULL,
                              limit_value NUMERIC(19,2) NOT NULL,
                              valid_from  DATE          NOT NULL,
                              valid_to    DATE,
                              created_at  TIMESTAMPTZ   NOT NULL DEFAULT now(),
                              CONSTRAINT fk_budget_items_user
                                  FOREIGN KEY (user_id) REFERENCES users (id),
                              CONSTRAINT fk_budget_items_category
                                  FOREIGN KEY (category_id) REFERENCES categories (id),
                              CONSTRAINT ck_budget_items_mode
                                  CHECK (mode IN ('AMOUNT', 'PERCENTAGE')),
                              CONSTRAINT ck_budget_items_limit_value_positive
                                  CHECK (limit_value > 0),
                              CONSTRAINT ck_budget_items_percentage_max
                                  CHECK (mode <> 'PERCENTAGE' OR limit_value <= 100),
                              CONSTRAINT ck_budget_items_valid_from_first_day
                                  CHECK (EXTRACT(DAY FROM valid_from) = 1),
                              CONSTRAINT ck_budget_items_valid_to_first_day
                                  CHECK (EXTRACT(DAY FROM valid_to) = 1),
                              CONSTRAINT ck_budget_items_valid_period
                                  CHECK (valid_to >= valid_from)
);

CREATE INDEX idx_budget_items_user_id
    ON budget_items (user_id);

CREATE INDEX idx_budget_items_category_id
    ON budget_items (category_id);

CREATE UNIQUE INDEX uk_budget_items_active
    ON budget_items (user_id, category_id) WHERE valid_to IS NULL;