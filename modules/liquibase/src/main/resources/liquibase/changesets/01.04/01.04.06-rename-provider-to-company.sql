--liquibase formatted sql
--changeset billing:1.04.06 dbms:postgresql
--comment Renomeando os objetos usando Provider para Company

ALTER TABLE base.payment RENAME COLUMN provider_fee TO company_fee;

UPDATE base.bank SET billet_demonstratives='${DEALER_NAME} processa seus pagamentos utilizando tecnologia e segurança ${COMPANY_TRADING_NAME}<br/><br/>'
    || 'Não pague este Boleto com Cheque ou Depósito.<br/>Isso poderá fazer com que o pagamento não seja reconhecido.<br/><br/>'
    || 'Se não for possível pagar este boleto imediatamente, teste novamente após algumas horas.',
  billet_beneficiary_name='${DEALER_NAME} (Por ${COMPANY_TRADING_NAME} - ${COMPANY_DOCUMENT_NUMBER})'
WHERE code in ('001','033');
