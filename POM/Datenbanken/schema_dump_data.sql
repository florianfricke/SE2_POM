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

SET search_path = pom, pg_catalog;

--
-- Data for Name: customer; Type: TABLE DATA; Schema: pom; Owner: postgres
--

COPY customer (id, companyname, ranking, comment) FROM stdin;
4    	Hugo Boss	C 	
3    	Peter	A 	Maffay
5    	Lego AG	A 	
\.


--
-- Data for Name: address; Type: TABLE DATA; Schema: pom; Owner: postgres
--

COPY address (id, customerid, street, houseno, city, zipcode, country, billingaddress) FROM stdin;
1    	3    	Straße	12	Berlin	08197	Deutschland	f
2    	4    	Str	21	Hameln	00223		f
3    	5    	Wallstr. 	64	Simmern	01923	GERMANY	f
\.


--
-- Data for Name: bankaccount; Type: TABLE DATA; Schema: pom; Owner: postgres
--

COPY bankaccount (id, customerid, iban, bic, bankname) FROM stdin;
5    	4    	DE89389	GENIDEFF	BossBank
6    	4    	BO8398398	BOSSDEFF	Hagebau
4    	3    	98298398	23232323	VB
7    	3    	PM839898	MAPETOR389	MafyaBank
8    	5    	DE8983984	GENODEFF3	VB Simmern
\.


--
-- Data for Name: contact; Type: TABLE DATA; Schema: pom; Owner: postgres
--

COPY contact (id, customerid, phoneno, name, firstname, mailadress, "position") FROM stdin;
4    	4    		Boos	Hogu		
5    	4    		Klaien 	Peter		
1    	3    	234324	Maffay	Peter	p@m.de	CEO
2    	3    		Glas	Uschi		Sekretärin
6    	5    	00192029	Lego	Jakob	j.lego@lego.de	CEO
\.


--
-- Name: customer_sq_pk; Type: SEQUENCE SET; Schema: pom; Owner: postgres
--

SELECT pg_catalog.setval('customer_sq_pk', 1, true);


--
-- Data for Name: order; Type: TABLE DATA; Schema: pom; Owner: postgres
--

COPY "order" (orderno, customerid, adressid, contactid, product, price, volume, state, baselotid, orderdate, startdate, releasedate, completiondate, duedate, actualdeliverydate, lotsize, priority, comment) FROM stdin;
5    	4    	2    	4    	2009MF	1.45	204	PLANNED		2017-06-13	2017-06-14	2017-06-14	\N	2017-06-28	\N	10	3	
8    	5    	3    	6    	A100	1.3400000000000001	200	PLANNED	SAG	2017-06-13	2017-06-20	2017-06-13	\N	2017-07-05	\N	13	3	Steine
6    	3    	1    	1    	2017DB	2.5600000000000001	500	PLANNED	Bango	2017-06-13	2017-06-13	2017-06-13	\N	2017-06-27	\N	10	5	
\.


--
-- Data for Name: setup; Type: TABLE DATA; Schema: pom; Owner: postgres
--

COPY setup (attribute, value) FROM stdin;
DAYCAPACITY                     	10
\.


--
-- Name: sq_address; Type: SEQUENCE SET; Schema: pom; Owner: postgres
--

SELECT pg_catalog.setval('sq_address', 3, true);


--
-- Name: sq_bankaccount; Type: SEQUENCE SET; Schema: pom; Owner: postgres
--

SELECT pg_catalog.setval('sq_bankaccount', 8, true);


--
-- Name: sq_contact; Type: SEQUENCE SET; Schema: pom; Owner: postgres
--

SELECT pg_catalog.setval('sq_contact', 6, true);


--
-- Name: sq_customer; Type: SEQUENCE SET; Schema: pom; Owner: postgres
--

SELECT pg_catalog.setval('sq_customer', 5, true);


--
-- Name: sq_order; Type: SEQUENCE SET; Schema: pom; Owner: postgres
--

SELECT pg_catalog.setval('sq_order', 8, true);


--
-- Name: sq_pk; Type: SEQUENCE SET; Schema: pom; Owner: postgres
--

SELECT pg_catalog.setval('sq_pk', 30, true);


--
-- Name: sq_setup; Type: SEQUENCE SET; Schema: pom; Owner: postgres
--

SELECT pg_catalog.setval('sq_setup', 1, false);


--
-- PostgreSQL database dump complete
--

