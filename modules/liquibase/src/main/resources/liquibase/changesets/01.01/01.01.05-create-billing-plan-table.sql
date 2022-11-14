--liquibase formatted sql
--changeset billing:1.01.05 dbms:postgresql
--comment Criando tabela de Plano Tarifario

-- Cria tabela
CREATE TABLE base.company_billing_plan
(
  id smallserial NOT NULL,
  company_id smallint NOT NULL,
  token character varying(4) NOT NULL,
  description character varying(30) NOT NULL,
  paid_bank_billet_fee integer,
  CONSTRAINT pk_company_billing_plan PRIMARY KEY (id),
  CONSTRAINT fk_1_company_billing_plan FOREIGN KEY (company_id)
    REFERENCES base.company (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT uk_1_company_billing_plan UNIQUE (token)
);
ALTER TABLE base.company_billing_plan OWNER TO billing;

-- Relaciona com a tabela dealer
ALTER TABLE base.dealer ADD COLUMN company_billing_plan_id smallint;
ALTER TABLE base.dealer ADD CONSTRAINT fk_4_dealer FOREIGN KEY (company_billing_plan_id)
  REFERENCES base.company_billing_plan (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION;

-- Inclui campos de data na tabela dealer
ALTER TABLE base.dealer ADD COLUMN creation_date timestamp without time zone;
UPDATE base.dealer SET creation_date = now();
ALTER TABLE base.dealer ALTER COLUMN creation_date SET NOT NULL;
ALTER TABLE base.dealer ADD COLUMN deletion_date timestamp without time zone;
