--liquibase formatted sql
--changeset billing:1.03.51 dbms:postgresql context:dev,sandbox
--comment Atualizando os dados de taxas dos Fornecedores de de teste

UPDATE base.company_billing_plan SET paid_bank_billet_fee = 450, market_withdraw_fee = 500 WHERE token = '1234';

UPDATE base.company_bank_account SET paid_billet_fee = 120, interbank_transfer_fee  = 350
WHERE company_id = (SELECT id FROM base.company WHERE token = '1234');

UPDATE base.company SET minimum_invoice_amount = 1000,
  maximum_invoice_amount = 99999,
  minimum_market_withdraw_amount = 1000,
  maximum_market_withdraw_amount = 99999
WHERE token = '1234';
