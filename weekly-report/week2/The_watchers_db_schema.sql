--
-- PostgreSQL database dump
--

-- Dumped from database version 15.5
-- Dumped by pg_dump version 16.0

-- Started on 2024-01-31 09:41:06 +07

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
-- TOC entry 223 (class 1259 OID 16582)
-- Name: ct_cungcap; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.ct_cungcap (
    ma_ncc uuid NOT NULL,
    ma_dongho uuid NOT NULL,
    da_xoa boolean DEFAULT false NOT NULL
);


ALTER TABLE public.ct_cungcap OWNER TO postgres;

--
-- TOC entry 224 (class 1259 OID 16598)
-- Name: ct_don_dat_hang; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.ct_don_dat_hang (
    ma_ddh uuid NOT NULL,
    ma_dongho uuid NOT NULL,
    soluong bigint NOT NULL,
    gia double precision NOT NULL,
    da_xoa boolean DEFAULT false NOT NULL,
    CONSTRAINT ct_ddh_gia_check CHECK ((gia > (0)::double precision)),
    CONSTRAINT ct_ddh_soluong_check CHECK ((soluong > 0))
);


ALTER TABLE public.ct_don_dat_hang OWNER TO postgres;

--
-- TOC entry 226 (class 1259 OID 16628)
-- Name: ct_phieu_dat; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.ct_phieu_dat (
    ma_phieudat uuid NOT NULL,
    ma_dongho uuid NOT NULL,
    soluong bigint NOT NULL,
    gia double precision NOT NULL,
    da_xoa boolean DEFAULT false NOT NULL,
    CONSTRAINT ct_phieudat_gia_check CHECK ((gia > (0)::double precision)),
    CONSTRAINT ct_phieudat_soluong_check CHECK ((soluong > 0))
);


ALTER TABLE public.ct_phieu_dat OWNER TO postgres;

--
-- TOC entry 227 (class 1259 OID 16701)
-- Name: customer_account; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.customer_account (
    id uuid DEFAULT public.uuid_generate_v4() NOT NULL,
    email text NOT NULL,
    password text NOT NULL,
    delete_status boolean DEFAULT false NOT NULL,
    CONSTRAINT "check delete status" CHECK ((delete_status = ANY (ARRAY[true, false])))
);


ALTER TABLE public.customer_account OWNER TO postgres;

--
-- TOC entry 221 (class 1259 OID 16489)
-- Name: don_dat_hang; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.don_dat_hang (
    ma_ddh uuid DEFAULT public.uuid_generate_v4() NOT NULL,
    ngay_dathang date NOT NULL,
    ma_ncc uuid NOT NULL,
    da_xoa boolean DEFAULT false NOT NULL
);


ALTER TABLE public.don_dat_hang OWNER TO postgres;

--
-- TOC entry 219 (class 1259 OID 16454)
-- Name: dong_ho; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.dong_ho (
    madh uuid DEFAULT public.uuid_generate_v4() NOT NULL,
    tendh text NOT NULL,
    gia double precision NOT NULL,
    sl_ton bigint NOT NULL,
    mota text NOT NULL,
    trangthai text NOT NULL,
    hinhanh text NOT NULL,
    maloai uuid NOT NULL,
    mahang uuid NOT NULL,
    da_xoa boolean DEFAULT false NOT NULL,
    CONSTRAINT gia_check CHECK ((gia > (0)::double precision)),
    CONSTRAINT sl_ton_check CHECK ((sl_ton > 0))
);


ALTER TABLE public.dong_ho OWNER TO postgres;

--
-- TOC entry 218 (class 1259 OID 16439)
-- Name: hang_dong_ho; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.hang_dong_ho (
    ma_hang uuid DEFAULT public.uuid_generate_v4() NOT NULL,
    ten_hang text NOT NULL
);


ALTER TABLE public.hang_dong_ho OWNER TO postgres;

--
-- TOC entry 222 (class 1259 OID 16500)
-- Name: hoa_don; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.hoa_don (
    so_hoadon uuid DEFAULT public.uuid_generate_v4() NOT NULL,
    ngay_in date NOT NULL,
    tong_tien bigint NOT NULL,
    maso_thue text NOT NULL,
    ma_phieudat uuid NOT NULL,
    da_xoa boolean DEFAULT false NOT NULL,
    CONSTRAINT tongtien_check CHECK ((tong_tien > 0))
);


