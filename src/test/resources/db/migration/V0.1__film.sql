CREATE DOMAIN year AS integer
    CONSTRAINT year_check CHECK (((VALUE >= 1901) AND (VALUE <= 2155)));
CREATE SEQUENCE film_film_id_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;
CREATE TYPE mpaa_rating AS ENUM (
    'G',
    'PG',
    'PG-13',
    'R',
    'NC-17'
);
CREATE TABLE film (
                      film_id integer DEFAULT nextval('film_film_id_seq'::regclass) NOT NULL,
                      title character varying(255) NOT NULL,
                      description text,
                      release_year year,
                      language_id smallint NOT NULL,
                      original_language_id smallint,
                      rental_duration smallint DEFAULT 3 NOT NULL,
                      rental_rate numeric(4,2) DEFAULT 4.99 NOT NULL,
                      length smallint,
                      replacement_cost numeric(5,2) DEFAULT 19.99 NOT NULL,
                      rating mpaa_rating DEFAULT 'G'::mpaa_rating,
                      last_update timestamp without time zone DEFAULT now() NOT NULL,
                      special_features text[],
                      fulltext tsvector NOT NULL
);
CREATE TRIGGER film_fulltext_trigger
    BEFORE INSERT OR UPDATE ON film
                         FOR EACH ROW
                         EXECUTE PROCEDURE tsvector_update_trigger('fulltext', 'pg_catalog.english', 'title', 'description');