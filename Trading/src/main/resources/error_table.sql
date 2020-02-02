CREATE TABLE IF NOT EXISTS error_table
(
identifier BIGSERIAL PRIMARY KEY,
error_message character varying(255) NOT NULL,
status  character varying(255) NOT NULL,
creation_time timestamp NOT NULL,
fix_time timestamp
);

