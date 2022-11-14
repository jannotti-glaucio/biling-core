package tech.jannotti.billing.core.banking.santander.exchange.records.cnab400.remessa;

import tech.jannotti.billing.core.banking.santander.exchange.records.cnab400.AbstractRecord;

public class TraillerRemessa extends AbstractRecord {

    public Integer qtdTotalDeLinhas;
    public Long valorTotalDosTitulos;
    public Integer zeros1;

    public TraillerRemessa() {
        codigoDeRegistro = 9;
    }

}
