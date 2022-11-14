--liquibase formatted sql
--changeset billing:1.03.04 dbms:postgresql
--comment Correcoes relativas ao cancelamento de boletos

ALTER DOMAIN base.payment_status_enum RENAME TO payment_status_enum_tmp;
CREATE DOMAIN base.payment_status_enum AS character varying(2) NOT NULL
  CHECK(VALUE IN ('G', 'D', 'A', 'P', 'CR', 'C', 'R', 'T', 'E'));
ALTER TABLE base.payment ALTER COLUMN status TYPE base.payment_status_enum;
DROP DOMAIN base.payment_status_enum_tmp;

ALTER TABLE base.payment ADD cancelation_request_date timestamp without time zone;
UPDATE base.payment SET cancelation_request_date = (cancelation_date - interval '1 day') WHERE status = 'C';

ALTER TABLE banking_bb.cnab400_discharge_detail ALTER COLUMN dv_prefixo_agencia_recebedora TYPE character(1);

DELETE FROM base.result_code_message WHERE result_code_id IN (SELECT id FROM base.result_code WHERE key IN ('264','265'));
DELETE FROM base.result_code WHERE key IN ('264','265');
