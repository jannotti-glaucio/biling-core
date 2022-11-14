--liquibase formatted sql
--changeset billing:1.01.51 dbms:postgresql context:dev,sandbox
--comment Atualizando dados das tabelas Bancarias revisadas

UPDATE base.bank_account SET description = token;
UPDATE base.bank_account SET token = '12345678901234', description = 'Conta no Banco do Brasil' WHERE token = 'BILLING-BB';

UPDATE base.dealer SET company_bank_account_id =
  (SELECT id FROM base.bank_account WHERE token = '12345678901234')
WHERE company_id = (SELECT id FROM base.company WHERE token = '1234');
