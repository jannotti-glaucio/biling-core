package tech.jannotti.billing.core.banking.bb.exchange.records.cnab400.remessa;

import tech.jannotti.billing.core.banking.bb.exchange.records.cnab400.AbstractRecord;

public class TraillerRemessa extends AbstractRecord {

    public String complementoRegistro;

    public TraillerRemessa() {
        identificacaoRegistro = 9;
    }

}
