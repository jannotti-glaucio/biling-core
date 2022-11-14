--liquibase formatted sql
--changeset billing:1.03.03 dbms:postgresql
--comment Adicionando colunas para cobranca de taxas em transferencias

ALTER TABLE base.payment RENAME COLUMN market_fee TO provider_fee;
ALTER TABLE base.payment RENAME COLUMN payment_fee TO payment_cost;

ALTER TABLE base.market_withdraw ADD withdraw_fee integer;
UPDATE base.market_withdraw SET withdraw_fee = 0;
ALTER TABLE base.market_withdraw ALTER COLUMN withdraw_fee SET NOT NULL;

ALTER TABLE base.transfer RENAME COLUMN operation_date TO transfer_date;
ALTER TABLE base.transfer ADD transfer_cost integer;
UPDATE base.transfer SET transfer_cost = 0;
ALTER TABLE base.transfer ALTER COLUMN transfer_cost SET NOT NULL; 

ALTER TABLE base.company_bank_account ADD paid_billet_fee integer, ADD interbank_transfer_fee integer;
UPDATE base.company_bank_account SET paid_billet_fee = 0, interbank_transfer_fee = 0;
ALTER TABLE base.company_bank_account ALTER COLUMN paid_billet_fee SET NOT NULL,
  ALTER COLUMN interbank_transfer_fee SET NOT NULL;
 
ALTER TABLE base.company ADD minimum_invoice_amount integer,
  ADD maximum_invoice_amount integer,
  ADD minimum_market_withdraw_amount integer,
  ADD maximum_market_withdraw_amount integer;
UPDATE base.company SET minimum_invoice_amount = 0,
  maximum_invoice_amount = 99999999,
  minimum_market_withdraw_amount = 0,
  maximum_market_withdraw_amount = 99999999;
ALTER TABLE base.company ALTER COLUMN minimum_invoice_amount SET NOT NULL,
  ALTER COLUMN maximum_invoice_amount SET NOT NULL,
  ALTER COLUMN minimum_market_withdraw_amount SET NOT NULL,
  ALTER COLUMN maximum_market_withdraw_amount SET NOT NULL;

SELECT base.insert_result_code('251', 'WITHDRAW_AMOUNT_BELOW_MINIMUM', false, 'pt-BR',
  'Valor de Saque abaixo do mínimo permitido', 'R', null);
SELECT base.insert_result_code('252', 'WITHDRAW_AMOUNT_ABOVE_MAXIMUM', false, 'pt-BR',
  'Valor de Saque acima do máximo permitido', 'R', null);
SELECT base.insert_result_code('253', 'WITHDRAW_NET_AMOUNT_BELOW_MINIMUM', false, 'pt-BR',
  'Valor de Saque não pode ser menor que a taxa de Saque', 'R', null);
SELECT base.insert_result_code('254', 'INVOICE_AMOUNT_BELOW_MINIMUM', false, 'pt-BR',
  'Valor de Fatura abaixo do mínimo permitido', 'R', null);
SELECT base.insert_result_code('255', 'INVOICE_AMOUNT_ABOVE_MAXIMUM', false, 'pt-BR',
  'Valor de Fatura acima do máximo permitido', 'R', null);
SELECT base.insert_result_code('256', 'INVOICE_AMOUNT_BELOW_MINIMUM', false, 'pt-BR',
  'Valor de Fatura não pode ser menor que a taxa de Cobrança', 'R', null);
