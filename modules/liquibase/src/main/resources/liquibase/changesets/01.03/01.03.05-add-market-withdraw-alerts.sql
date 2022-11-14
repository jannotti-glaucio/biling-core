--liquibase formatted sql
--changeset billing:1.03.05 dbms:postgresql
--comment Adicionando suporte a alertas de Saques

ALTER TABLE base.bank ADD COLUMN billet_beneficiary_name character varying(100);
ALTER TABLE base.bank ADD COLUMN billet_demonstratives text;
ALTER TABLE base.bank RENAME COLUMN payment_place TO billet_payment_place;
ALTER TABLE base.bank RENAME COLUMN instructions TO billet_instructions;

ALTER TABLE base.company_user ADD COLUMN market_withdraw_notifications boolean;
UPDATE base.company_user SET market_withdraw_notifications = false;
ALTER TABLE base.company_user ALTER COLUMN market_withdraw_notifications SET NOT NULL;

CREATE TABLE base.market_withdraw_alert (
  id bigint NOT NULL,
  market_withdraw_id bigint NOT NULL,
  CONSTRAINT pk_market_withdraw_alert PRIMARY KEY (id),
  CONSTRAINT fk_1_market_withdraw_alert FOREIGN KEY (id)
    REFERENCES base.alert (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_2_market_withdraw_alert FOREIGN KEY (market_withdraw_id)
    REFERENCES base.market_withdraw (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION
);
ALTER TABLE base.market_withdraw_alert OWNER TO billing;

INSERT INTO base.alert_type (code, name) VALUES ('MWR', 'Market Withdraw Request');
INSERT INTO base.alert_type (code, name) VALUES ('MWD', 'Market Withdraw Deny');
INSERT INTO base.alert_type (code, name) VALUES ('MWL', 'Market Withdraw Release');

UPDATE base.bank SET billet_beneficiary_name = '${DEALER_NAME} (Por ${PROVIDER_TRADING_NAME} - ${PROVIDER_DOCUMENT_NUMBER})', 
  billet_demonstratives = '${DEALER_NAME} processa seus pagamentos utilizando tecnologia e segurança ${PROVIDER_TRADING_NAME}'
   || '<br/><br/>Não pague este Boleto com Cheque ou Depósito.<br/>Isso poderá fazer com que o pagamento não seja reconhecido.<br/>'
   || '<br/>Se não for possível pagar este boleto imediatamente, teste novamente após algumas horas.',
   billet_payment_place = 'Pagável em qualquer banco até o vencimento',
   billet_instructions = 'Não receber após o vencimento.<br/><br/>Não aceitamos pagamentos em cheque.'
WHERE code = '001';
