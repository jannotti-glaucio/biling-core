drop view vw_base_invoice cascade;
drop view vw_base_payment_bank_billet cascade;

create view vw_base_invoice as
select d.token dealer_token, d.name dealer_name,
   c.token customer_token, c.name customer_name,
   i.token invoice_token, i.description, i.amount, i.creation_date, i.expiration_date, i.instalment, i.status, i.payment_date, i.cancelation_date
from base.invoice i
  inner join base.customer c on c.id = i.customer_id
  inner join base.dealer d on d.id = c.dealer_id
order by d.name, c.name, i.expiration_date desc;

create view vw_base_payment_bank_billet as
select i.token invoice_token, p.token as payment_token, bb.payer_name, t.code payer_document_type, bb.payer_document_number, p.amount,
  bb.expiration_date, p.status, bb.document_number, bb.billet_number, p.payment_date, p.paid_amount, p.payment_cost, p.provider_fee, 
  p.cancelation_date
from base.payment_bank_billet bb
  inner join base.payment p on p.id = bb.id
  inner join base.invoice i on i.id = p.invoice_id
  inner join base.document_type t on t.id = bb.payer_document_type_id
where p.payment_method = 'B'
order by bb.expiration_date;
