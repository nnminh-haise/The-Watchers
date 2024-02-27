--
-- PostgreSQL database dump
--

-- Dumped from database version 15.5
-- Dumped by pg_dump version 16.0

-- Started on 2024-02-27 20:14:24 +07

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
-- TOC entry 5 (class 2615 OID 2200)
-- Name: public; Type: SCHEMA; Schema: -; Owner: pg_database_owner
--

CREATE SCHEMA public;


ALTER SCHEMA public OWNER TO pg_database_owner;

--
-- TOC entry 3725 (class 0 OID 0)
-- Dependencies: 5
-- Name: SCHEMA public; Type: COMMENT; Schema: -; Owner: pg_database_owner
--

COMMENT ON SCHEMA public IS 'standard public schema';


SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 215 (class 1259 OID 16680)
-- Name: account; Type: TABLE; Schema: public; Owner: nnminh_dev
--

CREATE TABLE public.account (
    id uuid DEFAULT public.uuid_generate_v4() NOT NULL,
    email text NOT NULL,
    password text NOT NULL,
    is_deleted boolean DEFAULT false NOT NULL,
    CONSTRAINT watch_selling_is_deleted_check CHECK ((is_deleted = ANY (ARRAY[true, false])))
);


ALTER TABLE public.account OWNER TO nnminh_dev;

--
-- TOC entry 223 (class 1259 OID 16897)
-- Name: cart; Type: TABLE; Schema: public; Owner: nnminh_dev
--

CREATE TABLE public.cart (
    id uuid DEFAULT public.uuid_generate_v4() NOT NULL,
    account_id uuid,
    is_deleted boolean DEFAULT false
);


ALTER TABLE public.cart OWNER TO nnminh_dev;

--
-- TOC entry 224 (class 1259 OID 16905)
-- Name: cart_detail; Type: TABLE; Schema: public; Owner: nnminh_dev
--

CREATE TABLE public.cart_detail (
    id uuid DEFAULT public.uuid_generate_v4() NOT NULL,
    cart_id uuid NOT NULL,
    watch_id uuid NOT NULL,
    price double precision NOT NULL,
    quantity integer NOT NULL
);


ALTER TABLE public.cart_detail OWNER TO nnminh_dev;

--
-- TOC entry 216 (class 1259 OID 16709)
-- Name: customer; Type: TABLE; Schema: public; Owner: nnminh_dev
--

CREATE TABLE public.customer (
    id uuid DEFAULT public.uuid_generate_v4() NOT NULL,
    citizen_id text NOT NULL,
    first_name text NOT NULL,
    last_name text NOT NULL,
    phone_number text NOT NULL,
    email text NOT NULL,
    gender text NOT NULL,
    date_of_birth timestamp(6) without time zone NOT NULL,
    address text NOT NULL,
    tax_code text NOT NULL,
    is_deleted boolean DEFAULT false,
    account_id uuid NOT NULL,
    photo text,
    CONSTRAINT watch_selling_cusomters_date_of_birth_check CHECK ((date_of_birth < now())),
    CONSTRAINT watch_selling_cusomters_gender_check CHECK ((gender = ANY (ARRAY['Nam'::text, 'Nữ'::text])))
);


ALTER TABLE public.customer OWNER TO nnminh_dev;

--
-- TOC entry 221 (class 1259 OID 16772)
-- Name: invoice; Type: TABLE; Schema: public; Owner: nnminh_dev
--

CREATE TABLE public.invoice (
    id uuid DEFAULT public.uuid_generate_v4() NOT NULL,
    create_date date NOT NULL,
    total double precision NOT NULL,
    tax_code text NOT NULL,
    is_deleted boolean DEFAULT false,
    order_id uuid NOT NULL,
    CONSTRAINT watch_selling_invoices_total_check CHECK ((total >= (0)::double precision))
);


ALTER TABLE public.invoice OWNER TO nnminh_dev;

--
-- TOC entry 222 (class 1259 OID 16866)
-- Name: order_detail; Type: TABLE; Schema: public; Owner: nnminh_dev
--

