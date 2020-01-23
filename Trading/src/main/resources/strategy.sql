CREATE TABLE IF NOT EXISTS public.strategy
(
    identifier SERIAL,
    strategy_name character varying(255) ,
    creation_date timestamp NOT NULL,
    max_steps INTEGER NOT NULL,
    CONSTRAINT strategy_pkey PRIMARY KEY (identifier)
)

--INSERT INTO public.strategy( identifier, strategy_name, creation_date, max_steps) 	VALUES (1, 'Multiple of 2.5 of last bid amount', now(), 5);
--INSERT INTO public.strategy( identifier, strategy_name, creation_date, max_steps) VALUES (2, '2.5 Multiple Strategys 6th step amount divided in £10 Bid, and also recovering all the 10 - £1 bids', now(), 11);

--Helpful Query
--select * From strategy_steps ss join strategy s on ( ss.strategy_id = s.identifier)

