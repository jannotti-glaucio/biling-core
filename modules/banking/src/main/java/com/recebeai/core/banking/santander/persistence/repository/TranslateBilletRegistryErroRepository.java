package tech.jannotti.billing.core.banking.santander.persistence.repository;

import org.springframework.stereotype.Repository;

import tech.jannotti.billing.core.banking.santander.persistence.model.SantanderTranslateBilletRegistryErro;
import tech.jannotti.billing.core.persistence.repository.AbstractRepository;

@Repository("banking.santander.translateBilletRegistryErroRepository")
public interface TranslateBilletRegistryErroRepository
    extends AbstractRepository<SantanderTranslateBilletRegistryErro, Integer> {

    public SantanderTranslateBilletRegistryErro getByCodigo(String codigo);

}
