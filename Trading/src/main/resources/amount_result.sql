create table IF NOT EXISTS amount_result(
strategy_id integer PRIMARY KEY,
total_bid_amount bigint,
total_amount_won bigint,
net_amount_diff bigint,
total_trades integer,
total_successful_trades integer,
total_failed_trades integer
);
