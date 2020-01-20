CREATE TABLE IF NOT EXISTS public.candle
(
    identifier bigint NOT NULL,
    close numeric(19,2),
    epoch bigint,
    high numeric(19,2),
    low numeric(19,2),
    open numeric(19,2),
    CONSTRAINT candle_pkey PRIMARY KEY (identifier)
)
