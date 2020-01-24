CREATE TABLE IF NOT EXISTS public.strategy
(
    identifier SERIAL,
    strategy_name character varying(255) ,
    creation_date timestamp NOT NULL,
    max_steps INTEGER NOT NULL,
    next_strategy_id_link INTEGER,
    is_default_strategy boolean,
    CONSTRAINT strategy_pkey PRIMARY KEY (identifier)
)

--Helpful Query
--select * From strategy_steps ss join strategy s on ( ss.strategy_id = s.identifier)

