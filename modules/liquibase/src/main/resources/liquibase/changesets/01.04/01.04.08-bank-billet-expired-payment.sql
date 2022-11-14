--liquibase formatted sql
--changeset billing:1.04.08 dbms:postgresql
--comment Criando objetos para suporte pagamentos de Boleto com multa e juros

ALTER DOMAIN base.bank_discharge_operation_enum DROP CONSTRAINT bank_discharge_operation_enum_check;
ALTER DOMAIN base.bank_discharge_operation_enum ADD CONSTRAINT bank_discharge_operation_enum_check CHECK(VALUE IN ('R', 'P', 'C', 'G', 'U'));

-- Base

CREATE DOMAIN base.interest_frequency_enum AS character(1) CHECK(VALUE IN ('D', 'M'));

ALTER TABLE base.dealer ADD bank_billet_expired_payment boolean,
  ADD bank_billet_penalty_percent integer,
  ADD bank_billet_interest_frequency base.interest_frequency_enum,
  ADD bank_billet_interest_percent integer;
UPDATE base.dealer SET bank_billet_expired_payment = false;
ALTER TABLE base.dealer ALTER COLUMN bank_billet_expired_payment SET NOT NULL;

ALTER TABLE base.payment_bank_billet ADD expired_payment boolean,
  ADD penalty_start_date date,
  ADD penalty_percent integer,
  ADD interest_frequency base.interest_frequency_enum,
  ADD interest_percent integer;
UPDATE base.payment_bank_billet SET expired_payment = false;
ALTER TABLE base.payment_bank_billet ALTER COLUMN expired_payment SET NOT NULL;

ALTER TABLE base.bank ADD billet_payment_place_payable_expirated character varying(100);
ALTER TABLE base.bank ADD billet_instructions_payable_expirated text;
ALTER TABLE base.bank RENAME COLUMN billet_payment_place TO billet_payment_place_payable_until_expiration;
ALTER TABLE base.bank RENAME COLUMN billet_instructions TO billet_instructions_payable_until_expiration;
UPDATE base.bank SET billet_instructions_payable_expirated='Não aceitamos pagamentos em cheque.', 
  billet_payment_place_payable_expirated='Pagável em qualquer banco' WHERE code IN ('001', '033');

-- BB

ALTER TABLE banking_bb.company_bank_account RENAME COLUMN numero_convenio_cobranca TO convenio_cobranca_pagavel_ate_vencimento;
ALTER TABLE banking_bb.company_bank_account ADD convenio_cobranca_pagavel_vencido integer;
UPDATE banking_bb.company_bank_account SET convenio_cobranca_pagavel_vencido = convenio_cobranca_pagavel_ate_vencimento;
ALTER TABLE banking_bb.company_bank_account ALTER COLUMN convenio_cobranca_pagavel_vencido SET NOT NULL;

ALTER TABLE banking_bb.billet_registry_request DROP COLUMN valor_multa_titulo, DROP COLUMN valor_juro_mora_titulo;

-- Santander

ALTER TABLE banking_santander.billet_ticket_request ADD titulo_pc_multa smallint,
    ADD titulo_qtd_dias_multa smallint,
    ADD titulo_pc_juro smallint;
UPDATE banking_santander.billet_ticket_request SET titulo_pc_multa = 0, titulo_pc_juro = 0;
ALTER TABLE banking_santander.billet_ticket_request ALTER COLUMN titulo_pc_multa SET NOT NULL,
    ALTER COLUMN titulo_pc_juro SET NOT NULL;

-- Insere os result codes

SELECT base.insert_result_code('126', 'INVALID_INTEREST_FREQUENCY_PARAMETER', false, 'pt-BR',
  'Campo [%s] com frequência de juros inválida [%s]', 'V', null);
SELECT base.insert_result_code('227', 'INVALID_INVOICE_STATUS_TO_PURGE', false, 'pt-BR',
  'Fatura em status inválido para ser baixada', 'R', null);

SELECT base.insert_result_code('239', 'PENALTY_START_DATE_GREATER_THAN_EXPIRATION_DATE', false, 'pt-BR',
  'A data de início de cobrança de multa deve ser superior a data de vencimento', 'R', null);
