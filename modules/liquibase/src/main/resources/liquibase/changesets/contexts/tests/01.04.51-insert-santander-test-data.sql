--liquibase formatted sql
--changeset billing:1.04.51 dbms:postgresql context:dev,sandbox
--comment Insercao dos dados de teste para o banco santander

-- Conta Santander

INSERT INTO base.bank_account(
  bank_id,
  token,
  agency_number,
  agency_check_digit,
  account_number,
  account_check_digit,
  description,
  status
) VALUES (
  (SELECT id FROM base.bank WHERE code = '033'),
  '34567890123456',
  '3216',
  null,
  '13003696',
  '2',
  'Conta no Santander',
  'A'
);

INSERT INTO base.company_bank_account(
  id,
  company_id,
  acceptance,
  billet_unpaid_limit,
  bank_channel_id,
  paid_billet_fee,
  interbank_transfer_fee
) VALUES (
  (SELECT id FROM base.bank_account WHERE token = '34567890123456'),
  (SELECT id FROM base.company WHERE token = '1234'),
  FALSE,
  58,
  (SELECT id FROM base.bank_channel WHERE code = 'SANTANDER-HOMO'),
  124,
  350
);

-- Dealer 1

UPDATE base.user SET username = 'dealer1', real_name = 'Dealer Itaú' WHERE username = 'dealer';
UPDATE base.application SET name = 'Aplicação 1' WHERE token = '123456789';

-- Dealer 2

INSERT INTO base.dealer
(company_id, token, name, person_type, document_type_id, document_number, email, company_bank_account_id, company_billing_plan_id, 
  bank_billet_instructions, status, creation_date)
VALUES (
  (SELECT id FROM base.company WHERE token = '1234'),
  '234567',
  'Dealer 2',
  'L', -- Jurídica
  2, -- CNPJ
  '88473263000169',
  'dealer.tests@jannotti.tech',
  (SELECT id FROM base.bank_account WHERE token = '34567890123456'),
  (SELECT id FROM base.company_billing_plan WHERE token = '1234'),
  'Instruções customizadas de pagamento',
  'A',
  CURRENT_TIMESTAMP
);

INSERT INTO base.user(token, username, email, real_name, password, role_id, status, creation_date) VALUES
  ('456789012345', 'dealer2', 'dealer.tests@jannotti.tech', 'Dealer Santader', '$2a$10$YY4bi4ahfZxSCvNOzhdcMO2dvwI1E4DgdvL290LLNBsAduXvHFHt.',
    (SELECT id FROM base.role WHERE code = 'D'), 'A', CURRENT_TIMESTAMP);

INSERT INTO base.dealer_user(id, dealer_id) VALUES
  ((SELECT id FROM base.user WHERE username = 'dealer2'), (SELECT id FROM base.dealer WHERE token = '234567'));    
    
INSERT INTO base.dealer_address
  (dealer_id, token, address_type, billing_address, street, number, complement, district, zip_code, city, state, status)
VALUES (
  (SELECT id FROM base.dealer WHERE token = '234567'),
  '23456789',
  'B', -- Comercial
  true,
  'Rua Otávio Carneiro',
  '100',
  'Sala 1304',
  'Icaraí', 
  '24230191',
  'Niterói',
  'RJ',
  'A'
);

INSERT INTO base.application (dealer_id, token, name, client_id, client_secret, status, creation_date) VALUES
  ((SELECT id FROM base.dealer WHERE token = '234567'), '234567890', 'Aplicação 2', '234567890123456789012345678901',
  '$2a$10$YY4bi4ahfZxSCvNOzhdcMO2dvwI1E4DgdvL290LLNBsAduXvHFHt.', 'A', CURRENT_TIMESTAMP);

INSERT INTO base.bank_account(
  bank_id, token, agency_number, agency_check_digit, account_number, account_check_digit, description, status
) VALUES (
  (SELECT id FROM base.bank WHERE code = '237'), '45678901234567', '6592', NULL, '0796145', '6', 'Conta no Bradesco', 'A'
);

INSERT INTO base.dealer_bank_account(id, dealer_id) VALUES (
  (SELECT id FROM base.bank_account WHERE token = '45678901234567'),
  (SELECT id FROM base.dealer WHERE token = '234567')
);

INSERT INTO base.market_account (token, status) VALUES ('23456789012345', 'A');

INSERT INTO base.dealer_market_account (id, dealer_id) VALUES (
  (SELECT id FROM base.market_account WHERE token = '23456789012345'),
  (SELECT id FROM base.dealer WHERE token = '234567')
);
