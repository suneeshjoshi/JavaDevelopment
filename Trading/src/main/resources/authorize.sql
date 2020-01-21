CREATE TABLE IF NOT EXISTS public.authorize
(
    identifier BIGSERIAL,
    allow_omnibus integer NOT NULL,
    balance numeric(19,2),
    country character varying(255) ,
    currency character varying(255) ,
    email character varying(255) ,
    full_name character varying(255) ,
    is_virtual integer NOT NULL,
    landing_company_full_name character varying(255) ,
    landing_company_name character varying(255) ,
    login_id character varying(255) ,
    CONSTRAINT authorize_pkey PRIMARY KEY (identifier)
)
