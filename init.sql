--
-- PostgreSQL database cluster dump
--

-- Started on 2024-06-10 23:21:11

SET default_transaction_read_only = off;

SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;

--
-- Roles
--

CREATE ROLE student;
ALTER ROLE student WITH SUPERUSER INHERIT CREATEROLE CREATEDB LOGIN REPLICATION BYPASSRLS;

--
-- User Configurations
--








--
-- Databases
--

--
-- Database "template1" dump
--

\connect template1

--
-- PostgreSQL database dump
--

-- Dumped from database version 15.4 (Debian 15.4-2.pgdg120+1)
-- Dumped by pg_dump version 15.3

-- Started on 2024-06-10 23:21:11

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

-- Completed on 2024-06-10 23:21:11

--
-- PostgreSQL database dump complete
--

--
-- Database "rsww_180066_offer" dump
--

--
-- PostgreSQL database dump
--

-- Dumped from database version 15.4 (Debian 15.4-2.pgdg120+1)
-- Dumped by pg_dump version 15.3

-- Started on 2024-06-10 23:21:11

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 3434 (class 1262 OID 16389)
-- Name: rsww_180066_offer; Type: DATABASE; Schema: -; Owner: student
--

CREATE DATABASE rsww_180066_offer WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'en_US.utf8';


ALTER DATABASE rsww_180066_offer OWNER TO student;

\connect rsww_180066_offer

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 214 (class 1259 OID 16552)
-- Name: booking; Type: TABLE; Schema: public; Owner: student
--

CREATE TABLE public.booking (
    check_in date,
    check_out date,
    confirmed boolean NOT NULL,
    lock boolean NOT NULL,
    locked_at date,
    last_processed_position bigint NOT NULL,
    version bigint NOT NULL,
    id uuid NOT NULL,
    room_id uuid NOT NULL
);


ALTER TABLE public.booking OWNER TO student;

--
-- TOC entry 215 (class 1259 OID 16557)
-- Name: bus; Type: TABLE; Schema: public; Owner: student
--

CREATE TABLE public.bus (
    date date,
    last_processed_position bigint NOT NULL,
    version bigint NOT NULL,
    departure_id uuid,
    destination_id uuid,
    id uuid NOT NULL,
    bus_number character varying(255)
);


ALTER TABLE public.bus OWNER TO student;

--
-- TOC entry 216 (class 1259 OID 16564)
-- Name: bus_offer; Type: TABLE; Schema: public; Owner: student
--

CREATE TABLE public.bus_offer (
    hotel_room_id uuid,
    id uuid NOT NULL,
    initial_bus_id uuid,
    return_bus_id uuid
);


ALTER TABLE public.bus_offer OWNER TO student;

--
-- TOC entry 217 (class 1259 OID 16571)
-- Name: flight; Type: TABLE; Schema: public; Owner: student
--

CREATE TABLE public.flight (
    date date,
    last_processed_position bigint NOT NULL,
    version bigint NOT NULL,
    departure_id uuid,
    destination_id uuid,
    id uuid NOT NULL,
    flight_number character varying(255)
);


ALTER TABLE public.flight OWNER TO student;

--
-- TOC entry 218 (class 1259 OID 16578)
-- Name: flight_offer; Type: TABLE; Schema: public; Owner: student
--

CREATE TABLE public.flight_offer (
    hotel_room_id uuid,
    id uuid NOT NULL,
    initial_flight_id uuid,
    return_flight_id uuid
);


ALTER TABLE public.flight_offer OWNER TO student;

--
-- TOC entry 219 (class 1259 OID 16585)
-- Name: hotel; Type: TABLE; Schema: public; Owner: student
--

CREATE TABLE public.hotel (
    last_processed_position bigint NOT NULL,
    version bigint NOT NULL,
    id uuid NOT NULL,
    location_id uuid,
    name character varying(255)
);


ALTER TABLE public.hotel OWNER TO student;

--
-- TOC entry 220 (class 1259 OID 16590)
-- Name: hotel_room; Type: TABLE; Schema: public; Owner: student
--

CREATE TABLE public.hotel_room (
    beds integer,
    capacity integer,
    last_processed_position bigint NOT NULL,
    version bigint NOT NULL,
    hotel_id uuid NOT NULL,
    id uuid NOT NULL,
    type character varying(255)
);


ALTER TABLE public.hotel_room OWNER TO student;

--
-- TOC entry 221 (class 1259 OID 16595)
-- Name: location; Type: TABLE; Schema: public; Owner: student
--

CREATE TABLE public.location (
    id uuid NOT NULL,
    city character varying(255),
    country character varying(255)
);


ALTER TABLE public.location OWNER TO student;

--
-- TOC entry 222 (class 1259 OID 16604)
-- Name: offer; Type: TABLE; Schema: public; Owner: student
--

CREATE TABLE public.offer (
    id uuid NOT NULL
);


ALTER TABLE public.offer OWNER TO student;

--
-- TOC entry 3420 (class 0 OID 16552)
-- Dependencies: 214
-- Data for Name: booking; Type: TABLE DATA; Schema: public; Owner: student
--

COPY public.booking (check_in, check_out, confirmed, lock, locked_at, last_processed_position, version, id, room_id) FROM stdin;
\.


--
-- TOC entry 3421 (class 0 OID 16557)
-- Dependencies: 215
-- Data for Name: bus; Type: TABLE DATA; Schema: public; Owner: student
--

COPY public.bus (date, last_processed_position, version, departure_id, destination_id, id, bus_number) FROM stdin;
\.


--
-- TOC entry 3422 (class 0 OID 16564)
-- Dependencies: 216
-- Data for Name: bus_offer; Type: TABLE DATA; Schema: public; Owner: student
--

