DELETE FROM public.strategy WHERE identifier in ( 1,2 );

INSERT INTO public.strategy( identifier, strategy_name, creation_date, max_steps) VALUES (1, 'Multiple of 2.5 of last bid amount', now(), 5);
INSERT INTO public.strategy( identifier, strategy_name, creation_date, max_steps) VALUES (2, '2.5 Multiple Strategys 6th step amount divided in £10 Bid, and also recovering all the 10 - £1 bids', now(), 11);

