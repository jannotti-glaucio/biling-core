--liquibase formatted sql
--changeset billing:1.02.04 dbms:postgresql
--comment Removendo relacionando com a tabela cidade

ALTER TABLE base.company_address ADD city character varying(50),
  ADD state character varying(2);
ALTER TABLE base.dealer_address ADD city character varying(50),
  ADD state character varying(2);
ALTER TABLE base.customer_address ADD city character varying(50),
  ADD state character varying(2);
ALTER TABLE base.payment_bank_billet ADD payer_address_city character varying(50),
  ADD payer_address_state character varying(2);
  
UPDATE base.company_address a SET city = c.name, state = s.code
  FROM base.city c, base.state s WHERE a.city_id = c.id AND c.state_id = s.id;
UPDATE base.dealer_address a SET city = c.name, state = s.code
  FROM base.city c, base.state s WHERE a.city_id = c.id AND c.state_id = s.id;
UPDATE base.customer_address a SET city = c.name, state = s.code
  FROM base.city c, base.state s WHERE a.city_id = c.id AND c.state_id = s.id;
UPDATE base.payment_bank_billet a SET payer_address_city = c.name, payer_address_state = s.code
  FROM base.city c, base.state s WHERE a.payer_address_city_id = c.id AND c.state_id = s.id;

ALTER TABLE base.company_address ALTER COLUMN city SET NOT NULL;
ALTER TABLE base.company_address ALTER COLUMN state SET NOT NULL;
ALTER TABLE base.company_address DROP COLUMN city_id;

ALTER TABLE base.dealer_address ALTER COLUMN city SET NOT NULL;
ALTER TABLE base.dealer_address ALTER COLUMN state SET NOT NULL;
ALTER TABLE base.dealer_address DROP COLUMN city_id;

ALTER TABLE base.customer_address ALTER COLUMN city SET NOT NULL;
ALTER TABLE base.customer_address ALTER COLUMN state SET NOT NULL;
ALTER TABLE base.customer_address DROP COLUMN city_id;

ALTER TABLE base.payment_bank_billet ALTER COLUMN payer_address_city SET NOT NULL;
ALTER TABLE base.payment_bank_billet ALTER COLUMN payer_address_state SET NOT NULL;
ALTER TABLE base.payment_bank_billet DROP COLUMN payer_address_city_id;
