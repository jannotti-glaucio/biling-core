--liquibase formatted sql
--changeset billing:1.04.01 dbms:postgresql
--comment Criacao de schema banking_santander tabelas

-- schema
CREATE SCHEMA banking_santander;
ALTER SCHEMA banking_santander OWNER TO billing;


-- tabelas

CREATE TABLE banking_santander.document_type (
  id smallserial NOT NULL,
  base_document_type_id smallint NOT NULL,
  tipo_de_documento smallint NOT NULL,
  CONSTRAINT pk_document_type PRIMARY KEY (id),
  CONSTRAINT fk_1_document_type FOREIGN KEY (base_document_type_id)
    REFERENCES base.document_type (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION
);
ALTER TABLE banking_santander.document_type OWNER TO billing;

CREATE TABLE banking_santander.company_bank_account (
  id serial NOT NULL,
  base_company_bank_account_id integer NOT NULL,
  codigo_convenio_cobranca integer NOT NULL,
  codigo_estacao_cobranca character varying(4) NOT NULL,
  especie_titulo_xml smallint NOT NULL,
  especie_titulo_boleto character varying(2) NOT NULL,
  especie_titulo_cnab smallint NOT NULL,
  codigo_carteira_boleto smallint NOT NULL,
  sigla_carteira_boleto character varying(3) NOT NULL,
  codigo_carteira_cnab smallint NOT NULL,
  codigo_transmissao_cnab character varying(20) NOT NULL,
  remittance_files_dir character varying(100) NOT NULL,
  discharge_files_source_dir character varying(100) NOT NULL,
  discharge_files_processed_dir character varying(100) NOT NULL,
  discharge_files_rejected_dir character varying(100) NOT NULL,
  CONSTRAINT pk_company_bank_account PRIMARY KEY (id),
  CONSTRAINT fk_1_company_bank_account FOREIGN KEY (base_company_bank_account_id)
    REFERENCES base.company_bank_account (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION
);
ALTER TABLE banking_santander.company_bank_account OWNER TO billing;

CREATE TABLE banking_santander.bank_channel (
  id smallserial NOT NULL,
  base_bank_channel_id smallint NOT NULL,
  ticket_url character varying(100) NOT NULL,
  cobranca_url character varying(100) NOT NULL,
  request_timeout integer NOT NULL,
  expiracao_ticket smallint NOT NULL,
  tipo_ambiente_xml character(1) NOT NULL,
  CONSTRAINT pk_bank_channel PRIMARY KEY (id),
  CONSTRAINT fk_1_bank_channel FOREIGN KEY (base_bank_channel_id) REFERENCES base.bank_channel(id),
  CONSTRAINT uk_1_bank_channel UNIQUE (base_bank_channel_id)
);
ALTER TABLE banking_santander.bank_channel OWNER TO billing;

ALTER TABLE base.bank_channel ALTER COLUMN code TYPE varchar(20);


-- soap

CREATE TABLE banking_santander.billet_ticket_request (
  id bigserial NOT NULL,
  base_payment_bank_billet_id bigint NOT NULL,    
  base_bank_remittance_id bigint NOT NULL,
  request_date timestamp without time zone NOT NULL,
  pagador_tipo_documento smallint NOT NULL,
  pagador_numero_documento character varying(15) NOT NULL,
  titulo_nosso_numero bigint NOT NULL,
  titulo_seu_numero bigint NOT NULL,
  titulo_data_vencimento date NOT NULL,
  titulo_data_emissao date NOT NULL,
  titulo_especie smallint NOT NULL,
  titulo_valor_nominal integer NOT NULL,
  titulo_qtd_dias_baixa smallint NOT NULL,
  CONSTRAINT pk_billet_ticket_request PRIMARY KEY (id),
  CONSTRAINT fk_1_billet_ticket_request FOREIGN KEY (base_payment_bank_billet_id) REFERENCES base.payment_bank_billet(id),
  CONSTRAINT fk_2_billet_ticket_request FOREIGN KEY (base_bank_remittance_id) REFERENCES base.bank_remittance(id),
  CONSTRAINT uk_1_billet_ticket_request UNIQUE (base_payment_bank_billet_id),
  CONSTRAINT uk_2_billet_ticket_request UNIQUE (base_bank_remittance_id)
);
ALTER TABLE banking_santander.billet_ticket_request OWNER TO billing;

CREATE TABLE banking_santander.billet_ticket_response (
  id bigserial NOT NULL,
  billet_ticket_request_id bigint NOT NULL,
  response_date timestamp without time zone NOT NULL,
  ret_code smallint NOT NULL,
  message character varying(100),
  CONSTRAINT pk_billet_ticket_response PRIMARY KEY (id),
  CONSTRAINT fk_1_billet_ticket_response FOREIGN KEY (billet_ticket_request_id) REFERENCES banking_santander.billet_ticket_request(id),
  CONSTRAINT uk_1_billet_ticket_response UNIQUE (billet_ticket_request_id)

);
ALTER TABLE banking_santander.billet_ticket_response OWNER TO billing;

CREATE TABLE banking_santander.billet_registry_request (
  id bigserial NOT NULL,
  base_payment_bank_billet_id bigint NOT NULL,    
  base_bank_remittance_id bigint NOT NULL,
  billet_ticket_response_id bigint NOT NULL,
  request_date timestamp without time zone NOT NULL,
  dt_nsu date NOT NULL,
  nsu character varying(20) NOT NULL,
  tp_ambiente character(1) NOT NULL,
  CONSTRAINT pk_billet_registry_request PRIMARY KEY (id),
  CONSTRAINT fk_1_billet_registry_request FOREIGN KEY (base_payment_bank_billet_id) REFERENCES base.payment_bank_billet(id),
  CONSTRAINT fk_2_billet_registry_request FOREIGN KEY (base_bank_remittance_id) REFERENCES base.bank_remittance(id),
  CONSTRAINT fk_3_billet_registry_request FOREIGN KEY (billet_ticket_response_id) REFERENCES banking_santander.billet_ticket_response(id),
  CONSTRAINT uk_1_billet_registry_request UNIQUE (base_payment_bank_billet_id),
  CONSTRAINT uk_2_billet_registry_request UNIQUE (base_bank_remittance_id)
);
ALTER TABLE banking_santander.billet_registry_request OWNER TO billing;

CREATE TABLE banking_santander.billet_registry_response (
  id bigserial NOT NULL,
  billet_registry_request_id bigint NOT NULL,
  response_date timestamp without time zone NOT NULL,
  situacao smallint,
  descricao_erro_codigo character varying(5),
  descricao_erro_mensagem character varying(100),
  titulo_lin_dig character varying(47),
  CONSTRAINT pk_billet_registry_response PRIMARY KEY (id),
  CONSTRAINT fk_1_billet_registry_response FOREIGN KEY (billet_registry_request_id) REFERENCES banking_santander.billet_registry_request(id),
  CONSTRAINT uk_1_billet_registry_response UNIQUE (billet_registry_request_id)

);
ALTER TABLE banking_santander.billet_registry_response OWNER TO billing;


CREATE TABLE banking_santander.translate_billet_registry_erro (
  id serial NOT NULL,
  codigo character varying(5) NOT NULL,
  descricao character varying(100) NOT NULL,
  base_result_code_id integer NOT NULL,
  CONSTRAINT pk_translate_billet_registry_erro PRIMARY KEY (id),
  CONSTRAINT fk_1_translate_billet_registry_erro FOREIGN KEY (base_result_code_id)
    REFERENCES base.result_code (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT uk_1_translate_billet_registry_erro UNIQUE (codigo)
);
ALTER TABLE banking_santander.translate_billet_registry_erro OWNER TO billing;

-- cnab400

CREATE TABLE banking_santander.cnab400_remittance_file (
  id bigserial NOT NULL,
  company_bank_account_id bigint NOT NULL,
  file_name character varying(50) NOT NULL,
  creation_date timestamp without time zone NOT NULL,
  codigo_de_transmissao character varying(20) NOT NULL,
  data_de_gravacao date NOT NULL,
  CONSTRAINT pk_cnab400_remittance_file PRIMARY KEY (id),
  CONSTRAINT fk_1_cnab400_remittance_file FOREIGN KEY (company_bank_account_id)
    REFERENCES banking_santander.company_bank_account (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT uk_1_cnab400_remittance_file UNIQUE (file_name)
);
ALTER TABLE banking_santander.cnab400_remittance_file OWNER TO billing;

CREATE TABLE banking_santander.cnab400_remittance_detail (
  id bigserial NOT NULL,
  base_bank_remittance_id bigint NOT NULL,
  cnab400_remittance_file_id bigint NOT NULL,
  creation_date timestamp without time zone NOT NULL,
  nosso_numero bigint NOT NULL,
  codigo_da_carteira smallint NOT NULL,
  codigo_da_ocorrencia smallint NOT NULL,
  seu_numero bigint NOT NULL,
  data_de_vencimento date NOT NULL,
  valor_do_titulo integer NOT NULL,
  especie_de_documento smallint NOT NULL,
  data_de_emissao date NOT NULL,
  tipo_de_inscricao_do_pagador smallint NOT NULL,
  numero_de_inscricao_do_pagador character varying(14) NOT NULL,
  CONSTRAINT pk_cnab400_remittance_detail PRIMARY KEY (id),
  CONSTRAINT fk_1_cnab400_remittance_detail FOREIGN KEY (base_bank_remittance_id)
    REFERENCES base.bank_remittance (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_2_cnab400_remittance_detail FOREIGN KEY (cnab400_remittance_file_id)
    REFERENCES banking_santander.cnab400_remittance_file (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION
);
ALTER TABLE banking_santander.cnab400_remittance_detail OWNER TO billing;

CREATE TABLE banking_santander.cnab400_discharge_file (
  id bigserial NOT NULL,
  company_bank_account_id bigint NOT NULL,
  file_name character varying(50) NOT NULL,
  receive_date timestamp without time zone NOT NULL,
  agencia_beneficiario character varying(4) NOT NULL,
  conta_movimento_beneficiario character varying(8) NOT NULL,
  data_do_movimento date NOT NULL,
  qtd_de_cobrancas_simples integer NOT NULL,
  valor_de_cobrancas_simples bigint NOT NULL,
  CONSTRAINT pk_cnab400_discharge_file PRIMARY KEY (id),
  CONSTRAINT fk_1_cnab400_discharge_file FOREIGN KEY (company_bank_account_id)
    REFERENCES banking_santander.company_bank_account (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT uk_1_cnab400_discharge_file UNIQUE (file_name)
);
ALTER TABLE banking_santander.cnab400_discharge_file OWNER TO billing;

CREATE TABLE banking_santander.cnab400_discharge_detail (
  id bigserial NOT NULL,
  base_bank_discharge_id bigint,
  cnab400_discharge_file_id bigint NOT NULL,
  receive_date timestamp without time zone NOT NULL,
  status base.bank_discharge_status_enum,
  processing_date timestamp without time zone,
  nosso_numero bigint NOT NULL,
  seu_numero bigint,
  data_do_credito date NOT NULL,
  codigo_do_banco_cobrador character varying(3) NOT NULL,
  agencia_recebedora character varying(5) NOT NULL,
  valor_do_titulo integer NOT NULL,
  valor_da_tarifa_cobrada integer NOT NULL,
  valor_dos_juros_de_atraso integer NOT NULL,
  valor_total_recebido integer NOT NULL,
  valor_dos_juros_de_mora integer NOT NULL,
  codigo_de_ocorrencia smallint NOT NULL,
  data_da_ocorrencia date NOT NULL,
  codigo_original_da_remessa smallint,
  codigo_do_primeiro_erro character varying(3),
  codigo_do_segundo_erro character varying(3),
  codigo_do_terceiro_erro character varying(3),
  CONSTRAINT pk_cnab400_discharge_detail PRIMARY KEY (id),
  CONSTRAINT fk_1_cnab400_discharge_detail FOREIGN KEY (base_bank_discharge_id)
    REFERENCES base.bank_discharge (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_2_cnab400_discharge_detail FOREIGN KEY (cnab400_discharge_file_id)
    REFERENCES banking_santander.cnab400_discharge_file (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION    
);
ALTER TABLE banking_santander.cnab400_discharge_detail OWNER TO billing;

CREATE TABLE banking_santander.translate_cnab400_ocorrencia (
  id serial NOT NULL,
  codigo character varying(5) NOT NULL,
  descricao character varying(100) NOT NULL,
  base_result_code_id integer NOT NULL,
  CONSTRAINT pk_translate_cnab400_ocorrencia PRIMARY KEY (id),
  CONSTRAINT fk_1_translate_cnab400_ocorrencia FOREIGN KEY (base_result_code_id)
    REFERENCES base.result_code (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT uk_1_translate_cnab400_ocorrencia UNIQUE (codigo)
);
ALTER TABLE banking_santander.translate_cnab400_ocorrencia OWNER TO billing;
