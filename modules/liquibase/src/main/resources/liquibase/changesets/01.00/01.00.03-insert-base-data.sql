--liquibase formatted sql
--changeset billing:1.00.03 dbms:postgresql
--comment Insercao de dados iniciais do schema base

INSERT INTO base.country (id, code, name) VALUES (1, 'BR', 'Brasil');
INSERT INTO base.language(id, country_id, code, name, date_format, number_format, number_decimal_separator) VALUES (1, 1, 'pt-BR', 'Português Brasil', 'dd/MM/yyyy', '0.00', ',');
  
--- Perfis
INSERT INTO base.role(code, name) VALUES
  ('A', 'Administrator'),
  ('P', 'Company'),
  ('D', 'Dealer'),
  ('C', 'Customer');

-- Tipos de documento
INSERT INTO base.document_type(id, country_id, person_type, code, name, mask, web_mask, validator_path) VALUES
  (1, 1, 'I', 'CPF-BR', 'CPF',  '###.###.###-##', '000.000.000-00', 'documentValidators.cpfBR'),
  (2, 1, 'L', 'CNPJ-BR', 'CNPJ', '##.###.###/####-##', '00.000.000/0000-00', 'documentValidators.cnpjBR');

-- Bancos
INSERT INTO base.bank (code, name, connector_path, payment_place, instructions) VALUES
  ('001', 'Banco do Brasil', 'bankingConnectors.bb',  
    'Pagável em qualquer banco até o vencimento. Após, atualize o boleto no site bb.com.br', 
    'Intermediado por: ${COMPANY_NAME} - ${COMPANY_DOCUMENT_TYPE}: ${COMPANY_DOCUMENT_NUMBER}'),
  ('104', 'Caixa Econômica', 'bankingConnectors.mock', null, null),
  ('033', 'Santander', 'bankingConnectors.mock', null, null),
  ('341', 'Itaú', 'bankingConnectors.mock', null, null),
  ('237', 'Bradesco', 'bankingConnectors.mock', null, null);
  
-- Codigos de resposta
INSERT INTO base.result_code_group(key, name, http_status) VALUES
  ('V', 'Erros de Validação', 400),
  ('R', 'Regras de Negócio', 403),
  ('A', 'Erros da Adquirente', 403),
  ('B', 'Erros do Banco', 403);

SELECT base.insert_result_code('000', 'SUCCESS', true, 'pt-BR', 'Sucesso', null, null);
SELECT base.insert_result_code('001', 'EMPTY_QUERY_RESULT', true, 'pt-BR', 'A consulta não retornou resultados', null, null);
SELECT base.insert_result_code('999', 'GENERIC_ERROR', false, 'pt-BR', 'Erro genérico', null, 500);

SELECT base.insert_result_code('101', 'REQUIRED_PARAMETER_MISSING', false, 'pt-BR', 'O campo [%s] é obrigatório', 'V', null);
SELECT base.insert_result_code('102', 'INVALID_INTEGER_PARAMETER', false, 'pt-BR', 'Campo [%s] com inteiro inválido [%s]', 'V', null);
SELECT base.insert_result_code('103', 'INVALID_BOOLEAN_PARAMETER', false, 'pt-BR', 'Campo [%s] com boleano inválido [%s]', 'V', null);
SELECT base.insert_result_code('104', 'INVALID_DATE_PARAMETER', false, 'pt-BR', 'Campo [%s] com valor data inválida [%s]', 'V', null);
SELECT base.insert_result_code('105', 'INVALID_DATE_TIME_PARAMETER', false, 'pt-BR', 'Campo [%s] com data/hora inválida [%s]', 'V', null);
SELECT base.insert_result_code('106', 'INVALID_MONTH_PARAMETER', false, 'pt-BR', 'Campo [%s] com mês inválido [%s]', 'V', null);
SELECT base.insert_result_code('107', 'INVALID_YEAR_PARAMETER', false, 'pt-BR', 'Campo [%s] com ano inválido [%s]', 'V', null);
SELECT base.insert_result_code('108', 'INVALID_EMAIL_PARAMETER', false, 'pt-BR', 'Campo [%s] com email inválido [%s]', 'V', null);
SELECT base.insert_result_code('109', 'INVALID_PERSON_TYPE_PARAMETER', false, 'pt-BR', 'Campo [%s] com tipo de pessoa inválido [%s]', 'V', null);
SELECT base.insert_result_code('110', 'INVALID_DOCUMENT_TYPE_PARAMETER', false, 'pt-BR', 'Campo [%s] com tipo de documento inválido [%s]', 'V', null);
SELECT base.insert_result_code('111', 'INVALID_ADDRESS_TYPE_PARAMETER', false, 'pt-BR', 'Campo [%s] com tipo de endereço inválido [%s]', 'V', null);
SELECT base.insert_result_code('112', 'INVALID_PAYMENT_METHOD_PARAMETER', false, 'pt-BR', 'Campo [%s] com forma de pagamento inválida [%s]', 'V', null);
SELECT base.insert_result_code('113', 'INVALID_MEDIA_TYPE_PARAMETER', false, 'pt-BR', 'Campo [%s] com tipo de mídia inválido [%s]', 'V', null);
SELECT base.insert_result_code('114', 'INVALID_COUNTRY_PARAMETER', false, 'pt-BR', 'Campo [%s] com pais inválido [%s]', 'V', null);
SELECT base.insert_result_code('115', 'INVALID_STATUS_LIST_PARAMETER', false, 'pt-BR', 'Campo [%s] com lista de status inválida [%s]', 'V', null);
SELECT base.insert_result_code('116', 'INVALID_CRUD_OPERATION_PARAMETER', false, 'pt-BR', 'Campo [%s] com  operação inválida [%s]', 'V', null);
SELECT base.insert_result_code('117', 'INVALID_FILTER_LENGTH_PARAMETER', false, 'pt-BR', 'Expressão de filtro fora do tamanho permitido', 'V', null);
SELECT base.insert_result_code('118', 'INVALID_SORT_DIRECTION_PARAMETER', false, 'pt-BR', 'Valor inválido para o parâmetro direction [%2$s]', 'V', null);
SELECT base.insert_result_code('120', 'NOT_FOUND_TOKEN_PARAMETER', false, 'pt-BR', 'Campo [%s] com token não localizado [%s]', 'V', null);
SELECT base.insert_result_code('121', 'LENGTH_PARAMETER_EXCEEDS_LMIT', false, 'pt-BR', 'Campo [%s] acima do tamanho máximo permitido', 'V', null);

