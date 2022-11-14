--liquibase formatted sql
--changeset billing:1.01.07 dbms:postgresql
--comment Criando as tabelas de Transferencia

CREATE DOMAIN base.bank_remittance_source_enum AS character(1) CHECK(VALUE IN ('B', 'T'));
CREATE DOMAIN base.transfer_status_enum AS character(1) NOT NULL CHECK(VALUE IN ('G', 'D', 'C'));

-- Cria as tabelas

CREATE TABLE base.transfer_type
(
  id smallserial NOT NULL,
  code character varying(4) NOT NULL,
  name character varying(50) NOT NULL,
  CONSTRAINT pk_transfer_type PRIMARY KEY (id),
  CONSTRAINT uk_1_transfer_type UNIQUE (code)
);
ALTER TABLE base.transfer_type OWNER TO billing;

CREATE TABLE base.transfer
(
  id bigserial NOT NULL,
  token character varying(20) NOT NULL,
  transfer_type_id smallint NOT NULL,
  amount integer NOT NULL,
  operation_date date NOT NULL,
  status base.transfer_status_enum,
  creation_date timestamp without time zone NOT NULL,
  processing_date timestamp without time zone,
  cancelation_date timestamp without time zone,
  CONSTRAINT pk_transfer PRIMARY KEY (id),
  CONSTRAINT fk_1_transfer FOREIGN KEY (transfer_type_id)
    REFERENCES base.transfer_type (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION,  
  CONSTRAINT uk_1_transfer UNIQUE (token)
);
ALTER TABLE base.transfer OWNER TO billing;

CREATE TABLE base.transfer_bank_account
(
  id bigint NOT NULL,
  source_bank_account_id bigint NOT NULL,
  destination_bank_account_id bigint NOT NULL,
  CONSTRAINT pk_transfer_bank_account PRIMARY KEY (id),
  CONSTRAINT fk_1_transfer_bank_account FOREIGN KEY (id)
    REFERENCES base.transfer (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_2_transfer_bank_account FOREIGN KEY (source_bank_account_id)
    REFERENCES base.bank_account (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_3_transfer_bank_account FOREIGN KEY (destination_bank_account_id)
    REFERENCES base.bank_account (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION
);
ALTER TABLE base.transfer_bank_account OWNER TO billing;

-- Adiciona nas tabelas bank remittance e dischage referencia para transfer

ALTER TABLE base.bank_remittance ADD COLUMN source base.bank_remittance_source_enum;
UPDATE base.bank_remittance SET source = 'B';
ALTER TABLE base.bank_remittance ALTER COLUMN source SET NOT NULL;
ALTER DOMAIN base.bank_remittance_source_enum SET NOT NULL;

ALTER TABLE base.bank_remittance ADD COLUMN transfer_id bigint;
ALTER TABLE base.bank_remittance ALTER COLUMN payment_id DROP NOT NULL;
ALTER TABLE base.bank_remittance ADD CONSTRAINT fk_5_bank_remittance FOREIGN KEY (transfer_id)
  REFERENCES base.transfer (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION;   

ALTER TABLE base.bank_discharge ADD COLUMN transfer_id bigint;
ALTER TABLE base.bank_discharge ALTER COLUMN payment_id DROP NOT NULL;
ALTER TABLE base.bank_discharge ADD CONSTRAINT fk_5_bank_discharge FOREIGN KEY (transfer_id)
  REFERENCES base.transfer (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION;    


-- Insere os novos dados

INSERT INTO base.transfer_type (code, name) VALUES
  ('PDBA', 'From Company to Dealer Bank Account'),
  ('PCBA', 'From Company to Customer Bank Account'),
  ('DDMA', 'From Dealer to Dealer Market Account'),
  ('DCMA', 'From Dealer to Customer Market Account'),
  ('CDMA', 'From Customer to Dealer Market Account');
