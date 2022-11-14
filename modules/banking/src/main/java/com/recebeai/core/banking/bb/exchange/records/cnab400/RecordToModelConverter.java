package tech.jannotti.billing.core.banking.bb.exchange.records.cnab400;

import tech.jannotti.billing.core.banking.bb.exchange.records.cnab400.retorno.DetalheRetorno;
import tech.jannotti.billing.core.banking.bb.exchange.records.cnab400.retorno.HeaderRetorno;
import tech.jannotti.billing.core.banking.bb.exchange.records.cnab400.retorno.TraillerRetorno;
import tech.jannotti.billing.core.banking.bb.persistence.model.BancoBrasilCNAB400DischargeDetail;
import tech.jannotti.billing.core.banking.bb.persistence.model.BancoBrasilCNAB400DischargeFile;
import tech.jannotti.billing.core.commons.util.DateTimeHelper;
import tech.jannotti.billing.core.commons.util.NumberHelper;

public class RecordToModelConverter {

    public static void loadHeaderRetornoToDischargeFile(HeaderRetorno headerRetorno,
        BancoBrasilCNAB400DischargeFile dischargeFile) {
        dischargeFile.setDataGravacao(DateTimeHelper.toLocalDate(headerRetorno.dataGravacao));
    }

    public static BancoBrasilCNAB400DischargeDetail convertDetalheRetornoToDischargeDetail(DetalheRetorno detalheRetorno) {

        BancoBrasilCNAB400DischargeDetail dischargeDetail = new BancoBrasilCNAB400DischargeDetail();
        dischargeDetail.setReceiveDate(DateTimeHelper.getNowDateTime());

        dischargeDetail.setNumeroTituloCedente(NumberHelper.parseAsLong(detalheRetorno.numeroTituloDadoPeloCedente));
        dischargeDetail.setNossoNumero(detalheRetorno.nossoNumero);
        dischargeDetail.setNumeroConvenioCobrancaCedente(detalheRetorno.numeroConvenioCobrancaCedente);
        dischargeDetail.setComando(detalheRetorno.comando);
        dischargeDetail.setNaturezaRecebimento(detalheRetorno.naturezaRecebimento);
        dischargeDetail.setDataVencimento(DateTimeHelper.toLocalDate(detalheRetorno.dataVencimento));
        dischargeDetail.setValorTitulo(detalheRetorno.valorTitulo);

        dischargeDetail.setCodigoBancoRecebedor(detalheRetorno.codigoBancoRecebedor);
        dischargeDetail.setPrefixoAgenciaRecebedora(detalheRetorno.prefixoAgenciaRecebedora);
        dischargeDetail.setDvPrefixoAgenciaRecebedora(detalheRetorno.dvPrefixoAgenciaRecebedora);
        dischargeDetail.setDataLiquidacao(DateTimeHelper.toLocalDate(detalheRetorno.dataLiquidacao));
        dischargeDetail.setDataCredito(DateTimeHelper.toLocalDate(detalheRetorno.dataCredito));

        dischargeDetail.setValorTarifa(detalheRetorno.valorTarifa);
        dischargeDetail.setValorRecebido(detalheRetorno.valorRecibo);
        dischargeDetail.setJurosMora(detalheRetorno.jurosMora);
        dischargeDetail.setValorLancamento(detalheRetorno.valorLancamento);
        dischargeDetail.setIndicativoDebitoCredito(detalheRetorno.indicativoDebitoCredito);

        return dischargeDetail;
    }

    public static void loadTraillerRetornoToDischargeFile(TraillerRetorno traillerRetorno,
        BancoBrasilCNAB400DischargeFile dischargeFile) {
        dischargeFile.setQtdTitulos(traillerRetorno.quantidadeTitulos1);
        dischargeFile.setValorTotal(traillerRetorno.valorTotal1);
    }

}
