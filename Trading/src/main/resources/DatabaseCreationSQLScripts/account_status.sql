-- Table: public.account_status

-- DROP TABLE public.account_status;

CREATE TABLE public.account_status
(
    identifier bigint NOT NULL,
    prompt_client_to_authenticate integer NOT NULL,
    risk_classification character varying(255) ,
    status character varying(255) ,
    CONSTRAINT account_status_pkey PRIMARY KEY (identifier)
)
