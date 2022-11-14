--liquibase formatted sql
--changeset billing:1.04.02 dbms:postgresql
--comment Insercao de dados iniciais do schema banking_santander

INSERT INTO banking_santander.document_type (base_document_type_id, tipo_de_documento) VALUES 
  (1, 1), -- CPF
  (2, 2); -- CNPJ

UPDATE base.bank SET connector_path='bankingConnectors.santander',
  billet_demonstratives='${DEALER_NAME} processa seus pagamentos utilizando tecnologia e segurança ${PROVIDER_TRADING_NAME}<br/><br/>'
    || 'Não pague este Boleto com Cheque ou Depósito.<br/>Isso poderá fazer com que o pagamento não seja reconhecido.<br/><br/>'
    || 'Se não for possível pagar este boleto imediatamente, teste novamente após algumas horas.',
  billet_instructions='Não receber após o vencimento.<br/><br/>Não aceitamos pagamentos em cheque.',
  billet_beneficiary_name='${DEALER_NAME} (Por ${PROVIDER_TRADING_NAME} - ${PROVIDER_DOCUMENT_NUMBER})',
  billet_payment_place='Pagável em qualquer banco até o vencimento'  
WHERE code = '033';

INSERT INTO base.bank_channel (bank_id, code, description) VALUES
  ((SELECT id FROM base.bank WHERE code = '033'), 'SANTANDER-HOMO', 'Homologação do Santander'),
  ((SELECT id FROM base.bank WHERE code = '033'), 'SANTANDER-PROD', 'Produção do Santander');
  
INSERT INTO banking_santander.bank_channel (base_bank_channel_id, ticket_url, cobranca_url, request_timeout, expiracao_ticket, tipo_ambiente_xml) VALUES
  ((SELECT id FROM base.bank_channel WHERE code = 'SANTANDER-HOMO'),
   'https://ymbdlb.santander.com.br/dl-ticket-services/TicketEndpointService',
   'https://ymbcash.santander.com.br/ymbsrv/CobrancaEndpointService',
   30000, 100, 'T'
  ),
  ((SELECT id FROM base.bank_channel WHERE code = 'SANTANDER-PROD'), 
   'https://ymbdlb.santander.com.br/dl-ticket-services/TicketEndpointService',
   'https://ymbcash.santander.com.br/ymbsrv/CobrancaEndpointService',
   30000, 100, 'P'
  );