COPY public.bus_offer (hotel_room_id, id, initial_bus_id, return_bus_id) FROM stdin;
\.


--
-- TOC entry 3423 (class 0 OID 16571)
-- Dependencies: 217
-- Data for Name: flight; Type: TABLE DATA; Schema: public; Owner: student
--

COPY public.flight (date, last_processed_position, version, departure_id, destination_id, id, flight_number) FROM stdin;
\.


--
-- TOC entry 3424 (class 0 OID 16578)
-- Dependencies: 218
-- Data for Name: flight_offer; Type: TABLE DATA; Schema: public; Owner: student
--

COPY public.flight_offer (hotel_room_id, id, initial_flight_id, return_flight_id) FROM stdin;
\.


--
-- TOC entry 3425 (class 0 OID 16585)
-- Dependencies: 219
-- Data for Name: hotel; Type: TABLE DATA; Schema: public; Owner: student
--

COPY public.hotel (last_processed_position, version, id, location_id, name) FROM stdin;
\.


--
-- TOC entry 3426 (class 0 OID 16590)
-- Dependencies: 220
-- Data for Name: hotel_room; Type: TABLE DATA; Schema: public; Owner: student
--

COPY public.hotel_room (beds, capacity, last_processed_position, version, hotel_id, id, type) FROM stdin;
\.


--
-- TOC entry 3427 (class 0 OID 16595)
-- Dependencies: 221
-- Data for Name: location; Type: TABLE DATA; Schema: public; Owner: student
--

COPY public.location (id, city, country) FROM stdin;
\.


--
-- TOC entry 3428 (class 0 OID 16604)
-- Dependencies: 222
-- Data for Name: offer; Type: TABLE DATA; Schema: public; Owner: student
--

COPY public.offer (id) FROM stdin;
\.


--
-- TOC entry 3231 (class 2606 OID 16556)
-- Name: booking booking_pkey; Type: CONSTRAINT; Schema: public; Owner: student
--

ALTER TABLE ONLY public.booking
    ADD CONSTRAINT booking_pkey PRIMARY KEY (id);


--
-- TOC entry 3233 (class 2606 OID 16563)
-- Name: bus bus_bus_number_date_key; Type: CONSTRAINT; Schema: public; Owner: student
--

ALTER TABLE ONLY public.bus
    ADD CONSTRAINT bus_bus_number_date_key UNIQUE (bus_number, date);


--
-- TOC entry 3239 (class 2606 OID 16570)
-- Name: bus_offer bus_offer_initial_bus_id_return_bus_id_hotel_room_id_key; Type: CONSTRAINT; Schema: public; Owner: student
--

ALTER TABLE ONLY public.bus_offer
    ADD CONSTRAINT bus_offer_initial_bus_id_return_bus_id_hotel_room_id_key UNIQUE (initial_bus_id, return_bus_id, hotel_room_id);


--
-- TOC entry 3241 (class 2606 OID 16568)
-- Name: bus_offer bus_offer_pkey; Type: CONSTRAINT; Schema: public; Owner: student
--

ALTER TABLE ONLY public.bus_offer
    ADD CONSTRAINT bus_offer_pkey PRIMARY KEY (id);


--
-- TOC entry 3235 (class 2606 OID 16561)
-- Name: bus bus_pkey; Type: CONSTRAINT; Schema: public; Owner: student
--

ALTER TABLE ONLY public.bus
    ADD CONSTRAINT bus_pkey PRIMARY KEY (id);


--
-- TOC entry 3243 (class 2606 OID 16577)
-- Name: flight flight_flight_number_date_key; Type: CONSTRAINT; Schema: public; Owner: student
--

ALTER TABLE ONLY public.flight
    ADD CONSTRAINT flight_flight_number_date_key UNIQUE (flight_number, date);


--
-- TOC entry 3249 (class 2606 OID 16584)
-- Name: flight_offer flight_offer_initial_flight_id_return_flight_id_hotel_room__key; Type: CONSTRAINT; Schema: public; Owner: student
--

ALTER TABLE ONLY public.flight_offer
    ADD CONSTRAINT flight_offer_initial_flight_id_return_flight_id_hotel_room__key UNIQUE (initial_flight_id, return_flight_id, hotel_room_id);


--
-- TOC entry 3251 (class 2606 OID 16582)
-- Name: flight_offer flight_offer_pkey; Type: CONSTRAINT; Schema: public; Owner: student
--

ALTER TABLE ONLY public.flight_offer
    ADD CONSTRAINT flight_offer_pkey PRIMARY KEY (id);


--
-- TOC entry 3245 (class 2606 OID 16575)
-- Name: flight flight_pkey; Type: CONSTRAINT; Schema: public; Owner: student
--

ALTER TABLE ONLY public.flight
    ADD CONSTRAINT flight_pkey PRIMARY KEY (id);


--
-- TOC entry 3253 (class 2606 OID 16589)
-- Name: hotel hotel_pkey; Type: CONSTRAINT; Schema: public; Owner: student
--

ALTER TABLE ONLY public.hotel
    ADD CONSTRAINT hotel_pkey PRIMARY KEY (id);


--
-- TOC entry 3256 (class 2606 OID 16594)
-- Name: hotel_room hotel_room_pkey; Type: CONSTRAINT; Schema: public; Owner: student
--

ALTER TABLE ONLY public.hotel_room
    ADD CONSTRAINT hotel_room_pkey PRIMARY KEY (id);


--
-- TOC entry 3258 (class 2606 OID 16603)
-- Name: location location_country_city_key; Type: CONSTRAINT; Schema: public; Owner: student
--

ALTER TABLE ONLY public.location
    ADD CONSTRAINT location_country_city_key UNIQUE (country, city);


