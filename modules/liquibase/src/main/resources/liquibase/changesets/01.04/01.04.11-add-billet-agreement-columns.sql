--liquibase formatted sql
--changeset billing:1.04.11 dbms:postgresql
--comment Criando colunas pra guardar o convenio usado no registro e cancelamento de boletos

ALTER TABLE banking_bb.billet_registry_request ADD numero_convenio integer,
  ADD numero_carteira smallint, 
  ADD numero_variacao_carteira smallint;

ALTER TABLE banking_bb.cnab400_remittance_detail ADD numero_convenio_cobranca_cedente integer;
ALTER TABLE banking_bb.cnab400_discharge_detail ADD numero_convenio_cobranca_cedente integer;

ALTER TABLE banking_santander.billet_ticket_request ADD cod_convenio integer;
