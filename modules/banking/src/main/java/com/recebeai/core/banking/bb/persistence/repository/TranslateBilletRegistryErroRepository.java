package tech.jannotti.billing.core.banking.bb.persistence.repository;

import org.springframework.stereotype.Repository;

import tech.jannotti.billing.core.banking.bb.persistence.model.BancoBrasilTranslateBilletRegistryErro;
import tech.jannotti.billing.core.persistence.repository.AbstractRepository;

@Repository("banking.bb.translateBilletRegistryErroRepository")
public interface TranslateBilletRegistryErroRepository
    extends AbstractRepository<BancoBrasilTranslateBilletRegistryErro, Integer> {

    public BancoBrasilTranslateBilletRegistryErro getByCodigo(String codigo);

}
