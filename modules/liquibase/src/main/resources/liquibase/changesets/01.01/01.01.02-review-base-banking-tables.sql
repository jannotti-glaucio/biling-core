--liquibase formatted sql
--changeset billing:1.01.02 dbms:postgresql
--comment Revisando as tabelas de Contas Bancarias

-- Restrutra as tabelas

CREATE TABLE base.company_bank_account 
(
  id bigint NOT NULL,
  company_id smallint NOT NULL,
  acceptance boolean NOT NULL,
  enviroment base.bank_enviroment_enum NOT NULL,
  online_billet_registration boolean NOT NULL,
  billet_unpaid_limit integer NOT NULL,
  CONSTRAINT pk_company_bank_account PRIMARY KEY (id),
  CONSTRAINT fk_1_company_bank_account FOREIGN KEY (id)
    REFERENCES base.bank_account (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION,  
  CONSTRAINT fk_2_company_bank_account FOREIGN KEY (company_id)
    REFERENCES base.company (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION
);
ALTER TABLE base.company_bank_account OWNER TO billing;

INSERT INTO base.company_bank_account (id, company_id, acceptance, enviroment, online_billet_registration, billet_unpaid_limit)
SELECT id, company_id, acceptance, enviroment, online_billet_registration, billet_unpaid_limit FROM base.bank_account;

CREATE TABLE base.dealer_bank_account 
(
  id bigint NOT NULL,
  dealer_id bigint NOT NULL,
  CONSTRAINT pk_dealer_bank_account PRIMARY KEY (id),
  CONSTRAINT fk_1_dealer_bank_account FOREIGN KEY (id)
    REFERENCES base.bank_account (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION,  
  CONSTRAINT fk_2_dealer_bank_account FOREIGN KEY (dealer_id)
    REFERENCES base.dealer (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION
);
ALTER TABLE base.dealer_bank_account OWNER TO billing;

CREATE TABLE base.customer_bank_account 
(
  id bigint NOT NULL,
  customer_id bigint NOT NULL,
  CONSTRAINT pk_customer_bank_account PRIMARY KEY (id),
  CONSTRAINT fk_1_customer_bank_account FOREIGN KEY (id)
    REFERENCES base.bank_account (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION,  
  CONSTRAINT fk_2_customer_bank_account FOREIGN KEY (customer_id)
    REFERENCES base.customer (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION
);
ALTER TABLE base.customer_bank_account OWNER TO billing;

ALTER TABLE base.bank_account DROP CONSTRAINT uk_1_bank_account;
ALTER TABLE base.bank_account ALTER COLUMN id TYPE bigint,
  DROP COLUMN company_id,
  DROP COLUMN acceptance,
  DROP COLUMN enviroment,
  DROP COLUMN online_billet_registration,
  DROP COLUMN billet_unpaid_limit;
ALTER TABLE base.bank_account RENAME COLUMN alias TO token;
ALTER TABLE base.bank_account ADD COLUMN description character varying(50);
ALTER TABLE base.bank_account RENAME CONSTRAINT uk_2_bank_account TO uk_1_bank_account;

ALTER TABLE base.bank_account ADD COLUMN status character(1);
UPDATE base.bank_account SET status = 'A';
ALTER TABLE base.bank_account ALTER COLUMN status TYPE base.entity_child_status_enum;

-- Revisa as referencias das tabelas

-- Cria referencia de bank remittance e discharge para bank account
ALTER TABLE base.bank_remittance ADD bank_account_id bigint;
ALTER TABLE base.bank_remittance ADD CONSTRAINT fk_2_bank_remittance FOREIGN KEY (bank_account_id)
  REFERENCES base.bank_account (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION;
UPDATE base.bank_remittance r SET bank_account_id = b.bank_account_id FROM base.payment_bank_billet b
  WHERE b.id = r.payment_bank_billet_id;
ALTER TABLE base.bank_remittance ALTER COLUMN bank_account_id SET NOT NULL;

ALTER TABLE base.bank_discharge ADD bank_account_id bigint;
ALTER TABLE base.bank_discharge ADD CONSTRAINT fk_4_bank_discharge FOREIGN KEY (bank_account_id)
  REFERENCES base.bank_account (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION;  
UPDATE base.bank_discharge d SET bank_account_id = b.bank_account_id FROM base.payment_bank_billet b
  WHERE b.id = d.payment_bank_billet_id;
ALTER TABLE base.bank_discharge ALTER COLUMN bank_account_id SET NOT NULL;

-- Altera referencia de bank remittance e discharge de bank billet para payment
ALTER TABLE base.bank_remittance RENAME COLUMN payment_bank_billet_id TO payment_id;
ALTER TABLE base.bank_remittance DROP CONSTRAINT fk_1_bank_remittance;
ALTER TABLE base.bank_remittance ADD CONSTRAINT fk_1_bank_remittance FOREIGN KEY (payment_id)
  REFERENCES base.payment (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE base.bank_discharge RENAME COLUMN payment_bank_billet_id TO payment_id;
ALTER TABLE base.bank_discharge DROP CONSTRAINT fk_1_bank_remittance;
ALTER TABLE base.bank_discharge RENAME CONSTRAINT fk_2_bank_remittance TO fk_2_bank_discharge;
ALTER TABLE base.bank_discharge RENAME CONSTRAINT fk_3_bank_remittance TO fk_3_bank_discharge;
ALTER TABLE base.bank_discharge ADD CONSTRAINT fk_1_bank_discharge FOREIGN KEY (payment_id)
  REFERENCES base.payment (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION;

-- Muda a referencia de payment bank billet de bank account para company bank account
ALTER TABLE base.payment_bank_billet DROP CONSTRAINT fk_2_payment_bank_billet;
ALTER TABLE base.payment_bank_billet RENAME COLUMN bank_account_id TO company_bank_account_id;
ALTER TABLE base.payment_bank_billet ALTER COLUMN company_bank_account_id TYPE bigint;
ALTER TABLE base.payment_bank_billet ADD CONSTRAINT fk_2_payment_bank_billet FOREIGN KEY (company_bank_account_id)
  REFERENCES base.company_bank_account (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION;
-- Adiciona unique pelo billet_number
ALTER TABLE base.payment_bank_billet ADD CONSTRAINT uk_1_payment_bank_billet UNIQUE (company_bank_account_id, billet_number);    

-- Muda a referencia de dealer de bank account para company bank account  
ALTER TABLE base.dealer DROP CONSTRAINT fk_3_dealer;
ALTER TABLE base.dealer RENAME COLUMN bank_account_id TO company_bank_account_id;
ALTER TABLE base.dealer ALTER COLUMN company_bank_account_id TYPE bigint;
ALTER TABLE base.dealer ADD CONSTRAINT fk_3_dealer FOREIGN KEY (company_bank_account_id)
  REFERENCES base.company_bank_account (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION;


-- Adiciona novo result code
SELECT base.insert_result_code('203', 'ACCOUNT_TOKEN_NOT_FOUND', false, 'pt-BR', 'Token de conta [%s] não localizado', 'R', null);
