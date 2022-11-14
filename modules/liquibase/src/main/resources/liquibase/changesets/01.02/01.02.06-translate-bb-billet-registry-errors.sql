--liquibase formatted sql
--changeset billing:1.02.06 dbms:postgresql
--comment Criando tabelas para traducao de erros de registro online de boleto no BB

CREATE TABLE banking_bb.translate_billet_registry_error(
  id serial NOT NULL,
  codigo character varying (10),
  mensagem character varying(100),
  base_result_code_id integer NOT NULL,
  CONSTRAINT pk_translate_billet_registry_error PRIMARY KEY (id),
  CONSTRAINT fk_1_translate_billet_registry_error FOREIGN KEY (base_result_code_id)
    REFERENCES base.result_code (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT uk_1_translate_billet_registry_error UNIQUE (codigo)    
);
ALTER TABLE banking_bb.translate_billet_registry_error OWNER TO billing;

SELECT base.insert_result_code('411', 'INVALID_WALLET_OR_BANK_AGREEMENT', false, 'pt-BR', 'Número da carteira ou convênio inválido', 'B', null);
SELECT base.insert_result_code('412', 'INVALID_PAYER_ADDRESS', false, 'pt-BR', 'Endereço do pagador inválido ou inconsistente', 'B', null);

INSERT INTO banking_bb.translate_billet_registry_error (mensagem, codigo, base_result_code_id)
VALUES
('Beneficiario Nao Autorizado a Emitir Boleto','SR0020109',(SELECT id from base.result_code where key = '411')),
('Carteira do convenio informado invalida para registro de boleto ONLINE','SR0020040',(SELECT id from base.result_code where key = '402')),
('Carteira do convenio nao informada','SR0030006',(SELECT id from base.result_code where key = '402')),
('Carteira informada invalida','SR0020038',(SELECT id from base.result_code where key = '402')),
('Carteira informada não pode ser convertida','SR0130009',(SELECT id from base.result_code where key = '402')),
('CEP do endereco do pagador invalido','SR0020033',(SELECT id from base.result_code where key = '407')),
('CEP do pagador invalido','SR0050014',(SELECT id from base.result_code where key = '407')),
('CEP do pagador nao informado','SR0050016',(SELECT id from base.result_code where key = '407')),
('Codigo Beneficiario invalido','SB0030002',(SELECT id from base.result_code where key = '411')),
('Codigo cliente invalido','S001I0034',(SELECT id from base.result_code where key = '411')),
('Codigo item restricao cobranca invalido','S101C0305',(SELECT id from base.result_code where key = '411')),
('Codigo prefixo dependencia cedente invalido','S001I0033',(SELECT id from base.result_code where key = '407')),
('Codigo restricao beneficiario cobranca invalido','S101C0304',(SELECT id from base.result_code where key = '411')),
('Codigo tipo Convenio invalido','SR0060002',(SELECT id from base.result_code where key = '410')),
('Codigo tipo inscricao sacado invalido','S001I0004',(SELECT id from base.result_code where key = '406')),
('Convenio inexistente ou numeração estourada','SR0070007',(SELECT id from base.result_code where key = '410')),
('Convenio informado esta encerrado','SR0020041',(SELECT id from base.result_code where key = '410')),
('Convenio invalido para numeracao pelo Banco','SR0070006',(SELECT id from base.result_code where key = '410')),
('Convenio invalido para Numeracao pelo Cliente','SR0040010',(SELECT id from base.result_code where key = '410')),
('Convenio nao cadastrado','SR0030008',(SELECT id from base.result_code where key = '410')),
('Convenio só permite registro por remessa','SR0040012',(SELECT id from base.result_code where key = '410')),
('Convenio/Carteira/Variacao nao cadastrado','SR0030007',(SELECT id from base.result_code where key = '411')),
('CPF do pagador nao encontrado na base','SR0050005',(SELECT id from base.result_code where key = '406')),
('CPF pagador existe 6 ou mais vezes na base','SR0050003',(SELECT id from base.result_code where key = '406')),
('Digito verificador do CPF ou CGC Invalido','SR0050004',(SELECT id from base.result_code where key = '406')),
('DV do CPF ou CGC pagador Invalido','SR0050002',(SELECT id from base.result_code where key = '406')),
('Informar Desconto, Modalidade ou Carteira','SR0130004',(SELECT id from base.result_code where key = '402')),
('Modalidade 4, carteira deve ser informada','SR0130010',(SELECT id from base.result_code where key = '402')),
('Modalidade 6, carteira deve ser informada','SR0130011',(SELECT id from base.result_code where key = '402')),
('Numero sequencial inválido informado','S101C0303',(SELECT id from base.result_code where key = '411')),
('Numero cep sacado cobranca invalido','S001I0032',(SELECT id from base.result_code where key = '407')),
('Numero contrato cobranca invalido','S032C0003',(SELECT id from base.result_code where key = '411')),
('Numero contrato cobranca invalido','SC0350006',(SELECT id from base.result_code where key = '411')),
('Numero convenio não foi informado','SR0070002',(SELECT id from base.result_code where key = '410')),
('Numero da carteira invalido para registro ONLINE','SR0020004',(SELECT id from base.result_code where key = '402')),
('Numero do convenio invalido para registro ONLINE','SR0020003',(SELECT id from base.result_code where key = '410')),
('Numero do Convenio Invalido','SR0040002',(SELECT id from base.result_code where key = '410')),
('Numero do convenio nao informado','SR0020036',(SELECT id from base.result_code where key = '410')),
('Numero do convenio nao informado','SR0030003',(SELECT id from base.result_code where key = '410')),
('Numero inscricao sacado cobranca invalido','S001I0005',(SELECT id from base.result_code where key = '406')),
('Numero operacao cliente cobranca invalido','S001I0038',(SELECT id from base.result_code where key = '411')),
('O Tipo de Convenio nao permite numeracao pelo cliente','SR0040005',(SELECT id from base.result_code where key = '410')),
('Obrigatorio fornecer carteira variacao para convenio informado','SR0030004',(SELECT id from base.result_code where key = '402')),
('Pagamento Parcial não permitido p/ convenio','SR0020083',(SELECT id from base.result_code where key = '410')),
('Tipo do Convenio não permite numeracao pelo Banco','SR0070005',(SELECT id from base.result_code where key = '410')),
('Titulo não pertence a carteira 17','SR0110004',(SELECT id from base.result_code where key = '402')),
('Valor do boleto invalido','SR0020010',(SELECT id from base.result_code where key = '404')),
('Valor original titulo cobranca invalido','S001I0018',(SELECT id from base.result_code where key = '404')),
('Variacao da carteira nao informada','SR0030005',(SELECT id from base.result_code where key = '402')),
('Variacao informada invalida','SR0020039',(SELECT id from base.result_code where key = '402')),
('Variação informada sem carteira','SR0130003',(SELECT id from base.result_code where key = '402')),
('UF Informada nao corresponde ao CEP','SR0050026',(SELECT id from base.result_code where key = '412')),
('UF informada no endereco pagador diferente do CEP','SR0050030',(SELECT id from base.result_code where key = '412'));
