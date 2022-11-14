--liquibase formatted sql
--changeset billing:1.01.04 dbms:postgresql
--comment Revisando tabelas do BB para suporte a contas de Dealer e Company

ALTER TABLE banking_bb.bank_account RENAME TO company_bank_account;
ALTER TABLE banking_bb.company_bank_account ALTER COLUMN id TYPE bigint;
ALTER TABLE banking_bb.company_bank_account RENAME CONSTRAINT pk_bank_account TO pk_company_bank_account;

ALTER TABLE banking_bb.company_bank_account RENAME COLUMN base_bank_account_id TO base_company_bank_account_id;
ALTER TABLE banking_bb.company_bank_account ALTER COLUMN base_company_bank_account_id TYPE bigint;
ALTER TABLE banking_bb.company_bank_account DROP CONSTRAINT fk_1_bank_account;
ALTER TABLE banking_bb.company_bank_account  ADD CONSTRAINT fk_1_bank_account
  FOREIGN KEY (base_company_bank_account_id) REFERENCES base.company_bank_account (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE banking_bb.bank_remittance_file RENAME COLUMN bank_account_id TO company_bank_account_id;
ALTER TABLE banking_bb.bank_remittance_file ALTER COLUMN company_bank_account_id TYPE bigint;

ALTER TABLE banking_bb.bank_remittance RENAME TO bank_remittance_file_detail;
ALTER TABLE banking_bb.bank_remittance_file_detail RENAME CONSTRAINT pk_bank_remittance TO pk_bank_remittance_file_detail;
ALTER TABLE banking_bb.bank_remittance_file_detail RENAME CONSTRAINT fk_1_bank_remittance TO fk_1_bank_remittance_file_detail;
ALTER TABLE banking_bb.bank_remittance_file_detail RENAME CONSTRAINT fk_2_bank_remittance TO fk_2_bank_remittance_file_detail;


ALTER TABLE banking_bb.bank_discharge_file RENAME COLUMN bank_account_id TO company_bank_account_id;
ALTER TABLE banking_bb.bank_discharge_file ALTER COLUMN company_bank_account_id TYPE bigint;

ALTER TABLE banking_bb.bank_discharge RENAME TO bank_discharge_file_detail;
ALTER TABLE banking_bb.bank_discharge_file_detail RENAME CONSTRAINT pk_bank_discharge TO pk_bank_discharge_file_detail;
ALTER TABLE banking_bb.bank_discharge_file_detail RENAME CONSTRAINT fk_1_bank_discharge TO fk_1_bank_discharge_file_detail;
ALTER TABLE banking_bb.bank_discharge_file_detail RENAME CONSTRAINT fk_2_bank_discharge TO fk_2_bank_discharge_file_detail;

ALTER TABLE banking_bb.bank_remittance_file_detail ADD creation_date timestamp without time zone;
UPDATE banking_bb.bank_remittance_file_detail d SET creation_date = f.creation_date
  FROM banking_bb.bank_remittance_file f WHERE f.id = d.bank_remittance_file_id;
ALTER TABLE banking_bb.bank_remittance_file_detail ALTER COLUMN creation_date SET NOT NULL;

ALTER TABLE banking_bb.bank_discharge_file_detail ADD receive_date timestamp without time zone;
UPDATE banking_bb.bank_discharge_file_detail d SET receive_date = f.receive_date
  FROM banking_bb.bank_discharge_file f WHERE f.id = d.bank_discharge_file_id;  
ALTER TABLE banking_bb.bank_discharge_file_detail ALTER COLUMN receive_date SET NOT NULL;

ALTER TABLE banking_bb.bank_discharge_file_detail ADD data_liquidacao date;
UPDATE banking_bb.bank_discharge_file_detail SET data_liquidacao = data_credito - 1;

ALTER TABLE banking_bb.bank_discharge_file_detail ADD codigo_banco_recebedor character varying(3),
  ADD prefixo_agencia_recebedora character varying(4),
  ADD dv_prefixo_recebedora smallint;

ALTER TABLE banking_bb.bank_discharge_file_detail RENAME COLUMN data_vencimento_titulo TO data_vencimento;
