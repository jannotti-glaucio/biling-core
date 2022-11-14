--liquibase formatted sql
--changeset billing:1.03.10 dbms:postgresql
--comment Removendo a constraint unique da tabela bank_remittance

ALTER TABLE base.bank_remittance DROP CONSTRAINT uk_1_bank_remittance;
