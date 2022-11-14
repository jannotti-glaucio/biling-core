--liquibase formatted sql
--changeset billing:1.02.05 dbms:postgresql
--comment Criando tabelas para consulta de Videos de Ajuda

CREATE TABLE base.help_video_category (
  id smallint NOT NULL,
  name character varying(50) NOT NULL,
  CONSTRAINT pk_help_video_category PRIMARY KEY (id)
);

CREATE TABLE base.help_video (
  id serial NOT NULL,
  url character varying(100) NOT NULL,
  title character varying(50) NOT NULL,
  description character varying(200) NOT NULL,
  help_video_category_id smallint NOT NULL,
  CONSTRAINT pk_help_video PRIMARY KEY (id),
  CONSTRAINT fk_1_help_video FOREIGN KEY (help_video_category_id)
    REFERENCES base.help_video_category (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION
);
