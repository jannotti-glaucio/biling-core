--liquibase formatted sql
--changeset billing:1.02.52 dbms:postgresql context:dev,sandbox
--comment Inserindo dados de teste de Apliacoes externas

INSERT INTO base.application (dealer_id, token, name, client_id, client_secret, status, creation_date) VALUES
  ((SELECT id FROM base.dealer WHERE token = '123456'), '123456789', 'Aplicação', '123456789012345678901234567890',
  '$2a$10$YY4bi4ahfZxSCvNOzhdcMO2dvwI1E4DgdvL290LLNBsAduXvHFHt.', 'A', CURRENT_TIMESTAMP);
