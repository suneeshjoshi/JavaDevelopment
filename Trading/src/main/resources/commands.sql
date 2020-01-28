select result, count(*) from trade group by result;

create table amount_result(
strategy_id integer PRIMARY KEY,
total_bid_amount bigint,
total_amount_won bigint,
total_number_of_trades integer,
total_number_of_successful_trades integer,
total_number_of_failed_trades integer
);

select * from amount_result;