SELECT base.insert_result_code('131', 'INVALID_PAGGING_PARAMETERS', false, 'pt-BR', 'Combinação inválida de parâmetros de paginação (page=%s, size=%s)', 'V', null);
SELECT base.insert_result_code('132', 'INVALID_SORTING_PARAMETERS', false, 'pt-BR', 'Combinação inválida de parâmetros de ordenação (sort=%s, direction=%s)', 'V', null);
SELECT base.insert_result_code('133', 'INVALID_SORTING_SORT_VALUE', false, 'pt-BR', 'Valor inválido para o parâmetro sort [%s]', 'V', null);
SELECT base.insert_result_code('134', 'PAGGING_SIZE_EXCEEDS_LIMIT', false, 'pt-BR', 'Valor do parâmetro size acima do limite permitido [%s]', 'V', null);
SELECT base.insert_result_code('135', 'INVALID_DOCUMENT_TYPE_VALUE', false, 'pt-BR', 'O tipo de documento [%s] é inválido', 'V', null);
SELECT base.insert_result_code('136', 'INVALID_STATE_VALUE', false, 'pt-BR', 'O tipo de estado [%s] é inválido', 'V', null);
SELECT base.insert_result_code('137', 'INVALID_DATE_TO_FILTER_VALUE', false, 'pt-BR', 'Valor inválido para o parâmetro dateToFiler [%s]', 'V', null);

SELECT base.insert_result_code('201', 'TOKEN_NOT_FOUND', false, 'pt-BR', 'Token [%s] não localizado', 'R', null);
SELECT base.insert_result_code('202', 'ADDRESS_TOKEN_NOT_FOUND', false, 'pt-BR', 'Token de endereço [%s] não localizado', 'R', null);
SELECT base.insert_result_code('210', 'INVALID_USER_STATUS_TO_DELETE', false, 'pt-BR', 'Usuário em status inválido para ser excluído', 'R', null);
SELECT base.insert_result_code('211', 'INVALID_COMPANY_STATUS_TO_DELETE', false, 'pt-BR', 'Empresa em status inválido para ser excluído', 'R', null);
SELECT base.insert_result_code('212', 'INVALID_DEALER_STATUS_TO_DELETE', false, 'pt-BR', 'Fornecedor em status inválido para ser excluído', 'R', null);
SELECT base.insert_result_code('213', 'INVALID_CUSTOMER_STATUS_TO_DELETE', false, 'pt-BR', 'Cliente em status inválido para ser excluído', 'R', null);
SELECT base.insert_result_code('214', 'INVALID_INVOICE_STATUS_TO_CANCEL', false, 'pt-BR', 'Fatura em status inválido para ser cancelada', 'R', null);
SELECT base.insert_result_code('215', 'INVALID_PAYMENT_STATUS_TO_CANCEL', false, 'pt-BR', 'Pagamento em status inválido para ser cancelado', 'R', null);
SELECT base.insert_result_code('216', 'INVALID_INVOICE_STATUS_TO_UPDATE', false, 'pt-BR', 'Fatura em status inválido para ser atualizado', 'R', null);

