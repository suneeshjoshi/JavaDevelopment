CREATE TABLE IF NOT EXISTS public.portfolio_transaction
(
    identifier  BIGSERIAL,
    app_id integer,
    buy_price numeric(19,4),
    contract_id bigint,
    contract_type character varying(255) ,
    currency character varying(255) ,
    date_start bigint NOT NULL,
    expiry_time bigint NOT NULL,
    purchase_time bigint NOT NULL,
    long_code character varying(255) ,
    short_code character varying(255) ,
    payout numeric(19,4),
    symbol character varying(255) ,
    transaction_id bigint,
    date_start_string timestamp,
    expiry_time_string timestamp,
    purchase_time_string timestamp,
    CONSTRAINT portfolio_transaction_pkey PRIMARY KEY (identifier)
)

