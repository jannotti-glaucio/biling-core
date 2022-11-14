--liquibase formatted sql
--changeset billing:1.01.11 dbms:postgresql
--comment Finalizando a divisao das tabelas de usuarios por nivel

ALTER TABLE base.user ADD CONSTRAINT uk_2_user UNIQUE (token);
ALTER TABLE base.user ALTER COLUMN token SET NOT NULL;
  