package tech.jannotti.billing.core.banking.santander.persistence.repository;

import org.springframework.stereotype.Repository;

import tech.jannotti.billing.core.banking.santander.persistence.model.SantanderTranslateCNAB400Ocorrencia;
import tech.jannotti.billing.core.persistence.repository.AbstractRepository;

@Repository("banking.santander.translateCNAB400OcorrenciaRepository")
public interface TranslateCNAB400OcorrenciaRepository extends AbstractRepository<SantanderTranslateCNAB400Ocorrencia, Integer> {

    public SantanderTranslateCNAB400Ocorrencia getByCodigo(String codigo);

}