ALTER TABLE public.hoa_don OWNER TO postgres;

--
-- TOC entry 216 (class 1259 OID 16418)
-- Name: khach_hang; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.khach_hang (
    makh uuid DEFAULT public.uuid_generate_v4() NOT NULL,
    cmnd text NOT NULL,
    ho text NOT NULL,
    ten text NOT NULL,
    gioitinh text NOT NULL,
    ngaysinh timestamp(6) without time zone NOT NULL,
    diachi text NOT NULL,
    sdt text NOT NULL,
    email text NOT NULL,
    masothue text NOT NULL,
    da_xoa boolean DEFAULT false NOT NULL,
    account_id uuid NOT NULL,
    hinhanh text,
    CONSTRAINT gioitinh_check CHECK ((gioitinh = ANY (ARRAY['Nam'::text, 'Nữ'::text])))
);


ALTER TABLE public.khach_hang OWNER TO postgres;

--
-- TOC entry 217 (class 1259 OID 16430)
-- Name: loai_dong_ho; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.loai_dong_ho (
    ma_loai uuid DEFAULT public.uuid_generate_v4() NOT NULL,
    ten_loai text NOT NULL
);


ALTER TABLE public.loai_dong_ho OWNER TO postgres;

--
-- TOC entry 220 (class 1259 OID 16475)
-- Name: nha_cung_cap; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.nha_cung_cap (
    ma_ncc uuid DEFAULT public.uuid_generate_v4() NOT NULL,
    ten_ncc text NOT NULL,
    diachi text NOT NULL,
    email text NOT NULL,
    sdt text NOT NULL,
    da_xoa boolean DEFAULT false NOT NULL
);


ALTER TABLE public.nha_cung_cap OWNER TO postgres;

--
-- TOC entry 225 (class 1259 OID 16615)
-- Name: phieu_dat; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.phieu_dat (
    ma_phieudat uuid DEFAULT public.uuid_generate_v4() NOT NULL,
    ngaydat date NOT NULL,
    hoten_nguoi_nhan text NOT NULL,
    diachi_nguoi_nhan text NOT NULL,
    sdt_nguoi_nhan text NOT NULL,
    ngay_giaohang date NOT NULL,
    gio_giaohang time without time zone NOT NULL,
    trangthai text NOT NULL,
    ma_kh uuid NOT NULL,
    da_xoa boolean DEFAULT false NOT NULL
);


ALTER TABLE public.phieu_dat OWNER TO postgres;

--
-- TOC entry 4496 (class 0 OID 16582)
-- Dependencies: 223
-- Data for Name: ct_cungcap; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.ct_cungcap (ma_ncc, ma_dongho, da_xoa) FROM stdin;
\.


--
-- TOC entry 4497 (class 0 OID 16598)
-- Dependencies: 224
-- Data for Name: ct_don_dat_hang; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.ct_don_dat_hang (ma_ddh, ma_dongho, soluong, gia, da_xoa) FROM stdin;
\.


--
-- TOC entry 4499 (class 0 OID 16628)
-- Dependencies: 226
-- Data for Name: ct_phieu_dat; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.ct_phieu_dat (ma_phieudat, ma_dongho, soluong, gia, da_xoa) FROM stdin;
\.


--
-- TOC entry 4500 (class 0 OID 16701)
-- Dependencies: 227
-- Data for Name: customer_account; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.customer_account (id, email, password, delete_status) FROM stdin;
d5bab05c-0cba-4f6d-9c44-9e776ce816e0	nnminh.anne.183@gmail.com	$2a$10$H7BO2LDdqt47TNBsQQPPTevEqPUH4lW2MNZG7Ic5DEiVBXK7/Iqvy	f
26d010a0-844a-4e78-8561-93c167922b1a	nnminh.sam.183@gmail.com	$2a$10$1deOpCOPjPZJIXT35DOVKeJHSg8ogKsp5rIhFkKOVbPE2cF/BNspm	f
5055d41a-abda-429d-9b92-1bfb95d1f793	nnminh.sam.1803@gmail.com	$2a$10$nSNBkWVXtLKxESU2kokMdOcsm5ttCq0tWAZwl8.txG7LDKcYYkSi.	f
b15ab076-696d-45df-a110-5b883fae733f	a@gmail.com	$2a$10$9kjjjSeVouXmAQiGkdgUVeBhOT6G0QbE9viK1qVhFHgtgAQc73LsO	f
c7ca3cde-6c54-4b22-ad01-0c8f338073f3	b@gmail.com	$2a$10$bqHNlxGQMqyK.TdSw/xlRuMy29XqiUjIzQO/x00.UxcTeZYNPFNX6	f
\.


