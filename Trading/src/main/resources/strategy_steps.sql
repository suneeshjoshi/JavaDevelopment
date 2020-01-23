CREATE TABLE IF NOT EXISTS public.strategy_steps
(
    identifier SERIAL,
    strategy_id INTEGER ,
    step_count integer NOT NULL,
    value numeric(19,2) NOT NULL,
    CONSTRAINT strategy_pkey PRIMARY KEY (identifier)
)