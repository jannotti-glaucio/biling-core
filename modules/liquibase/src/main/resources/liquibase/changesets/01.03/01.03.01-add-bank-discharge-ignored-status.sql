--liquibase formatted sql
--changeset billing:1.03.01 dbms:postgresql
--comment Adicionando o status ignored para o enum bank_discharge_status

ALTER DOMAIN base.bank_discharge_status_enum DROP CONSTRAINT bank_discharge_status_enum_check;
UPDATE banking_bb.cnab400_discharge_detail set status = 'I' where status = 'C';
ALTER DOMAIN base.bank_discharge_status_enum ADD CONSTRAINT bank_discharge_status_enum_check
  CHECK(VALUE IN ('G', 'P', 'D', 'I'));
