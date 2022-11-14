package tech.jannotti.billing.core.banking.santander.exchange.records.cnab400.retorno;

import java.util.Date;

import tech.jannotti.billing.core.banking.santander.exchange.records.cnab400.AbstractRecord;

public class HeaderRetorno extends AbstractRecord {

    public Integer codigoDaRemessa;
    public String literalDeTransmissao;
    public Integer codigoDoServico;
    public String literalDeServico;
    public String agenciaBeneficiario;
    public String contaMovimentoBeneficiario;
    public String contaCobrancaBeneficiario;
    public String nomeDoBeneficiario;
    public String codigoDoBanco;
    public String nomeDoBanco;
    public Date dataDoMovimento;
    public Integer zeros1;
    public String codigoDoBeneficiario;
    public String brancos1;
    public Integer numeroDaVersao;

}
