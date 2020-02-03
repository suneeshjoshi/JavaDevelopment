DELETE FROM public.strategy WHERE identifier in ( 1,2,3 ,4 );

INSERT INTO public.strategy( identifier, strategy_name, strategy_algorithm_id, creation_date, max_steps, next_strategy_id_link, is_backtesting_strategy, is_default_strategy, reset_step_count_on_success, active_strategy) VALUES
 (1, 'Multiple of 2.5 of last bid amount', 1, now(), 5, 2, FALSE, TRUE, TRUE, FALSE);
INSERT INTO public.strategy( identifier, strategy_name, strategy_algorithm_id, creation_date, max_steps, next_strategy_id_link, is_backtesting_strategy, is_default_strategy, reset_step_count_on_success, active_strategy) VALUES
(2, '2.5 Multiple Strategys 6th step amount divided in £10 Bid, and also recovering all the 10 - £1 bids', 2,now(), 11, 1, FALSE, FALSE, FALSE , FALSE);
INSERT INTO public.strategy( identifier, strategy_name, strategy_algorithm_id, creation_date, max_steps, next_strategy_id_link, is_backtesting_strategy, is_default_strategy, reset_step_count_on_success, active_strategy) VALUES
(3, 'Backtesting strategy 1', 1,now(), 10, 3, TRUE, FALSE, TRUE, TRUE);
INSERT INTO public.strategy( identifier, strategy_name, strategy_algorithm_id, creation_date, max_steps, next_strategy_id_link, is_backtesting_strategy, is_default_strategy, reset_step_count_on_success, active_strategy) VALUES
(4, 'Bollinger Band Strategy', 4, now(), 10, 4, FALSE, FALSE, TRUE, FALSE);
INSERT INTO public.strategy( identifier, strategy_name, strategy_algorithm_id, creation_date, max_steps, next_strategy_id_link, is_backtesting_strategy, is_default_strategy, reset_step_count_on_success, active_strategy) VALUES
(5, 'Close Price hitting High or Low Strategy', 3, now(), 10, 5, FALSE, FALSE, TRUE, FALSE);