SELECT base.insert_result_code('220', 'ADMIN_USER_REQUIRED_TO_ACCESS', false, 'pt-BR', 'Um usuário do nível Administrador é necessário para este acesso', 'R', null);
SELECT base.insert_result_code('221', 'COMPANY_USER_REQUIRED_TO_ACCESS', false, 'pt-BR', 'Um usuário do nível Empresa é necessário para este acesso', 'R', null);
SELECT base.insert_result_code('222', 'DEALER_USER_REQUIRED_TO_ACCESS', false, 'pt-BR', 'Um usuário do nível Fornecedor é necessário para este acesso', 'R', null);
SELECT base.insert_result_code('223', 'CUSTOMER_USER_REQUIRED_TO_ACCESS', false, 'pt-BR', 'Um usuário do nível Cliente é necessário para este acesso', 'R', null);

SELECT base.insert_result_code('230', 'INVALID_DOCUMENT_NUMBER', false, 'pt-BR', 'Número do documento inválido [%s]', 'R', null);
SELECT base.insert_result_code('231', 'INVALID_CURRENT_PASSWORD', false, 'pt-BR', 'Senha atual incorreta', 'R', null);
SELECT base.insert_result_code('232', 'ONE_BILLING_ADDRESS_REQUIRED', false, 'pt-BR', 'Um endereço de cobrança deve ser informado', 'R', null);
SELECT base.insert_result_code('233', 'DUPLICATED_BILLING_ADDRESS', false, 'pt-BR', 'Apenas um endereço de cobrança é permitido', 'R', null);
SELECT base.insert_result_code('234', 'COMPANY_BILLING_ADDRESS_REQUIRED', false, 'pt-BR', 'A empresa deve ter um endereço de cobrança', 'R', null);
SELECT base.insert_result_code('235', 'DEALER_BILLING_ADDRESS_REQUIRED', false, 'pt-BR', 'O fornecedor deve ter um endereço de cobrança', 'R', null);
SELECT base.insert_result_code('236', 'CUSTOMER_BILLING_ADDRESS_REQUIRED', false, 'pt-BR', 'O cliente deve ter um endereço de cobrança', 'R', null);
SELECT base.insert_result_code('237', 'INVOICE_WITHOUT_ACTIVE_BANK_BILLET', false, 'pt-BR', 'Fatura sem um boleto ativo', 'R', null);
SELECT base.insert_result_code('238', 'INVOICE_BANK_BILLET_PAYMENT_METHOD_REQUIRED', false, 'pt-BR', 'Um fatura na forma de pagamento Boleto é requerida', 'R', null);
SELECT base.insert_result_code('239', 'BANK_BILLET_WAITING_REGISTRY_RESPONSE', false, 'pt-BR', 'Boleto aguardando resposta de registro', 'R', null);
SELECT base.insert_result_code('240', 'BANK_BILLET_WAITING_CANCELLATION_RESPONSE', false, 'pt-BR', 'Boleto aguardando resposta de cancelamento', 'R', null);
SELECT base.insert_result_code('241', 'DEALER_BANK_ACCOUNT_MISSING', false, 'pt-BR', 'Fornecedor sem conta bancária configurada', 'R', null);
SELECT base.insert_result_code('242', 'INVOICE_INVALID_EXPIRATION_DATE', false, 'pt-BR', 'A data de vencimento da fatura não pode ser inferior a %s', 'R', null);
SELECT base.insert_result_code('243', 'CUSTOMER_EMAIL_REQUIRED_TO_SEND', false, 'pt-BR', 'Endereço de email do Cliente necessário para fazer o envio', 'R', null);

SELECT base.insert_result_code('398', 'ACQUIRER_UNKNOW_RETURN', false, 'pt-BR', 'Retorno desconhecido enviado pela Adquirente', 'A', null);
SELECT base.insert_result_code('399', 'ACQUIRER_GENERIC_ERROR', false, 'pt-BR', 'Erro generico recebido da Adquirente', 'A', 500);  

SELECT base.insert_result_code('498', 'BANKING_UNKNOW_RETURN', false, 'pt-BR', 'Retorno desconhecido enviado pelo Banco', 'B', null);
SELECT base.insert_result_code('499', 'BANKING_GENERIC_ERROR', false, 'pt-BR', 'Erro generico recebido do Banco', 'B', 500);
