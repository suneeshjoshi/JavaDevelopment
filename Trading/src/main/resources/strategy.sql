CREATE TABLE IF NOT EXISTS public.strategy
(
    identifier SERIAL,
    strategy_name character varying(255) ,
    max_steps INTEGER NOT NULL,
    next_strategy_id_link INTEGER,
    active_strategy boolean,
    is_default_strategy boolean,
    is_backtesting_strategy boolean,
    reset_step_count_on_success boolean,
    strategy_algorithm bigint ,
    creation_date timestamp NOT NULL,
    CONSTRAINT strategy_pkey PRIMARY KEY (identifier)
)

--Helpful Query
--select * From strategy_steps ss join strategy s on ( ss.strategy_id = s.identifier)

