package tech.jannotti.billing.core.banking.bb.exchange.records.cnab400.remessa;

import java.util.Date;

import tech.jannotti.billing.core.banking.bb.exchange.records.cnab400.AbstractRecord;

public class DetalheRemessa extends AbstractRecord {

    public Integer tipoInscricaoCedente;
    public String numeroCpfCnpjCedente;
    public String prefixoAgencia;
    public String dvPrefixoAgencia;
    public String numeroContaCorrenteCedente;
    public String dvNumContaCorCedente;
    public Integer numeroConvenioCobrancaCedente;
    public String codigoControleEmpresa;
    public Long nossoNumero;
    public Integer numeroPrestacao;
    public Integer grupoValor;
    public String complementoRegistro;
    public String indicativoMensagemSacadorAvalista;
    public String prefixoTitulo;
    public Integer variacaoCarteira;
    public Integer contaCaucao;
    public Integer numeroBordero;
    public String tipoCobranca;
    public Integer carteiraCobranca;
    public Integer comando;
    public String numeroTituloAtribuidoCedente;
    public Date dataVencimento;
    public Integer valorTitulo;
    public String numeroBanco;
    public String prefixoAgenciaCobradora;
    public String dvPrefixoAgenciaCobradora;
    public Integer especieTitulo;
    public String aceiteTitulo;
    public Date dataEmissao;
    public Integer instrucaoCodificada1;
    public Integer instrucaoCodificada2;
    public Integer jurosMoraDiaAtraso;
    public Date dataLimitiConcessaoDesconto;
    public Integer valorDesconto;
    public Integer valorIOFQtdeUnidadeVariavel;
    public Integer valorAbatimento;
    public Integer tipoInscricaoSacado;
    public String numeroCnpjCpfSacado;
    public String nomeSacado;
    public String complementoRegistro1;
    public String enderecoSacado;
    public String bairroSacado;
    public String cepSacado;
    public String cidadeSacado;
    public String ufSacado;
    public String observacoesMensagemSacadorAvalista;
    public String numeroDiasProtesto;
    public String complementoRegistro2;

    public DetalheRemessa() {
        identificacaoRegistro = 7;
        numeroBanco = "001";
    }

}
