package tech.jannotti.billing.core.banking.bb.soap.translate;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import tech.jannotti.billing.core.banking.bb.persistence.model.BancoBrasilTranslateBilletRegistryErro;
import tech.jannotti.billing.core.banking.bb.persistence.repository.TranslateBilletRegistryErroRepository;
import tech.jannotti.billing.core.commons.log.LogFactory;
import tech.jannotti.billing.core.commons.log.LogManager;
import tech.jannotti.billing.core.connector.response.AbstractResultCodeTranslator;
import tech.jannotti.billing.core.persistence.model.base.resultcode.BaseResultCode;

@Component("banking.bb.soap.resultCodeTranslator")
public class ResultCodeTranslator extends AbstractResultCodeTranslator {

    private static final LogManager logManager = LogFactory.getManager(ResultCodeTranslator.class);

    @Autowired
    private TranslateBilletRegistryErroRepository translateBilletRegistryErroRepository;

    public BaseResultCode translateErrorToResultCode(String nomeProgramaErro, Short numeroPosicaoErroPrograma,
        String textoMensagemErro, long textoNumeroTituloBeneficiario) {

        String codigo = nomeProgramaErro.substring(3) + StringUtils.leftPad(numeroPosicaoErroPrograma.toString(), 4, "0");

        BancoBrasilTranslateBilletRegistryErro translateErro = translateBilletRegistryErroRepository.getByCodigo(codigo);

        if (translateErro == null) {
            logManager.logWARN("Traducao de codigo de erro nao mapeado [nomeProgramaErro=%s, numeroPosicaoErroPrograma=%s, " +
                "textoMensagemErro=%s, textoNumeroTituloBeneficiario=%s]", nomeProgramaErro, numeroPosicaoErroPrograma,
                textoMensagemErro, textoNumeroTituloBeneficiario);
            return getBankUnknowReturnResultCode();
        }

        return translateErro.getBaseResultCode();
    }

}
