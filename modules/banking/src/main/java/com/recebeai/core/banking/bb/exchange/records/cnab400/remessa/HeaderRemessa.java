package tech.jannotti.billing.core.banking.bb.exchange.records.cnab400.remessa;

import java.util.Date;

import tech.jannotti.billing.core.banking.bb.exchange.records.cnab400.AbstractRecord;

public class HeaderRemessa extends AbstractRecord {

    public Integer tipoOperacao;
    public String identificacaoExtensoTipoOperacao;
    public Integer identificacaoTipoServico;
    public String identificacaoExtensoTipoServico;
    public String complementoRegistro1;
    public String prefixoAgencia;
    public String dvPrefixoAgencia;
    public String numeroContaCorrenteCedente;
    public String dvNumeroContaCorrenteCedente;
    public Integer complementoRegistro2;
    public String nomeCedente;
    public String codigoNomeBanco;
    public Date dataGravacao;
    public Long sequenciaRemessa;
    public String complementoRegistro3;
    public Integer numeroConvenioLider;
    public String complementoRegistro4;

    public HeaderRemessa() {
        identificacaoRegistro = 0;
        tipoOperacao = 1;
        identificacaoTipoServico = 1;
        identificacaoExtensoTipoServico = "COBRANCA";
        codigoNomeBanco = "001BANCODOBRASIL";
    }

}