--
-- TOC entry 3260 (class 2606 OID 16601)
-- Name: location location_pkey; Type: CONSTRAINT; Schema: public; Owner: student
--

ALTER TABLE ONLY public.location
    ADD CONSTRAINT location_pkey PRIMARY KEY (id);


--
-- TOC entry 3262 (class 2606 OID 16608)
-- Name: offer offer_pkey; Type: CONSTRAINT; Schema: public; Owner: student
--

ALTER TABLE ONLY public.offer
    ADD CONSTRAINT offer_pkey PRIMARY KEY (id);


--
-- TOC entry 3236 (class 1259 OID 16609)
-- Name: idx4xkeqiybxfq3mkknjqltkmohx; Type: INDEX; Schema: public; Owner: student
--

CREATE INDEX idx4xkeqiybxfq3mkknjqltkmohx ON public.bus USING btree (departure_id);


--
-- TOC entry 3254 (class 1259 OID 16613)
-- Name: idxcyya6nj6k5hva36gkajm5i47v; Type: INDEX; Schema: public; Owner: student
--

CREATE INDEX idxcyya6nj6k5hva36gkajm5i47v ON public.hotel USING btree (location_id);


--
-- TOC entry 3246 (class 1259 OID 16612)
-- Name: idxgkq1lsjyyysixtjg3q4kb6am4; Type: INDEX; Schema: public; Owner: student
--

CREATE INDEX idxgkq1lsjyyysixtjg3q4kb6am4 ON public.flight USING btree (destination_id);


--
-- TOC entry 3237 (class 1259 OID 16610)
-- Name: idxjh55jnk1wkp5vh4cw07v4oxsc; Type: INDEX; Schema: public; Owner: student
--

CREATE INDEX idxjh55jnk1wkp5vh4cw07v4oxsc ON public.bus USING btree (destination_id);


--
-- TOC entry 3247 (class 1259 OID 16611)
-- Name: idxpy72oc2bqrwvfolm8y594ll0c; Type: INDEX; Schema: public; Owner: student
--

CREATE INDEX idxpy72oc2bqrwvfolm8y594ll0c ON public.flight USING btree (departure_id);


--
-- TOC entry 3272 (class 2606 OID 16669)
-- Name: flight_offer fk1e4x6u3sb2e0pc851g0m0wxdx; Type: FK CONSTRAINT; Schema: public; Owner: student
--

ALTER TABLE ONLY public.flight_offer
    ADD CONSTRAINT fk1e4x6u3sb2e0pc851g0m0wxdx FOREIGN KEY (return_flight_id) REFERENCES public.flight(id);


--
-- TOC entry 3273 (class 2606 OID 16664)
-- Name: flight_offer fk2m2p4v3s5x5libf0tgq7jhbdc; Type: FK CONSTRAINT; Schema: public; Owner: student
--

ALTER TABLE ONLY public.flight_offer
    ADD CONSTRAINT fk2m2p4v3s5x5libf0tgq7jhbdc FOREIGN KEY (initial_flight_id) REFERENCES public.flight(id);


--
-- TOC entry 3266 (class 2606 OID 16644)
-- Name: bus_offer fk3jmjm21ere8nv0qj9c0ujqcy7; Type: FK CONSTRAINT; Schema: public; Owner: student
--

ALTER TABLE ONLY public.bus_offer
    ADD CONSTRAINT fk3jmjm21ere8nv0qj9c0ujqcy7 FOREIGN KEY (id) REFERENCES public.offer(id);


--
-- TOC entry 3276 (class 2606 OID 16679)
-- Name: hotel fk67s51cnq7o3nlcjh6pm27dqxb; Type: FK CONSTRAINT; Schema: public; Owner: student
--

ALTER TABLE ONLY public.hotel
    ADD CONSTRAINT fk67s51cnq7o3nlcjh6pm27dqxb FOREIGN KEY (location_id) REFERENCES public.location(id);


--
-- TOC entry 3267 (class 2606 OID 16629)
-- Name: bus_offer fk6adiiw0s9ts2694lnrmg95pxj; Type: FK CONSTRAINT; Schema: public; Owner: student
--

ALTER TABLE ONLY public.bus_offer
    ADD CONSTRAINT fk6adiiw0s9ts2694lnrmg95pxj FOREIGN KEY (hotel_room_id) REFERENCES public.hotel_room(id);


--
-- TOC entry 3268 (class 2606 OID 16639)
-- Name: bus_offer fkarr6bmw50qipf99jcvxwxyysq; Type: FK CONSTRAINT; Schema: public; Owner: student
--

ALTER TABLE ONLY public.bus_offer
    ADD CONSTRAINT fkarr6bmw50qipf99jcvxwxyysq FOREIGN KEY (return_bus_id) REFERENCES public.bus(id);


--
-- TOC entry 3270 (class 2606 OID 16654)
-- Name: flight fkbevanaac10ymtkpphtuo99qge; Type: FK CONSTRAINT; Schema: public; Owner: student
--

ALTER TABLE ONLY public.flight
    ADD CONSTRAINT fkbevanaac10ymtkpphtuo99qge FOREIGN KEY (destination_id) REFERENCES public.location(id);


--
-- TOC entry 3274 (class 2606 OID 16659)
-- Name: flight_offer fkcku4qly6n1mglylr8jld5h51u; Type: FK CONSTRAINT; Schema: public; Owner: student
--

ALTER TABLE ONLY public.flight_offer
    ADD CONSTRAINT fkcku4qly6n1mglylr8jld5h51u FOREIGN KEY (hotel_room_id) REFERENCES public.hotel_room(id);


--
-- TOC entry 3275 (class 2606 OID 16674)
-- Name: flight_offer fkf97907fxws1dp4sey61qg0p1k; Type: FK CONSTRAINT; Schema: public; Owner: student
--

