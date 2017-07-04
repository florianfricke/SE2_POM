--
-- PostgreSQL database dump
--

-- Dumped from database version 9.6.3
-- Dumped by pg_dump version 9.6.3

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = pom, pg_catalog;

--
-- Name: sq_address; Type: SEQUENCE; Schema: pom; Owner: postgres
--

CREATE SEQUENCE sq_address
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE sq_address OWNER TO postgres;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: address; Type: TABLE; Schema: pom; Owner: postgres
--

CREATE TABLE address (
    id character(5) DEFAULT nextval('sq_address'::regclass) NOT NULL,
    customerid character(5),
    street character varying(32) NOT NULL,
    houseno character varying(5) NOT NULL,
    city character varying(32) NOT NULL,
    zipcode character(5) NOT NULL,
    country character varying(32) NOT NULL,
    billingaddress boolean
);


ALTER TABLE address OWNER TO postgres;

--
-- Name: sq_bankaccount; Type: SEQUENCE; Schema: pom; Owner: postgres
--

CREATE SEQUENCE sq_bankaccount
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE sq_bankaccount OWNER TO postgres;

--
-- Name: bankaccount; Type: TABLE; Schema: pom; Owner: postgres
--

CREATE TABLE bankaccount (
    id character(5) DEFAULT nextval('sq_bankaccount'::regclass) NOT NULL,
    customerid character(5) NOT NULL,
    iban character varying(34) NOT NULL,
    bic character varying(11) NOT NULL,
    bankname character varying(50) NOT NULL
);


ALTER TABLE bankaccount OWNER TO postgres;

--
-- Name: sq_contact; Type: SEQUENCE; Schema: pom; Owner: postgres
--

CREATE SEQUENCE sq_contact
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE sq_contact OWNER TO postgres;

--
-- Name: contact; Type: TABLE; Schema: pom; Owner: postgres
--

CREATE TABLE contact (
    id character(5) DEFAULT nextval('sq_contact'::regclass) NOT NULL,
    customerid character(5) NOT NULL,
    phoneno character varying(15),
    name character varying(32) NOT NULL,
    firstname character varying(32) NOT NULL,
    mailadress character varying(30),
    "position" character varying(30)
);


ALTER TABLE contact OWNER TO postgres;

--
-- Name: sq_customer; Type: SEQUENCE; Schema: pom; Owner: postgres
--

CREATE SEQUENCE sq_customer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE sq_customer OWNER TO postgres;

--
-- Name: customer; Type: TABLE; Schema: pom; Owner: postgres
--

CREATE TABLE customer (
    id character(5) DEFAULT nextval('sq_customer'::regclass) NOT NULL,
    companyname character varying(32) NOT NULL,
    ranking character(2),
    comment character varying(250)
);


ALTER TABLE customer OWNER TO postgres;

--
-- Name: customer_sq_pk; Type: SEQUENCE; Schema: pom; Owner: postgres
--

CREATE SEQUENCE customer_sq_pk
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE customer_sq_pk OWNER TO postgres;

--
-- Name: customer_sq_pk; Type: SEQUENCE OWNED BY; Schema: pom; Owner: postgres
--

ALTER SEQUENCE customer_sq_pk OWNED BY customer.id;


--
-- Name: sq_order; Type: SEQUENCE; Schema: pom; Owner: postgres
--

CREATE SEQUENCE sq_order
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE sq_order OWNER TO postgres;

--
-- Name: order; Type: TABLE; Schema: pom; Owner: postgres
--

CREATE TABLE "order" (
    orderno character(5) DEFAULT nextval('sq_order'::regclass) NOT NULL,
    customerid character(5) NOT NULL,
    adressid character(5) NOT NULL,
    contactid character(5) NOT NULL,
    product character varying(32) NOT NULL,
    price double precision NOT NULL,
    volume integer NOT NULL,
    state character varying(20) NOT NULL,
    baselotid character varying(20) NOT NULL,
    orderdate date NOT NULL,
    startdate date,
    releasedate date,
    completiondate date,
    duedate date,
    actualdeliverydate date,
    lotsize integer NOT NULL,
    priority integer,
    comment character varying(250)
);


ALTER TABLE "order" OWNER TO postgres;

--
-- Name: setup; Type: TABLE; Schema: pom; Owner: postgres
--

