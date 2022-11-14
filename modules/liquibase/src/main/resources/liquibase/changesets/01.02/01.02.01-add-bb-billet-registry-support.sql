--liquibase formatted sql
--changeset billing:1.02.01 dbms:postgresql
--comment Adicionando suporte a registro online de boleto no BB

-- base

ALTER TABLE base.dealer RENAME COLUMN bank_instructions TO bank_billet_instructions;

CREATE TABLE base.bank_channel (
  id smallserial NOT NULL,
  bank_id smallint NOT NULL,
  code character varying(10) NOT NULL,
  description character varying(50) NOT NULL,
  CONSTRAINT pk_bank_channel PRIMARY KEY (id),
  CONSTRAINT fk_1_bank_channel FOREIGN KEY (bank_id) REFERENCES base.bank(id),
  CONSTRAINT uk_1_bank_channel UNIQUE (code)
);
ALTER TABLE base.bank_channel OWNER TO billing;

ALTER TABLE base.company_bank_account ADD bank_channel_id smallint,
  ADD CONSTRAINT fk_3_company_bank_account FOREIGN KEY (bank_channel_id) REFERENCES base.bank_channel(id);

ALTER TABLE base.company_bank_account DROP COLUMN enviroment, 
  DROP COLUMN online_billet_registration;  
DROP DOMAIN base.bank_enviroment_enum;
  
INSERT INTO base.bank_channel (bank_id, code, description) VALUES
  ((SELECT id FROM base.bank WHERE code = '001'), 'BB-HOMO', 'Homologação do Banco do Brasil'),
  ((SELECT id FROM base.bank WHERE code = '001'), 'BB-PROD', 'Produção do Banco do Brasil');

CREATE DOMAIN base.bank_remittance_delivery_mode_enum AS character(1) CHECK(VALUE IN ('F', 'W'));  
ALTER TABLE base.bank_remittance ADD delivery_mode base.bank_remittance_delivery_mode_enum;
ALTER TABLE base.dealer ADD bank_billet_registry_delivery_mode base.bank_remittance_delivery_mode_enum;
UPDATE base.bank_remittance SET delivery_mode = 'F';
UPDATE base.dealer SET bank_billet_registry_delivery_mode = 'F';
ALTER DOMAIN base.bank_remittance_delivery_mode_enum SET NOT NULL;
ALTER TABLE base.bank_remittance RENAME CONSTRAINT fk_5_bank_remittance TO fk_3_bank_remittance;

ALTER DOMAIN base.bank_remittance_status_enum DROP CONSTRAINT bank_remittance_status_enum_check;
ALTER DOMAIN base.bank_remittance_status_enum ADD CONSTRAINT bank_remittance_status_enum_check CHECK(VALUE IN ('G', 'P', 'C'));

ALTER TABLE base.bank_discharge ALTER COLUMN bank_remittance_id DROP NOT NULL;


-- banking bb

-- cria as tabelas

CREATE TABLE banking_bb.bank_channel (
  id smallserial NOT NULL,
  base_bank_channel_id smallint NOT NULL,
  oauth_url character varying(100) NOT NULL,
  registro_boleto_url character varying(100) NOT NULL,
  request_timeout integer NOT NULL,
  validate_ssl boolean NOT NULL,
  tipo_operacao_remessa character varying(8) NOT NULL,
  CONSTRAINT pk_bank_channel PRIMARY KEY (id),
  CONSTRAINT fk_1_bank_channel FOREIGN KEY (base_bank_channel_id) REFERENCES base.bank_channel(id),
  CONSTRAINT uk_1_bank_channel UNIQUE (base_bank_channel_id)
);
ALTER TABLE banking_bb.bank_channel OWNER TO billing;

ALTER TABLE banking_bb.company_bank_account DROP COLUMN tipo_cobranca,
  ALTER COLUMN numero_convenio_cobranca TYPE integer,
  ALTER COLUMN numero_convenio_lider TYPE integer,
  ADD especie_titulo_ws smallint,
  ADD chave_usuario_j character varying(8),
  ADD token_clientid character varying(150),
  ADD token_secret character varying(250);

