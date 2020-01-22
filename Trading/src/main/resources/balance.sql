CREATE TABLE IF NOT EXISTS public.balance
(
    identifier BIGSERIAL,
    balance numeric(19,2),
    currency character varying(255) ,
    login_id character varying(255) ,
    time bigint,
    CONSTRAINT balance_pkey PRIMARY KEY (identifier)
)
