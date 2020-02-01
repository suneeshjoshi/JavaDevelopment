DELETE FROM public.strategy_steps WHERE strategy_id in ( 1,2,3);

INSERT INTO public.strategy_steps(strategy_id, step_count, value) VALUES (1, 1, 1.00);
INSERT INTO public.strategy_steps(strategy_id, step_count, value) VALUES (1, 2, 2.50);
INSERT INTO public.strategy_steps(strategy_id, step_count, value) VALUES (1, 3, 6.25);
INSERT INTO public.strategy_steps(strategy_id, step_count, value) VALUES (1, 4, 15.63);
INSERT INTO public.strategy_steps(strategy_id, step_count, value) VALUES (1, 5, 39.07);

INSERT INTO public.strategy_steps(strategy_id, step_count, value) VALUES (2, 1, 10.00);
INSERT INTO public.strategy_steps(strategy_id, step_count, value) VALUES (2, 2, 10.00);
INSERT INTO public.strategy_steps(strategy_id, step_count, value) VALUES (2, 3, 10.00);
INSERT INTO public.strategy_steps(strategy_id, step_count, value) VALUES (2, 4, 10.00);
INSERT INTO public.strategy_steps(strategy_id, step_count, value) VALUES (2, 5, 10.00);
INSERT INTO public.strategy_steps(strategy_id, step_count, value) VALUES (2, 6, 10.00);
INSERT INTO public.strategy_steps(strategy_id, step_count, value) VALUES (2, 7, 10.00);
INSERT INTO public.strategy_steps(strategy_id, step_count, value) VALUES (2, 8, 10.00);
INSERT INTO public.strategy_steps(strategy_id, step_count, value) VALUES (2, 9, 10.00);
INSERT INTO public.strategy_steps(strategy_id, step_count, value) VALUES (2, 10, 10.00);
INSERT INTO public.strategy_steps(strategy_id, step_count, value) VALUES (2, 11, 10.00);

INSERT INTO public.strategy_steps(strategy_id, step_count, value) VALUES (3, 1, 1.00);
INSERT INTO public.strategy_steps(strategy_id, step_count, value) VALUES (3, 2, 2.50);
INSERT INTO public.strategy_steps(strategy_id, step_count, value, profit_percentage_threshold) VALUES (3, 3, 6.25, 80);
INSERT INTO public.strategy_steps(strategy_id, step_count, value, profit_percentage_threshold) VALUES (3, 4, 15.63, 70);
INSERT INTO public.strategy_steps(strategy_id, step_count, value, profit_percentage_threshold) VALUES (3, 5, 39.07, 70);
INSERT INTO public.strategy_steps(strategy_id, step_count, value, profit_percentage_threshold) VALUES (3, 6, 97.66, 60);
INSERT INTO public.strategy_steps(strategy_id, step_count, value, profit_percentage_threshold) VALUES (3, 7, 244.14, 60);
INSERT INTO public.strategy_steps(strategy_id, step_count, value, profit_percentage_threshold) VALUES (3, 8, 610.35, 60);
INSERT INTO public.strategy_steps(strategy_id, step_count, value, profit_percentage_threshold) VALUES (3, 9, 1525.88, 60);
INSERT INTO public.strategy_steps(strategy_id, step_count, value, profit_percentage_threshold) VALUES (3, 10, 3814.70, 60);

INSERT INTO public.strategy_steps(strategy_id, step_count, value) VALUES (4, 1, 1.00);
INSERT INTO public.strategy_steps(strategy_id, step_count, value) VALUES (4, 2, 2.50);
INSERT INTO public.strategy_steps(strategy_id, step_count, value) VALUES (4, 3, 6.25);
INSERT INTO public.strategy_steps(strategy_id, step_count, value) VALUES (4, 4, 15.63);
INSERT INTO public.strategy_steps(strategy_id, step_count, value) VALUES (4, 5, 39.07);
INSERT INTO public.strategy_steps(strategy_id, step_count, value) VALUES (4, 6, 97.66);
INSERT INTO public.strategy_steps(strategy_id, step_count, value) VALUES (4, 7, 244.14);
INSERT INTO public.strategy_steps(strategy_id, step_count, value) VALUES (4, 8, 610.35);
INSERT INTO public.strategy_steps(strategy_id, step_count, value) VALUES (4, 9, 1525.88);
INSERT INTO public.strategy_steps(strategy_id, step_count, value) VALUES (4, 10, 3814.70);

INSERT INTO public.strategy_steps(strategy_id, step_count, value) VALUES (5, 1, 1.00);
INSERT INTO public.strategy_steps(strategy_id, step_count, value) VALUES (5, 2, 2.50);
INSERT INTO public.strategy_steps(strategy_id, step_count, value) VALUES (5, 3, 6.25);
INSERT INTO public.strategy_steps(strategy_id, step_count, value) VALUES (5, 4, 15.63);
INSERT INTO public.strategy_steps(strategy_id, step_count, value) VALUES (5, 5, 39.07);
INSERT INTO public.strategy_steps(strategy_id, step_count, value) VALUES (5, 6, 97.66);
INSERT INTO public.strategy_steps(strategy_id, step_count, value) VALUES (5, 7, 244.14);
INSERT INTO public.strategy_steps(strategy_id, step_count, value) VALUES (5, 8, 610.35);
INSERT INTO public.strategy_steps(strategy_id, step_count, value) VALUES (5, 9, 1525.88);
INSERT INTO public.strategy_steps(strategy_id, step_count, value) VALUES (5, 10, 3814.70);

