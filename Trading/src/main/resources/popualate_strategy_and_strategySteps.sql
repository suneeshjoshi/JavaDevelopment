DELETE FROM public.strategy WHERE identifier in ( 1,2 );
DELETE FROM public.strategy_steps WHERE strategy_id in ( 1,2 );

INSERT INTO public.strategy( identifier, strategy_name, creation_date, max_steps) VALUES (1, 'Multiple of 2.5 of last bid amount', now(), 5);
INSERT INTO public.strategy( identifier, strategy_name, creation_date, max_steps) VALUES (2, '2.5 Multiple Strategys 6th step amount divided in £10 Bid, and also recovering all the 10 - £1 bids', now(), 11);

INSERT INTO public.strategy_steps(identifier, strategy_id, step_count, value) VALUES (1, 1, 1, 1.00);
INSERT INTO public.strategy_steps(identifier, strategy_id, step_count, value) VALUES (2, 1, 2, 2.50);
INSERT INTO public.strategy_steps(identifier, strategy_id, step_count, value) VALUES (3, 1, 3, 6.25);
INSERT INTO public.strategy_steps(identifier, strategy_id, step_count, value) VALUES (4, 1, 4, 15.63);
INSERT INTO public.strategy_steps(identifier, strategy_id, step_count, value) VALUES (5, 1, 5, 39.07);

INSERT INTO public.strategy_steps(identifier, strategy_id, step_count, value) VALUES (6, 2, 1, 10.00);
INSERT INTO public.strategy_steps(identifier, strategy_id, step_count, value) VALUES (7, 2, 2, 10.00);
INSERT INTO public.strategy_steps(identifier, strategy_id, step_count, value) VALUES (8, 2, 3, 10.00);
INSERT INTO public.strategy_steps(identifier, strategy_id, step_count, value) VALUES (9, 2, 4, 10.00);
INSERT INTO public.strategy_steps(identifier, strategy_id, step_count, value) VALUES (10, 2, 5, 10.00);
INSERT INTO public.strategy_steps(identifier, strategy_id, step_count, value) VALUES (11, 2, 6, 10.00);
INSERT INTO public.strategy_steps(identifier, strategy_id, step_count, value) VALUES (12, 2, 7, 10.00);
INSERT INTO public.strategy_steps(identifier, strategy_id, step_count, value) VALUES (13, 2, 8, 10.00);
INSERT INTO public.strategy_steps(identifier, strategy_id, step_count, value) VALUES (14, 2, 9, 10.00);
INSERT INTO public.strategy_steps(identifier, strategy_id, step_count, value) VALUES (15, 2, 10, 10.00);
INSERT INTO public.strategy_steps(identifier, strategy_id, step_count, value) VALUES (16, 2, 11, 10.00);
