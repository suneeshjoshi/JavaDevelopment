CREATE TABLE IF NOT EXISTS public.trade
(
    identifier BIGSERIAL,
    bid_amount numeric(19,4) NOT NULL,
    call_or_put character varying(30) ,
    symbol character varying(255) ,
    step_count integer NOT NULL,
    strategy_id int NOT NULL,
    result character varying(30) ,
    contract_id bigint,
    trade_time bigint,
    trade_time_string timestamp,
    amount_won numeric(19,4) NOT NULL,
    CONSTRAINT trade_pkey PRIMARY KEY (identifier)
)
