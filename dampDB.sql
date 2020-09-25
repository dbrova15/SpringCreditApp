--
-- PostgreSQL database dump
--

-- Dumped from database version 12.4 (Ubuntu 12.4-0ubuntu0.20.04.1)
-- Dumped by pg_dump version 12.4 (Ubuntu 12.4-0ubuntu0.20.04.1)
CREATE DATABASE bank_db
CREATE ROLE postgres WITH SUPERUSER CREATEDB CREATEROLE LOGIN ENCRYPTED PASSWORD '123';

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
-- Name: client; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.client (
    idx_ref character varying(30) NOT NULL,
    address character varying(45) DEFAULT NULL::character varying,
    curr_salary character(3),
    date_birthday timestamp without time zone NOT NULL,
    decision character varying(45) DEFAULT NULL::character varying,
    id_client integer NOT NULL,
    limit_itog real,
    mail character varying(45) DEFAULT NULL::character varying,
    month_salary numeric(15,2) DEFAULT NULL::numeric,
    phone character varying(45) DEFAULT NULL::character varying
);


ALTER TABLE public.client OWNER TO postgres;

--
-- Name: credit; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.credit (
    id_credit bigint NOT NULL,
    amt_credit real,
    date_start date NOT NULL,
    id_client bigint NOT NULL,
    state_credit character varying(1)
);


ALTER TABLE public.credit OWNER TO postgres;

--
-- Name: hibernate_sequence; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.hibernate_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.hibernate_sequence OWNER TO postgres;

--
-- Name: client client_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.client
    ADD CONSTRAINT client_pkey PRIMARY KEY (idx_ref);


--
-- Name: credit credit_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.credit
    ADD CONSTRAINT credit_pkey PRIMARY KEY (id_credit);


--
-- PostgreSQL database dump complete
--
