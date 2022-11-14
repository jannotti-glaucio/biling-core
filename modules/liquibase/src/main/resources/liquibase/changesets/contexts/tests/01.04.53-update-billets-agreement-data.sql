--liquibase formatted sql
--changeset billing:1.04.53 dbms:postgresql context:dev,sandbox
--comment Atualizando o convenio de cobranca usado no registro e cancelamento de boletos

UPDATE banking_bb.billet_registry_request SET numero_convenio = 2625444,
  numero_carteira = 17,
  numero_variacao_carteira = 19;

UPDATE banking_bb.cnab400_remittance_detail SET numero_convenio_cobranca_cedente = 2625444;
UPDATE banking_bb.cnab400_discharge_detail SET numero_convenio_cobranca_cedente = 2625444;

UPDATE banking_santander.billet_ticket_request SET cod_convenio = 7829140;