CREATE TABLE public.order_detail (
    id uuid DEFAULT public.uuid_generate_v4() NOT NULL,
    orders_id uuid NOT NULL,
    watch_id uuid NOT NULL,
    price double precision NOT NULL,
    quantity integer NOT NULL,
    is_deleted boolean DEFAULT false,
    CONSTRAINT watch_selling_order_details_price_check CHECK ((price >= (0)::double precision)),
    CONSTRAINT watch_selling_order_details_quantity_check CHECK ((quantity >= 0))
);


ALTER TABLE public.order_detail OWNER TO nnminh_dev;

--
-- TOC entry 220 (class 1259 OID 16762)
-- Name: orders; Type: TABLE; Schema: public; Owner: nnminh_dev
--

CREATE TABLE public.orders (
    id uuid DEFAULT public.uuid_generate_v4() NOT NULL,
    order_date timestamp(6) without time zone NOT NULL,
    name text NOT NULL,
    address text NOT NULL,
    phonenumber text NOT NULL,
    status text DEFAULT 'Chờ duyệt'::text,
    is_deleted boolean DEFAULT false,
    account_id uuid NOT NULL,
    delivery_date timestamp(6) without time zone NOT NULL
);


ALTER TABLE public.orders OWNER TO nnminh_dev;

--
-- TOC entry 217 (class 1259 OID 16728)
-- Name: watch; Type: TABLE; Schema: public; Owner: nnminh_dev
--

CREATE TABLE public.watch (
    id uuid DEFAULT public.uuid_generate_v4() NOT NULL,
    name text NOT NULL,
    price double precision NOT NULL,
    quantity integer NOT NULL,
    description text,
    status text NOT NULL,
    photo text,
    is_deleted boolean DEFAULT false,
    type_id uuid NOT NULL,
    brand_id uuid NOT NULL,
    CONSTRAINT watch_selling_price_check CHECK ((price >= (0)::double precision)),
    CONSTRAINT watch_selling_quantity_check CHECK ((quantity >= 0)),
    CONSTRAINT watch_selling_status_check CHECK ((status = ANY (ARRAY['Đang kinh doanh'::text, 'Ngừng kinh doanh'::text])))
);


ALTER TABLE public.watch OWNER TO nnminh_dev;

--
-- TOC entry 219 (class 1259 OID 16752)
-- Name: watch_brand; Type: TABLE; Schema: public; Owner: nnminh_dev
--

CREATE TABLE public.watch_brand (
    id uuid DEFAULT public.uuid_generate_v4() NOT NULL,
    name text NOT NULL,
    is_deleted boolean DEFAULT false
);


ALTER TABLE public.watch_brand OWNER TO nnminh_dev;

--
-- TOC entry 218 (class 1259 OID 16742)
-- Name: watch_type; Type: TABLE; Schema: public; Owner: nnminh_dev
--

CREATE TABLE public.watch_type (
    id uuid DEFAULT public.uuid_generate_v4() NOT NULL,
    name text NOT NULL,
    is_deleted boolean DEFAULT false
);


ALTER TABLE public.watch_type OWNER TO nnminh_dev;

--
-- TOC entry 3710 (class 0 OID 16680)
-- Dependencies: 215
-- Data for Name: account; Type: TABLE DATA; Schema: public; Owner: nnminh_dev
--

COPY public.account (id, email, password, is_deleted) FROM stdin;
8bf2662c-8199-41ab-9173-cf434bea35d4	bbbbbbbb@gmail.com	$2a$10$FhG3AEfWeogiC0xjCocfSeX9e0UnbzTI8OZhsgNQfcqzDBDbzOQoa	f
f17af1ab-5bad-4a42-8024-cc61f4ac99b3	aaaaaaaa@gmail.com	$2a$10$91rhQs8E91Da3BRVtnD1F.Dbb2gnENtjOld/mtgmwft99Df6BHUxa	f
8e79ffd3-ca7f-4728-9132-52a8a1d2df1e	cccccccc@gmail.com	$2a$10$9coLdx7aFrwEfQMcA16If.g2H1BOUgrNgGZt9xhU.q3iJ0tlTOdJa	f
1f0caa1d-7da9-4d63-b112-530be269eb22	nnminh.sam.1803@gmail.com	$2a$10$cqabHBLd6In8Y7.kUsebSuFFrm53shuBBnNP6oHVIoHnA.KbGzide	f
446371ce-d249-4d38-85d4-14ae9507ac2c	ddddddd@gmail.com	$2a$10$s2jXAjPUU0vsvxyNbDzUYeyhOgfcaHk/SqGRalmnRTRx82hQN87OC	f
\.


