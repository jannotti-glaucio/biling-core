--liquibase formatted sql
--changeset billing:1.01.53 dbms:postgresql context:dev,sandbox
--comment Atualizando as tabelas de usuarios dividadas por nivel

UPDATE base.user SET token = '123456789012' WHERE username = 'admin';
UPDATE base.user SET token = '234567890123' WHERE username = 'company';
UPDATE base.user SET token = '345678901234' WHERE username = 'dealer';