CREATE TABLE setup (
    attribute character(32) NOT NULL,
    value character varying(32) NOT NULL
);


ALTER TABLE setup OWNER TO postgres;

--
-- Name: sq_pk; Type: SEQUENCE; Schema: pom; Owner: postgres
--

CREATE SEQUENCE sq_pk
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE sq_pk OWNER TO postgres;

--
-- Name: sq_setup; Type: SEQUENCE; Schema: pom; Owner: postgres
--

CREATE SEQUENCE sq_setup
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE sq_setup OWNER TO postgres;

--
-- Name: address adress_pkey; Type: CONSTRAINT; Schema: pom; Owner: postgres
--

ALTER TABLE ONLY address
    ADD CONSTRAINT adress_pkey PRIMARY KEY (id);


--
-- Name: bankaccount bankaccount_pkey; Type: CONSTRAINT; Schema: pom; Owner: postgres
--

ALTER TABLE ONLY bankaccount
    ADD CONSTRAINT bankaccount_pkey PRIMARY KEY (id);


--
-- Name: order commission_pkey; Type: CONSTRAINT; Schema: pom; Owner: postgres
--

ALTER TABLE ONLY "order"
    ADD CONSTRAINT commission_pkey PRIMARY KEY (orderno);


--
-- Name: contact contact_pkey; Type: CONSTRAINT; Schema: pom; Owner: postgres
--

ALTER TABLE ONLY contact
    ADD CONSTRAINT contact_pkey PRIMARY KEY (id);


--
-- Name: customer customer_pkey; Type: CONSTRAINT; Schema: pom; Owner: postgres
--

ALTER TABLE ONLY customer
    ADD CONSTRAINT customer_pkey PRIMARY KEY (id);


--
-- Name: setup setup_pkey; Type: CONSTRAINT; Schema: pom; Owner: postgres
--

ALTER TABLE ONLY setup
    ADD CONSTRAINT setup_pkey PRIMARY KEY (attribute);


--
-- Name: fki_Fk_contact; Type: INDEX; Schema: pom; Owner: postgres
--

CREATE INDEX "fki_Fk_contact" ON "order" USING btree (contactid);


--
-- Name: fki_ordertoadress; Type: INDEX; Schema: pom; Owner: postgres
--

CREATE INDEX fki_ordertoadress ON "order" USING btree (adressid);


--
-- Name: order Fk_contact; Type: FK CONSTRAINT; Schema: pom; Owner: postgres
--

ALTER TABLE ONLY "order"
    ADD CONSTRAINT "Fk_contact" FOREIGN KEY (contactid) REFERENCES contact(id);


--
-- Name: address adress_customerid_fkey; Type: FK CONSTRAINT; Schema: pom; Owner: postgres
--

ALTER TABLE ONLY address
    ADD CONSTRAINT adress_customerid_fkey FOREIGN KEY (customerid) REFERENCES customer(id);


--
-- Name: bankaccount bankaccount_fk; Type: FK CONSTRAINT; Schema: pom; Owner: postgres
--

ALTER TABLE ONLY bankaccount
    ADD CONSTRAINT bankaccount_fk FOREIGN KEY (customerid) REFERENCES customer(id);


--
-- Name: order commission_customerid_fkey; Type: FK CONSTRAINT; Schema: pom; Owner: postgres
--

ALTER TABLE ONLY "order"
    ADD CONSTRAINT commission_customerid_fkey FOREIGN KEY (customerid) REFERENCES customer(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: order commission_fk; Type: FK CONSTRAINT; Schema: pom; Owner: postgres
--

ALTER TABLE ONLY "order"
    ADD CONSTRAINT commission_fk FOREIGN KEY (customerid) REFERENCES customer(id);


--
-- Name: contact contact_fk; Type: FK CONSTRAINT; Schema: pom; Owner: postgres
--

ALTER TABLE ONLY contact
    ADD CONSTRAINT contact_fk FOREIGN KEY (customerid) REFERENCES customer(id);


--
-- Name: order fk_ordertoadress; Type: FK CONSTRAINT; Schema: pom; Owner: postgres
--

ALTER TABLE ONLY "order"
    ADD CONSTRAINT fk_ordertoadress FOREIGN KEY (adressid) REFERENCES address(id);


--
-- PostgreSQL database dump complete
--

