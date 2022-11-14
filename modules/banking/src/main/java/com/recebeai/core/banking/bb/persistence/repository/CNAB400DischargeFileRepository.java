package tech.jannotti.billing.core.banking.bb.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import tech.jannotti.billing.core.banking.bb.persistence.model.BancoBrasilCNAB400DischargeFile;
import tech.jannotti.billing.core.banking.bb.persistence.model.BancoBrasilCompanyBankAccount;
import tech.jannotti.billing.core.persistence.enums.BankDischargeStatusEnum;
import tech.jannotti.billing.core.persistence.repository.AbstractRepository;

@Repository("banking.bb.cnab400DischargeFileRepository")
public interface CNAB400DischargeFileRepository extends AbstractRepository<BancoBrasilCNAB400DischargeFile, Long> {

    @Query("SELECT f FROM BancoBrasilCNAB400DischargeFile f WHERE companyBankAccount=:companyBankAccount "
        + "AND EXISTS(SELECT d FROM BancoBrasilCNAB400DischargeDetail d WHERE d.dischargeFile=f AND d.status=:status)")
    public List<BancoBrasilCNAB400DischargeFile> findByCompanyBankAccountAndDischargeStatus(
        @Param("companyBankAccount") BancoBrasilCompanyBankAccount companyBankAccount,
        @Param("status") BankDischargeStatusEnum status);

}
