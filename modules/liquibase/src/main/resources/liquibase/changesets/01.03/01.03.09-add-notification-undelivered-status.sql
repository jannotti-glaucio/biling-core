--liquibase formatted sql
--changeset billing:1.03.09 dbms:postgresql
--comment Adicionando o status de notificacoes nao entregues

ALTER DOMAIN base.notification_status_enum DROP CONSTRAINT notification_status_enum_check;
ALTER DOMAIN base.notification_status_enum ADD CONSTRAINT notification_status_enum_check CHECK(VALUE IN ('P', 'D', 'U', 'I'));

UPDATE base.notification SET status = 'U' WHERE status = 'P';
