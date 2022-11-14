--liquibase formatted sql
--changeset billing:1.00.01 dbms:postgresql
--comment Criacao de schema base e tabelas

-- schema
CREATE SCHEMA base;
ALTER SCHEMA base OWNER TO billing;

-- enums
CREATE DOMAIN base.address_type_enum AS character(1) NOT NULL CHECK(VALUE IN ('H', 'B', 'M'));
CREATE DOMAIN base.bank_discharge_operation_enum AS character(1) NOT NULL CHECK(VALUE IN ('R', 'P', 'C', 'U'));
CREATE DOMAIN base.bank_discharge_status_enum AS character(1) NOT NULL CHECK(VALUE IN ('G', 'P', 'D', 'C'));
CREATE DOMAIN base.bank_enviroment_enum AS character(1) NOT NULL CHECK(VALUE IN ('T', 'P'));
CREATE DOMAIN base.bank_remittance_operation_enum AS character(1) NOT NULL CHECK(VALUE IN ('R', 'C'));
CREATE DOMAIN base.bank_remittance_status_enum AS character(1) NOT NULL CHECK(VALUE IN ('G', 'P', 'D', 'C'));
CREATE DOMAIN base.entity_status_enum AS character(1) NOT NULL CHECK(VALUE IN ('A', 'B', 'D'));
CREATE DOMAIN base.entity_child_status_enum AS character(1) NOT NULL CHECK(VALUE IN ('A', 'D'));
CREATE DOMAIN base.invoice_status_enum AS character(1) NOT NULL CHECK(VALUE IN ('O', 'E', 'P', 'A', 'C'));
CREATE DOMAIN base.payment_method_enum AS character(1) NOT NULL CHECK(VALUE IN ('B', 'C', 'D'));
CREATE DOMAIN base.payment_status_enum AS character(1) NOT NULL CHECK(VALUE IN ('G', 'D', 'A', 'P', 'C', 'R', 'T', 'E'));
CREATE DOMAIN base.person_type_enum AS character(1) NOT NULL CHECK(VALUE IN ('I', 'L'));

-- tabelas

CREATE TABLE base.country
(
  id smallint NOT NULL,
  code character varying(2) NULL,
  name character varying(30) NULL,
  CONSTRAINT pk_country PRIMARY KEY (id),
  CONSTRAINT uk_1_country UNIQUE (code)
);
ALTER TABLE base.country OWNER TO billing;

