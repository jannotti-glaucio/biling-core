--liquibase formatted sql
--changeset billing:1.01.06 dbms:postgresql
--comment Finalizando a criacao da tabela de Plano Tarifario

ALTER TABLE base.dealer ALTER COLUMN company_billing_plan_id SET NOT NULL;
