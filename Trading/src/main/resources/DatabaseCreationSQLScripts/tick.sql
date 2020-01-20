-- Table: public.tick

-- DROP TABLE public.tick;

CREATE TABLE public.tick
(
    identifier bigint NOT NULL,
    ask numeric(19,4) ,
    bid numeric(19,4) ,
    epoch bigint ,
    id character varying(255) ,
    quote numeric(19,4) ,
    symbol character varying(255) ,
    CONSTRAINT tick_pkey PRIMARY KEY (identifier)
)

