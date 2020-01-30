CREATE TABLE IF NOT EXISTS public.trade
(
    identifier BIGSERIAL,
    trade_time_string timestamp,
    result character varying(30) ,
    bid_amount numeric(19,4) NOT NULL,
    amount_won numeric(19,4) NOT NULL,
    call_or_put character varying(30) ,
    symbol character varying(255) ,
    step_count integer NOT NULL,
    strategy_id int NOT NULL,
    contract_id bigint,
    trade_time bigint,
    CONSTRAINT trade_pkey PRIMARY KEY (identifier)
);

