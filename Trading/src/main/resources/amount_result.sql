create table IF NOT EXISTS amount_result(
strategy_id integer PRIMARY KEY,
total_bid_amount bigint,
total_amount_won bigint,
net_amount_diff bigint,
total_trades integer,
total_successful_trades integer,
total_failed_trades integer
);

insert into amount_result (strategy_id, total_bid_amount ,total_amount_won ,net_amount_diff,total_trades ,total_successful_trades, total_failed_trades)
VALUES
(
    1,
    (select sum(bid_amount) from trade where trade.strategy_id=1),
    (select sum(amount_won) from trade where trade.strategy_id=1),
    ((select sum(amount_won) from trade where trade.strategy_id=1) - (select sum(bid_amount) from trade where trade.strategy_id=1)),
    (select count(*) from trade where trade.strategy_id=1),
    (select count(*) from trade where trade.strategy_id=1 AND result='SUCCESS'),
    (select count(*) from trade where trade.strategy_id=1 AND result='FAIL')
);

insert into amount_result (strategy_id, total_bid_amount ,total_amount_won ,net_amount_diff,total_trades ,total_successful_trades, total_failed_trades)
VALUES
(
    2,
    (select sum(bid_amount) from trade where trade.strategy_id=2),
    (select sum(amount_won) from trade where trade.strategy_id=2),
    ((select sum(amount_won) from trade where trade.strategy_id=2) - (select sum(bid_amount) from trade where trade.strategy_id=2)),
    (select count(*) from trade where trade.strategy_id=2),
    (select count(*) from trade where trade.strategy_id=2 AND result='SUCCESS'),
    (select count(*) from trade where trade.strategy_id=2 AND result='FAIL')
);

insert into amount_result (strategy_id, total_bid_amount ,total_amount_won ,net_amount_diff,total_trades ,total_successful_trades, total_failed_trades)
VALUES
(
    3,
    (select sum(bid_amount) from trade where trade.strategy_id=3),
    (select sum(amount_won) from trade where trade.strategy_id=3),
    ((select sum(amount_won) from trade where trade.strategy_id=3) - (select sum(bid_amount) from trade where trade.strategy_id=3)),
    (select count(*) from trade where trade.strategy_id=3),
    (select count(*) from trade where trade.strategy_id=3 AND result='SUCCESS'),
    (select count(*) from trade where trade.strategy_id=3 AND result='FAIL')
);

select * from amount_result;
