
create view vw_banking_bb_cnab400_remittance as
select i.token as invoice_token, i.status token_status, p.token payment_token, p.status payment_status, 
  bb.payer_name, bb.payer_document_number, p.amount, bb.expiration_date,
  r.operation, r.status remittance_status, r.creation_date, r.processing_date, r.cancelation_date,
  rf.file_name, rd.numero_titulo_cedente, rd.nosso_numero, rd.comando
from base.bank_remittance r
  inner join base.payment p on p.id = r.payment_id
  inner join base.payment_bank_billet bb on bb.id = p.id
  inner join base.invoice i on i.id = p.invoice_id
  inner join banking_bb.cnab400_remittance_detail rd on rd.base_bank_remittance_id = r.id
  inner join banking_bb.cnab400_remittance_file rf on rf.id = rd.cnab400_remittance_file_id
order by r.processing_date;

create view vw_banking_bb_cnab400_discharge as
select i.token as invoice_token, i.status token_status, p.token payment_token, p.status payment_status, 
  bb.payer_name, bb.payer_document_number, p.amount, bb.expiration_date,
  d.operation, d.processing_date, d.result_code_id,
  df.file_name, dd.numero_titulo_cedente, dd.nosso_numero, dd.comando, dd.natureza_recebimento, dd.status
from base.bank_discharge d
  inner join base.payment p on p.id = d.payment_id
  inner join base.payment_bank_billet bb on bb.id = p.id
  inner join base.invoice i on i.id = p.invoice_id
  inner join banking_bb.cnab400_discharge_detail dd on dd.base_bank_discharge_id = d.id
  inner join banking_bb.cnab400_discharge_file df on df.id = dd.cnab400_discharge_file_id
order by d.processing_date;
