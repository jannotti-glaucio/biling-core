package tech.jannotti.billing.core.banking.santander.exchange.records.cnab400.remessa;

import java.util.Date;

import tech.jannotti.billing.core.banking.santander.SantanderConstants;
import tech.jannotti.billing.core.banking.santander.exchange.records.cnab400.AbstractRecord;

public class DetalheRemessa extends AbstractRecord {

    public Integer tipoDeInscricaoDoBeneficiario;
    public String numeroDeInscricaoDoBeneficiario;
    public String codigoDeTransmissao;
    public String numeroDeControleDoParticipante;
    public Long nossoNumero;
    public Date dataDoSegundoDesconto;
    public String brancos1;
    public Integer informacaoDeMulta;
    public Integer percentualDeMultaPorAtraso;
    public Integer unidadeDeValorDaMoeda;
    public Integer valorDoTituloEmOutraUnidade;
    public String brancos2;
    public Date dataDeCobrandaDaMulta;
    public Integer codigoDaCarteira;
    public Integer codigoDeOcorrencia;
    public Long seuNumero;
    public Date dataDeVencimento;
    public Integer valorDoTitulo;
    public String codigoDoBancoCobrador;
    public String agenciaCobradora;
    public Integer especieDeDocumento;
    public String aceite;
    public Date dataDeEmissao;
    public Integer primeiraInstrucaoDeCobranca;
    public Integer segundaInstrucaoDeCobranca;
    public Integer valorDeMoraPorDiaDeAtraso;
    public Date dataLimiteParaDesconto;
    public Integer valorDoDesconto;
    public Integer valorDaIOF;
    public Integer valorDoBatimento;
    public Integer tipoDeInscricaoDoPagador;
    public String numeroDeInscricaoDoPagador;
    public String nomeDoPagador;
    public String enderecoDoPagador;
    public String bairroDoPagador;
    public String cepDoPagador;
    public String complementoDoCepDoPagador;
    public String municipioDoPagador;
    public String estadoDoPagador;
    public String nomeDoPagadorAvalista;
    public String brancos3;
    public String identificadorDoComplementoConta;
    public String complementoConta;
    public String brancos4;
    public Integer numeroDeDiasParaProtesto;
    public String brancos5;

    public DetalheRemessa() {
        codigoDeRegistro = 1;
        codigoDoBancoCobrador = SantanderConstants.BANK_NUMBER;
    }

}
