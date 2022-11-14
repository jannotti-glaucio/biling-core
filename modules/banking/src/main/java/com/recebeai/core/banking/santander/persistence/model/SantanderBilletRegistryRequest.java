package tech.jannotti.billing.core.banking.santander.persistence.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import tech.jannotti.billing.core.persistence.model.AbstractModel;
import tech.jannotti.billing.core.persistence.model.base.bank.BaseBankRemittance;
import tech.jannotti.billing.core.persistence.model.base.payment.BasePaymentBankBillet;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DynamicInsert
@DynamicUpdate
@Table(schema = "banking_santander", name = "billet_registry_request")
public class SantanderBilletRegistryRequest extends AbstractModel {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "base_payment_bank_billet_id")
    private BasePaymentBankBillet bankBillet;

    @ManyToOne
    @JoinColumn(name = "base_bank_remittance_id")
    private BaseBankRemittance baseBankRemittance;

    @ManyToOne
    @JoinColumn(name = "billet_ticket_response_id")
    private SantanderBilletTicketResponse billetTicketResponse;

    @Column(name = "request_date")
    private LocalDateTime requestDate;

    @Column(name = "dt_nsu")
    private LocalDate dtNsu;

    @Column(name = "nsu")
    private String nsu;

    @Column(name = "tp_ambiente")
    private String tpAmbiente;

}
