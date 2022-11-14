--liquibase formatted sql
--changeset billing:1.04.03 dbms:postgresql
--comment Remove o suporte a integracao bancaria em outro fuso horario

-- Remove o campo timezone no canal bancario
ALTER TABLE banking_bb.bank_channel DROP column timezone;

-- Retorna a data de emissao para NOTNULL
UPDATE base.payment_bank_billet b SET issue_date = p.creation_date::DATE
FROM base.payment p
WHERE b.id = p.id AND b.issue_date IS NULL;

ALTER TABLE base.payment_bank_billet ALTER COLUMN issue_date SET NOT NULL;
