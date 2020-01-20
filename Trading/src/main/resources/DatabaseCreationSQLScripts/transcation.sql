-- Table: public.transaction

-- DROP TABLE public.transaction;

CREATE TABLE public.transaction
(
    identifier bigint NOT NULL,
    action character varying(255) ,
    amount numeric(19,2),
    balance numeric(19,2),
    barrier character varying(255) ,
    contract_id bigint,
    currency character varying(255) ,
    date_expiry bigint,
    display_name character varying(255) ,
    high_barrier character varying(255) ,
    id character varying(255) ,
    long_code character varying(255) ,
    low_barrier character varying(255) ,
    purchase_time bigint,
    symbol character varying(255) ,
    transaction_id bigint,
    transaction_time bigint,
    CONSTRAINT transaction_pkey PRIMARY KEY (identifier)
)