--
-- TOC entry 3718 (class 0 OID 16897)
-- Dependencies: 223
-- Data for Name: cart; Type: TABLE DATA; Schema: public; Owner: nnminh_dev
--

COPY public.cart (id, account_id, is_deleted) FROM stdin;
d5f0c5fa-201c-42b2-b2c5-c0774ab94897	8bf2662c-8199-41ab-9173-cf434bea35d4	f
3541317f-5c60-4556-bdd8-907708c67313	8e79ffd3-ca7f-4728-9132-52a8a1d2df1e	f
bfe405df-f76d-4128-8780-dfaa9d149fb6	446371ce-d249-4d38-85d4-14ae9507ac2c	f
\.


--
-- TOC entry 3719 (class 0 OID 16905)
-- Dependencies: 224
-- Data for Name: cart_detail; Type: TABLE DATA; Schema: public; Owner: nnminh_dev
--

COPY public.cart_detail (id, cart_id, watch_id, price, quantity) FROM stdin;
\.


--
-- TOC entry 3711 (class 0 OID 16709)
-- Dependencies: 216
-- Data for Name: customer; Type: TABLE DATA; Schema: public; Owner: nnminh_dev
--

COPY public.customer (id, citizen_id, first_name, last_name, phone_number, email, gender, date_of_birth, address, tax_code, is_deleted, account_id, photo) FROM stdin;
9aba1be0-f288-41dd-bcf4-c8f0f417793f	BBBBBBBBBB	Nguyễn Văn	B	BBBBBBBBBB	bbbbbbbb@gmail.com	Nam	2022-12-12 00:00:00	B đường B	BBBBBBBBBB	f	8bf2662c-8199-41ab-9173-cf434bea35d4	
\.


--
-- TOC entry 3716 (class 0 OID 16772)
-- Dependencies: 221
-- Data for Name: invoice; Type: TABLE DATA; Schema: public; Owner: nnminh_dev
--

COPY public.invoice (id, create_date, total, tax_code, is_deleted, order_id) FROM stdin;
\.


--
-- TOC entry 3717 (class 0 OID 16866)
-- Dependencies: 222
-- Data for Name: order_detail; Type: TABLE DATA; Schema: public; Owner: nnminh_dev
--

COPY public.order_detail (id, orders_id, watch_id, price, quantity, is_deleted) FROM stdin;
\.


--
-- TOC entry 3715 (class 0 OID 16762)
-- Dependencies: 220
-- Data for Name: orders; Type: TABLE DATA; Schema: public; Owner: nnminh_dev
--

COPY public.orders (id, order_date, name, address, phonenumber, status, is_deleted, account_id, delivery_date) FROM stdin;
05b2d9f6-4936-4ab9-b5d2-95f5b23cf391	2024-02-22 00:00:00	Nguyễn Nhật Minh	97 Man Thiện	0123456789	Chờ duyệt	f	1f0caa1d-7da9-4d63-b112-530be269eb22	2024-02-24 00:00:00
5740d430-3438-426e-be4b-0803b0f53808	2024-02-22 00:00:00	Mai Thuỵ Quỳnh Giang	97 Man Thiện	0123456789	Chờ duyệt	f	1f0caa1d-7da9-4d63-b112-530be269eb22	2024-02-24 00:00:00
c5554849-b809-4d73-9266-d8cf33a515bf	2024-02-25 00:00:00	Lê Hoàng Khang	691 Lê Văn Việt	0987654321	Chờ duyệt	t	1f0caa1d-7da9-4d63-b112-530be269eb22	2024-03-01 00:00:00
\.


