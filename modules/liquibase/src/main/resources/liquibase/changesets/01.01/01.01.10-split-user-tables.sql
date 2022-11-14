--liquibase formatted sql
--changeset billing:1.01.10 dbms:postgresql
--comment Dividindo tabelas de usuarios por nivel (Company, Dealer e Customer)

-- Divide as tabelas

CREATE TABLE base.company_user
(
  id bigint NOT NULL,
  company_id smallint NOT NULL,
  CONSTRAINT pk_company_user PRIMARY KEY (id),
  CONSTRAINT fk_1_company_user FOREIGN KEY (company_id)
    REFERENCES base.company (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION
);
ALTER TABLE base.company_user OWNER TO billing;

CREATE TABLE base.dealer_user
(
  id bigint NOT NULL,
  dealer_id bigint NOT NULL,
  CONSTRAINT pk_dealer_user PRIMARY KEY (id),
  CONSTRAINT fk_1_dealer_user FOREIGN KEY (dealer_id)
    REFERENCES base.dealer (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION
);
ALTER TABLE base.dealer_user OWNER TO billing;

CREATE TABLE base.customer_user
(
  id bigint NOT NULL,
  customer_id bigint NOT NULL,
  CONSTRAINT pk_customer_user PRIMARY KEY (id),
  CONSTRAINT fk_1_customer_user FOREIGN KEY (customer_id)
    REFERENCES base.customer (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION
);
ALTER TABLE base.customer_user OWNER TO billing;


INSERT INTO base.company_user (id, company_id)
SELECT id, company_id FROM base.user WHERE company_id IS NOT NULL;

INSERT INTO base.dealer_user (id, dealer_id)
SELECT id, dealer_id FROM base.user WHERE dealer_id IS NOT NULL;

INSERT INTO base.customer_user (id, customer_id)
SELECT id, customer_id FROM base.user WHERE customer_id IS NOT NULL;


ALTER TABLE base.user RENAME COLUMN name TO real_name;
ALTER TABLE base.user DROP COLUMN company_id,
  DROP COLUMN dealer_id,
  DROP COLUMN customer_id;

ALTER TABLE base.user ADD COLUMN token character varying(12);
  
ALTER TABLE base.user ADD COLUMN creation_date timestamp without time zone;
UPDATE base.user SET creation_date = now();
ALTER TABLE base.user ALTER COLUMN creation_date SET NOT NULL;

ALTER TABLE base.user ADD COLUMN deletion_date timestamp without time zone;


-- Atualiza os result codes

UPDATE base.result_code SET key = '264' WHERE key = '216';
UPDATE base.result_code SET key = '216' WHERE key = '217';
UPDATE base.result_code SET key = '217' WHERE key = '250';
UPDATE base.result_code SET key = '265' WHERE key = '218';

SELECT base.insert_result_code('250', 'DUPLICATED_USERNAME', false, 'pt-BR', 'Nome de usuário [%s] já utilizado', 'R', null);
SELECT base.insert_result_code('260', 'INVALID_USER_STATUS_TO_UPDATE', false, 'pt-BR', 'Usuário em status inválido para ser atualizado', 'R', null);
SELECT base.insert_result_code('261', 'INVALID_COMPANY_STATUS_TO_UPDATE', false, 'pt-BR', 'Empresa em status inválido para ser atualizado', 'R', null);
SELECT base.insert_result_code('262', 'INVALID_DEALER_STATUS_TO_UPDATE', false, 'pt-BR', 'Fornecedor em status inválido para ser atualizado', 'R', null);
SELECT base.insert_result_code('263', 'INVALID_CUSTOMER_STATUS_TO_UPDATE', false, 'pt-BR', 'Cliente em status inválido para ser atualizado', 'R', null);
SELECT base.insert_result_code('270', 'INVALID_USER_STATUS_TO_BLOCK', false, 'pt-BR', 'Usuário em status inválido para ser bloqueado', 'R', null);
SELECT base.insert_result_code('271', 'INVALID_USER_STATUS_TO_UNBLOCK', false, 'pt-BR', 'Usuário em status inválido para ser desbloqueado', 'R', null);
