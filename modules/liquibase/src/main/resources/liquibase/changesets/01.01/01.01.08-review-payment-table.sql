--liquibase formatted sql
--changeset billing:1.01.08 dbms:postgresql
--comment Revisa os campos da tabela Payment

ALTER TABLE base.payment ADD payment_date date,
  ADD paid_amount integer,
  ADD payment_fee integer,
  ADD release_date date,
  ADD released_amount integer,
  ADD market_fee integer;

UPDATE base.payment p SET payment_date = b.payment_date,
  paid_amount = b.net_amount,
  payment_fee = b.fee,
  released_amount = (b.net_amount - b.fee)
FROM base.payment_bank_billet b WHERE b.id = p.id;

UPDATE base.payment SET release_date = payment_date;
UPDATE base.payment SET payment_date = payment_date - 1;

ALTER TABLE base.payment_bank_billet DROP COLUMN payment_date, DROP COLUMN fee, DROP COLUMN net_amount;
