-- public.song definition

-- Drop table

-- DROP TABLE song;

CREATE TABLE IF NOT EXISTS public.song (
	"year" int4 NOT NULL,
	id int8 NOT NULL,
	resource_id int8 NULL,
	album varchar(255) NULL,
	artist varchar(255) NULL,
	length varchar(255) NULL,
	"name" varchar(255) NULL,
	genre varchar(255) NULL,
	CONSTRAINT song_pkey PRIMARY KEY (id)
);

CREATE UNIQUE INDEX IF NOT EXISTS song_pkey ON public.song USING btree (id);

-- public.song_seq definition

-- DROP SEQUENCE public.song_seq;

CREATE SEQUENCE IF NOT EXISTS public.song_seq
	INCREMENT BY 50
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START 1
	CACHE 1
	NO CYCLE;
