package tech.jannotti.billing.core.banking.santander.exchange.records.cnab400.remessa;

import java.util.Date;

import tech.jannotti.billing.core.banking.santander.exchange.records.cnab400.AbstractRecord;

public class HeaderRemessa extends AbstractRecord {

    public Integer codigoDaRemessa;
    public String literalDeTransmissao;
    public Integer codigoDoServico;
    public String literalDeServico;
    public String codigoDeTransmissao;
    public String nomeDoBeneficiario;
    public String codigoDoBanco;
    public String nomeDoBanco;
    public Date dataDeGravacao;
    public Integer zeros1;
    public String brancos1;
    public Integer numeroDaVersao;

    public HeaderRemessa() {
        codigoDeRegistro = 0;
        codigoDaRemessa = 1;
        literalDeTransmissao = "REMESSA";
        codigoDoServico = 1;
        literalDeServico = "COBRANCA";
        codigoDoBanco = "033";
        codigoDoBanco = "SANTANDER";
    }

}