--
-- TOC entry 3712 (class 0 OID 16728)
-- Dependencies: 217
-- Data for Name: watch; Type: TABLE DATA; Schema: public; Owner: nnminh_dev
--

COPY public.watch (id, name, price, quantity, description, status, photo, is_deleted, type_id, brand_id) FROM stdin;
3429528d-b712-41ea-9913-440b673f014a	W3	1234.567	1234567	Đây là mô tả của đồng hồ W3	Đang kinh doanh	\N	f	09a281ea-e587-4e0c-a54e-2a90275b8ca5	76ca2cc2-e83e-4acd-b29b-91652e39e656
cb148510-2212-4c79-b7fb-37d16b065042	W4	12345.678	12345678	Đây là mô tả của đồng hồ W4	Đang kinh doanh	\N	f	09a281ea-e587-4e0c-a54e-2a90275b8ca5	76ca2cc2-e83e-4acd-b29b-91652e39e656
95a80a3a-deeb-4d37-b992-634b60c2234a	Watch 5	123.45	123456	Đây là mô tả của đồng hồ W5	Ngừng kinh doanh	\N	f	499c4189-7a9b-4671-a2de-4079a7cebfd8	d2b75755-4868-49b0-9de8-81fb6bf31f42
c337b159-d241-41f9-8209-6ef07f0c1e8d	W1	123.456	123	Đây là mô tả của đồng hồ W1	Đang kinh doanh	\N	f	499c4189-7a9b-4671-a2de-4079a7cebfd8	d2b75755-4868-49b0-9de8-81fb6bf31f42
98e24d66-4cb5-4786-bf94-080333f15530	W2	123.45	123456	Đây là mô tả của đồng hồ W2	Đang kinh doanh	\N	f	499c4189-7a9b-4671-a2de-4079a7cebfd8	d2b75755-4868-49b0-9de8-81fb6bf31f42
\.


--
-- TOC entry 3714 (class 0 OID 16752)
-- Dependencies: 219
-- Data for Name: watch_brand; Type: TABLE DATA; Schema: public; Owner: nnminh_dev
--

COPY public.watch_brand (id, name, is_deleted) FROM stdin;
d2b75755-4868-49b0-9de8-81fb6bf31f42	B1	f
76ca2cc2-e83e-4acd-b29b-91652e39e656	B2	f
2a2ed287-a6a5-40f5-9639-bba91a8f9ef8	B3	f
72060d0e-3200-4c80-87ce-26cd4e259f1b	B5	f
7f11a5ea-df91-40f8-94d7-c11fd719cf76	B10	f
\.


--
-- TOC entry 3713 (class 0 OID 16742)
-- Dependencies: 218
-- Data for Name: watch_type; Type: TABLE DATA; Schema: public; Owner: nnminh_dev
--

COPY public.watch_type (id, name, is_deleted) FROM stdin;
499c4189-7a9b-4671-a2de-4079a7cebfd8	T1	f
09a281ea-e587-4e0c-a54e-2a90275b8ca5	T2	f
e062be03-f444-49ea-9530-d8486f83baeb	T3	t
\.


--
-- TOC entry 3555 (class 2606 OID 16910)
-- Name: cart_detail cart_details_pkey; Type: CONSTRAINT; Schema: public; Owner: nnminh_dev
--

ALTER TABLE ONLY public.cart_detail
    ADD CONSTRAINT cart_details_pkey PRIMARY KEY (id);


--
-- TOC entry 3551 (class 2606 OID 16902)
-- Name: cart carts_pkey; Type: CONSTRAINT; Schema: public; Owner: nnminh_dev
--

ALTER TABLE ONLY public.cart
    ADD CONSTRAINT carts_pkey PRIMARY KEY (id);