ALTER TABLE ONLY public.flight_offer
    ADD CONSTRAINT fkf97907fxws1dp4sey61qg0p1k FOREIGN KEY (id) REFERENCES public.offer(id);


--
-- TOC entry 3264 (class 2606 OID 16624)
-- Name: bus fkgy90aswsfunhmvfx3o3kh8u4l; Type: FK CONSTRAINT; Schema: public; Owner: student
--

ALTER TABLE ONLY public.bus
    ADD CONSTRAINT fkgy90aswsfunhmvfx3o3kh8u4l FOREIGN KEY (destination_id) REFERENCES public.location(id);


--
-- TOC entry 3265 (class 2606 OID 16619)
-- Name: bus fki228icspj959ubobtufrc77ep; Type: FK CONSTRAINT; Schema: public; Owner: student
--

ALTER TABLE ONLY public.bus
    ADD CONSTRAINT fki228icspj959ubobtufrc77ep FOREIGN KEY (departure_id) REFERENCES public.location(id);


--
-- TOC entry 3271 (class 2606 OID 16649)
-- Name: flight fkil8ciy75fkwyohak64p374aom; Type: FK CONSTRAINT; Schema: public; Owner: student
--

ALTER TABLE ONLY public.flight
    ADD CONSTRAINT fkil8ciy75fkwyohak64p374aom FOREIGN KEY (departure_id) REFERENCES public.location(id);


--
-- TOC entry 3263 (class 2606 OID 16614)
-- Name: booking fkkgu7irw9cdxxmslvryq3ftg1h; Type: FK CONSTRAINT; Schema: public; Owner: student
--

ALTER TABLE ONLY public.booking
    ADD CONSTRAINT fkkgu7irw9cdxxmslvryq3ftg1h FOREIGN KEY (room_id) REFERENCES public.hotel_room(id);


--
-- TOC entry 3277 (class 2606 OID 16684)
-- Name: hotel_room fkkidx9n5p4parnjnpg912svvgi; Type: FK CONSTRAINT; Schema: public; Owner: student
--

ALTER TABLE ONLY public.hotel_room
    ADD CONSTRAINT fkkidx9n5p4parnjnpg912svvgi FOREIGN KEY (hotel_id) REFERENCES public.hotel(id);


--
-- TOC entry 3269 (class 2606 OID 16634)
-- Name: bus_offer fkri4nm4setj7d8kbis8f9qwofv; Type: FK CONSTRAINT; Schema: public; Owner: student
--

ALTER TABLE ONLY public.bus_offer
    ADD CONSTRAINT fkri4nm4setj7d8kbis8f9qwofv FOREIGN KEY (initial_bus_id) REFERENCES public.bus(id);


-- Completed on 2024-06-10 23:21:11

--
-- PostgreSQL database dump complete
--

--
-- Database "rsww_180066_order" dump
--

--
-- PostgreSQL database dump
--

-- Dumped from database version 15.4 (Debian 15.4-2.pgdg120+1)
-- Dumped by pg_dump version 15.3

-- Started on 2024-06-10 23:21:11

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 3349 (class 1262 OID 16390)
-- Name: rsww_180066_order; Type: DATABASE; Schema: -; Owner: student
--

CREATE DATABASE rsww_180066_order WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'en_US.utf8';


ALTER DATABASE rsww_180066_order OWNER TO student;

\connect rsww_180066_order

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 214 (class 1259 OID 16393)
-- Name: orders; Type: TABLE; Schema: public; Owner: student
--

CREATE TABLE public.orders (
    total_price numeric(38,2),
    order_date timestamp(6) without time zone,
    user_id bigint,
    offer_id uuid,
    order_id uuid NOT NULL,
    order_status character varying(255),
    CONSTRAINT orders_order_status_check CHECK (((order_status)::text = ANY ((ARRAY['PENDING'::character varying, 'CREATED'::character varying, 'PROCESSING'::character varying, 'ACCEPTED'::character varying, 'CANCELLED'::character varying])::text[])))
);


ALTER TABLE public.orders OWNER TO student;

--
-- TOC entry 3343 (class 0 OID 16393)
-- Dependencies: 214
-- Data for Name: orders; Type: TABLE DATA; Schema: public; Owner: student
--

COPY public.orders (total_price, order_date, user_id, offer_id, order_id, order_status) FROM stdin;
\.


--
-- TOC entry 3200 (class 2606 OID 16398)
-- Name: orders orders_pkey; Type: CONSTRAINT; Schema: public; Owner: student
--

ALTER TABLE ONLY public.orders
    ADD CONSTRAINT orders_pkey PRIMARY KEY (order_id);


-- Completed on 2024-06-10 23:21:11

--
-- PostgreSQL database dump complete
--

--
-- Database "rsww_180066_payment" dump
--

--
-- PostgreSQL database dump
--

-- Dumped from database version 15.4 (Debian 15.4-2.pgdg120+1)
-- Dumped by pg_dump version 15.3

-- Started on 2024-06-10 23:21:11

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 3349 (class 1262 OID 16391)
-- Name: rsww_180066_payment; Type: DATABASE; Schema: -; Owner: student
--

CREATE DATABASE rsww_180066_payment WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'en_US.utf8';


ALTER DATABASE rsww_180066_payment OWNER TO student;

\connect rsww_180066_payment

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 214 (class 1259 OID 16399)
-- Name: payments; Type: TABLE; Schema: public; Owner: student
--

