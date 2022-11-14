--liquibase formatted sql
--changeset billing:1.04.09 dbms:postgresql
--comment Divindo as informacoes de convenios de cobranca das contas do BB

-- BB

CREATE TABLE banking_bb.levying_agreement
(
  id serial NOT NULL,
  company_bank_account_id bigint NOT NULL,
  billet_expired_payment boolean NOT NULL,
  numero_convenio integer NOT NULL,
  numero_carteira smallint NOT NULL,
  variacao_carteira smallint NOT NULL,
  CONSTRAINT pk_levying_agreement PRIMARY KEY (id),
  CONSTRAINT fk_1_levying_agreement FOREIGN KEY (company_bank_account_id)
    REFERENCES banking_bb.company_bank_account (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION
);
ALTER TABLE banking_bb.levying_agreement OWNER TO billing;

ALTER TABLE banking_bb.company_bank_account
  ADD levying_agreement_id_until_expiration integer,
  ADD levying_agreement_id_expirated integer;
    
ALTER TABLE banking_bb.company_bank_account RENAME CONSTRAINT fk_1_bank_account TO fk_1_company_bank_account;    
ALTER TABLE banking_bb.company_bank_account
  ADD CONSTRAINT fk_2_company_bank_account FOREIGN KEY (levying_agreement_id_until_expiration)
    REFERENCES banking_bb.levying_agreement (id),
  ADD CONSTRAINT fk_3_company_bank_account FOREIGN KEY (levying_agreement_id_expirated)
    REFERENCES banking_bb.levying_agreement (id);

INSERT INTO banking_bb.levying_agreement (company_bank_account_id, billet_expired_payment, numero_convenio, numero_carteira, variacao_carteira)
  SELECT id, false, convenio_cobranca_pagavel_ate_vencimento, numero_carteira, variacao_carteira FROM banking_bb.company_bank_account;
INSERT INTO banking_bb.levying_agreement (company_bank_account_id, billet_expired_payment, numero_convenio, numero_carteira, variacao_carteira)
  SELECT id, true, convenio_cobranca_pagavel_vencido, numero_carteira, variacao_carteira FROM banking_bb.company_bank_account;

UPDATE banking_bb.company_bank_account ba SET levying_agreement_id_until_expiration = la_ue.id,
  levying_agreement_id_expirated = la_e.id
FROM banking_bb.levying_agreement la_ue, banking_bb.levying_agreement la_e
WHERE la_ue.company_bank_account_id = ba.id AND la_ue.billet_expired_payment = false
  AND la_e.company_bank_account_id = ba.id AND la_e.billet_expired_payment = true;
  
ALTER TABLE banking_bb.company_bank_account
  ALTER COLUMN levying_agreement_id_until_expiration SET NOT NULL,
  ALTER COLUMN levying_agreement_id_expirated  SET NOT NULL;

ALTER TABLE banking_bb.company_bank_account DROP COLUMN numero_carteira,
  DROP COLUMN variacao_carteira,
  DROP COLUMN convenio_cobranca_pagavel_ate_vencimento,
  DROP COLUMN convenio_cobranca_pagavel_vencido;
