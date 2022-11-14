--liquibase formatted sql
--changeset billing:1.00.06 dbms:postgresql
--comment Insercao de dados iniciais do schema banking_bb

INSERT INTO banking_bb.document_type (base_document_type_id,codigo_de_inscricao) VALUES 
  (1, 1), -- CPF
  (2, 2); -- CNPJ

-- result codes    

SELECT base.insert_result_code('401', 'INVALID_AGENCY_NUMBER_OR_DIGIT', false, 'pt-BR', 'Agência, conta ou digito inválido',
  'B', null);
SELECT base.insert_result_code('402', 'INVALID_WALLET_NUMBER', false, 'pt-BR', 'Número da carteira inválido', 'B', null);
SELECT base.insert_result_code('403', 'INVALID_EXPIRE_DATE', false, 'pt-BR', 'Data de vencimento inválida', 'B', null);
SELECT base.insert_result_code('404', 'INVALID_BILLET_AMOUNT', false, 'pt-BR', 'Valor do título inválido', 'B', null);
SELECT base.insert_result_code('405', 'INVALID_WALLET_PAYMENT_CODE', false, 'pt-BR', 'Código para baixa/devolução inválido',
  'B', null);
SELECT base.insert_result_code('406', 'INVALID_PAYER_DOCUMENT', false, 'pt-BR', 'Tipo/número de documento do pagador inválido',
  'B', null);
SELECT base.insert_result_code('407', 'INVALID_ZIP_CODE', false, 'pt-BR', 'CEP inválido', 'B', null);
SELECT base.insert_result_code('408', 'INVALID_BILLET_DOCUMENT_KIND', false, 'pt-BR', 'Espécie de título inválida', 'B', null);
SELECT base.insert_result_code('409', 'INVALID_OCURRENCY_CODE', false, 'pt-BR', 'Operação não permitida para a carteira',
  'B', null);
SELECT base.insert_result_code('410', 'INVALID_BANK_AGREEMENT', false, 'pt-BR', 'Convênio bancário inválido ou encerrado',
  'B', null);

-- translation
    
INSERT INTO banking_bb.translate_comando(comando_codigo, comando_mensagem) VALUES (3, 'Comando Recusado');

INSERT INTO banking_bb.translate_comando_natureza
  (translate_comando_id, natureza_recebimento_codigo, natureza_recebimento_mensagem, base_result_code_id) VALUES
((SELECT id FROM banking_bb.translate_comando WHERE comando_codigo = 3), 2, 'variação da carteira inválida',
  (SELECT id FROM base.result_code WHERE key = '402')),
((SELECT id FROM banking_bb.translate_comando WHERE comando_codigo = 3),  5, 'Espécie de título inválida para carteira/variação',
  (SELECT id FROM base.result_code WHERE key = '408')),
((SELECT id FROM banking_bb.translate_comando WHERE comando_codigo = 3),  7, 'Prefixo da agência usuária inválido',
  (SELECT id FROM base.result_code WHERE key = '401')),
((SELECT id FROM banking_bb.translate_comando WHERE comando_codigo = 3),  8, 'Valor do título/apólice inválido',
  (SELECT id FROM base.result_code WHERE key = '404')),
((SELECT id FROM banking_bb.translate_comando WHERE comando_codigo = 3),  9, 'Data de vencimento inválida',                                 
  (SELECT id FROM base.result_code WHERE key = '403')),
((SELECT id FROM banking_bb.translate_comando WHERE comando_codigo = 3), 10, 'Fora do prazo/só admissível na carteira',
  (SELECT id FROM base.result_code WHERE key = '409')),
((SELECT id FROM banking_bb.translate_comando WHERE comando_codigo = 3), 21, 'Carteira inválida',
  (SELECT id FROM base.result_code WHERE key = '402')),
((SELECT id FROM banking_bb.translate_comando WHERE comando_codigo = 3), 27, 'Nome do sacado/cedente inválido',
  (SELECT id FROM base.result_code WHERE key = '405')),
((SELECT id FROM banking_bb.translate_comando WHERE comando_codigo = 3), 42, 'CEP/UF inválido/não compatíveis (ECT)',
  (SELECT id FROM base.result_code WHERE key = '407')),
((SELECT id FROM banking_bb.translate_comando WHERE comando_codigo = 3), 45, 'Carteira/variação encerrada',
  (SELECT id FROM base.result_code WHERE key = '402')),
((SELECT id FROM banking_bb.translate_comando WHERE comando_codigo = 3), 46, 'Convenio encerrado',
  (SELECT id FROM base.result_code WHERE key = '410')),
((SELECT id FROM banking_bb.translate_comando WHERE comando_codigo = 3), 47, 'Titulo tem valor diverso do informado',
  (SELECT id FROM base.result_code WHERE key = '404')),
((SELECT id FROM banking_bb.translate_comando WHERE comando_codigo = 3), 48, 'Motivo de baixa invalido para a carteira',
  (SELECT id FROM base.result_code WHERE key = '405')),
((SELECT id FROM banking_bb.translate_comando WHERE comando_codigo = 3), 50, 'Comando incompatível com a carteira',
  (SELECT id FROM base.result_code WHERE key = '409')),
((SELECT id FROM banking_bb.translate_comando WHERE comando_codigo = 3), 66, 'Número do documento do sacado (CNPJ/CPF) inválido',
  (SELECT id FROM base.result_code WHERE key = '406')),
((SELECT id FROM banking_bb.translate_comando WHERE comando_codigo = 3), 72, 'Prefixo da Ag. cobradora inválido',
  (SELECT id FROM base.result_code WHERE key = '401')),
((SELECT id FROM banking_bb.translate_comando WHERE comando_codigo = 3), 83, 'Carteira/variação não localizada no cedente',
  (SELECT id FROM base.result_code WHERE key = '407'));
