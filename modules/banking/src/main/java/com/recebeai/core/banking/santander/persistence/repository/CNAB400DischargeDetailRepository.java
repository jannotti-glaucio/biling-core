package tech.jannotti.billing.core.banking.santander.persistence.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import tech.jannotti.billing.core.banking.santander.persistence.model.SantanderCNAB400DischargeDetail;
import tech.jannotti.billing.core.banking.santander.persistence.model.SantanderCNAB400DischargeFile;
import tech.jannotti.billing.core.persistence.enums.BankDischargeStatusEnum;
import tech.jannotti.billing.core.persistence.model.base.bank.BaseBankDischarge;
import tech.jannotti.billing.core.persistence.repository.AbstractRepository;

@Repository("banking.santander.cnab400DischargeDetailRepository")
public interface CNAB400DischargeDetailRepository extends AbstractRepository<SantanderCNAB400DischargeDetail, Long> {

    public List<SantanderCNAB400DischargeDetail> findByDischargeFileAndStatus(
        SantanderCNAB400DischargeFile dischargeFile, BankDischargeStatusEnum status);

    @Modifying
    @Query("UPDATE SantanderCNAB400DischargeDetail SET status=:status, processingDate=:processingDate WHERE id=:id")
    public void updateStatusAndProcessingDateById(@Param("id") long id, @Param("status") BankDischargeStatusEnum status,
        @Param("processingDate") LocalDateTime processingDate);

    @Modifying
    @Query("UPDATE SantanderCNAB400DischargeDetail SET baseBankDischarge=:baseBankDischarge, status=:status,"
        + " processingDate=:processingDate WHERE id=:id")
    public void updateBaseBankDischargeAndStatusAndProcessingDateById(@Param("id") long id,
        @Param("baseBankDischarge") BaseBankDischarge baseBankDischarge, @Param("status") BankDischargeStatusEnum status,
        @Param("processingDate") LocalDateTime processingDate);

}
