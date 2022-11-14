package tech.jannotti.billing.core.banking.santander.soap.translate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import tech.jannotti.billing.core.banking.santander.persistence.model.SantanderTranslateBilletRegistryErro;
import tech.jannotti.billing.core.banking.santander.persistence.repository.TranslateBilletRegistryErroRepository;
import tech.jannotti.billing.core.commons.log.LogFactory;
import tech.jannotti.billing.core.commons.log.LogManager;
import tech.jannotti.billing.core.connector.response.AbstractResultCodeTranslator;
import tech.jannotti.billing.core.persistence.model.base.resultcode.BaseResultCode;

@Component("banking.santander.soap.resultCodeTranslator")
public class ResultCodeTranslator extends AbstractResultCodeTranslator {

    private static final LogManager logManager = LogFactory.getManager(ResultCodeTranslator.class);

    @Autowired
    private TranslateBilletRegistryErroRepository translateBilletRegistryErroRepository;

    public BaseResultCode translateDescricaoErroCodigoToResultCode(String descricaoErroCodigo, String descricaoErroCodigoMensagem,
        long seuNumero) {

        SantanderTranslateBilletRegistryErro translateErro = translateBilletRegistryErroRepository
            .getByCodigo(descricaoErroCodigo);

        if (translateErro == null) {
            logManager.logWARN(
                "Traducao de erro de registro de boleto nao mapeado [descricaoErroCodigo=%s, descricaoErroCodigoMensagem=%s, seuNumero=%s]",
                descricaoErroCodigo, descricaoErroCodigoMensagem, seuNumero);
            return getBankUnknowReturnResultCode();
        }

        return translateErro.getBaseResultCode();
    }
}
