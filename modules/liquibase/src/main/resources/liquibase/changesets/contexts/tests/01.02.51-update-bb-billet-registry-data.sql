--liquibase formatted sql
--changeset billing:1.02.51 dbms:postgresql context:dev,sandbox
--comment Atualizando dados de teste para registro online de boletos no BB

UPDATE base.company_bank_account SET bank_channel_id = (SELECT id FROM base.bank_channel WHERE code = 'BB-HOMO')
  WHERE id = (SELECT id FROM base.bank_account WHERE token = '12345678901234');

UPDATE banking_bb.company_bank_account SET
  numero_carteira = 17,
  variacao_carteira = 19,
  numero_convenio_cobranca = 2625444,
  numero_convenio_lider = 2625444,
  especie_titulo_ws = 4,
  chave_usuario_j = 'J1234567',
  token_clientid = 'eyJpZCI6IjgwNDNiNTMtZjQ5Mi00YyIsImNvZGlnb1B1YmxpY2Fkb3IiOjEwOSwiY29kaWdvU29mdHdhcmUiOjEsInNlcXVlbmNpYWxJbnN0YWxhY2FvIjoxfQ',
  token_secret = 'eyJpZCI6IjBjZDFlMGQtN2UyNC00MGQyLWI0YSIsImNvZGlnb1B1YmxpY2Fkb3IiOjEwOSwiY29kaWdvU29mdHdhcmUiOjEsInNlcXVlbmNpYWxJbnN0YWxhY2FvIjoxLCJzZXF1ZW5jaWFsQ3JlZGVuY2lhbCI6MX0'
WHERE base_company_bank_account_id = (SELECT id FROM base.bank_account WHERE token = '12345678901234');
