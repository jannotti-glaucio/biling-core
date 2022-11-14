--liquibase formatted sql
--changeset billing:1.04.05 dbms:postgresql
--comment Alterando os identificadores do boleto no banco (seuNumero e nossoNumero)

ALTER TABLE base.payment_bank_billet RENAME COLUMN billet_number TO our_number;
ALTER TABLE base.payment_bank_billet DROP COLUMN billet_number_digit;

ALTER TABLE base.payment_bank_billet ADD COLUMN your_number bigint;
ALTER TABLE base.payment_bank_billet ADD CONSTRAINT uk_2_payment_bank_billet UNIQUE (company_bank_account_id, your_number);

-- atualiza o seu numero
UPDATE base.payment_bank_billet SET your_number = our_number WHERE our_number IS NOT NULL;

-- atualiza o nosso numero
UPDATE base.payment_bank_billet bb SET our_number = rd.nosso_numero
FROM base.bank_remittance br, banking_bb.cnab400_remittance_detail rd
WHERE br.payment_id = bb.id AND br.operation = 'R' AND rd.base_bank_remittance_id = br.id;

UPDATE base.payment_bank_billet bb SET our_number = rr.texto_numero_titulo_cliente::bigint
FROM base.bank_remittance br, banking_bb.billet_registry_request rr
WHERE br.payment_id = bb.id AND br.operation = 'R' AND rr.base_bank_remittance_id = br.id;

ALTER TABLE base.bank ADD COLUMN billet_our_number_digits smallint;
UPDATE base.bank SET billet_our_number_digits = 1 WHERE code = '033';
UPDATE base.bank SET billet_our_number_digits = 0 WHERE code != '033';
ALTER TABLE base.bank ALTER COLUMN billet_our_number_digits SET NOT NULL;

ALTER TABLE base.bank ADD COLUMN billet_our_number_print_mask character varying(10);
UPDATE base.bank SET billet_our_number_print_mask = '.{10}$' WHERE code = '001';
