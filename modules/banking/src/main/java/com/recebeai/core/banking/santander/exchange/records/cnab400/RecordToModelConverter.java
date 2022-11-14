package tech.jannotti.billing.core.banking.santander.exchange.records.cnab400;

import org.apache.commons.lang.StringUtils;

import tech.jannotti.billing.core.banking.santander.exchange.records.cnab400.retorno.DetalheRetorno;
import tech.jannotti.billing.core.banking.santander.exchange.records.cnab400.retorno.HeaderRetorno;
import tech.jannotti.billing.core.banking.santander.exchange.records.cnab400.retorno.TraillerRetorno;
import tech.jannotti.billing.core.banking.santander.persistence.model.SantanderCNAB400DischargeDetail;
import tech.jannotti.billing.core.banking.santander.persistence.model.SantanderCNAB400DischargeFile;
import tech.jannotti.billing.core.commons.util.DateTimeHelper;

public class RecordToModelConverter {

    public static void loadHeaderRetornoToDischargeFile(HeaderRetorno headerRetorno,
        SantanderCNAB400DischargeFile dischargeFile) {

        dischargeFile.setAgenciaBeneficiario(headerRetorno.agenciaBeneficiario);
        dischargeFile.setContaMovimentoBeneficiario(headerRetorno.contaMovimentoBeneficiario);
        dischargeFile.setDataDoMovimento(DateTimeHelper.toLocalDate(headerRetorno.dataDoMovimento));
    }

    public static SantanderCNAB400DischargeDetail convertDetalheRetornoToDetail(DetalheRetorno detalheRetorno) {

        SantanderCNAB400DischargeDetail dischargeDetail = new SantanderCNAB400DischargeDetail();
        dischargeDetail.setReceiveDate(DateTimeHelper.getNowDateTime());

        dischargeDetail.setNossoNumero(detalheRetorno.nossoNumero);
        dischargeDetail.setSeuNumero(detalheRetorno.seuNumero);
        dischargeDetail.setDataDoCredito(DateTimeHelper.toLocalDate(detalheRetorno.dataDoCredito));
        dischargeDetail.setCodigoDoBancoCobrador(detalheRetorno.codigoDoBancoCobrador);
        dischargeDetail.setAgenciaRecebedora(detalheRetorno.agenciaRecebedora);
        dischargeDetail.setValorDoTitulo(detalheRetorno.valorDoTitulo);
        dischargeDetail.setValorDaTarifaCobrada(detalheRetorno.valorDaTarifaCobrada);
        dischargeDetail.setValorDosJurosDeAtraso(detalheRetorno.valorDosJurosDeAtraso);
        dischargeDetail.setValorTotalRecebido(detalheRetorno.valorTotalRecebido);
        dischargeDetail.setValorDosJurosDeMora(detalheRetorno.valorDosJurosDeMora);
        dischargeDetail.setCodigoDeOcorrencia(detalheRetorno.codigoDeOcorrencia);
        dischargeDetail.setDataDaOcorrencia(DateTimeHelper.toLocalDate(detalheRetorno.dataDaOcorrencia));
        dischargeDetail.setCodigoOriginalDaRemessa(detalheRetorno.codigoOriginalDaRemessa);
        dischargeDetail.setCodigoDoPrimeiroErro(StringUtils.trimToEmpty(detalheRetorno.codigoDoPrimeiroErro));
        dischargeDetail.setCodigoDoSegundoErro(StringUtils.trimToEmpty(detalheRetorno.codigoDoSegundoErro));
        dischargeDetail.setCodigoDoTerceiroErro(StringUtils.trimToEmpty(detalheRetorno.codigoDoTerceiroErro));

        return dischargeDetail;
    }

    public static void loadTrailerRetornoToDischargeFile(TraillerRetorno traillerRetorno,
        SantanderCNAB400DischargeFile dischargeFile) {

        dischargeFile.setQtdDeCobrancasSimples(traillerRetorno.qtdDeCobrancasSimples);
        dischargeFile.setValorDeCobrancasSimples(traillerRetorno.valorDeCobrancasSimples);
    }

}
