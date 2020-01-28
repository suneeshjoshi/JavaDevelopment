create table IF NOT EXISTS amount_result(
identifier bigserial,
test_run_id bigint,
strategy_id integer,
total_bid_amount bigint,
total_amount_won bigint,
net_amount_diff bigint,
total_trades integer,
total_successful_trades integer,
total_failed_trades integer,
max_steps integer,
max_failed_steps integer,
PRIMARY KEY(test_run_id, strategy_id)
);