--
-- TOC entry 3521 (class 2606 OID 16723)
-- Name: customer wach_selling_customers_citizen_id_unique; Type: CONSTRAINT; Schema: public; Owner: nnminh_dev
--

ALTER TABLE ONLY public.customer
    ADD CONSTRAINT wach_selling_customers_citizen_id_unique UNIQUE (citizen_id);


--
-- TOC entry 3523 (class 2606 OID 16721)
-- Name: customer wach_selling_customers_email_unique; Type: CONSTRAINT; Schema: public; Owner: nnminh_dev
--

ALTER TABLE ONLY public.customer
    ADD CONSTRAINT wach_selling_customers_email_unique UNIQUE (email);


--
-- TOC entry 3525 (class 2606 OID 16725)
-- Name: customer wach_selling_customers_phonenumber_unique; Type: CONSTRAINT; Schema: public; Owner: nnminh_dev
--

ALTER TABLE ONLY public.customer
    ADD CONSTRAINT wach_selling_customers_phonenumber_unique UNIQUE (phone_number);


--
-- TOC entry 3527 (class 2606 OID 16727)
-- Name: customer wach_selling_customers_tax_code_unique; Type: CONSTRAINT; Schema: public; Owner: nnminh_dev
--

ALTER TABLE ONLY public.customer
    ADD CONSTRAINT wach_selling_customers_tax_code_unique UNIQUE (tax_code);


--
-- TOC entry 3531 (class 2606 OID 16739)
-- Name: watch wach_selling_wacthes_primarykey; Type: CONSTRAINT; Schema: public; Owner: nnminh_dev
--

ALTER TABLE ONLY public.watch
    ADD CONSTRAINT wach_selling_wacthes_primarykey PRIMARY KEY (id);


--
-- TOC entry 3535 (class 2606 OID 16751)
-- Name: watch_type wach_selling_watch_types_name_unique; Type: CONSTRAINT; Schema: public; Owner: nnminh_dev
--

ALTER TABLE ONLY public.watch_type
    ADD CONSTRAINT wach_selling_watch_types_name_unique UNIQUE (name);


--
-- TOC entry 3557 (class 2606 OID 16912)
-- Name: cart_detail watch_selling_cart_detail_watch_and_cart_unique; Type: CONSTRAINT; Schema: public; Owner: nnminh_dev
--

ALTER TABLE ONLY public.cart_detail
    ADD CONSTRAINT watch_selling_cart_detail_watch_and_cart_unique UNIQUE (cart_id) INCLUDE (watch_id);


--
-- TOC entry 3553 (class 2606 OID 16904)
-- Name: cart watch_selling_carts_account_id_unique; Type: CONSTRAINT; Schema: public; Owner: nnminh_dev
--

ALTER TABLE ONLY public.cart
    ADD CONSTRAINT watch_selling_carts_account_id_unique UNIQUE (account_id);


--
-- TOC entry 3517 (class 2606 OID 16688)
-- Name: account watch_selling_customer_account_PK; Type: CONSTRAINT; Schema: public; Owner: nnminh_dev
--

ALTER TABLE ONLY public.account
    ADD CONSTRAINT "watch_selling_customer_account_PK" PRIMARY KEY (id);


--
-- TOC entry 3529 (class 2606 OID 16719)
-- Name: customer watch_selling_customers_primarykey; Type: CONSTRAINT; Schema: public; Owner: nnminh_dev
--

ALTER TABLE ONLY public.customer
    ADD CONSTRAINT watch_selling_customers_primarykey PRIMARY KEY (id);


--
-- TOC entry 3519 (class 2606 OID 16690)
-- Name: account watch_selling_email_unique; Type: CONSTRAINT; Schema: public; Owner: nnminh_dev
--

ALTER TABLE ONLY public.account
    ADD CONSTRAINT watch_selling_email_unique UNIQUE (email);


--
-- TOC entry 3545 (class 2606 OID 16780)
-- Name: invoice watch_selling_invoices_primarykey; Type: CONSTRAINT; Schema: public; Owner: nnminh_dev
--

