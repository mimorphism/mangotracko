
-- DROP SEQUENCE public.confirmation_token_sequence;

CREATE SEQUENCE public.confirmation_token_sequence
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START 1
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE public.mangoes_mango_id_seq;

CREATE SEQUENCE public.mangoes_mango_id_seq
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START 1
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE public.user_sequence;

CREATE SEQUENCE public.user_sequence
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START 1
	CACHE 1
	NO CYCLE;-- public.mangoes definition

-- Drop table

-- DROP TABLE public.mangoes;

CREATE TABLE public.mangoes (
	mango_id int8 NOT NULL GENERATED BY DEFAULT AS IDENTITY,
	anilist_id int8 NULL,
	author varchar(255) NULL,
	bnr_img_path varchar(255) NULL,
	cvr_img_path varchar(255) NULL,
	last_chapter int4 NULL,
	mango_title varchar(255) NULL,
	status varchar(255) NULL,
	CONSTRAINT mangoes_pkey PRIMARY KEY (mango_id),
	CONSTRAINT uk_ohje8ijhov69h7n2yeptkp31g UNIQUE (mango_title)
);


-- public.users definition

-- Drop table

-- DROP TABLE public.users;

CREATE TABLE public.users (
	id int8 NOT NULL,
	app_user_role varchar(255) NULL,
	enabled bool NULL,
	"locked" bool NULL,
	"password" varchar(255) NULL,
	username varchar(255) NULL,
	CONSTRAINT users_pkey PRIMARY KEY (id)
);


-- public.backlog definition

-- Drop table

-- DROP TABLE public.backlog;

CREATE TABLE public.backlog (
	backlog_id int8 NOT NULL,
	added_datetime varchar(255) NOT NULL,
	mango_id int8 NOT NULL,
	user_id int8 NOT NULL,
	CONSTRAINT backlog_pkey PRIMARY KEY (backlog_id),
	CONSTRAINT fk4l4xbw9856uqdce724cc02y9s FOREIGN KEY (mango_id) REFERENCES public.mangoes(mango_id),
	CONSTRAINT fkpr56p9fnt6viemfky62nyhvx9 FOREIGN KEY (user_id) REFERENCES public.users(id)
);


-- public.confirmation_token definition

-- Drop table

-- DROP TABLE public.confirmation_token;

CREATE TABLE public.confirmation_token (
	id int8 NOT NULL,
	confirmed_at timestamp NULL,
	created_at timestamp NOT NULL,
	expires_at timestamp NOT NULL,
	"token" varchar(255) NOT NULL,
	app_user_id int8 NOT NULL,
	CONSTRAINT confirmation_token_pkey PRIMARY KEY (id),
	CONSTRAINT fkbcnap2kh2odaogu0jwb6yhubt FOREIGN KEY (app_user_id) REFERENCES public.users(id)
);


-- public.currently_reading definition

-- Drop table

-- DROP TABLE public.currently_reading;

CREATE TABLE public.currently_reading (
	ctly_reading_id int8 NOT NULL,
	last_chapter int4 NULL,
	last_read_time varchar(255) NOT NULL,
	mango_id int8 NOT NULL,
	user_id int8 NOT NULL,
	CONSTRAINT currently_reading_pkey PRIMARY KEY (ctly_reading_id),
	CONSTRAINT fk9r7it6w8ltbhxlh0umyluxite FOREIGN KEY (mango_id) REFERENCES public.mangoes(mango_id),
	CONSTRAINT fkqc7rluq6qs7lihmd41mxa4o9u FOREIGN KEY (user_id) REFERENCES public.users(id)
);


-- public.finished definition

-- Drop table

-- DROP TABLE public.finished;

CREATE TABLE public.finished (
	finished_id int8 NOT NULL,
	completion_date varchar(255) NOT NULL,
	remarks varchar(255) NOT NULL,
	mango_id int8 NOT NULL,
	user_id int8 NOT NULL,
	CONSTRAINT finished_pkey PRIMARY KEY (finished_id),
	CONSTRAINT fk73om81nscf9o64si9h6vpvd47 FOREIGN KEY (mango_id) REFERENCES public.mangoes(mango_id),
	CONSTRAINT fko7m5cyiis839mmmqm82mvywld FOREIGN KEY (user_id) REFERENCES public.users(id)
);
