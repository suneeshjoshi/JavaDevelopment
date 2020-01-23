CREATE TABLE IF NOT EXISTS public.transaction
(
    identifier BIGSERIAL,
    action character varying(255) ,
    amount numeric(19,4),
    balance numeric(19,4),
    barrier character varying(255) ,
    contract_id bigint,
    currency character varying(255) ,
    date_expiry bigint,
    purchase_time bigint,
    transaction_time bigint,
    display_name character varying(255) ,
    long_code character varying(255) ,
    symbol character varying(255) ,
    transaction_id bigint,
    date_expiry_string timestamp,
    purchase_time_string timestamp,
    transaction_time_string timestamp,
    CONSTRAINT transaction_pkey PRIMARY KEY (identifier)
)

