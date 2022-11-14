package tech.jannotti.billing.core.banking.bb.exchange;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import tech.jannotti.billing.core.banking.bb.persistence.model.BancoBrasilTranslateCNB400Natureza;
import tech.jannotti.billing.core.banking.bb.persistence.repository.TranslateCNB400NaturezaRepository;
import tech.jannotti.billing.core.commons.log.LogFactory;
import tech.jannotti.billing.core.commons.log.LogManager;
import tech.jannotti.billing.core.connector.response.AbstractResultCodeTranslator;
import tech.jannotti.billing.core.persistence.model.base.resultcode.BaseResultCode;

@Component("banking.bb.exchange.resultCodeTranslator")
public class ResultCodeTranslator extends AbstractResultCodeTranslator {

    private static final LogManager logManager = LogFactory.getManager(ResultCodeTranslator.class);

    @Autowired
    private TranslateCNB400NaturezaRepository translateCNB400NaturezaRepository;

    public BaseResultCode translateCNAB400NaturezaRecebimentoToResultCode(int naturezaRecebimento, long numeroTituloCedente) {

        BancoBrasilTranslateCNB400Natureza translateCNAB400Natureza = translateCNB400NaturezaRepository
            .getByCodigo(naturezaRecebimento);

        if (translateCNAB400Natureza == null) {
            logManager.logWARN(
                "Traducao de natureza de recebimento nao mapeada [naturezaRecebimento=%s, numeroTituloCedente=%s]",
                naturezaRecebimento, numeroTituloCedente);
            return getBankUnknowReturnResultCode();
        }

        return translateCNAB400Natureza.getBaseResultCode();
    }

}