CREATE TABLE base.language
(
  id smallint NOT NULL,
  country_id smallint NOT NULL,
  code character(5) NOT NULL,
  name character varying(30) NOT NULL,
  date_format character varying(10) NOT NULL, 
  number_format character varying(4) NOT NULL,
  number_decimal_separator character varying(1) NOT NULL,
  CONSTRAINT pk_language PRIMARY KEY (id),
  CONSTRAINT fk_1_language FOREIGN KEY (country_id)
    REFERENCES base.country (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT uk_1_language UNIQUE (code),
  CONSTRAINT uk_2_language UNIQUE (country_id)    
);
ALTER TABLE base.language OWNER TO billing;

CREATE TABLE base.result_code_group
(
  id smallserial NOT NULL,
  key character(1) NOT NULL,
  name character varying(30) NOT NULL,
  http_status smallint NOT NULL,
  CONSTRAINT pk_result_code_group PRIMARY KEY (id)
);
ALTER TABLE base.result_code_group OWNER TO billing;

CREATE TABLE base.result_code
(
  id serial NOT NULL,
  key character(3) NOT NULL,
  name character varying(50) NOT NULL,
  success boolean NOT NULL,
  result_code_group_id smallint,
  http_status smallint,
  CONSTRAINT fk_1_result_code FOREIGN KEY (result_code_group_id)
    REFERENCES base.result_code_group (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT pk_result_code PRIMARY KEY (id)
);
ALTER TABLE base.result_code OWNER TO billing;

CREATE TABLE base.result_code_message
(
  id serial NOT NULL,
  result_code_id integer NOT NULL,
  language_id smallint NOT NULL,
  message character varying(200) NOT NULL,
  CONSTRAINT pk_result_code_message PRIMARY KEY (id),
  CONSTRAINT fk_1_result_code_message FOREIGN KEY (result_code_id)
    REFERENCES base.result_code (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_2_result_code_message FOREIGN KEY (language_id)
    REFERENCES base.language (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION  
);
ALTER TABLE base.result_code_message OWNER TO billing;

-- tabelas base

CREATE TABLE base.state
(
  id smallint NOT NULL,
  country_id smallint NOT NULL,
  code character varying(2) NOT NULL,
  name character varying(30) NULL,
  CONSTRAINT pk_state PRIMARY KEY (id),
  CONSTRAINT fk_1_state FOREIGN KEY (country_id)
    REFERENCES base.country (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT uk_1_state UNIQUE (country_id, code)
);
ALTER TABLE base.state OWNER TO billing;

CREATE TABLE base.city
(
  id serial NOT NULL,
  state_id smallint NOT NULL,
  name character varying(50) NOT NULL,
  CONSTRAINT pk_city PRIMARY KEY (id) ,
  CONSTRAINT fk_1_city FOREIGN KEY (state_id)
    REFERENCES base.state (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION
);
ALTER TABLE base.city OWNER TO billing;

CREATE TABLE base.document_type
(
  id smallint NOT NULL,
  country_id smallint NOT NULL,
  person_type base.person_type_enum,
  code character varying(10) NOT NULL,
  name character varying(20) NOT NULL,
  mask character varying(50) NOT NULL,
  web_mask character varying(20) NOT NULL,
  validator_path character varying(50) NOT NULL,
  CONSTRAINT pk_document_type PRIMARY KEY (id),
  CONSTRAINT fk_1_document_type FOREIGN KEY (country_id)
    REFERENCES base.country (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT uk_1_document_type UNIQUE (code)
);
ALTER TABLE base.document_type OWNER TO billing;

-- entidades

CREATE TABLE base.company
(
  id smallserial NOT NULL,
  token character varying(4) NOT NULL,
  trading_name character varying(50) NOT NULL,
  corporate_name character varying(100) NOT NULL,
  country_id smallint NOT NULL,
  document_type_id smallint NOT NULL,
  document_number character varying(20) NOT NULL,
  status base.entity_status_enum,
  CONSTRAINT pk_company PRIMARY KEY (id),
  CONSTRAINT fk_1_company FOREIGN KEY (country_id)
    REFERENCES base.country (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_2_company FOREIGN KEY (document_type_id)
    REFERENCES base.document_type (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT uk_1_company UNIQUE (token)
);
ALTER TABLE base.company OWNER TO billing;

CREATE TABLE base.company_address
(
  id bigserial NOT NULL,
  company_id bigint NOT NULL,
  token character varying(6) NOT NULL,
  address_type base.address_type_enum,
  billing_address boolean NOT NULL,
  street character varying(80) NOT NULL,
  number character varying(10),
  complement character varying(20),
  district character varying(30) NOT NULL,
  zip_code character varying(8) NOT NULL,
  city_id integer NOT NULL,
  status base.entity_child_status_enum,
  CONSTRAINT pk_company_address PRIMARY KEY (id),
  CONSTRAINT fk_1_company_address FOREIGN KEY (company_id)
    REFERENCES base.company (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_2_company_address FOREIGN KEY (city_id)
    REFERENCES base.city (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT uk_1_company_address UNIQUE (token)
);
ALTER TABLE base.company_address OWNER TO billing;

CREATE TABLE base.dealer
(
  id bigserial NOT NULL,
  company_id smallint NOT NULL,
  token character varying(8) NOT NULL,
  name character varying(100) NOT NULL,
  person_type base.person_type_enum,
  document_type_id smallint NOT NULL,
  document_number character varying(20) NOT NULL,
  phone_number character varying(15),
  mobile_number character varying(15),
  email character varying(60),
  comments text,
  bank_account_id integer,
  bank_instructions text,
  status base.entity_status_enum,
  CONSTRAINT pk_dealer PRIMARY KEY (id),
  CONSTRAINT fk_1_dealer FOREIGN KEY (company_id)
    REFERENCES base.company (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_2_dealer FOREIGN KEY (document_type_id)
    REFERENCES base.document_type (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT uk_1_dealer UNIQUE (token)
);
ALTER TABLE base.dealer OWNER TO billing;

CREATE TABLE base.dealer_address
(
  id bigserial NOT NULL,
  dealer_id bigint NOT NULL,
  token character varying(10) NOT NULL,
  address_type base.address_type_enum,
  billing_address boolean NOT NULL,
  street character varying(80) NOT NULL,
  number character varying(10),
  complement character varying(20),
  district character varying(30) NOT NULL,
  zip_code character varying(8) NOT NULL,
  city_id integer NOT NULL,
  status base.entity_child_status_enum,
  CONSTRAINT pk_dealer_address PRIMARY KEY (id),
  CONSTRAINT fk_1_dealer_address FOREIGN KEY (dealer_id)
    REFERENCES base.dealer (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_2_dealer_address FOREIGN KEY (city_id)
    REFERENCES base.city (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT uk_1_dealer_address UNIQUE (token)
);
ALTER TABLE base.dealer_address OWNER TO billing;

CREATE TABLE base.customer
(
  id bigserial NOT NULL,
  dealer_id bigint NOT NULL,
  token character varying(12) NOT NULL,
  name character varying(100) NOT NULL,
  person_type base.person_type_enum,
  document_type_id smallint NOT NULL,
  document_number character varying(20) NOT NULL,
  phone_number character varying(15),
  mobile_number character varying(15),
  email character varying(60),
  comments text,
  status base.entity_status_enum,
  creation_date timestamp without time zone NOT NULL,
  deletion_date timestamp without time zone,
  CONSTRAINT pk_customer PRIMARY KEY (id),
  CONSTRAINT fk_1_customer FOREIGN KEY (dealer_id)
    REFERENCES base.dealer (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_2_customer FOREIGN KEY (document_type_id)
    REFERENCES base.document_type (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT uk_1_customer UNIQUE (token)
);
ALTER TABLE base.customer OWNER TO billing;

CREATE TABLE base.customer_address
(
  id bigserial NOT NULL,
  customer_id bigint NOT NULL,
  token character varying(14) NOT NULL,
  address_type base.address_type_enum,
  billing_address boolean NOT NULL,
  street character varying(80) NOT NULL,
  number character varying(10),
  complement character varying(20),
  district character varying(30) NOT NULL,
  zip_code character varying(8) NOT NULL,
  city_id integer NOT NULL,
  status base.entity_child_status_enum,
  CONSTRAINT pk_customer_address PRIMARY KEY (id),
  CONSTRAINT fk_1_customer_address FOREIGN KEY (customer_id)
    REFERENCES base.customer (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_2_customer_address FOREIGN KEY (city_id)
    REFERENCES base.city (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT uk_1_customer_address UNIQUE (token)
);
ALTER TABLE base.customer_address OWNER TO billing;

-- contas bancarias

CREATE TABLE base.bank
(
  id smallserial NOT NULL,
  code character varying(10) NOT NULL,
  name character varying(30) NOT NULL,
  connector_path character varying(50) NOT NULL,
  payment_place character varying(100),
  instructions text,
  CONSTRAINT pk_bank PRIMARY KEY (id),
  CONSTRAINT uk_1_bank UNIQUE (code)
);
ALTER TABLE base.bank OWNER TO billing;

CREATE TABLE base.bank_account
(
  id serial NOT NULL,
  bank_id smallint NOT NULL,
  company_id smallint NOT NULL,
  alias character varying(30) NOT NULL,
  agency_number character varying(4) NOT NULL,
  agency_check_digit character(1),
  account_number character varying(8) NOT NULL,
  account_check_digit character(1),
  acceptance boolean NOT NULL,
  enviroment base.bank_enviroment_enum NOT NULL,
  online_billet_registration boolean NOT NULL,
  billet_unpaid_limit integer NOT NULL,
  CONSTRAINT pk_bank_account PRIMARY KEY (id),
  CONSTRAINT uk_1_bank_account UNIQUE (bank_id, company_id),
  CONSTRAINT uk_2_bank_account UNIQUE (alias)
);
ALTER TABLE base.bank_account OWNER TO billing;

ALTER TABLE base.dealer ADD CONSTRAINT fk_3_dealer FOREIGN KEY (bank_account_id)
  REFERENCES base.bank_account (id) MATCH SIMPLE
  ON UPDATE NO ACTION ON DELETE NO ACTION;

-- cobranca

CREATE TABLE base.invoice
(
  id bigserial NOT NULL,
  customer_id bigint NOT NULL,
  token character varying(22) NOT NULL,
  description character varying(100),
  document_number bigint,
  amount integer NOT NULL,
  instalment smallint,
  expiration_date date NOT NULL,
  payment_method base.payment_method_enum NOT NULL,
  status base.invoice_status_enum,
  creation_date timestamp without time zone NOT NULL,
  payment_date date,
  cancelation_date timestamp without time zone,
  CONSTRAINT pk_invoice PRIMARY KEY (id),
  CONSTRAINT fk_1_invoice FOREIGN KEY (customer_id)
    REFERENCES base.customer (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT uk_1_invoice UNIQUE (token)
);
ALTER TABLE base.invoice OWNER TO billing;

CREATE TABLE base.payment
(
  id bigserial NOT NULL,
  invoice_id bigint NOT NULL,
  token character varying(24) NOT NULL,
  amount integer NOT NULL,
  payment_method base.payment_method_enum NOT NULL,
  status base.payment_status_enum,
  creation_date  timestamp without time zone NOT NULL,
  cancelation_date timestamp without time zone,
  CONSTRAINT pk_payment PRIMARY KEY (id),
  CONSTRAINT fk_1_payment FOREIGN KEY (invoice_id)
    REFERENCES base.invoice (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT uk_1_payment UNIQUE (token)
);
ALTER TABLE base.payment OWNER TO billing;

CREATE TABLE base.payment_bank_billet
(
  id bigint NOT NULL,
  bank_account_id integer NOT NULL,
  issue_date date NOT NULL,
  expiration_date date NOT NULL,  
  payer_name character varying(100) NOT NULL,
  payer_document_type_id smallint NOT NULL,
  payer_document_number character varying(20) NOT NULL,
  payer_address_street character varying(80) NOT NULL,
  payer_address_number character varying(10),
  payer_address_complement character varying(20),
  payer_address_district character varying(30) NOT NULL,
  payer_address_zip_code character varying(8) NOT NULL,
  payer_address_city_id integer NOT NULL,
  billet_number bigint,
  billet_number_digit smallint,
  document_number bigint,
  line_code character varying(58),
  payment_date date,
  fee integer,
  net_amount integer,
  CONSTRAINT pk_payment_bank_billet PRIMARY KEY (id),
  CONSTRAINT fk_1_payment_bank_billet FOREIGN KEY (id)
    REFERENCES base.payment (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_2_payment_bank_billet FOREIGN KEY (bank_account_id)
    REFERENCES base.bank_account (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_3_payment_bank_billet FOREIGN KEY (payer_document_type_id)
    REFERENCES base.document_type (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION,    
  CONSTRAINT fk_4_payment_bank_billet FOREIGN KEY (payer_address_city_id)
    REFERENCES base.city (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION
);
ALTER TABLE base.payment_bank_billet OWNER TO billing;

CREATE TABLE base.bank_remittance
(
  id bigserial NOT NULL,
  payment_bank_billet_id bigint NOT NULL,
  operation base.bank_remittance_operation_enum,
  status base.bank_remittance_status_enum,
  creation_date  timestamp without time zone NOT NULL,
  processing_date timestamp without time zone,
  cancelation_date timestamp without time zone,
  CONSTRAINT pk_bank_remittance PRIMARY KEY (id),
  CONSTRAINT fk_1_bank_remittance FOREIGN KEY (payment_bank_billet_id)
    REFERENCES base.payment_bank_billet (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT uk_1_bank_remittance UNIQUE (payment_bank_billet_id, operation)
);
ALTER TABLE base.bank_remittance OWNER TO billing;

CREATE TABLE base.bank_discharge
(
  id bigserial NOT NULL,
  payment_bank_billet_id bigint NOT NULL,
  bank_remittance_id bigint NOT NULL,
  operation base.bank_discharge_operation_enum,
  processing_date timestamp without time zone,
  result_code_id integer,
  CONSTRAINT pk_bank_discharge PRIMARY KEY (id),
  CONSTRAINT fk_1_bank_remittance FOREIGN KEY (payment_bank_billet_id)
    REFERENCES base.payment_bank_billet (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_2_bank_remittance FOREIGN KEY (bank_remittance_id)
    REFERENCES base.bank_remittance (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_3_bank_remittance FOREIGN KEY (result_code_id)
    REFERENCES base.result_code (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION
);
ALTER TABLE base.bank_discharge OWNER TO billing;

-- seguranca

CREATE TABLE base.role
(
  id smallserial NOT NULL,
  code character(1) NOT NULL,
  name character varying(30) NOT NULL,
  CONSTRAINT pk_role PRIMARY KEY (id)
);
ALTER TABLE base.role OWNER TO billing;

CREATE TABLE base.user
(
  id bigserial NOT NULL,
  username character varying(30) NOT NULL,
  email character varying(60) NOT NULL,
  name character varying(100) NOT NULL,
  password character varying(60) NOT NULL,
  role_id smallint NOT NULL,
  company_id smallint,
  dealer_id bigint,
  customer_id bigint,
  status base.entity_status_enum,
  CONSTRAINT pk_user PRIMARY KEY (id),
  CONSTRAINT fk_1_user FOREIGN KEY (role_id)
    REFERENCES base.role (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_2_user FOREIGN KEY (company_id)
    REFERENCES base.company (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_3_user FOREIGN KEY (dealer_id)
    REFERENCES base.dealer (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_4_user FOREIGN KEY (customer_id)
    REFERENCES base.customer (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT uk_1_user UNIQUE (username)
);
ALTER TABLE base.user OWNER TO billing;
