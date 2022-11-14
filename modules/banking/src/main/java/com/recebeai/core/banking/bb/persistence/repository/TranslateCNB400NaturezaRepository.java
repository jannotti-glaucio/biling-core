package tech.jannotti.billing.core.banking.bb.persistence.repository;

import org.springframework.stereotype.Repository;

import tech.jannotti.billing.core.banking.bb.persistence.model.BancoBrasilTranslateCNB400Natureza;
import tech.jannotti.billing.core.persistence.repository.AbstractRepository;

@Repository("banking.bb.translateCNB400NaturezaRepository")
public interface TranslateCNB400NaturezaRepository
    extends AbstractRepository<BancoBrasilTranslateCNB400Natureza, Integer> {

    public BancoBrasilTranslateCNB400Natureza getByCodigo(int codigo);

}
