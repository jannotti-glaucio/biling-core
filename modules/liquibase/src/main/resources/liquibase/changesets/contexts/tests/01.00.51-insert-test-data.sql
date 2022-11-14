--liquibase formatted sql
--changeset billing:1.00.51 dbms:postgresql context:dev,sandbox
--comment Insercao dos dados iniciais de teste

-- Admin

INSERT INTO base.user(username, email, name, password, role_id, status) VALUES
  ('admin', 'admin.tests@jannotti.tech', 'Administrador', '$2a$10$YY4bi4ahfZxSCvNOzhdcMO2dvwI1E4DgdvL290LLNBsAduXvHFHt.',
    (SELECT id FROM base.role WHERE code = 'A'), 'A');

-- Company

INSERT INTO base.company
(token, trading_name, corporate_name, document_type_id, document_number, country_id, status) VALUES (
  '1234',
  'Billing',
  'LML de Nova Friburgo Cobranças e Serviços EIRELI',
  2, -- CNPJ
  '23168857000110',
  1, -- BR
  'A'
);

INSERT INTO base.user(username, email, name, password, role_id, company_id, status) VALUES
  ('company', 'company.tests@jannotti.tech', 'Company Manager', '$2a$10$YY4bi4ahfZxSCvNOzhdcMO2dvwI1E4DgdvL290LLNBsAduXvHFHt.',
    (SELECT id FROM base.role WHERE code = 'P'), (SELECT id FROM base.company WHERE token = '1234'), 'A');

INSERT INTO base.company_address
  (company_id, token, address_type, billing_address, street, number, complement, district, zip_code, city_id, status)
VALUES (
  (SELECT id FROM base.company WHERE token = '1234'),
  '123456',
  'B', -- Comercial
  true,
  'Rua Olavo Bilac',
  '8',
  'Sala 01',
  'Centro', 
  '28625060',
  (SELECT id FROM base.city WHERE state_id = 19 AND name = 'Nova Friburgo'),
  'A'
);

INSERT INTO base.bank_account(
  bank_id,
  company_id,
  alias,
  agency_number,
  agency_check_digit,
  account_number,
  account_check_digit,
  acceptance,
  enviroment,
  online_billet_registration,
  billet_unpaid_limit
) VALUES (
  (SELECT id FROM base.bank WHERE code = '001'),
  (SELECT id FROM base.company WHERE token = '1234'),
  'BILLING-BB',
  '0335',
  '2',
  '68964',
  '5',
  FALSE,
  'T', -- Test
  FALSE,
  58
);


-- Dealer

INSERT INTO base.dealer
(company_id, token, name, person_type, document_type_id, document_number, email, bank_account_id, bank_instructions, status)
VALUES (
  (SELECT id FROM base.company WHERE token = '1234'),
  '123456',
  'Dealer 1',
  'L', -- Jurídica
  2, -- CNPJ
  '88473263000169',
  'dealer.tests@jannotti.tech',
  (SELECT id FROM base.bank_account WHERE alias = 'BILLING-BB'),
  'Instruções customizadas de pagamento',
  'A'
);

INSERT INTO base.user(username, email, name, password, role_id, dealer_id, status) VALUES
  ('dealer', 'dealer.tests@jannotti.tech', 'Dealer Manager', '$2a$10$YY4bi4ahfZxSCvNOzhdcMO2dvwI1E4DgdvL290LLNBsAduXvHFHt.',
    (SELECT id FROM base.role WHERE code = 'D'), (SELECT id FROM base.dealer WHERE token = '123456'), 'A');

INSERT INTO base.dealer_address
  (dealer_id, token, address_type, billing_address, street, number, complement, district, zip_code, city_id, status)
VALUES (
  (SELECT id FROM base.dealer WHERE token = '123456'),
  '12345678',
  'B', -- Comercial
  true,
  'Rua Edson Zuzart Junior',
  '105',
  null,
  'Itaipú', 
  '24342080',
  (SELECT id FROM base.city WHERE state_id = 19 AND name = 'Niterói'),
  'A'
);
