CREATE TABLE IF NOT EXISTS public.storage (
	id serial8 NOT NULL,
	storage_type varchar NOT NULL,
	bucket varchar NOT NULL,
	path varchar NOT NULL,
	CONSTRAINT storage_pkey PRIMARY KEY (id)
);

CREATE UNIQUE INDEX IF NOT EXISTS storage_pkey ON public.storage USING btree (id);


CREATE SEQUENCE IF NOT EXISTS  public.storage_seq
	INCREMENT BY 50
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START 1
	CACHE 1
	NO CYCLE;