CREATE TABLE public.payments (
    amount numeric(38,2),
    created_at timestamp(6) without time zone,
    user_id bigint,
    id uuid NOT NULL,
    order_id uuid,
    payment_status character varying(255),
    CONSTRAINT payments_payment_status_check CHECK (((payment_status)::text = ANY ((ARRAY['PENDING'::character varying, 'FAIL'::character varying, 'SUCCESS'::character varying, 'CANCELLED'::character varying])::text[])))
);


ALTER TABLE public.payments OWNER TO student;

--
-- TOC entry 3343 (class 0 OID 16399)
-- Dependencies: 214
-- Data for Name: payments; Type: TABLE DATA; Schema: public; Owner: student
--

COPY public.payments (amount, created_at, user_id, id, order_id, payment_status) FROM stdin;
\.


--
-- TOC entry 3200 (class 2606 OID 16404)
-- Name: payments payments_pkey; Type: CONSTRAINT; Schema: public; Owner: student
--

ALTER TABLE ONLY public.payments
    ADD CONSTRAINT payments_pkey PRIMARY KEY (id);


-- Completed on 2024-06-10 23:21:11

--
-- PostgreSQL database dump complete
--

--
-- Database "rsww_180066_tour_operator" dump
--

--
-- PostgreSQL database dump
--

-- Dumped from database version 15.4 (Debian 15.4-2.pgdg120+1)
-- Dumped by pg_dump version 15.3

-- Started on 2024-06-10 23:21:11

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 3444 (class 1262 OID 16392)
-- Name: rsww_180066_tour_operator; Type: DATABASE; Schema: -; Owner: student
--

CREATE DATABASE rsww_180066_tour_operator WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'en_US.utf8';


ALTER DATABASE rsww_180066_tour_operator OWNER TO student;

\connect rsww_180066_tour_operator

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 222 (class 1259 OID 16413)
-- Name: age_range_price_item; Type: TABLE; Schema: public; Owner: student
--

CREATE TABLE public.age_range_price_item (
    bus_line_id integer,
    ending_range integer,
    flight_line_id integer,
    hotel_room_id integer,
    id integer NOT NULL,
    price numeric(38,2),
    starting_range integer
);


ALTER TABLE public.age_range_price_item OWNER TO student;

--
-- TOC entry 214 (class 1259 OID 16405)
-- Name: age_range_price_item_seq; Type: SEQUENCE; Schema: public; Owner: student
--

CREATE SEQUENCE public.age_range_price_item_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.age_range_price_item_seq OWNER TO student;

--
-- TOC entry 223 (class 1259 OID 16418)
-- Name: airport_location; Type: TABLE; Schema: public; Owner: student
--

CREATE TABLE public.airport_location (
    id integer NOT NULL,
    city character varying(255),
    country character varying(255)
);


ALTER TABLE public.airport_location OWNER TO student;

--
-- TOC entry 215 (class 1259 OID 16406)
-- Name: airport_location_seq; Type: SEQUENCE; Schema: public; Owner: student
--

CREATE SEQUENCE public.airport_location_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.airport_location_seq OWNER TO student;

--
-- TOC entry 224 (class 1259 OID 16425)
-- Name: bus; Type: TABLE; Schema: public; Owner: student
--

CREATE TABLE public.bus (
    departure_date date,
    id integer NOT NULL,
    is_it_returning boolean,
    line_id integer
);


ALTER TABLE public.bus OWNER TO student;

--
-- TOC entry 225 (class 1259 OID 16430)
-- Name: bus_line; Type: TABLE; Schema: public; Owner: student
--

CREATE TABLE public.bus_line (
    destination_location_id integer,
    home_location_id integer,
    id integer NOT NULL,
    max_passengers integer NOT NULL,
    price_id integer
);


ALTER TABLE public.bus_line OWNER TO student;

--
-- TOC entry 216 (class 1259 OID 16407)
-- Name: bus_line_seq; Type: SEQUENCE; Schema: public; Owner: student
--

CREATE SEQUENCE public.bus_line_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.bus_line_seq OWNER TO student;

--
-- TOC entry 217 (class 1259 OID 16408)
-- Name: bus_seq; Type: SEQUENCE; Schema: public; Owner: student
--

CREATE SEQUENCE public.bus_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.bus_seq OWNER TO student;

--
-- TOC entry 226 (class 1259 OID 16437)
-- Name: flight; Type: TABLE; Schema: public; Owner: student
--

CREATE TABLE public.flight (
    departure_date date,
    id integer NOT NULL,
    is_it_returning_flight boolean,
    line_id integer
);


ALTER TABLE public.flight OWNER TO student;

--
-- TOC entry 227 (class 1259 OID 16442)
-- Name: flight_line; Type: TABLE; Schema: public; Owner: student
--

CREATE TABLE public.flight_line (
    destination_location_id integer,
    home_location_id integer,
    id integer NOT NULL,
    max_passengers integer NOT NULL,
    price_id integer
);


ALTER TABLE public.flight_line OWNER TO student;

--
-- TOC entry 218 (class 1259 OID 16409)
-- Name: flight_line_seq; Type: SEQUENCE; Schema: public; Owner: student
--

CREATE SEQUENCE public.flight_line_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.flight_line_seq OWNER TO student;

--
-- TOC entry 219 (class 1259 OID 16410)
-- Name: flight_seq; Type: SEQUENCE; Schema: public; Owner: student
--

CREATE SEQUENCE public.flight_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.flight_seq OWNER TO student;

--
-- TOC entry 228 (class 1259 OID 16449)
-- Name: hotel; Type: TABLE; Schema: public; Owner: student
--

CREATE TABLE public.hotel (
    location_id integer,
    mode_of_transport smallint,
    id uuid NOT NULL,
    name character varying(255),
    CONSTRAINT hotel_mode_of_transport_check CHECK (((mode_of_transport >= 0) AND (mode_of_transport <= 1)))
);


ALTER TABLE public.hotel OWNER TO student;