--
-- TOC entry 4494 (class 0 OID 16489)
-- Dependencies: 221
-- Data for Name: don_dat_hang; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.don_dat_hang (ma_ddh, ngay_dathang, ma_ncc, da_xoa) FROM stdin;
\.


--
-- TOC entry 4492 (class 0 OID 16454)
-- Dependencies: 219
-- Data for Name: dong_ho; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.dong_ho (madh, tendh, gia, sl_ton, mota, trangthai, hinhanh, maloai, mahang, da_xoa) FROM stdin;
\.


--
-- TOC entry 4491 (class 0 OID 16439)
-- Dependencies: 218
-- Data for Name: hang_dong_ho; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.hang_dong_ho (ma_hang, ten_hang) FROM stdin;
\.


--
-- TOC entry 4495 (class 0 OID 16500)
-- Dependencies: 222
-- Data for Name: hoa_don; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.hoa_don (so_hoadon, ngay_in, tong_tien, maso_thue, ma_phieudat, da_xoa) FROM stdin;
\.


--
-- TOC entry 4489 (class 0 OID 16418)
-- Dependencies: 216
-- Data for Name: khach_hang; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.khach_hang (makh, cmnd, ho, ten, gioitinh, ngaysinh, diachi, sdt, email, masothue, da_xoa, account_id, hinhanh) FROM stdin;
a33b1cda-f2fc-423a-a782-0183cd361d90	1234567890	Nguyen Nhat	Minh	Nam	2003-03-18 00:00:00	691 Le Van Viet	0704098399	nnminh.sam.1803@gmail.com	1234567890	f	5055d41a-abda-429d-9b92-1bfb95d1f793	\N
\.


--
-- TOC entry 4490 (class 0 OID 16430)
-- Dependencies: 217
-- Data for Name: loai_dong_ho; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.loai_dong_ho (ma_loai, ten_loai) FROM stdin;
\.


--
-- TOC entry 4493 (class 0 OID 16475)
-- Dependencies: 220
-- Data for Name: nha_cung_cap; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.nha_cung_cap (ma_ncc, ten_ncc, diachi, email, sdt, da_xoa) FROM stdin;
\.


--
-- TOC entry 4498 (class 0 OID 16615)
-- Dependencies: 225
-- Data for Name: phieu_dat; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.phieu_dat (ma_phieudat, ngaydat, hoten_nguoi_nhan, diachi_nguoi_nhan, sdt_nguoi_nhan, ngay_giaohang, gio_giaohang, trangthai, ma_kh, da_xoa) FROM stdin;
\.


--
-- TOC entry 4332 (class 2606 OID 16711)
-- Name: customer_account account_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.customer_account
    ADD CONSTRAINT account_pkey PRIMARY KEY (id);


--
-- TOC entry 4334 (class 2606 OID 16713)
-- Name: customer_account account_unique_email; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.customer_account
    ADD CONSTRAINT account_unique_email UNIQUE (email);


--
-- TOC entry 4324 (class 2606 OID 16586)
-- Name: ct_cungcap ct_cungcap_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ct_cungcap
    ADD CONSTRAINT ct_cungcap_pkey PRIMARY KEY (ma_ncc, ma_dongho);


--
-- TOC entry 4326 (class 2606 OID 16604)
-- Name: ct_don_dat_hang ct_don_dat_hang_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ct_don_dat_hang
    ADD CONSTRAINT ct_don_dat_hang_pkey PRIMARY KEY (ma_ddh, ma_dongho);


--
-- TOC entry 4330 (class 2606 OID 16634)
-- Name: ct_phieu_dat ct_phieu_dat_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ct_phieu_dat
    ADD CONSTRAINT ct_phieu_dat_pkey PRIMARY KEY (ma_phieudat, ma_dongho);


