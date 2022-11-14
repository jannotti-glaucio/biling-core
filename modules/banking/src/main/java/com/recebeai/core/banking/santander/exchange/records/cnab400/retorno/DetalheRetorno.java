package tech.jannotti.billing.core.banking.santander.exchange.records.cnab400.retorno;

import java.util.Date;

import tech.jannotti.billing.core.banking.santander.exchange.records.cnab400.AbstractRecord;

public class DetalheRetorno extends AbstractRecord {

    public Integer tipoDeInscricaoDoBeneficiario;
    public String numeroDeInscricaoDoBeneficiario;
    public String agenciaDoBeneficiario;
    public String contaMovimentoBeneficiario;
    public String contaCobrancaBeneficiario;
    public String numeroDeControleDoParticipante;
    public Long nossoNumero;
    public String brancos1;
    public Integer codigoDaCarteira;
    public Integer codigoDeOcorrencia;
    public Date dataDaOcorrencia;
    public Long seuNumero;
    public Long nossoNumero2;
    public Integer codigoOriginalDaRemessa;
    public String codigoDoPrimeiroErro;
    public String codigoDoSegundoErro;
    public String codigoDoTerceiroErro;
    public String brancos2;
    public Date dataDeVencimento;
    public Integer valorDoTitulo;
    public String codigoDoBancoCobrador;
    public String agenciaRecebedora;
    public Integer especieDeDocumento;
    public Integer valorDaTarifaCobrada;
    public Integer valorDeOutrasDespesas;
    public Integer valorDosJurosDeAtraso;
    public Integer valorDaIOF;
    public Integer valorDoAbatimento;
    public Integer valorDoDesconto;
    public Integer valorTotalRecebido;
    public Integer valorDosJurosDeMora;
    public Integer valorDeOutrosCreditos;
    public String brancos3;
    public String aceite;
    public String brancos4;
    public Date dataDoCredito;
    public String nomeDoPagador;
    public String identificadorDoComplementoConta;
    public Integer unidadeDeValorDaMoeda;
    public Integer valorDoTituloEmOutraUnidade;
    public Integer valorDoIOCEmOutraUnidade;
    public Integer valorDoDebitoOuCredito;
    public String indicadorDeDebitoOuCredito;
    public String brancos5;
    public Integer numeroDaVersao;

}