CREATE TABLE banking_bb.billet_registry_request (
  id bigserial NOT NULL,
  base_payment_bank_billet_id bigint NOT NULL,    
  base_bank_remittance_id bigint NOT NULL,
  request_date timestamp without time zone NOT NULL,
  data_emissao_titulo date NOT NULL,
  data_vencimento_titulo date NOT NULL,
  valor_original_titulo integer NOT NULL,
  codigo_tipo_juro_mora smallint NOT NULL,
  percentual_juro_mora_titulo integer,
  valor_juro_mora_titulo integer,
  codigo_tipo_multa smallint NOT NULL,
  data_multa_titulo date,
  percentual_multa_titulo integer,
  valor_multa_titulo integer,
  codigo_tipo_titulo smallint NOT NULL,
  texto_numero_titulo_beneficiario bigint NOT NULL,
  texto_numero_titulo_cliente character varying(20) NOT NULL,
  codigo_tipo_inscricao_pagador smallint NOT NULL,
  numero_inscricao_pagador bigint NOT NULL,
  CONSTRAINT pk_billet_registry_request PRIMARY KEY (id),
  CONSTRAINT fk_1_billet_registry_request FOREIGN KEY (base_payment_bank_billet_id) REFERENCES base.payment_bank_billet(id),
  CONSTRAINT fk_2_billet_registry_request FOREIGN KEY (base_bank_remittance_id) REFERENCES base.bank_remittance(id),
  CONSTRAINT uk_1_billet_registry_request UNIQUE (base_payment_bank_billet_id),
  CONSTRAINT uk_2_billet_registry_request UNIQUE (base_bank_remittance_id)
);
ALTER TABLE banking_bb.billet_registry_request OWNER TO billing;

CREATE TABLE banking_bb.billet_registry_response (
  id bigserial NOT NULL,
  billet_registry_request_id bigint NOT NULL,
  response_date timestamp without time zone NOT NULL,
  codigo_retorno_programa smallint NOT NULL,
  nome_programa_erro character varying(8),
  texto_mensagem_erro character varying(68),
  numero_posicao_erro_programa smallint,
  texto_numero_titulo_cobranca_bb character varying(20),
  linha_digitavel character varying(47),
  codigo_barra_numerico character varying(52),
  CONSTRAINT pk_billet_registry_response PRIMARY KEY (id),
  CONSTRAINT fk_1_billet_registry_response FOREIGN KEY (billet_registry_request_id) REFERENCES banking_bb.billet_registry_request(id),
  CONSTRAINT uk_1_billet_registry_response UNIQUE (billet_registry_request_id)

);
ALTER TABLE banking_bb.billet_registry_response OWNER TO billing;

-- renomeia as tabelas
  
ALTER TABLE banking_bb.bank_remittance_file RENAME TO cnab400_remittance_file;
ALTER TABLE banking_bb.cnab400_remittance_file RENAME CONSTRAINT fk_1_bank_remittance_file TO fk_1_cnab400_remittance_file;
ALTER TABLE banking_bb.cnab400_remittance_file RENAME CONSTRAINT pk_bank_remittance_file TO pk_cnab400_remittance_file;
ALTER TABLE banking_bb.cnab400_remittance_file RENAME CONSTRAINT uk_1_bank_remittance_file TO uk_1_cnab400_remittance_file;
ALTER SEQUENCE banking_bb.bank_remittance_file_id_seq RENAME TO cnab400_remittance_file_id_seq;