--
-- TOC entry 4290 (class 2606 OID 16429)
-- Name: khach_hang customer_cmnd_unique; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.khach_hang
    ADD CONSTRAINT customer_cmnd_unique UNIQUE (cmnd);


--
-- TOC entry 4292 (class 2606 OID 16556)
-- Name: khach_hang customer_email_unique; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.khach_hang
    ADD CONSTRAINT customer_email_unique UNIQUE (email);


--
-- TOC entry 4294 (class 2606 OID 16715)
-- Name: khach_hang customer_masothue_unique; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.khach_hang
    ADD CONSTRAINT customer_masothue_unique UNIQUE (masothue);


--
-- TOC entry 4296 (class 2606 OID 16558)
-- Name: khach_hang customer_sdt_unique; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.khach_hang
    ADD CONSTRAINT customer_sdt_unique UNIQUE (sdt);


--
-- TOC entry 4279 (class 2606 OID 16721)
-- Name: khach_hang da_xoa_check; Type: CHECK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE public.khach_hang
    ADD CONSTRAINT da_xoa_check CHECK ((da_xoa = ANY (ARRAY[true, false]))) NOT VALID;


--
-- TOC entry 4320 (class 2606 OID 16494)
-- Name: don_dat_hang don_dat_hang_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.don_dat_hang
    ADD CONSTRAINT don_dat_hang_pkey PRIMARY KEY (ma_ddh);


--
-- TOC entry 4308 (class 2606 OID 16462)
-- Name: dong_ho dong_ho_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.dong_ho
    ADD CONSTRAINT dong_ho_pkey PRIMARY KEY (madh);


--
-- TOC entry 4312 (class 2606 OID 16486)
-- Name: nha_cung_cap email_unique; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.nha_cung_cap
    ADD CONSTRAINT email_unique UNIQUE (email);


--
-- TOC entry 4304 (class 2606 OID 16445)
-- Name: hang_dong_ho hang_dong_ho_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hang_dong_ho
    ADD CONSTRAINT hang_dong_ho_pkey PRIMARY KEY (ma_hang);


--
-- TOC entry 4306 (class 2606 OID 16447)
-- Name: hang_dong_ho hangdh_unique; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hang_dong_ho
    ADD CONSTRAINT hangdh_unique UNIQUE (ten_hang);


--
-- TOC entry 4322 (class 2606 OID 16508)
-- Name: hoa_don hoa_don_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hoa_don
    ADD CONSTRAINT hoa_don_pkey PRIMARY KEY (so_hoadon);


--
-- TOC entry 4298 (class 2606 OID 16425)
-- Name: khach_hang khachhang_primarykey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.khach_hang
    ADD CONSTRAINT khachhang_primarykey PRIMARY KEY (makh);


--
-- TOC entry 4300 (class 2606 OID 16436)
-- Name: loai_dong_ho loai_dong_hu_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.loai_dong_ho
    ADD CONSTRAINT loai_dong_hu_pkey PRIMARY KEY (ma_loai);


--
-- TOC entry 4302 (class 2606 OID 16438)
-- Name: loai_dong_ho loaidh_unique; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.loai_dong_ho
    ADD CONSTRAINT loaidh_unique UNIQUE (ten_loai);


--
-- TOC entry 4314 (class 2606 OID 16482)
-- Name: nha_cung_cap nha_cung_cap_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.nha_cung_cap
    ADD CONSTRAINT nha_cung_cap_pkey PRIMARY KEY (ma_ncc);


--
-- TOC entry 4328 (class 2606 OID 16622)
-- Name: phieu_dat phieu_dat_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.phieu_dat
    ADD CONSTRAINT phieu_dat_pkey PRIMARY KEY (ma_phieudat);


--
-- TOC entry 4316 (class 2606 OID 16488)
-- Name: nha_cung_cap sdt_unique; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.nha_cung_cap
    ADD CONSTRAINT sdt_unique UNIQUE (sdt);


--
-- TOC entry 4310 (class 2606 OID 16571)
-- Name: dong_ho ten_dong_ho_unique; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.dong_ho
    ADD CONSTRAINT ten_dong_ho_unique UNIQUE (tendh);


--
-- TOC entry 4318 (class 2606 OID 16484)
-- Name: nha_cung_cap ten_ncc_unique; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.nha_cung_cap
    ADD CONSTRAINT ten_ncc_unique UNIQUE (ten_ncc);


