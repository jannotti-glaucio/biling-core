--liquibase formatted sql
--changeset billing:1.03.08 dbms:postgresql
--comment Adicionando result codes de atualizacao de fatura

SELECT base.insert_result_code('268', 'INVALID_INVOICE_STATUS_TO_UPDATE', false, 'pt-BR', 'Fatura em status inválido para ser atualizada',
   'R', null);
 