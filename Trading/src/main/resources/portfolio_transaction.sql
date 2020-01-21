CREATE TABLE IF NOT EXISTS public.portfolio_transaction
(
    identifier  BIGSERIAL,
    app_id integer,
    buy_price numeric(19,2),
    contract_id bigint,
    contract_type character varying(255) ,
    currency character varying(255) ,
    date_start bigint NOT NULL,
    expiry_time bigint NOT NULL,
    long_code character varying(255) ,
    payout numeric(19,2),
    symbol character varying(255) ,
    purchase_time bigint NOT NULL,
    transaction_id bigint,
    CONSTRAINT portfolio_transaction_pkey PRIMARY KEY (identifier)
)

