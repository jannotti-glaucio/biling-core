package tech.jannotti.billing.core.banking.santander.exchange;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import tech.jannotti.billing.core.banking.santander.persistence.model.SantanderTranslateCNAB400Ocorrencia;
import tech.jannotti.billing.core.banking.santander.persistence.repository.TranslateCNAB400OcorrenciaRepository;
import tech.jannotti.billing.core.commons.log.LogFactory;
import tech.jannotti.billing.core.commons.log.LogManager;
import tech.jannotti.billing.core.connector.response.AbstractResultCodeTranslator;
import tech.jannotti.billing.core.persistence.model.base.resultcode.BaseResultCode;

@Component("banking.santander.exchange.resultCodeTranslator")
public class ResultCodeTranslator extends AbstractResultCodeTranslator {

    private static final LogManager logManager = LogFactory.getManager(ResultCodeTranslator.class);

    @Autowired
    private TranslateCNAB400OcorrenciaRepository translateCNAB400OcorrenciaRepository;

    public BaseResultCode translateOcorrenciaDeErroToResultCode(String codigoDeErro, long nossoNumero) {

        SantanderTranslateCNAB400Ocorrencia translateErro = translateCNAB400OcorrenciaRepository.getByCodigo(codigoDeErro);

        if (translateErro == null) {
            logManager.logWARN("Traducao de ocorrencia de erro nao mapeado [codigoDeErro=%s, nossoNumero=%s]", codigoDeErro,
                nossoNumero);
            return getBankUnknowReturnResultCode();
        }

        return translateErro.getBaseResultCode();
    }

}
