-- PrediX Market Schema - Initial migration
-- PostgreSQL

CREATE TABLE markets (
    id                      UUID PRIMARY KEY,
    market_code             VARCHAR(64) NOT NULL,
    title                   VARCHAR(512) NOT NULL,
    description             TEXT,
    category                VARCHAR(64) NOT NULL,
    market_type             VARCHAR(32) NOT NULL DEFAULT 'BINARY',
    chain_id                INTEGER NOT NULL,
    collateral_token_symbol VARCHAR(32) NOT NULL,
    ctf_condition_id        VARCHAR(128),
    uma_question_id         VARCHAR(128),
    status                  VARCHAR(32) NOT NULL,
    open_time               TIMESTAMPTZ NOT NULL,
    close_time              TIMESTAMPTZ NOT NULL,
    resolve_deadline        TIMESTAMPTZ NOT NULL,
    created_by              VARCHAR(128) NOT NULL,
    created_at              TIMESTAMPTZ NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC'),
    updated_at              TIMESTAMPTZ NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC'),
    CONSTRAINT uk_markets_market_code UNIQUE (market_code),
    CONSTRAINT chk_markets_status CHECK (status IN (
        'DRAFT', 'OPEN', 'CLOSED', 'RESOLVING', 'RESOLVED', 'SETTLED', 'CANCELLED'
    )),
    CONSTRAINT chk_markets_type CHECK (market_type IN ('BINARY', 'CATEGORICAL')),
    CONSTRAINT chk_markets_time_window CHECK (open_time < close_time AND close_time < resolve_deadline)
);

CREATE INDEX idx_markets_status_category_open ON markets (status, category, open_time);
CREATE INDEX idx_markets_chain_id ON markets (chain_id);

CREATE TABLE market_outcomes (
    id                  UUID PRIMARY KEY,
    market_id           UUID NOT NULL REFERENCES markets (id) ON DELETE CASCADE,
    outcome_code        VARCHAR(32) NOT NULL,
    outcome_index       INTEGER NOT NULL,
    outcome_label       VARCHAR(256) NOT NULL,
    is_winning          BOOLEAN,
    payout_numerator    NUMERIC(38, 18),
    payout_denominator  NUMERIC(38, 18),
    created_at          TIMESTAMPTZ NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC'),
    CONSTRAINT uk_outcomes_market_code UNIQUE (market_id, outcome_code),
    CONSTRAINT uk_outcomes_market_index UNIQUE (market_id, outcome_index),
    CONSTRAINT chk_outcomes_index_nonneg CHECK (outcome_index >= 0)
);

CREATE INDEX idx_outcomes_market_id ON market_outcomes (market_id);

CREATE TABLE orders (
    id               UUID PRIMARY KEY,
    order_code       VARCHAR(64) NOT NULL,
    market_id        UUID NOT NULL REFERENCES markets (id),
    outcome_id       UUID NOT NULL REFERENCES market_outcomes (id),
    side             VARCHAR(8) NOT NULL,
    order_type       VARCHAR(16) NOT NULL,
    price            NUMERIC(38, 18) NOT NULL,
    quantity         NUMERIC(38, 18) NOT NULL,
    filled_quantity  NUMERIC(38, 18) NOT NULL DEFAULT 0,
    user_id          VARCHAR(128) NOT NULL,
    status           VARCHAR(32) NOT NULL,
    created_at       TIMESTAMPTZ NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC'),
    updated_at       TIMESTAMPTZ NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC'),
    CONSTRAINT uk_orders_order_code UNIQUE (order_code),
    CONSTRAINT chk_orders_side CHECK (side IN ('BUY', 'SELL')),
    CONSTRAINT chk_orders_type CHECK (order_type IN ('LIMIT', 'MARKET')),
    CONSTRAINT chk_orders_status CHECK (status IN (
        'NEW', 'PARTIALLY_FILLED', 'FILLED', 'CANCELLED', 'REJECTED'
    )),
    CONSTRAINT chk_orders_qty_nonneg CHECK (quantity >= 0 AND filled_quantity >= 0)
);

CREATE INDEX idx_orders_market_user_status_created ON orders (market_id, user_id, status, created_at);
CREATE INDEX idx_orders_user_id ON orders (user_id);

CREATE TABLE positions (
    id               UUID PRIMARY KEY,
    market_id        UUID NOT NULL REFERENCES markets (id),
    outcome_id       UUID NOT NULL REFERENCES market_outcomes (id),
    user_id          VARCHAR(128) NOT NULL,
    quantity         NUMERIC(38, 18) NOT NULL DEFAULT 0,
    avg_cost         NUMERIC(38, 18) NOT NULL DEFAULT 0,
    unrealized_pnl   NUMERIC(38, 18),
    updated_at       TIMESTAMPTZ NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC'),
    CONSTRAINT uk_positions_market_outcome_user UNIQUE (market_id, outcome_id, user_id)
);

CREATE INDEX idx_positions_user_market ON positions (user_id, market_id);

CREATE TABLE resolution_records (
    id                      UUID PRIMARY KEY,
    market_id               UUID NOT NULL REFERENCES markets (id),
    resolution_source       VARCHAR(32) NOT NULL,
    uma_request_tx_hash     VARCHAR(128),
    uma_assertion_id        VARCHAR(128),
    proposed_outcome_code   VARCHAR(32),
    disputed                BOOLEAN NOT NULL DEFAULT FALSE,
    final_outcome_code      VARCHAR(32),
    resolved_at             TIMESTAMPTZ,
    resolver_ref            VARCHAR(128),
    raw_payload             JSONB,
    created_at              TIMESTAMPTZ NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC'),
    updated_at              TIMESTAMPTZ NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC'),
    CONSTRAINT chk_resolution_source CHECK (resolution_source IN ('UMA', 'MANUAL', 'ORACLE'))
);

CREATE INDEX idx_resolution_market_created ON resolution_records (market_id, created_at);

CREATE TABLE settlements (
    id                  UUID PRIMARY KEY,
    market_id           UUID NOT NULL REFERENCES markets (id),
    outcome_id          UUID NOT NULL REFERENCES market_outcomes (id),
    user_id             VARCHAR(128) NOT NULL,
    redeem_quantity     NUMERIC(38, 18) NOT NULL,
    payout_amount       NUMERIC(38, 18) NOT NULL,
    payout_token        VARCHAR(32) NOT NULL,
    settlement_tx_hash  VARCHAR(128),
    settled_at          TIMESTAMPTZ NOT NULL,
    created_at          TIMESTAMPTZ NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC')
);

CREATE INDEX idx_settlements_market_user ON settlements (market_id, user_id);

CREATE TABLE market_audit_logs (
    id              UUID PRIMARY KEY,
    market_id       UUID NOT NULL REFERENCES markets (id),
    from_status     VARCHAR(32),
    to_status       VARCHAR(32) NOT NULL,
    action          VARCHAR(64) NOT NULL,
    actor           VARCHAR(128) NOT NULL,
    detail          TEXT,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC')
);

CREATE INDEX idx_audit_market_created ON market_audit_logs (market_id, created_at);

-- Sequence for market code daily counter
CREATE SEQUENCE IF NOT EXISTS market_code_seq START 1;
