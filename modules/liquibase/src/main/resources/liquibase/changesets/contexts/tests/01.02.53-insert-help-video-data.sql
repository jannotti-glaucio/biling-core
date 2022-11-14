--liquibase formatted sql
--changeset billing:1.02.53 dbms:postgresql context:dev,sandbox
--comment Inserindo dados de teste de Videos de Ajuda

INSERT INTO base.help_video_category (id, name) VALUES (1, 'Informativo');

INSERT INTO base.help_video (url, title, description, help_video_category_id) VALUES
('https://www.youtube.com/embed/lPTrKBSDe6E','Conheça a Nova Plataforma de Cobrança',
 'Vídeo explicando como vai funcionar a Nova Plataforma de Cobrança, criado pela FEBRABAN para modernizar o pagamento de boletos no Brasil.',
 1);
