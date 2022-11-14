--liquibase formatted sql
--changeset billing:1.04.52 dbms:postgresql context:dev,sandbox
--comment Atualiznado o custo de registro de boletos

UPDATE base.company_bank_account SET registered_billet_fee = 014
  WHERE id = (SELECT id FROM base.bank_account WHERE token = '34567890123456');

UPDATE base.payment_bank_billet SET registration_cost = 014 WHERE company_bank_account_id =
  (SELECT id FROM base.bank_account WHERE token = '34567890123456');
