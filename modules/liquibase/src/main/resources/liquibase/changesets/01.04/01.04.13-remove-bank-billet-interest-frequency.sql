--liquibase formatted sql
--changeset billing:1.04.13 dbms:postgresql
--comment Remove opção de escolher juros diários ou mensal

-- Remove o campo que era utilizado para setar a frequência de juros (diário/mensal)
ALTER TABLE base.dealer DROP column bank_billet_interest_frequency;

-- Remove o tipo ENUM criado para a frequência de juros
DROP TYPE base.interest_frequency_enum CASCADE;