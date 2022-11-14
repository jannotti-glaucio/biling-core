package tech.jannotti.billing.core.banking.bb.persistence.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import tech.jannotti.billing.core.banking.bb.persistence.model.BancoBrasilCNAB400RemittanceFile;
import tech.jannotti.billing.core.persistence.repository.AbstractRepository;

@Repository("banking.bb.cnab400RemittanceFileRepository")
public interface CNAB400RemittanceFileRepository extends AbstractRepository<BancoBrasilCNAB400RemittanceFile, Long> {

    @Query(value = "SELECT NEXTVAL('banking_bb.cnab400_remittance_file_id_seq')", nativeQuery = true)
    public Long getNextRemittanceFileId();

}
