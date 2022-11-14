--liquibase formatted sql
--changeset billing:1.02.02 dbms:postgresql
--comment Finalizando adicao do suporte a registro online de boleto no BB

ALTER TABLE base.company_bank_account ALTER COLUMN bank_channel_id SET NOT NULL;

ALTER TABLE banking_bb.company_bank_account ALTER COLUMN especie_titulo_ws SET NOT NULL,
  ALTER COLUMN chave_usuario_j SET NOT NULL;
