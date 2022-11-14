--liquibase formatted sql
--changeset billing:1.02.03 dbms:postgresql
--comment Criando tabelas de suporte a Aplicacoes externas

CREATE DOMAIN base.notification_status_enum AS character(1) NOT NULL CHECK(VALUE IN ('P', 'D'));

CREATE TABLE base.application
(
  id bigserial NOT NULL,
  dealer_id bigint NOT NULL,
  token character varying(9) NOT NULL,
  name character varying(50) NOT NULL,
  client_id character varying(30) NOT NULL,
  client_secret character varying(60) NOT NULL,
  status base.entity_status_enum,
  creation_date timestamp without time zone NOT NULL,
  deletion_date timestamp without time zone,
  CONSTRAINT pk_application PRIMARY KEY (id),
  CONSTRAINT fk_1_application FOREIGN KEY (dealer_id)
    REFERENCES base.dealer (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT uk_1_application UNIQUE (token),
  CONSTRAINT uk_2_application UNIQUE (client_id)
);
ALTER TABLE base.application OWNER TO billing;

CREATE TABLE base.notification_type
(
  id smallserial NOT NULL,
  code character varying(3) NOT NULL,
  name character varying(50) NOT NULL,
  CONSTRAINT pk_notification_type PRIMARY KEY (id),
  CONSTRAINT uk_1_notification_type UNIQUE (code)
);
ALTER TABLE base.notification_type OWNER TO billing;

CREATE TABLE base.notification
(
  id bigserial NOT NULL,
  notification_type_id smallint NOT NULL,
  destination_url character varying(200) NOT NULL,
  message_content text,
  status base.notification_status_enum,
  creation_date timestamp without time zone NOT NULL,
  delivery_date timestamp without time zone,
  CONSTRAINT pk_notification PRIMARY KEY (id),
  CONSTRAINT fk_1_notification FOREIGN KEY (notification_type_id)
    REFERENCES base.notification_type (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION
);
ALTER TABLE base.notification OWNER TO billing;

CREATE TABLE base.invoice_notification
(
  id bigint NOT NULL,
  invoice_id bigint NOT NULL,
  CONSTRAINT pk_invoice_notification PRIMARY KEY (id),
  CONSTRAINT fk_1_invoice_notification FOREIGN KEY (id)
    REFERENCES base.notification (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_2_invoice_notification FOREIGN KEY (invoice_id)
    REFERENCES base.invoice (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION    
);
ALTER TABLE base.invoice_notification OWNER TO billing;

CREATE TABLE base.payment_notification
(
  id bigint NOT NULL,
  payment_id bigint NOT NULL,
  CONSTRAINT pk_payment_notification PRIMARY KEY (id),
  CONSTRAINT fk_1_payment_notification FOREIGN KEY (id)
    REFERENCES base.notification (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_2_payment_notification FOREIGN KEY (payment_id)
    REFERENCES base.payment (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION    
);
ALTER TABLE base.payment_notification OWNER TO billing;

CREATE TABLE base.notify_request
(
  id bigserial NOT NULL,
  notification_id bigint NOT NULL,
  request_date timestamp without time zone NOT NULL,
  CONSTRAINT pk_notify_request PRIMARY KEY (id),
  CONSTRAINT fk_1_notify_request FOREIGN KEY (notification_id)
    REFERENCES base.notification (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION
);
ALTER TABLE base.notify_request OWNER TO billing;

CREATE TABLE base.notify_response
(
  id bigserial NOT NULL,
  notify_request_id bigint NOT NULL,
  response_date timestamp without time zone NOT NULL,
  http_status integer,
  CONSTRAINT pk_notify_response PRIMARY KEY (id),
  CONSTRAINT fk_1_notify_response FOREIGN KEY (notify_request_id)
    REFERENCES base.notify_request (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION
);
ALTER TABLE base.notify_response OWNER TO billing;

ALTER TABLE base.invoice ADD COLUMN callback_url character varying(200);

INSERT INTO base.notification_type (code, name) VALUES ('ISU', 'Invoice Status Update');
SELECT base.insert_result_code('218', 'INVALID_APPLICATION_STATUS_TO_DELETE', false, 'pt-BR', 'Aplicação em status inválido para ser excluída', 'R', null);
SELECT base.insert_result_code('266', 'INVALID_APPLICATION_STATUS_TO_UPDATE', false, 'pt-BR', 'Aplicação em status inválido para ser atualizada', 'R', null);
