--liquibase formatted sql
--changeset billing:1.01.52 dbms:postgresql context:dev,sandbox
--comment Insere dados de Plano Tarifario de teste

INSERT INTO base.company_billing_plan (company_id, token, description, paid_bank_billet_fee) VALUES (
  (SELECT id FROM base.company WHERE token = '1234'), '1234', 'Plano de Testes', 400
);

UPDATE base.dealer SET company_billing_plan_id =
  (SELECT id FROM base.company_billing_plan WHERE token = '1234')
WHERE company_id = (SELECT id FROM base.company WHERE token = '1234');