--
-- TOC entry 4345 (class 2606 OID 16640)
-- Name: ct_phieu_dat ct_phieudat_ma_dongho_FK; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ct_phieu_dat
    ADD CONSTRAINT "ct_phieudat_ma_dongho_FK" FOREIGN KEY (ma_dongho) REFERENCES public.dong_ho(madh) ON UPDATE CASCADE;


--
-- TOC entry 4346 (class 2606 OID 16635)
-- Name: ct_phieu_dat ct_phieudat_ma_phieudat_FK; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ct_phieu_dat
    ADD CONSTRAINT "ct_phieudat_ma_phieudat_FK" FOREIGN KEY (ma_phieudat) REFERENCES public.phieu_dat(ma_phieudat) ON UPDATE CASCADE;


--
-- TOC entry 4335 (class 2606 OID 16716)
-- Name: khach_hang customer_account_FK; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.khach_hang
    ADD CONSTRAINT "customer_account_FK" FOREIGN KEY (account_id) REFERENCES public.customer_account(id) ON UPDATE CASCADE NOT VALID;


--
-- TOC entry 4339 (class 2606 OID 16645)
-- Name: hoa_don hoadon_ma_phieudat_FK; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hoa_don
    ADD CONSTRAINT "hoadon_ma_phieudat_FK" FOREIGN KEY (ma_phieudat) REFERENCES public.phieu_dat(ma_phieudat) ON UPDATE CASCADE NOT VALID;


--
-- TOC entry 4342 (class 2606 OID 16605)
-- Name: ct_don_dat_hang ma_ddh; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ct_don_dat_hang
    ADD CONSTRAINT ma_ddh FOREIGN KEY (ma_ddh) REFERENCES public.don_dat_hang(ma_ddh) ON UPDATE CASCADE;


--
-- TOC entry 4343 (class 2606 OID 16610)
-- Name: ct_don_dat_hang ma_dongho; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ct_don_dat_hang
    ADD CONSTRAINT ma_dongho FOREIGN KEY (ma_dongho) REFERENCES public.dong_ho(madh) ON UPDATE CASCADE;


--
-- TOC entry 4340 (class 2606 OID 16592)
-- Name: ct_cungcap ma_dongho_FK; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ct_cungcap
    ADD CONSTRAINT "ma_dongho_FK" FOREIGN KEY (ma_dongho) REFERENCES public.dong_ho(madh) ON UPDATE CASCADE;


--
-- TOC entry 4338 (class 2606 OID 16495)
-- Name: don_dat_hang ma_ncc_FK; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.don_dat_hang
    ADD CONSTRAINT "ma_ncc_FK" FOREIGN KEY (ma_ncc) REFERENCES public.nha_cung_cap(ma_ncc) ON UPDATE CASCADE;


--
-- TOC entry 4341 (class 2606 OID 16587)
-- Name: ct_cungcap ma_ncc_FK; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ct_cungcap
    ADD CONSTRAINT "ma_ncc_FK" FOREIGN KEY (ma_ncc) REFERENCES public.nha_cung_cap(ma_ncc) ON UPDATE CASCADE;


--
-- TOC entry 4336 (class 2606 OID 16577)
-- Name: dong_ho mahangdh_FK; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.dong_ho
    ADD CONSTRAINT "mahangdh_FK" FOREIGN KEY (mahang) REFERENCES public.hang_dong_ho(ma_hang) ON UPDATE CASCADE NOT VALID;


--
-- TOC entry 4337 (class 2606 OID 16572)
-- Name: dong_ho maloaidh_FK; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.dong_ho
    ADD CONSTRAINT "maloaidh_FK" FOREIGN KEY (maloai) REFERENCES public.loai_dong_ho(ma_loai) ON UPDATE CASCADE NOT VALID;


--
-- TOC entry 4344 (class 2606 OID 16623)
-- Name: phieu_dat phieudat_makh_FK; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.phieu_dat
    ADD CONSTRAINT "phieudat_makh_FK" FOREIGN KEY (ma_kh) REFERENCES public.khach_hang(makh) ON UPDATE CASCADE;


-- Completed on 2024-01-31 09:41:34 +07

--
-- PostgreSQL database dump complete
--

