--liquibase formatted sql
--changeset billing:1.03.02 dbms:postgresql
--comment Adicionando novas colunas na tabela de Planos Tarifários

ALTER TABLE base.company_billing_plan ADD market_withdraw_fee integer;
UPDATE base.company_billing_plan SET market_withdraw_fee = 0;
ALTER TABLE base.company_billing_plan ALTER COLUMN market_withdraw_fee SET NOT NULL;

ALTER TABLE base.company_billing_plan ADD status character(1);
UPDATE base.company_billing_plan SET status = 'A';
ALTER TABLE base.company_billing_plan ALTER COLUMN status TYPE base.entity_status_enum;

ALTER TABLE base.company_billing_plan ADD creation_date timestamp without time zone;
UPDATE base.company_billing_plan SET creation_date = now();
ALTER TABLE base.company_billing_plan ALTER COLUMN creation_date SET NOT NULL;

ALTER TABLE base.company_billing_plan ADD deletion_date timestamp without time zone;

SELECT base.insert_result_code('219', 'INVALID_BILLING_PLAN_STATUS_TO_DELETE', false, 'pt-BR', 'Plano Tarifário em status inválido para ser excluído', 'R', null);
SELECT base.insert_result_code('257', 'COULD_NOT_DELETE_IN_USE_BILLING_PLAN', false, 'pt-BR', 'Plano Tarifário em uso', 'R', null);
SELECT base.insert_result_code('267', 'INVALID_BILLING_PLAN_STATUS_TO_UPDATE', false, 'pt-BR', 'Plano Tarifário em status inválido para ser atualizado', 'R', null);
