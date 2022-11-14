--liquibase formatted sql
--changeset billing:1.04.04 dbms:postgresql
--comment Revisando as tabelas de traducao de natureza de recebimento do CNAB400 do BB

ALTER TABLE banking_bb.translate_comando_natureza RENAME TO translate_cnab400_natureza;
ALTER TABLE banking_bb.translate_cnab400_natureza RENAME COLUMN natureza_recebimento_codigo TO codigo;
ALTER TABLE banking_bb.translate_cnab400_natureza RENAME COLUMN natureza_recebimento_mensagem TO mensagem;

ALTER TABLE banking_bb.translate_cnab400_natureza DROP COLUMN translate_comando_id;
DROP TABLE banking_bb.translate_comando;

ALTER TABLE banking_bb.billet_registry_response DROP COLUMN texto_numero_titulo_cobranca_bb;
ALTER TABLE banking_bb.cnab400_remittance_detail ALTER COLUMN tipo_inscricao_sacado TYPE smallint USING tipo_inscricao_sacado::smallint;
ALTER TABLE banking_bb.cnab400_discharge_detail ALTER COLUMN indicativo_debito_credito TYPE smallint USING indicativo_debito_credito::smallint;

ALTER TABLE banking_bb.translate_billet_registry_error RENAME TO translate_billet_registry_erro;
ALTER TABLE banking_bb.translate_billet_registry_erro RENAME CONSTRAINT pk_translate_billet_registry_error TO pk_translate_billet_registry_erro;
ALTER TABLE banking_bb.translate_billet_registry_erro RENAME CONSTRAINT uk_1_translate_billet_registry_error TO uk_1_translate_billet_registry_erro;
ALTER TABLE banking_bb.translate_billet_registry_erro RENAME CONSTRAINT fk_1_translate_billet_registry_error TO fk_1_translate_billet_registry_erro;