ALTER TABLE banking_bb.bank_remittance_file_detail RENAME TO cnab400_remittance_detail;
ALTER TABLE banking_bb.cnab400_remittance_detail RENAME CONSTRAINT pk_bank_remittance_file_detail TO pk_cnab400_remittance_detail;
ALTER TABLE banking_bb.cnab400_remittance_detail RENAME CONSTRAINT fk_1_bank_remittance_file_detail TO fk_1_cnab400_remittance_detail;
ALTER TABLE banking_bb.cnab400_remittance_detail RENAME CONSTRAINT fk_2_bank_remittance_file_detail TO fk_2_cnab400_remittance_detail;
ALTER SEQUENCE banking_bb.bank_remittance_id_seq RENAME TO cnab400_remittance_detail_id_seq;
ALTER TABLE banking_bb.cnab400_remittance_detail RENAME COLUMN bank_remittance_file_id TO cnab400_remittance_file_id;

ALTER TABLE banking_bb.bank_discharge_file RENAME TO cnab400_discharge_file;
ALTER TABLE banking_bb.cnab400_discharge_file RENAME CONSTRAINT pk_bank_discharge_file TO pk_cnab400_discharge_file;
ALTER TABLE banking_bb.cnab400_discharge_file RENAME CONSTRAINT fk_1_bank_discharge_file TO fk_1_cnab400_discharge_file;
ALTER TABLE banking_bb.cnab400_discharge_file RENAME CONSTRAINT uk_1_bank_discharge_file TO uk_1_cnab400_discharge_file;
ALTER SEQUENCE banking_bb.bank_discharge_file_id_seq RENAME TO cnab400_discharge_file_id_seq;

ALTER TABLE banking_bb.bank_discharge_file_detail RENAME TO cnab400_discharge_detail;
ALTER TABLE banking_bb.cnab400_discharge_detail RENAME CONSTRAINT pk_bank_discharge_file_detail TO pk_cnab400_discharge_detail;
ALTER TABLE banking_bb.cnab400_discharge_detail RENAME CONSTRAINT fk_1_bank_discharge_file_detail TO fk_1_cnab400_discharge_detail;
ALTER TABLE banking_bb.cnab400_discharge_detail RENAME CONSTRAINT fk_2_bank_discharge_file_detail TO fk_2_cnab400_discharge_detail;
ALTER SEQUENCE banking_bb.bank_discharge_id_seq RENAME TO cnab400_discharge_detail_id_seq;
ALTER TABLE banking_bb.cnab400_discharge_detail RENAME COLUMN bank_discharge_file_id TO cnab400_discharge_file_id;
ALTER TABLE banking_bb.cnab400_discharge_detail RENAME COLUMN dv_prefixo_recebedora TO dv_prefixo_agencia_recebedora;

-- remove campos nao usados

ALTER TABLE banking_bb.cnab400_remittance_file DROP COLUMN nome_cedente,
  DROP COLUMN tipo_inscricao_cedente,
  DROP COLUMN numero_inscricao_cedente;

ALTER TABLE banking_bb.cnab400_remittance_detail DROP COLUMN nome_sacado,
  DROP COLUMN endereco_sacado,
  DROP COLUMN cep_sacado,
  DROP COLUMN bairro_sacado,
  DROP COLUMN cidade_sacado,
  DROP COLUMN uf_sacado;
  
ALTER TABLE banking_bb.cnab400_discharge_detail DROP COLUMN prefixo_agencia,
  DROP COLUMN dv_prefixo_agencia,
  DROP COLUMN numero_conta_corrente_cedente,
  DROP COLUMN dv_numero_conta_corrente_cedente;

-- insere os dados

INSERT INTO banking_bb.bank_channel (base_bank_channel_id, oauth_url, registro_boleto_url, request_timeout, validate_ssl,
  tipo_operacao_remessa) VALUES
  ((SELECT id FROM base.bank_channel WHERE code = 'BB-HOMO'),
   'https://oauth.hm.bb.com.br/oauth/token',
   'https://cobranca.homologa.bb.com.br:7101/registrarBoleto',
   30000, false, 'TESTE'
  ),
  ((SELECT id FROM base.bank_channel WHERE code = 'BB-PROD'), 
   'https://oauth.bb.com.br/oauth/token',
   'https://cobranca.bb.com.br:7101/registrarBoleto',
   30000, true, 'REMESSA'
  );
