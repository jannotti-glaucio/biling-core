--liquibase formatted sql
--changeset billing:1.04.52 dbms:postgresql context:dev
--comment Insercao dos dados de teste do santander pra desenvolvimento

INSERT INTO banking_santander.company_bank_account (
  base_company_bank_account_id,
  codigo_convenio_cobranca,
  codigo_estacao_cobranca,
  especie_titulo_xml,
  especie_titulo_boleto,
  especie_titulo_cnab,
  codigo_carteira_boleto,
  sigla_carteira_boleto,
  codigo_carteira_cnab,
  codigo_transmissao_cnab,
  remittance_files_dir,
  discharge_files_source_dir,
  discharge_files_processed_dir,
  discharge_files_rejected_dir
) VALUES (
  (SELECT id FROM base.bank_account WHERE token = '34567890123456'), 
  7829140,
  'SE7R',
  4,
  'DS',
  6,
  101,
  'RCR',
  5,
  '32160782914001300369',
  '/opt/billing/banking/santander/billing/cnab400/remittances/send',
  '/opt/billing/banking/santander/billing/cnab400/discharges/receive',
  '/opt/billing/banking/santander/billing/cnab400/discharges/processed',
  '/opt/billing/banking/santander/billing/cnab400/discharges/rejected'
);