--
-- TOC entry 229 (class 1259 OID 16455)
-- Name: hotel_room; Type: TABLE; Schema: public; Owner: student
--

CREATE TABLE public.hotel_room (
    id integer NOT NULL,
    max_people integer NOT NULL,
    number_in_hotel integer NOT NULL,
    number_of_beds integer NOT NULL,
    price_id integer,
    hotel_id uuid NOT NULL,
    description character varying(255)
);


ALTER TABLE public.hotel_room OWNER TO student;

--
-- TOC entry 220 (class 1259 OID 16411)
-- Name: hotel_room_seq; Type: SEQUENCE; Schema: public; Owner: student
--

CREATE SEQUENCE public.hotel_room_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.hotel_room_seq OWNER TO student;

--
-- TOC entry 230 (class 1259 OID 16462)
-- Name: price; Type: TABLE; Schema: public; Owner: student
--

CREATE TABLE public.price (
    bus_line_id integer,
    flight_line_id integer,
    hotel_room_id integer,
    id integer NOT NULL,
    price numeric(38,2)
);


ALTER TABLE public.price OWNER TO student;

--
-- TOC entry 221 (class 1259 OID 16412)
-- Name: price_seq; Type: SEQUENCE; Schema: public; Owner: student
--

CREATE SEQUENCE public.price_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.price_seq OWNER TO student;

--
-- TOC entry 3430 (class 0 OID 16413)
-- Dependencies: 222
-- Data for Name: age_range_price_item; Type: TABLE DATA; Schema: public; Owner: student
--

COPY public.age_range_price_item (bus_line_id, ending_range, flight_line_id, hotel_room_id, id, price, starting_range) FROM stdin;
\.


--
-- TOC entry 3431 (class 0 OID 16418)
-- Dependencies: 223
-- Data for Name: airport_location; Type: TABLE DATA; Schema: public; Owner: student
--

COPY public.airport_location (id, city, country) FROM stdin;
\.


--
-- TOC entry 3432 (class 0 OID 16425)
-- Dependencies: 224
-- Data for Name: bus; Type: TABLE DATA; Schema: public; Owner: student
--

COPY public.bus (departure_date, id, is_it_returning, line_id) FROM stdin;
\.


--
-- TOC entry 3433 (class 0 OID 16430)
-- Dependencies: 225
-- Data for Name: bus_line; Type: TABLE DATA; Schema: public; Owner: student
--

COPY public.bus_line (destination_location_id, home_location_id, id, max_passengers, price_id) FROM stdin;
\.


--
-- TOC entry 3434 (class 0 OID 16437)
-- Dependencies: 226
-- Data for Name: flight; Type: TABLE DATA; Schema: public; Owner: student
--

COPY public.flight (departure_date, id, is_it_returning_flight, line_id) FROM stdin;
\.


--
-- TOC entry 3435 (class 0 OID 16442)
-- Dependencies: 227
-- Data for Name: flight_line; Type: TABLE DATA; Schema: public; Owner: student
--

COPY public.flight_line (destination_location_id, home_location_id, id, max_passengers, price_id) FROM stdin;
\.


--
-- TOC entry 3436 (class 0 OID 16449)
-- Dependencies: 228
-- Data for Name: hotel; Type: TABLE DATA; Schema: public; Owner: student
--

COPY public.hotel (location_id, mode_of_transport, id, name) FROM stdin;
\.


--
-- TOC entry 3437 (class 0 OID 16455)
-- Dependencies: 229
-- Data for Name: hotel_room; Type: TABLE DATA; Schema: public; Owner: student
--

COPY public.hotel_room (id, max_people, number_in_hotel, number_of_beds, price_id, hotel_id, description) FROM stdin;
\.


--
-- TOC entry 3438 (class 0 OID 16462)
-- Dependencies: 230
-- Data for Name: price; Type: TABLE DATA; Schema: public; Owner: student
--

COPY public.price (bus_line_id, flight_line_id, hotel_room_id, id, price) FROM stdin;
\.


--
-- TOC entry 3445 (class 0 OID 0)
-- Dependencies: 214
-- Name: age_range_price_item_seq; Type: SEQUENCE SET; Schema: public; Owner: student
--

SELECT pg_catalog.setval('public.age_range_price_item_seq', 1, false);


--
-- TOC entry 3446 (class 0 OID 0)
-- Dependencies: 215
-- Name: airport_location_seq; Type: SEQUENCE SET; Schema: public; Owner: student
--

SELECT pg_catalog.setval('public.airport_location_seq', 1, false);


--
-- TOC entry 3447 (class 0 OID 0)
-- Dependencies: 216
-- Name: bus_line_seq; Type: SEQUENCE SET; Schema: public; Owner: student
--

SELECT pg_catalog.setval('public.bus_line_seq', 1, false);


--
-- TOC entry 3448 (class 0 OID 0)
-- Dependencies: 217
-- Name: bus_seq; Type: SEQUENCE SET; Schema: public; Owner: student
--

SELECT pg_catalog.setval('public.bus_seq', 1, false);


--
-- TOC entry 3449 (class 0 OID 0)
-- Dependencies: 218
-- Name: flight_line_seq; Type: SEQUENCE SET; Schema: public; Owner: student
--

SELECT pg_catalog.setval('public.flight_line_seq', 1, false);


--
-- TOC entry 3450 (class 0 OID 0)
-- Dependencies: 219
-- Name: flight_seq; Type: SEQUENCE SET; Schema: public; Owner: student
--

SELECT pg_catalog.setval('public.flight_seq', 1, false);


--
-- TOC entry 3451 (class 0 OID 0)
-- Dependencies: 220
-- Name: hotel_room_seq; Type: SEQUENCE SET; Schema: public; Owner: student
--

