--liquibase formatted sql
--changeset billing:1.00.52 dbms:postgresql context:sandbox
--comment Insercao dos dados iniciais de teste pra sandbox

INSERT INTO banking_bb.bank_account (
  base_bank_account_id,
  numero_carteira,
  variacao_carteira,
  numero_convenio_cobranca,
  numero_convenio_lider,
  especie_titulo_cnab,
  especie_titulo_boleto,
  tipo_cobranca,
  remittance_files_dir,
  discharge_files_source_dir,
  discharge_files_processed_dir,
  discharge_files_rejected_dir
) VALUES (
  (SELECT id FROM base.bank_account WHERE alias = 'BILLING-BB'), 
  17,
  43,
  3055147,
  3055147,
  1,
  'DS',
  null,
  '/data/banking/bb/billing/cnab400/remittances/send',
  '/data/banking/bb/billing/cnab400/discharges/receive',
  '/data/banking/bb/billing/cnab400/discharges/processed',
  '/data/banking/bb/billing/cnab400/discharges/rejected'
);
