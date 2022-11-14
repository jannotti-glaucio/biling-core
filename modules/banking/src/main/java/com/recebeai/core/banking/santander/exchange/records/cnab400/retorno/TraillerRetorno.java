package tech.jannotti.billing.core.banking.santander.exchange.records.cnab400.retorno;

import tech.jannotti.billing.core.banking.santander.exchange.records.cnab400.AbstractRecord;

public class TraillerRetorno extends AbstractRecord {

    public Integer codigoDaRemessa;
    public Integer codigoDoServico;
    public String codigoDoBanco;
    public String brancos1;
    public Integer qtdDeCobrancasSimples;
    public Long valorDeCobrancasSimples;
    public String numeroDoAvisoCobrancaSimples;
    public String brancos2;
    public Integer qtdDeCobrancasCaucionadas;
    public Long valorDeCobrancasCaucionadas;
    public String numeroDoAvisoCobrancaCaucionada;
    public String brancos3;
    public Integer qtdDeCobrancasDescontadas;
    public Long valorDeCobrancasDescontadas;
    public String numeroDoAvisoCobrancaDescontada;
    public String brancos4;
    public Integer numeroDaVersao;

}
