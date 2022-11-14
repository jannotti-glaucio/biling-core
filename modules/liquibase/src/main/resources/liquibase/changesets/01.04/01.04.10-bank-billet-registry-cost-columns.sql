--liquibase formatted sql
--changeset billing:1.04.10 dbms:postgresql
--comment Criando colunas para guardar o custo de regitros de boletos

ALTER TABLE base.company_bank_account ADD registered_billet_fee integer;
UPDATE base.company_bank_account SET registered_billet_fee = 0;
ALTER TABLE base.company_bank_account ALTER COLUMN registered_billet_fee SET NOT NULL;

ALTER TABLE base.payment_bank_billet ADD registration_cost integer;
UPDATE base.payment_bank_billet b SET registration_cost = 0 FROM base.payment p WHERE p.id = b.id AND p.status != 'E' 