SELECT pg_catalog.setval('public.hotel_room_seq', 1, false);


--
-- TOC entry 3452 (class 0 OID 0)
-- Dependencies: 221
-- Name: price_seq; Type: SEQUENCE SET; Schema: public; Owner: student
--

SELECT pg_catalog.setval('public.price_seq', 1, false);


--
-- TOC entry 3240 (class 2606 OID 16417)
-- Name: age_range_price_item age_range_price_item_pkey; Type: CONSTRAINT; Schema: public; Owner: student
--

ALTER TABLE ONLY public.age_range_price_item
    ADD CONSTRAINT age_range_price_item_pkey PRIMARY KEY (id);


--
-- TOC entry 3242 (class 2606 OID 16424)
-- Name: airport_location airport_location_pkey; Type: CONSTRAINT; Schema: public; Owner: student
--

ALTER TABLE ONLY public.airport_location
    ADD CONSTRAINT airport_location_pkey PRIMARY KEY (id);


--
-- TOC entry 3246 (class 2606 OID 16434)
-- Name: bus_line bus_line_pkey; Type: CONSTRAINT; Schema: public; Owner: student
--

ALTER TABLE ONLY public.bus_line
    ADD CONSTRAINT bus_line_pkey PRIMARY KEY (id);


--
-- TOC entry 3248 (class 2606 OID 16436)
-- Name: bus_line bus_line_price_id_key; Type: CONSTRAINT; Schema: public; Owner: student
--

ALTER TABLE ONLY public.bus_line
    ADD CONSTRAINT bus_line_price_id_key UNIQUE (price_id);


--
-- TOC entry 3244 (class 2606 OID 16429)
-- Name: bus bus_pkey; Type: CONSTRAINT; Schema: public; Owner: student
--

ALTER TABLE ONLY public.bus
    ADD CONSTRAINT bus_pkey PRIMARY KEY (id);


--
-- TOC entry 3252 (class 2606 OID 16446)
-- Name: flight_line flight_line_pkey; Type: CONSTRAINT; Schema: public; Owner: student
--

ALTER TABLE ONLY public.flight_line
    ADD CONSTRAINT flight_line_pkey PRIMARY KEY (id);


--
-- TOC entry 3254 (class 2606 OID 16448)
-- Name: flight_line flight_line_price_id_key; Type: CONSTRAINT; Schema: public; Owner: student
--

ALTER TABLE ONLY public.flight_line
    ADD CONSTRAINT flight_line_price_id_key UNIQUE (price_id);


--
-- TOC entry 3250 (class 2606 OID 16441)
-- Name: flight flight_pkey; Type: CONSTRAINT; Schema: public; Owner: student
--

ALTER TABLE ONLY public.flight
    ADD CONSTRAINT flight_pkey PRIMARY KEY (id);


--
-- TOC entry 3256 (class 2606 OID 16454)
-- Name: hotel hotel_pkey; Type: CONSTRAINT; Schema: public; Owner: student
--

ALTER TABLE ONLY public.hotel
    ADD CONSTRAINT hotel_pkey PRIMARY KEY (id);


--
-- TOC entry 3258 (class 2606 OID 16459)
-- Name: hotel_room hotel_room_pkey; Type: CONSTRAINT; Schema: public; Owner: student
--

ALTER TABLE ONLY public.hotel_room
    ADD CONSTRAINT hotel_room_pkey PRIMARY KEY (id);


--
-- TOC entry 3260 (class 2606 OID 16461)
-- Name: hotel_room hotel_room_price_id_key; Type: CONSTRAINT; Schema: public; Owner: student
--

ALTER TABLE ONLY public.hotel_room
    ADD CONSTRAINT hotel_room_price_id_key UNIQUE (price_id);


--
-- TOC entry 3262 (class 2606 OID 16466)
-- Name: price price_pkey; Type: CONSTRAINT; Schema: public; Owner: student
--

ALTER TABLE ONLY public.price
    ADD CONSTRAINT price_pkey PRIMARY KEY (id);


--
-- TOC entry 3267 (class 2606 OID 16487)
-- Name: bus_line fka86bam5sp2hdnlc3ia5csbldq; Type: FK CONSTRAINT; Schema: public; Owner: student
--

ALTER TABLE ONLY public.bus_line
    ADD CONSTRAINT fka86bam5sp2hdnlc3ia5csbldq FOREIGN KEY (destination_location_id) REFERENCES public.airport_location(id);


--
-- TOC entry 3263 (class 2606 OID 16467)
-- Name: age_range_price_item fkb2crtrnlqbxm27p6ajapmxvva; Type: FK CONSTRAINT; Schema: public; Owner: student
--

ALTER TABLE ONLY public.age_range_price_item
    ADD CONSTRAINT fkb2crtrnlqbxm27p6ajapmxvva FOREIGN KEY (bus_line_id) REFERENCES public.bus_line(id);


--
-- TOC entry 3277 (class 2606 OID 16537)
-- Name: price fkfwtjf9c424xy4yddw427o8lwi; Type: FK CONSTRAINT; Schema: public; Owner: student
--

ALTER TABLE ONLY public.price
    ADD CONSTRAINT fkfwtjf9c424xy4yddw427o8lwi FOREIGN KEY (bus_line_id) REFERENCES public.bus_line(id);


--
-- TOC entry 3274 (class 2606 OID 16522)
-- Name: hotel fkgwfdup6dv5lh6poili1bin27v; Type: FK CONSTRAINT; Schema: public; Owner: student
--

ALTER TABLE ONLY public.hotel
    ADD CONSTRAINT fkgwfdup6dv5lh6poili1bin27v FOREIGN KEY (location_id) REFERENCES public.airport_location(id);


