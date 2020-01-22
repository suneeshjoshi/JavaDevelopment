CREATE TABLE IF NOT EXISTS public.strategy
(
    identifier SERIAL,
    strategy_name character varying(255) ,
    creation_date date NOT NULL,
    CONSTRAINT strategy_pkey PRIMARY KEY (identifier)
)
