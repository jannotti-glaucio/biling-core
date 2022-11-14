package tech.jannotti.billing.core.banking.santander.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import tech.jannotti.billing.core.banking.santander.persistence.model.SantanderCNAB400DischargeFile;
import tech.jannotti.billing.core.banking.santander.persistence.model.SantanderCompanyBankAccount;
import tech.jannotti.billing.core.persistence.enums.BankDischargeStatusEnum;
import tech.jannotti.billing.core.persistence.repository.AbstractRepository;

@Repository("banking.santander.cnab400DischargeFileRepository")
public interface CNAB400DischargeFileRepository extends AbstractRepository<SantanderCNAB400DischargeFile, Long> {

    @Query("SELECT f FROM SantanderCNAB400DischargeFile f WHERE companyBankAccount=:companyBankAccount "
        + "AND EXISTS(SELECT d FROM SantanderCNAB400DischargeDetail d WHERE d.dischargeFile=f AND d.status=:status)")
    public List<SantanderCNAB400DischargeFile> findByCompanyBankAccountAndDischargeStatus(
        @Param("companyBankAccount") SantanderCompanyBankAccount companyBankAccount,
        @Param("status") BankDischargeStatusEnum status);

}
