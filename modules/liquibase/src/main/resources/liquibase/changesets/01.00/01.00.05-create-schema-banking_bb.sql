--liquibase formatted sql
--changeset billing:1.00.05 dbms:postgresql
--comment Criacao de schema banking_bb e tabelas

-- schema
CREATE SCHEMA banking_bb;
ALTER SCHEMA banking_bb OWNER TO billing;

-- tabelas

CREATE TABLE banking_bb.document_type (
  id smallserial NOT NULL,
  base_document_type_id smallint NOT NULL,
  codigo_de_inscricao smallint NOT NULL,
  CONSTRAINT pk_document_type PRIMARY KEY (id),
  CONSTRAINT fk_1_document_type FOREIGN KEY (base_document_type_id)
    REFERENCES base.document_type (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION
);
ALTER TABLE banking_bb.document_type OWNER TO billing;

CREATE TABLE banking_bb.bank_account (
  id serial NOT NULL,
  base_bank_account_id integer NOT NULL,
  numero_carteira smallint NOT NULL,
  variacao_carteira smallint NOT NULL,
  numero_convenio_cobranca bigint NOT NULL,
  numero_convenio_lider bigint NOT NULL,
  especie_titulo_cnab smallint NOT NULL,
  especie_titulo_boleto character varying(2) NOT NULL,
  tipo_cobranca character varying(5),
  remittance_files_dir character varying(100) NOT NULL,
  discharge_files_source_dir character varying(100) NOT NULL,
  discharge_files_processed_dir character varying(100) NOT NULL,
  discharge_files_rejected_dir character varying(100) NOT NULL,
  CONSTRAINT pk_bank_account PRIMARY KEY (id),
  CONSTRAINT fk_1_bank_account FOREIGN KEY (base_bank_account_id)
    REFERENCES base.bank_account (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION
);
ALTER TABLE banking_bb.bank_account OWNER TO billing;

CREATE TABLE banking_bb.bank_remittance_file (
  id bigserial NOT NULL,
  bank_account_id smallint NOT NULL,
  file_name character varying(50) NOT NULL,
  creation_date timestamp without time zone NOT NULL,
  nome_cedente character varying(30) NOT NULL,
  tipo_inscricao_cedente numeric(1,0) NOT NULL,
  numero_inscricao_cedente character varying(14) NOT NULL,
  numero_sequencial_remessa bigint NOT NULL,
  CONSTRAINT pk_bank_remittance_file PRIMARY KEY (id),
  CONSTRAINT fk_1_bank_remittance_file FOREIGN KEY (bank_account_id)
    REFERENCES banking_bb.bank_account (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT uk_1_bank_remittance_file UNIQUE (file_name)
);
ALTER TABLE banking_bb.bank_remittance_file OWNER TO billing;

CREATE TABLE banking_bb.bank_remittance (
  id bigserial NOT NULL,
  base_bank_remittance_id bigint NOT NULL,
  bank_remittance_file_id bigint NOT NULL,
  numero_titulo_cedente bigint NOT NULL,
  nosso_numero bigint NOT NULL,
  tipo_inscricao_sacado character varying(1) NOT NULL,
  numero_inscricao_sacado character varying(14) NOT NULL,
  nome_sacado character varying(37) NOT NULL,
  endereco_sacado character varying(40) NOT NULL,
  cep_sacado character varying(8) NOT NULL,
  bairro_sacado character varying(12) NOT NULL,
  cidade_sacado character varying(15) NOT NULL,
  uf_sacado character varying(2) NOT NULL,
  numero_carteira smallint NOT NULL,
  variacao_carteira smallint NOT NULL,
  data_vencimento_titulo date NOT NULL,
  data_emissao_titulo date NOT NULL,
  valor_titulo integer NOT NULL,
  especie_titulo smallint NOT NULL,
  comando smallint NOT NULL,
  CONSTRAINT pk_bank_remittance PRIMARY KEY (id),
  CONSTRAINT fk_1_bank_remittance FOREIGN KEY (base_bank_remittance_id)
    REFERENCES base.bank_remittance (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_2_bank_remittance FOREIGN KEY (bank_remittance_file_id)
    REFERENCES banking_bb.bank_remittance_file (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION
);
ALTER TABLE banking_bb.bank_remittance OWNER TO billing;

CREATE TABLE banking_bb.bank_discharge_file (
  id bigserial NOT NULL,
  bank_account_id smallint NOT NULL,
  file_name character varying(50) NOT NULL,
  receive_date timestamp without time zone NOT NULL,
  data_gravacao date NOT NULL,
  qtd_titulos integer NOT NULL,
  valor_total bigint NOT NULL,
  CONSTRAINT pk_bank_discharge_file PRIMARY KEY (id),
  CONSTRAINT fk_1_bank_discharge_file FOREIGN KEY (bank_account_id)
    REFERENCES banking_bb.bank_account (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT uk_1_bank_discharge_file UNIQUE (file_name)
);
ALTER TABLE banking_bb.bank_discharge_file OWNER TO billing;

CREATE TABLE banking_bb.bank_discharge (
  id bigserial NOT NULL,
  base_bank_discharge_id bigint,
  bank_discharge_file_id bigint NOT NULL,
  prefixo_agencia integer NOT NULL,
  dv_prefixo_agencia character varying(1) NOT NULL,
  numero_conta_corrente_cedente integer NOT NULL,
  dv_numero_conta_corrente_cedente character varying(1) NOT NULL,
  status base.bank_discharge_status_enum,
  processing_date timestamp without time zone,
  numero_titulo_cedente bigint,
  comando smallint NOT NULL,
  natureza_recebimento smallint NOT NULL,
  data_vencimento_titulo date,
  valor_titulo integer NOT NULL,
  data_credito date,
  valor_tarifa integer,
  valor_recebido integer,
  juros_mora integer,
  nosso_numero bigint,
  valor_lancamento integer,
  indicativo_debito_credito character(1),
  CONSTRAINT pk_bank_discharge PRIMARY KEY (id),
  CONSTRAINT fk_1_bank_discharge FOREIGN KEY (base_bank_discharge_id)
    REFERENCES base.bank_discharge (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_2_bank_discharge FOREIGN KEY (bank_discharge_file_id)
    REFERENCES banking_bb.bank_discharge_file (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION    
);
ALTER TABLE banking_bb.bank_discharge OWNER TO billing;

CREATE TABLE banking_bb.translate_comando (
  id smallserial NOT NULL,
  comando_codigo smallint NOT NULL,
  comando_mensagem character varying(60) NOT NULL,
  CONSTRAINT pk_translate_comando PRIMARY KEY (id),
  CONSTRAINT uk_1_translate_comando UNIQUE (comando_codigo)
);
ALTER TABLE banking_bb.translate_comando OWNER TO billing;

CREATE TABLE banking_bb.translate_comando_natureza (
  id serial NOT NULL,
  translate_comando_id smallint NOT NULL,
  natureza_recebimento_codigo smallint NOT NULL,
  natureza_recebimento_mensagem character varying(100),
  base_result_code_id integer NOT NULL,
  CONSTRAINT pk_translate_comando_natureza PRIMARY KEY (id),
  CONSTRAINT fk_1_translate_comando_natureza FOREIGN KEY (translate_comando_id)
    REFERENCES banking_bb.translate_comando (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_2_translate_comando_natureza FOREIGN KEY (base_result_code_id)
    REFERENCES base.result_code (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION
);
ALTER TABLE banking_bb.translate_comando_natureza OWNER TO billing;
