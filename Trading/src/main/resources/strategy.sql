CREATE TABLE IF NOT EXISTS public.strategy
(
    identifier SERIAL,
    strategy_name character varying(255) ,
    creation_date timestamp NOT NULL,
    max_steps INTEGER NOT NULL,
    CONSTRAINT strategy_pkey PRIMARY KEY (identifier)
)
