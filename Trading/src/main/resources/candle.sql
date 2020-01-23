CREATE TABLE IF NOT EXISTS public.candle
(
    identifier BIGSERIAL,
    close numeric(19,4),
    epoch bigint,
    high numeric(19,4),
    low numeric(19,4),
    open numeric(19,4),
    granularity int,
    symbol character varying(255) ,
    direction character varying(10) ,
    open_close_diff numeric(19,4),
    epoch_string timestamp,
    CONSTRAINT candle_pkey PRIMARY KEY (identifier)
)
