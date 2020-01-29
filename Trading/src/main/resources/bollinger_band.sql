create table IF NOT EXISTS bollinger_band(
	identifier bigserial,
    average NUMERIC(19,4),
    standardDeviation NUMERIC(19,4),
    upperBand NUMERIC(19,4),
    lowerBand NUMERIC(19,4),
    bandWidth NUMERIC(19,4),
    candle_id bigint,
	PRIMARY KEY(identifier)
);
