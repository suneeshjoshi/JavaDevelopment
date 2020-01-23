CREATE TABLE IF NOT EXISTS public.tick
(
    identifier BIGSERIAL ,
    ask numeric(19,4) ,
    bid numeric(19,4) ,
    epoch bigint ,
    quote numeric(19,4) ,
    symbol character varying(255) ,
    epoch_string timestamp,
    CONSTRAINT tick_pkey PRIMARY KEY (identifier)
)


