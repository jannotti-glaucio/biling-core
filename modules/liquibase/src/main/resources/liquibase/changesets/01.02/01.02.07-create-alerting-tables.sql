--liquibase formatted sql
--changeset billing:1.02.07 dbms:postgresql
--comment Criando tabelas para suporte de envio de alertas de Faturas

-- Cria os objetos

CREATE DOMAIN base.media_type_enum AS character(1) NOT NULL CHECK(VALUE IN ('E', 'W', 'S'));
CREATE DOMAIN base.alert_status_enum AS character(1) NOT NULL CHECK(VALUE IN ('P', 'D'));

CREATE TABLE base.alert_type (
  id smallserial NOT NULL,
  code character varying(3) NOT NULL,
  name character varying (50) NOT NULL,
  CONSTRAINT pk_alert_type PRIMARY KEY (id),
  CONSTRAINT uk_1_alert_type UNIQUE (code)
);
ALTER TABLE base.alert_type OWNER TO billing;

CREATE TABLE base.alert (
  id bigserial NOT NULL,
  media_type base.media_type_enum,
  alert_type_id smallint NOT NULL,
  status base.alert_status_enum,
  creation_date timestamp without time zone NOT NULL,
  request_date timestamp without time zone,
  response_date timestamp without time zone,  
  result_code_id integer,
  CONSTRAINT pk_alert PRIMARY KEY (id),
  CONSTRAINT fk_1_alert FOREIGN KEY (alert_type_id)
    REFERENCES base.alert_type (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_2_alert FOREIGN KEY (result_code_id)
    REFERENCES base.result_code (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION
);
ALTER TABLE base.alert OWNER TO billing;

CREATE TABLE base.invoice_alert (
  id bigint NOT NULL,
  invoice_id bigint NOT NULL,
  CONSTRAINT pk_invoice_alert PRIMARY KEY (id),
  CONSTRAINT fk_1_invoice_alert FOREIGN KEY (id)
    REFERENCES base.alert (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION,  
  CONSTRAINT fk_2_invoice_alert FOREIGN KEY (invoice_id)
    REFERENCES base.invoice (id) MATCH SIMPLE
	ON UPDATE NO ACTION ON DELETE NO ACTION
);
ALTER TABLE base.invoice_alert OWNER TO billing;

ALTER TABLE base.dealer ALTER COLUMN email SET NOT NULL;
ALTER TABLE base.dealer RENAME COLUMN bank_billet_registry_delivery_mode TO bank_billet_registry_mode;


-- Insere os dados

INSERT INTO base.alert_type (code, name) VALUES ('IS', 'Invoice Send');
INSERT INTO base.alert_type (code, name) VALUES ('CIE', 'Collection Invoice Expiring');

SELECT base.insert_result_code('122', 'INVALID_BANK_REMITTANCE_DELIVERY_MODE_PARAMETER', false, 'pt-BR', 
  'Campo [%s] com modo de envio de remessa bancária inválido', 'V', null);
