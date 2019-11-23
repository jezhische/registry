insert into roles values (1, 'CUSTOMER');
insert into roles values (2, 'ADMIN');

-- ==============================================================
CREATE TABLE public.roles
(id bigint NOT NULL,
    role character varying(255),
    CONSTRAINT roles_pkey PRIMARY KEY (id))
WITH (OIDS=FALSE);
ALTER TABLE public.roles OWNER TO postgres;