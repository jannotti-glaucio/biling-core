--liquibase formatted sql
--changeset billing:1.01.03 dbms:postgresql
--comment Finalizando a revisao das tabelas de Contas Bancarias

ALTER TABLE base.bank_account ALTER COLUMN token TYPE character varying(14),
  ALTER COLUMN description SET NOT NULL;

ALTER TABLE base.dealer ALTER COLUMN company_bank_account_id SET NOT NULL;
