--liquibase formatted sql
--changeset billing:1.04.07 dbms:postgresql
--comment Criando tabelas para suporte a Assinaturas

CREATE DOMAIN base.order_type_enum AS character(1) NOT NULL CHECK(VALUE IN ('C', 'S'));
CREATE DOMAIN base.frequency_type_enum AS character(1) NOT NULL CHECK(VALUE IN ('M'));
CREATE DOMAIN base.subscription_status_enum AS character(1) NOT NULL CHECK(VALUE IN ('O', 'S', 'F', 'C'));

ALTER TABLE base.order ADD order_type character(1);
UPDATE base.order SET order_type = 'C';
ALTER TABLE base.order ALTER COLUMN order_type TYPE base.order_type_enum;

ALTER TABLE base.invoice ADD reference_date date;

CREATE TABLE base.order_subscription
(
  id bigint NOT NULL,
  customer_id bigint NOT NULL,
  document_number bigint,
  frequency_type base.frequency_type_enum,
  expiration_day smallint NOT NULL,
  end_date date,  
  amount integer NOT NULL,
  payment_method base.payment_method_enum,
  status base.subscription_status_enum,
  finishiment_date timestamp without time zone,
  CONSTRAINT pk_order_subscription PRIMARY KEY (id),
  CONSTRAINT fk_1_order_subscription FOREIGN KEY (id)
    REFERENCES base.order (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_2_order_subscription FOREIGN KEY (customer_id)
    REFERENCES base.customer (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION
);
ALTER TABLE base.order_subscription OWNER TO billing;

SELECT base.insert_result_code('123', 'INVALID_FREQUENCY_TYPE_PARAMETER', false, 'pt-BR', 'Campo [%s] com tipo frequência inválida [%s]',
  'V', null);
SELECT base.insert_result_code('124', 'INVALID_FUTURE_DATE_PARAMETER', false, 'pt-BR', 'Campo [%s] com data anterior a hoje [%s]',
  'V', null);
SELECT base.insert_result_code('125', 'INVALID_DAY_PARAMETER', false, 'pt-BR', 'Campo [%s] com dia inválido [%s]',
  'V', null);

SELECT base.insert_result_code('224', 'INVALID_SUBSCRIPTION_STATUS_TO_CANCEL', false, 'pt-BR',
  'Assinatura em status inválido para ser cancelada', 'R', null);
SELECT base.insert_result_code('225', 'INVALID_SUBSCRIPTION_STATUS_TO_SUSPEND', false, 'pt-BR',
  'Assinatura em status inválido para ser suspensa', 'R', null);
SELECT base.insert_result_code('226', 'INVALID_SUBSCRIPTION_STATUS_TO_REOPEN', false, 'pt-BR',
  'Assinatura em status inválido para ser reativada', 'R', null);

SELECT base.insert_result_code('269', 'INVALID_SUBSCRIPTION_STATUS_TO_UPDATE', false, 'pt-BR',
  'Assinatura em status inválido para ser atualizada', 'R', null);
