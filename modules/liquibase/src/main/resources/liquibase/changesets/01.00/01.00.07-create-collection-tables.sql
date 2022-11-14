--liquibase formatted sql
--changeset billing:1.00.07 dbms:postgresql
--comment Criando tabelas e dados para o supporte a Parcelamentos

CREATE DOMAIN base.collection_status_enum AS character(1) NOT NULL CHECK(VALUE IN ('O', 'F', 'C'));

CREATE TABLE base.order
(
  id bigserial NOT NULL,
  token character varying(22) NOT NULL,
  description character varying(100),
  creation_date  timestamp without time zone NOT NULL,
  cancelation_date timestamp without time zone,
  CONSTRAINT pk_order PRIMARY KEY (id),  
  CONSTRAINT uk_1_order UNIQUE (token)
);
ALTER TABLE base.order OWNER TO billing;

CREATE TABLE base.order_collection
(
  id bigint NOT NULL,
  customer_id bigint NOT NULL,
  document_number bigint,
  instalments smallint NOT NULL,
  status base.collection_status_enum,
  CONSTRAINT pk_order_collection PRIMARY KEY (id),
  CONSTRAINT fk_1_order_collection FOREIGN KEY (id)
    REFERENCES base.order (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_2_order_collection FOREIGN KEY (customer_id)
    REFERENCES base.customer (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION
);
ALTER TABLE base.order_collection OWNER TO billing;

ALTER TABLE base.invoice ADD order_id bigint;
ALTER TABLE base.invoice ADD CONSTRAINT fk_2_invoice FOREIGN KEY (order_id)
  REFERENCES base.order (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION;

SELECT base.insert_result_code('119', 'INVALID_COLLECTION_AMOUNT_TYPE_PARAMETER', false, 'pt-BR', 'Campo [%s] com tipo de valor de parcelamento inválido [%s]', 'V', null);  
SELECT base.insert_result_code('217', 'INVALID_COLLECTION_STATUS_TO_CANCEL', false, 'pt-BR', 'Parcelamento em status inválido para ser cancelado', 'R', null);
SELECT base.insert_result_code('218', 'INVALID_COLLECTION_STATUS_TO_UPDATE', false, 'pt-BR', 'Parcelamento em status inválido para ser atualizado', 'R', null);
