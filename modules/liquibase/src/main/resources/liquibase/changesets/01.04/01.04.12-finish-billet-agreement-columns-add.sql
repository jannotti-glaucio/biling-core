--liquibase formatted sql
--changeset billing:1.04.12 dbms:postgresql
--comment Finalizando a cricao de colunas pra guardar o convenio usado no registro e cancelamento de boletos

ALTER TABLE banking_bb.billet_registry_request ALTER COLUMN numero_convenio SET NOT NULL;
ALTER TABLE banking_bb.billet_registry_request ALTER COLUMN numero_carteira SET NOT NULL;
ALTER TABLE banking_bb.billet_registry_request ALTER COLUMN numero_variacao_carteira SET NOT NULL;

ALTER TABLE banking_bb.cnab400_remittance_detail ALTER COLUMN numero_convenio_cobranca_cedente SET NOT NULL;
ALTER TABLE banking_bb.cnab400_discharge_detail ALTER COLUMN numero_convenio_cobranca_cedente SET NOT NULL;

ALTER TABLE banking_santander.billet_ticket_request ALTER COLUMN cod_convenio SET NOT NULL;