ALTER TABLE ONLY public.invoice
    ADD CONSTRAINT watch_selling_invoices_primarykey PRIMARY KEY (id);


--
-- TOC entry 3533 (class 2606 OID 16741)
-- Name: watch watch_selling_name_unique; Type: CONSTRAINT; Schema: public; Owner: nnminh_dev
--

ALTER TABLE ONLY public.watch
    ADD CONSTRAINT watch_selling_name_unique UNIQUE (name);


--
-- TOC entry 3547 (class 2606 OID 16876)
-- Name: order_detail watch_selling_order_details_order_and_watch_unique; Type: CONSTRAINT; Schema: public; Owner: nnminh_dev
--

ALTER TABLE ONLY public.order_detail
    ADD CONSTRAINT watch_selling_order_details_order_and_watch_unique UNIQUE (orders_id) INCLUDE (watch_id);


--
-- TOC entry 3511 (class 2606 OID 17046)
-- Name: orders watch_selling_orders_check_valid_dates; Type: CHECK CONSTRAINT; Schema: public; Owner: nnminh_dev
--

ALTER TABLE public.orders
    ADD CONSTRAINT watch_selling_orders_check_valid_dates CHECK ((order_date <= delivery_date)) NOT VALID;


--
-- TOC entry 3549 (class 2606 OID 16874)
-- Name: order_detail watch_selling_orders_details_primarykey; Type: CONSTRAINT; Schema: public; Owner: nnminh_dev
--

ALTER TABLE ONLY public.order_detail
    ADD CONSTRAINT watch_selling_orders_details_primarykey PRIMARY KEY (id);


--
-- TOC entry 3543 (class 2606 OID 16771)
-- Name: orders watch_selling_orders_primarykey; Type: CONSTRAINT; Schema: public; Owner: nnminh_dev
--

ALTER TABLE ONLY public.orders
    ADD CONSTRAINT watch_selling_orders_primarykey PRIMARY KEY (id);


--
-- TOC entry 3512 (class 2606 OID 17010)
-- Name: orders watch_selling_orders_status_check; Type: CHECK CONSTRAINT; Schema: public; Owner: nnminh_dev
--

ALTER TABLE public.orders
    ADD CONSTRAINT watch_selling_orders_status_check CHECK ((status = ANY (ARRAY['Chờ duyệt'::text, 'Đang giao hàng'::text, 'Hoàn tất'::text, 'Đã huỷ'::text]))) NOT VALID;


--
-- TOC entry 3539 (class 2606 OID 16761)
-- Name: watch_brand watch_selling_watch_brands_name_unique; Type: CONSTRAINT; Schema: public; Owner: nnminh_dev
--

ALTER TABLE ONLY public.watch_brand
    ADD CONSTRAINT watch_selling_watch_brands_name_unique UNIQUE (name);


--
-- TOC entry 3541 (class 2606 OID 16759)
-- Name: watch_brand watch_selling_watch_brands_primarykey; Type: CONSTRAINT; Schema: public; Owner: nnminh_dev
--

ALTER TABLE ONLY public.watch_brand
    ADD CONSTRAINT watch_selling_watch_brands_primarykey PRIMARY KEY (id);


--
-- TOC entry 3537 (class 2606 OID 16749)
-- Name: watch_type watch_selling_watch_types_primarykey; Type: CONSTRAINT; Schema: public; Owner: nnminh_dev
--

ALTER TABLE ONLY public.watch_type
    ADD CONSTRAINT watch_selling_watch_types_primarykey PRIMARY KEY (id);


--
-- TOC entry 3561 (class 2606 OID 16985)
-- Name: orders Watch_selling_Order_account_FK; Type: FK CONSTRAINT; Schema: public; Owner: nnminh_dev
--

ALTER TABLE ONLY public.orders
    ADD CONSTRAINT "Watch_selling_Order_account_FK" FOREIGN KEY (account_id) REFERENCES public.account(id) ON UPDATE CASCADE NOT VALID;


