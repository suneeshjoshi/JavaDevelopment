CREATE TABLE IF NOT EXISTS public.strategy_steps
(
    identifier SERIAL,
    strategy_id INTEGER NOT NULL,
    step_count integer NOT NULL,
    value numeric(19,4) NOT NULL,
    profit_percentage_threshold NUMERIC(10,2) NOT NULL DEFAULT 95.00,
    CONSTRAINT strategy_steps_pkey PRIMARY KEY (identifier)
);

