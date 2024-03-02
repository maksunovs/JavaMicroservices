CREATE TABLE IF NOT EXISTS public.resource (
	id serial8 NOT NULL,
	source_path varchar NOT NULL,
	storage varchar NOT NULL,
	CONSTRAINT resource_pkey PRIMARY KEY (id)
);

CREATE UNIQUE INDEX IF NOT EXISTS resource_pkey ON public.resource USING btree (id);


CREATE SEQUENCE IF NOT EXISTS  public.resource_seq
	INCREMENT BY 50
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START 1
	CACHE 1
	NO CYCLE;