--liquibase formatted sql
--changeset billing:1.03.07 dbms:postgresql
--comment Removendo o suporte a registro de boleto via CNAB

ALTER TABLE base.dealer DROP COLUMN bank_billet_registry_mode;

ALTER TABLE base.bank_remittance ADD COLUMN result_code_id integer;
ALTER TABLE base.bank_remittance ADD CONSTRAINT fk_4_bank_remittance FOREIGN KEY (result_code_id)
  REFERENCES base.result_code (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER DOMAIN base.bank_remittance_status_enum DROP CONSTRAINT bank_remittance_status_enum_check;
ALTER DOMAIN base.bank_remittance_status_enum ADD CONSTRAINT bank_remittance_status_enum_check CHECK(VALUE IN ('G', 'P', 'C', 'E'));

ALTER DOMAIN base.invoice_status_enum DROP CONSTRAINT invoice_status_enum_check;
UPDATE base.invoice SET status = 'X' WHERE status = 'E';
ALTER DOMAIN base.invoice_status_enum ADD CONSTRAINT invoice_status_enum_check CHECK(VALUE IN ('O', 'X', 'P', 'A', 'C', 'E'));

ALTER TABLE base.bank_remittance ADD COLUMN exception_message text;
ALTER TABLE base.notify_response ADD COLUMN exception_message text;

DELETE FROM base.result_code_message WHERE result_code_id = (SELECT id FROM base.result_code WHERE key = '239');
DELETE FROM base.result_code WHERE key = '239';
