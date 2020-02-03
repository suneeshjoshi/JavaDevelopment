create table IF NOT EXISTS bollinger_band(
	identifier bigserial,
    average NUMERIC(19,4),
    standard_deviation NUMERIC(19,4),
    upper_band NUMERIC(19,4),
    lower_band NUMERIC(19,4),
    bandwidth NUMERIC(19,4),
    candle_id bigint,
    candle_epoch_string timestamp,
    PRIMARY KEY(identifier)
);
