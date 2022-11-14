--liquibase formatted sql
--changeset billing:1.01.54 dbms:postgresql context:dev,sandbox
--comment Insercao das contas do Dealer de teste

INSERT INTO base.bank_account(
  bank_id, token, agency_number, agency_check_digit, account_number, account_check_digit, description, status
) VALUES (
  (SELECT id FROM base.bank WHERE code = '341'), '23456789012345', '7140', NULL, '29489', '0', 'Conta no Itaú', 'A'
);

INSERT INTO base.dealer_bank_account(id, dealer_id) VALUES (
  (SELECT id FROM base.bank_account WHERE token = '23456789012345'),
  (SELECT id FROM base.dealer WHERE token = '123456')
);

INSERT INTO base.market_account (token, status) VALUES ('12345678901234', 'A');

INSERT INTO base.dealer_market_account (id, dealer_id) VALUES (
  (SELECT id FROM base.market_account WHERE token = '12345678901234'),
  (SELECT id FROM base.dealer WHERE token = '123456')
);

INSERT INTO base.market_statement
  (market_account_id, token, market_statement_type_id, statement_date, amount, payment_id)
SELECT m.id,
  base.get_token(26),
  (SELECT id FROM base.market_statement_type WHERE code = '001'),
  p.payment_date,
  (p.paid_amount - p.market_fee),
  p.id
FROM base.payment p
  INNER JOIN base.invoice i ON i.id = p.invoice_id
  INNER JOIN base.customer c ON c.id = i.customer_id
  INNER JOIN base.dealer d ON d.id = c.dealer_id
  INNER JOIN base.dealer_market_account m ON m.dealer_id = d.id
WHERE p.status = 'P';
