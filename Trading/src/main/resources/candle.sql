CREATE TABLE IF NOT EXISTS public.candle
(
    identifier BIGSERIAL,
    close numeric(19,2),
    epoch bigint,
    high numeric(19,2),
    low numeric(19,2),
    open numeric(19,2),
    granularity int,
    symbol character varying(255) ,
    direction character varying(10) ,
    open_close_diff numeric(19,2),
    open_time bigint,
    CONSTRAINT candle_pkey PRIMARY KEY (identifier)
)