--
-- TOC entry 3565 (class 2606 OID 16968)
-- Name: cart watch_cart_account_id_FK; Type: FK CONSTRAINT; Schema: public; Owner: nnminh_dev
--

ALTER TABLE ONLY public.cart
    ADD CONSTRAINT "watch_cart_account_id_FK" FOREIGN KEY (account_id) REFERENCES public.account(id) ON UPDATE CASCADE NOT VALID;


--
-- TOC entry 3566 (class 2606 OID 16918)
-- Name: cart_detail watch_selling_cart_details_cart_id_FK; Type: FK CONSTRAINT; Schema: public; Owner: nnminh_dev
--

ALTER TABLE ONLY public.cart_detail
    ADD CONSTRAINT "watch_selling_cart_details_cart_id_FK" FOREIGN KEY (cart_id) REFERENCES public.cart(id) ON UPDATE CASCADE;


--
-- TOC entry 3567 (class 2606 OID 16913)
-- Name: cart_detail watch_selling_cart_details_watch_id_FK; Type: FK CONSTRAINT; Schema: public; Owner: nnminh_dev
--

ALTER TABLE ONLY public.cart_detail
    ADD CONSTRAINT "watch_selling_cart_details_watch_id_FK" FOREIGN KEY (watch_id) REFERENCES public.watch(id) ON UPDATE CASCADE;


--
-- TOC entry 3558 (class 2606 OID 16887)
-- Name: customer watch_selling_customers_account_FK; Type: FK CONSTRAINT; Schema: public; Owner: nnminh_dev
--

ALTER TABLE ONLY public.customer
    ADD CONSTRAINT "watch_selling_customers_account_FK" FOREIGN KEY (account_id) REFERENCES public.account(id) ON UPDATE CASCADE NOT VALID;


--
-- TOC entry 3562 (class 2606 OID 16892)
-- Name: invoice watch_selling_invoices_order_FK; Type: FK CONSTRAINT; Schema: public; Owner: nnminh_dev
--

ALTER TABLE ONLY public.invoice
    ADD CONSTRAINT "watch_selling_invoices_order_FK" FOREIGN KEY (order_id) REFERENCES public.orders(id) ON UPDATE CASCADE NOT VALID;


--
-- TOC entry 3563 (class 2606 OID 16877)
-- Name: order_detail watch_selling_order_details_order_FK; Type: FK CONSTRAINT; Schema: public; Owner: nnminh_dev
--

ALTER TABLE ONLY public.order_detail
    ADD CONSTRAINT "watch_selling_order_details_order_FK" FOREIGN KEY (orders_id) REFERENCES public.orders(id) ON UPDATE CASCADE;


--
-- TOC entry 3564 (class 2606 OID 16882)
-- Name: order_detail watch_selling_order_details_watch_FK; Type: FK CONSTRAINT; Schema: public; Owner: nnminh_dev
--

ALTER TABLE ONLY public.order_detail
    ADD CONSTRAINT "watch_selling_order_details_watch_FK" FOREIGN KEY (watch_id) REFERENCES public.watch(id) ON UPDATE CASCADE;


--
-- TOC entry 3559 (class 2606 OID 16812)
-- Name: watch watch_selling_watches_brand_FK; Type: FK CONSTRAINT; Schema: public; Owner: nnminh_dev
--

ALTER TABLE ONLY public.watch
    ADD CONSTRAINT "watch_selling_watches_brand_FK" FOREIGN KEY (brand_id) REFERENCES public.watch_brand(id) ON UPDATE CASCADE NOT VALID;


--
-- TOC entry 3560 (class 2606 OID 16807)
-- Name: watch watch_selling_watches_type_FK; Type: FK CONSTRAINT; Schema: public; Owner: nnminh_dev
--

ALTER TABLE ONLY public.watch
    ADD CONSTRAINT "watch_selling_watches_type_FK" FOREIGN KEY (type_id) REFERENCES public.watch_type(id) ON UPDATE CASCADE NOT VALID;


-- Completed on 2024-02-27 20:14:25 +07

--
-- PostgreSQL database dump complete
--

