--liquibase formatted sql
--changeset billing:1.03.06 dbms:postgresql
--comment Corrigindo o problema do fuso horario na data de emissao dos boletos do BB

ALTER TABLE base.payment_bank_billet ALTER COLUMN issue_date DROP NOT NULL;

ALTER TABLE banking_bb.bank_channel ADD timezone character varying(30);
UPDATE banking_bb.bank_channel SET timezone = 'America/Sao_Paulo';

SELECT base.insert_result_code('413', 'BANK_SYSTEM_UNAVAILABLE', false, 'pt-BR',
  'Sistema bancário indisponível, favor tentar novamente mais tarde', 'B', null);

INSERT INTO banking_bb.translate_billet_registry_error (mensagem, codigo, base_result_code_id) VALUES
  ('SERVICO INDISPONIVEL', 'S001I0333', (SELECT id from base.result_code where key = '413')),
  ('Sistema indisponivel no momento. tente mais tarde.', 'SR0040092', (SELECT id from base.result_code where key = '413'));
