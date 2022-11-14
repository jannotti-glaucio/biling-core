package tech.jannotti.billing.core.banking.bb.exchange.records.cnab400.retorno;

import java.util.Date;

import tech.jannotti.billing.core.banking.bb.exchange.records.cnab400.AbstractRecord;

public class DetalheRetorno extends AbstractRecord {

    public Integer zeros1;
    public Integer zeros2;
    public String prefixoAgencia;
    public String dvPrefixoAgencia;
    public String numeroContaCorrenteCedente;
    public String dvNumeroContaCorrenteCedente;
    public Integer numeroConvenioCobrancaCedente;
    public String numeroControleParticipante;
    public Long nossoNumero;
    public Integer tipoCobranca;
    public Integer tipoCobrançaEspecificoComando72;
    public Integer diasParaCalculo;
    public Integer naturezaRecebimento;
    public String prefixoTitulo;
    public Integer variacaoCarteira;
    public Integer contaCaucao;
    public Integer taxaDesconto;
    public Integer taxaIOF;
    public String branco1;
    public Integer carteira;
    public Integer comando;
    public Date dataLiquidacao;
    public String numeroTituloDadoPeloCedente;
    public String brancos1;
    public Date dataVencimento;
    public Integer valorTitulo;
    public String codigoBancoRecebedor;
    public String prefixoAgenciaRecebedora;
    public String dvPrefixoAgenciaRecebedora;
    public Integer especieTitulo;
    public Date dataCredito;
    public Integer valorTarifa;
    public Integer outrasDespesas;
    public Integer jurosDesconto;
    public Integer iofDesconto;
    public Integer valorAbatimento;
    public Integer descontoConcedido;
    public Integer valorRecibo;
    public Integer jurosMora;
    public Integer outrosRecebimentos;
    public Integer abatimentoAproveitadoSacado;
    public Integer valorLancamento;
    public Integer indicativoDebitoCredito;
    public Integer indicadorValor;
    public Integer valorAjuste;
    public String brancos2;
    public String brancos3;
    public Integer zeros3;
    public Integer zeros4;
    public Integer zeros5;
    public Integer zeros6;
    public Integer zeros7;
    public Integer zeros8;
    public Integer indicativoAutorizacaoLiquidacaoParcial;
    public String branco2;
    public Integer canalPagamentoTituloUtilizadoSacado;

}