--
-- TOC entry 3271 (class 2606 OID 16507)
-- Name: flight_line fki9o20e1mql2k0ixatgq6aycph; Type: FK CONSTRAINT; Schema: public; Owner: student
--

ALTER TABLE ONLY public.flight_line
    ADD CONSTRAINT fki9o20e1mql2k0ixatgq6aycph FOREIGN KEY (destination_location_id) REFERENCES public.airport_location(id);


--
-- TOC entry 3270 (class 2606 OID 16502)
-- Name: flight fkixafrc8v9sgsv5n4rasnf2vxx; Type: FK CONSTRAINT; Schema: public; Owner: student
--

ALTER TABLE ONLY public.flight
    ADD CONSTRAINT fkixafrc8v9sgsv5n4rasnf2vxx FOREIGN KEY (line_id) REFERENCES public.flight_line(id);


--
-- TOC entry 3272 (class 2606 OID 16517)
-- Name: flight_line fkk222ypuui5ieukyye6jp1lxox; Type: FK CONSTRAINT; Schema: public; Owner: student
--

ALTER TABLE ONLY public.flight_line
    ADD CONSTRAINT fkk222ypuui5ieukyye6jp1lxox FOREIGN KEY (price_id) REFERENCES public.price(id);


--
-- TOC entry 3273 (class 2606 OID 16512)
-- Name: flight_line fkk9xho4m2y9wyyscthbiuc1dyo; Type: FK CONSTRAINT; Schema: public; Owner: student
--

ALTER TABLE ONLY public.flight_line
    ADD CONSTRAINT fkk9xho4m2y9wyyscthbiuc1dyo FOREIGN KEY (home_location_id) REFERENCES public.airport_location(id);


--
-- TOC entry 3275 (class 2606 OID 16527)
-- Name: hotel_room fkkidx9n5p4parnjnpg912svvgi; Type: FK CONSTRAINT; Schema: public; Owner: student
--

ALTER TABLE ONLY public.hotel_room
    ADD CONSTRAINT fkkidx9n5p4parnjnpg912svvgi FOREIGN KEY (hotel_id) REFERENCES public.hotel(id);


--
-- TOC entry 3264 (class 2606 OID 16472)
-- Name: age_range_price_item fkledf8prn2s06vpl0ykewg1iij; Type: FK CONSTRAINT; Schema: public; Owner: student
--

ALTER TABLE ONLY public.age_range_price_item
    ADD CONSTRAINT fkledf8prn2s06vpl0ykewg1iij FOREIGN KEY (flight_line_id) REFERENCES public.flight_line(id);


--
-- TOC entry 3278 (class 2606 OID 16542)
-- Name: price fkn663scarvmigtgj6xstsbkph6; Type: FK CONSTRAINT; Schema: public; Owner: student
--

ALTER TABLE ONLY public.price
    ADD CONSTRAINT fkn663scarvmigtgj6xstsbkph6 FOREIGN KEY (flight_line_id) REFERENCES public.flight_line(id);


--
-- TOC entry 3279 (class 2606 OID 16547)
-- Name: price fkokog58t0kk7xgstioou906y8x; Type: FK CONSTRAINT; Schema: public; Owner: student
--

ALTER TABLE ONLY public.price
    ADD CONSTRAINT fkokog58t0kk7xgstioou906y8x FOREIGN KEY (hotel_room_id) REFERENCES public.hotel_room(id);


--
-- TOC entry 3265 (class 2606 OID 16477)
-- Name: age_range_price_item fkooxsm10r8nfnpnuf9y3xe8tip; Type: FK CONSTRAINT; Schema: public; Owner: student
--

ALTER TABLE ONLY public.age_range_price_item
    ADD CONSTRAINT fkooxsm10r8nfnpnuf9y3xe8tip FOREIGN KEY (hotel_room_id) REFERENCES public.hotel_room(id);


--
-- TOC entry 3276 (class 2606 OID 16532)
-- Name: hotel_room fkpm1iyo90qgo3ja0elvhart9ka; Type: FK CONSTRAINT; Schema: public; Owner: student
--

ALTER TABLE ONLY public.hotel_room
    ADD CONSTRAINT fkpm1iyo90qgo3ja0elvhart9ka FOREIGN KEY (price_id) REFERENCES public.price(id);


--
-- TOC entry 3268 (class 2606 OID 16492)
-- Name: bus_line fkqiydgoqnwue6ncqyavo43on4a; Type: FK CONSTRAINT; Schema: public; Owner: student
--

ALTER TABLE ONLY public.bus_line
    ADD CONSTRAINT fkqiydgoqnwue6ncqyavo43on4a FOREIGN KEY (home_location_id) REFERENCES public.airport_location(id);


--
-- TOC entry 3269 (class 2606 OID 16497)
-- Name: bus_line fkrkkr140malt0w7iti25b3vl12; Type: FK CONSTRAINT; Schema: public; Owner: student
--

ALTER TABLE ONLY public.bus_line
    ADD CONSTRAINT fkrkkr140malt0w7iti25b3vl12 FOREIGN KEY (price_id) REFERENCES public.price(id);


--
-- TOC entry 3266 (class 2606 OID 16482)
-- Name: bus fktn7owxjvn3bix86vanybe8634; Type: FK CONSTRAINT; Schema: public; Owner: student
--

ALTER TABLE ONLY public.bus
    ADD CONSTRAINT fktn7owxjvn3bix86vanybe8634 FOREIGN KEY (line_id) REFERENCES public.bus_line(id);


-- Completed on 2024-06-10 23:21:12

--
-- PostgreSQL database dump complete
--

-- Completed on 2024-06-10 23:21:12

--
-- PostgreSQL database cluster dump complete
--

