--liquibase formatted sql
--changeset billing:1.01.09 dbms:postgresql
--comment Criando tabelas de suporte a Market Accounts

CREATE DOMAIN base.market_withdraw_status_enum AS character(1) NOT NULL CHECK(VALUE IN ('R', 'A', 'D', 'L', 'C'));
CREATE DOMAIN base.market_statement_direction_enum AS character(1) NOT NULL CHECK(VALUE IN ('C', 'D'));

-- Cria as tabelas

CREATE TABLE base.market_account
(
  id bigserial not null,
  token character varying(14) not null,
  status base.entity_status_enum,
  CONSTRAINT pk_market_account PRIMARY KEY (id),
  CONSTRAINT uk_1_market_account UNIQUE (token)
);
ALTER TABLE base.market_account OWNER TO billing;

CREATE TABLE base.market_balance
(
  id bigserial NOT NULL,
  market_account_id bigint NOT NULL,
  balance_date date NOT NULL,
  debits_summary integer NOT NULL,
  credits_summary integer NOT NULL,
  prior_balance integer NOT NULL,
  CONSTRAINT pk_market_balance PRIMARY KEY (id),
  CONSTRAINT fk_1_market_balance FOREIGN KEY (market_account_id)
    REFERENCES base.market_account (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT uk_1_market_balance UNIQUE (market_account_id, balance_date)    
);
ALTER TABLE base.market_balance OWNER TO billing;

CREATE TABLE base.market_withdraw
(
  id bigserial NOT NULL,
  market_account_id bigint NOT NULL,
  token character varying(20) not null,
  amount integer NOT NULL,
  dealer_bank_account_id bigint NOT NULL,
  request_date timestamp without time zone NOT NULL,
  requester_user_id bigint NOT NULL,
  status base.market_withdraw_status_enum,
  review_date timestamp without time zone,
  reviewer_user_id bigint,
  deny_reason character varying(100),
  cancelation_date timestamp without time zone,
  release_date  timestamp without time zone,  
  CONSTRAINT pk_market_withdraw PRIMARY KEY (id),
  CONSTRAINT fk_1_market_withdraw FOREIGN KEY (market_account_id)
    REFERENCES base.market_account (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_2_market_withdraw FOREIGN KEY (dealer_bank_account_id)
    REFERENCES base.dealer_bank_account (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_3_market_withdraw FOREIGN KEY (requester_user_id)
    REFERENCES base.user (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_4_market_withdraw FOREIGN KEY (reviewer_user_id)
    REFERENCES base.user (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT uk_1_market_withdraw UNIQUE (token)
);
ALTER TABLE base.market_withdraw OWNER TO billing;

CREATE TABLE base.market_statement_type
(
  id smallserial NOT NULL,
  code character varying(3) NOT NULL,
  name character varying(30) NOT NULL,
  direction base.market_statement_direction_enum,
  CONSTRAINT pk_statement_type PRIMARY KEY (id),
  CONSTRAINT uk_1_market_statement_type UNIQUE (code)
);
ALTER TABLE base.market_statement_type OWNER TO billing;

CREATE TABLE base.market_statement_description
(
  id smallserial NOT NULL,
  market_statement_type_id integer NOT NULL,
  language_id smallint NOT NULL,
  description character varying(100) NOT NULL,
  CONSTRAINT pk_market_statement_description PRIMARY KEY (id),
  CONSTRAINT fk_1_market_statement_description FOREIGN KEY (market_statement_type_id)
    REFERENCES base.market_statement_type (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_2_market_statement_description FOREIGN KEY (language_id)
    REFERENCES base.language (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT uk_1_market_statement_description UNIQUE (market_statement_type_id, language_id)
);
ALTER TABLE base.market_statement_description OWNER TO billing;

CREATE TABLE base.market_statement
(
  id bigserial NOT NULL,
  market_account_id bigint NOT NULL,
  token character varying(26) NOT NULL,
  market_statement_type_id smallint NOT NULL,
  statement_date date NOT NULL,
  amount integer NOT NULL,
  payment_id bigint,
  market_withdraw_id bigint,
  CONSTRAINT pk_market_statement PRIMARY KEY (id),
  CONSTRAINT fk_1_market_statement FOREIGN KEY (market_account_id)
    REFERENCES base.market_account (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_2_market_statement FOREIGN KEY (market_statement_type_id)
    REFERENCES base.market_statement_type (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_3_market_statement FOREIGN KEY (payment_id)
    REFERENCES base.payment (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_4_market_statement FOREIGN KEY (market_withdraw_id)
    REFERENCES base.market_withdraw (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT uk_1_market_statement UNIQUE (token)
);
ALTER TABLE base.market_statement OWNER TO billing;

CREATE TABLE base.dealer_market_account
(
  id bigint NOT NULL,
  dealer_id bigint NOT NULL,
  CONSTRAINT pk_dealer_market_account PRIMARY KEY (id),
  CONSTRAINT fk_1_dealer_market_account FOREIGN KEY (id)
    REFERENCES base.market_account (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_2_dealer_market_account FOREIGN KEY (dealer_id)
    REFERENCES base.dealer (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION
);
ALTER TABLE base.dealer_market_account OWNER TO billing;

CREATE TABLE base.customer_market_account
(
  id bigint NOT NULL,
  customer_id bigint NOT NULL,
  CONSTRAINT pk_customer_market_account PRIMARY KEY (id),
  CONSTRAINT fk_1_customer_market_account FOREIGN KEY (id)
    REFERENCES base.market_account (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_2_customer_market_account FOREIGN KEY (customer_id)
    REFERENCES base.customer (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION
);
ALTER TABLE base.customer_market_account OWNER TO billing;

CREATE TABLE base.transfer_market_account
(
  id bigint NOT NULL,
  source_market_account_id bigint NOT NULL,
  destination_market_account_id bigint NOT NULL,
  CONSTRAINT pk_transfer_market_account PRIMARY KEY (id),
  CONSTRAINT fk_1_transfer_market_account FOREIGN KEY (id)
    REFERENCES base.transfer (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_2_transfer_market_account FOREIGN KEY (source_market_account_id)
    REFERENCES base.market_account (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_3_transfer_market_account FOREIGN KEY (destination_market_account_id)
    REFERENCES base.market_account (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION
);
ALTER TABLE base.transfer_market_account OWNER TO billing;

-- Cria um relacionamento entre transfer withdraw
ALTER TABLE base.transfer ADD market_withdraw_id bigint;
ALTER TABLE base.transfer ADD CONSTRAINT fk_2_transfer FOREIGN KEY (market_withdraw_id)
    REFERENCES base.market_withdraw (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION;

-- Insere os novos dados

INSERT INTO base.market_statement_type (code, name, direction) VALUES
  ('001', 'INVOICE_PAYMENT', 'C'),
  ('002', 'WITHDRAW_RESERVE', 'D'),
  ('003', 'WITHDRAW_REFUND', 'C');
INSERT INTO base.market_statement_description (market_statement_type_id, language_id, description) VALUES
  ((SELECT id FROM base.market_statement_type WHERE code = '001'), 1, 'Pagamento de Fatura'),
  ((SELECT id FROM base.market_statement_type WHERE code = '002'), 1, 'Saque'),
  ((SELECT id FROM base.market_statement_type WHERE code = '003'), 1, 'Estorno de Saque');  

SELECT base.insert_result_code('244', 'ACTIVE_MARKET_ACCOUNT_REQUIRED', false, 'pt-BR',
  'É necessária uma conta ativa para efetuar essa operação', 'R', null);
SELECT base.insert_result_code('245', 'INSUFFICIENT_BALANCE_TO_REQUEST_WITHDRAW', false, 'pt-BR',
  'Saldo insuficiente para solicitar Saque', 'R', null);
SELECT base.insert_result_code('246', 'INSUFFICIENT_BALANCE_TO_APPROVE_WITHDRAW', false, 'pt-BR',
  'Saldo insuficiente para aprovar Saque', 'R', null);  
SELECT base.insert_result_code('247', 'INVALID_MARKET_WITHDRAW_STATUS_TO_APPROVE', false, 'pt-BR',
  'Solicitação de Saque em status inválido para ser aprovada', 'R', null);
SELECT base.insert_result_code('248', 'INVALID_MARKET_WITHDRAW_STATUS_TO_DENY', false, 'pt-BR',
  'Solicitação de Saque em status inválido para ser negada', 'R', null);
SELECT base.insert_result_code('249', 'INVALID_MARKET_WITHDRAW_STATUS_TO_PROVIDE', false, 'pt-BR',
  'Solicitação de Saque em status inválido para ser disponibilizada', 'R', null);
SELECT base.insert_result_code('250', 'INVALID_MARKET_WITHDRAW_STATUS_TO_CANCEL', false, 'pt-BR',
  'Solicitação de Saque em status inválido para ser cancelada', 'R', null);
