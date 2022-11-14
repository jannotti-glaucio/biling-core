package tech.jannotti.billing.core.banking.santander.persistence.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import tech.jannotti.billing.core.banking.santander.persistence.model.SantanderCNAB400RemittanceFile;
import tech.jannotti.billing.core.persistence.repository.AbstractRepository;

@Repository("banking.santander.cnab400RemittanceFileRepository")
public interface CNAB400RemittanceFileRepository extends AbstractRepository<SantanderCNAB400RemittanceFile, Long> {

    @Query(value = "SELECT NEXTVAL('banking_bb.cnab400_remittance_file_id_seq')", nativeQuery = true)
    public Long getNextRemittanceFileId();

}
