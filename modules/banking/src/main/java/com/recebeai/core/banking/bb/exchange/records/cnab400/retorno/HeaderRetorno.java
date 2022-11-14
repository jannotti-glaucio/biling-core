package tech.jannotti.billing.core.banking.bb.exchange.records.cnab400.retorno;

import java.util.Date;

import tech.jannotti.billing.core.banking.bb.exchange.records.cnab400.AbstractRecord;

public class HeaderRetorno extends AbstractRecord {

    public Integer tipoOperacao;
    public String identificacaoTipoOperacao;
    public Integer identificacaoTipoServico;
    public String identificacaoExtensoTipoServico;
    public String complementoRegistro1;
    public String prefixoAgencia;
    public String dvPrefixoAgencia;
    public String numeroContaCorrente;
    public String dvNumeroContaCorrenteCedente;
    public Integer zeros;
    public String nomeCedente;
    public String codigoNomeBanco;
    public Date dataGravacao;
    public Integer sequencialRetorno;
    public String complementoRegistro2;
    public Integer numeroConvenio;
    public String complementoRegistro3;

}
