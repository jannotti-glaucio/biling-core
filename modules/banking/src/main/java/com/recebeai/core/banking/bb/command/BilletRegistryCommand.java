package tech.jannotti.billing.core.banking.bb.command;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Random;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import tech.jannotti.billing.core.banking.bb.BancoBrasilConstants;
import tech.jannotti.billing.core.banking.bb.persistence.model.BancoBrasilBankChannel;
import tech.jannotti.billing.core.banking.bb.persistence.model.BancoBrasilBilletRegistryRequest;
import tech.jannotti.billing.core.banking.bb.persistence.model.BancoBrasilBilletRegistryResponse;
import tech.jannotti.billing.core.banking.bb.persistence.model.BancoBrasilCompanyBankAccount;
import tech.jannotti.billing.core.banking.bb.persistence.model.BancoBrasilDocumentType;
import tech.jannotti.billing.core.banking.bb.persistence.model.BancoBrasilLevyingAgreement;
import tech.jannotti.billing.core.banking.bb.persistence.repository.BilletRegistryRequestRepository;
import tech.jannotti.billing.core.banking.bb.persistence.repository.BilletRegistryResponseRepository;
import tech.jannotti.billing.core.banking.bb.persistence.repository.DocumentTypeRepository;
import tech.jannotti.billing.core.banking.bb.rest.OAuthTokenRestClient;
import tech.jannotti.billing.core.banking.bb.rest.response.OAuthTokenResponse;
import tech.jannotti.billing.core.banking.bb.soap.RegistroCobrancaSOAPClient;
import tech.jannotti.billing.core.banking.bb.soap.stub.registro.Requisicao;
import tech.jannotti.billing.core.banking.bb.soap.stub.registro.Resposta;
import tech.jannotti.billing.core.banking.bb.soap.translate.ResultCodeTranslator;
import tech.jannotti.billing.core.commons.util.DateTimeHelper;
import tech.jannotti.billing.core.commons.util.NumberHelper;
import tech.jannotti.billing.core.connector.banking.response.RegisterBankBilletConnectorResponse;
import tech.jannotti.billing.core.connector.command.AbstractConnectorCommand;
import tech.jannotti.billing.core.persistence.model.base.bank.BaseBankRemittance;
import tech.jannotti.billing.core.persistence.model.base.payment.BasePaymentBankBillet;
import tech.jannotti.billing.core.persistence.model.base.resultcode.BaseResultCode;

@Component("banking.bb.billetRegistryCommand")
public class BilletRegistryCommand extends AbstractConnectorCommand implements BancoBrasilConstants {

    private static final short CODIGO_RETORNO_SUCESSO = 0;

    @Autowired
    private DocumentTypeRepository documentTypeRepository;

    @Autowired
    private BilletRegistryRequestRepository billetRegistryRequestRepository;

    @Autowired
    private BilletRegistryResponseRepository billetRegistryResponseRepository;

    @Autowired
    private OAuthTokenRestClient oauthTokenRestClient;

    @Autowired
    private RegistroCobrancaSOAPClient registroCobrancaSOAPClient;

    @Autowired
    private ResultCodeTranslator resultCodeTranslator;

    private Random numeroTituloClientePrefixRandom = new Random();

    public RegisterBankBilletConnectorResponse execute(BancoBrasilBankChannel bankChannel,
        BancoBrasilCompanyBankAccount companyBankAccount, BaseBankRemittance bankRemittance) {

        BasePaymentBankBillet bankBillet = bankRemittance.getBankBillet();
        BancoBrasilLevyingAgreement levyingAgreement = companyBankAccount.getLevyingAgreement(bankBillet.isExpiredPayment());
        long seuNumero = bankBillet.getId();

        // Pega o token de autenticacao
        OAuthTokenResponse tokenResponse = oauthTokenRestClient.getToken(bankChannel.getOauthUrl(),
            companyBankAccount.getTokenClientId(), companyBankAccount.getTokenSecret(), bankChannel.getRequestTimeout(),
            bankChannel.isValidateSSL());

        long nossoNumero = buildNossoNumero(levyingAgreement.getNumeroConvenio(), seuNumero);

        // Grava a requisicao no banco
        BancoBrasilBilletRegistryRequest registryRequest = buildRegistryRequest(companyBankAccount, levyingAgreement,
            bankRemittance, bankBillet, nossoNumero, seuNumero, bankBillet.getIssueDate());
        billetRegistryRequestRepository.save(registryRequest);

        // Envio o registro do boleto
        Requisicao requisicao = buildRequisicao(companyBankAccount, bankBillet, registryRequest);
        Resposta resposta = registroCobrancaSOAPClient.registrarBoleto(bankChannel.getRegistroBoletoUrl(),
            tokenResponse.getAccessToken(), requisicao, bankChannel.getRequestTimeout(), bankChannel.isValidateSSL());

        if (resposta.getCodigoRetornoPrograma() == CODIGO_RETORNO_SUCESSO) {

            // Grava a resposta de sucesso no banco
            BancoBrasilBilletRegistryResponse registryResponse = new BancoBrasilBilletRegistryResponse();
            registryResponse.setBilletRegistryRequest(registryRequest);
            registryResponse.setResponseDate(DateTimeHelper.getNowDateTime());
            registryResponse.setCodigoRetornoPrograma(resposta.getCodigoRetornoPrograma());
            registryResponse.setLinhaDigitavel(resposta.getLinhaDigitavel());
            registryResponse.setCodigoBarraNumerico(resposta.getCodigoBarraNumerico());
            billetRegistryResponseRepository.save(registryResponse);

            return new RegisterBankBilletConnectorResponse(getSuccessResultCode(), nossoNumero, seuNumero,
                resposta.getLinhaDigitavel());

        } else {

            // Grava a resposta erro no banco
            BancoBrasilBilletRegistryResponse registryResponse = new BancoBrasilBilletRegistryResponse();
            registryResponse.setBilletRegistryRequest(registryRequest);
            registryResponse.setResponseDate(DateTimeHelper.getNowDateTime());
            registryResponse.setCodigoRetornoPrograma(resposta.getCodigoRetornoPrograma());
            registryResponse.setNomeProgramaErro(resposta.getNomeProgramaErro());
            registryResponse.setTextoMensagemErro(resposta.getTextoMensagemErro());
            registryResponse.setNumeroPosicaoErroPrograma(resposta.getNumeroPosicaoErroPrograma());
            billetRegistryResponseRepository.save(registryResponse);

            BaseResultCode resultCode = resultCodeTranslator.translateErrorToResultCode(resposta.getNomeProgramaErro(),
                resposta.getNumeroPosicaoErroPrograma(), resposta.getTextoMensagemErro(),
                registryRequest.getTextoNumeroTituloBeneficiario());

            return new RegisterBankBilletConnectorResponse(resultCode);
        }
    }

    private long buildNossoNumero(Integer numeroConvenio, Long seuNumero) {

        String numeroTituloCliente = numeroConvenio.toString();

        if (springContextHelper.isOnTestsProfile()) {
            Integer numeroTituloClientePrefix = numeroTituloClientePrefixRandom.nextInt(999);
            numeroTituloCliente += StringUtils.leftPad(numeroTituloClientePrefix.toString() + seuNumero.toString(), 10, "0");

        } else {
            numeroTituloCliente += StringUtils.leftPad(seuNumero.toString(), 10, "0");
        }

        return Long.valueOf(numeroTituloCliente);
    }

    private String formatNumeroTituloCliente(Long nossoNumero) {
        return StringUtils.leftPad(nossoNumero.toString(), 20, "0");
    }

    private BancoBrasilBilletRegistryRequest buildRegistryRequest(BancoBrasilCompanyBankAccount companyBankAccount,
        BancoBrasilLevyingAgreement levyingAgreement, BaseBankRemittance bankRemittance, BasePaymentBankBillet bankBillet,
        long nossoNumero, long seuNumero, LocalDate issueDate) {

        BancoBrasilDocumentType payerDocumentType = documentTypeRepository
            .getByBaseDocumentType(bankBillet.getPayerDocumentType());

        BancoBrasilBilletRegistryRequest registryRequest = new BancoBrasilBilletRegistryRequest();
        registryRequest.setBankBillet(bankBillet);
        registryRequest.setBaseBankRemittance(bankRemittance);
        registryRequest.setRequestDate(DateTimeHelper.getNowDateTime());

        registryRequest.setNumeroConvenio(levyingAgreement.getNumeroConvenio());
        registryRequest.setNumeroCarteira(levyingAgreement.getNumeroCarteira());
        registryRequest.setNumeroVariacaoCarteira(levyingAgreement.getVariacaoCarteira());

        registryRequest.setDataEmissaoTitulo(issueDate);
        registryRequest.setDataVencimentoTitulo(bankBillet.getExpirationDate());
        registryRequest.setValorOriginalTitulo(bankBillet.getAmount());
        registryRequest.setCodigoTipoTitulo(companyBankAccount.getEspecieTituloWs());
        registryRequest.setTextoNumeroTituloBeneficiario(seuNumero); // Seu Numero
        registryRequest.setTextoNumeroTituloCliente(formatNumeroTituloCliente(nossoNumero)); // NossoNumero
        registryRequest.setCodigoTipoInscricaoPagador(payerDocumentType.getCodigoDeInscricao());
        registryRequest.setNumeroInscricaoPagador(Long.valueOf(bankBillet.getPayerDocumentNumber()));

        // Multa e Juros
        if (bankBillet.isExpiredPayment()) {
            registryRequest.setCodigoTipoMulta(2); // Multa em %
            registryRequest.setPercentualMultaTitulo(bankBillet.getPenaltyPercent());
            registryRequest.setDataMultaTitulo(bankBillet.getPenaltyStartDate());

            registryRequest.setCodigoTipoJuroMora(2); // Juros Mensais
            registryRequest.setPercentualJuroMoraTitulo(bankBillet.getInterestPercent());

        } else {
            registryRequest.setCodigoTipoMulta(0); // Sem multa
            registryRequest.setCodigoTipoJuroMora(3); // Isento de Juros
        }

        return registryRequest;
    }

    private Requisicao buildRequisicao(BancoBrasilCompanyBankAccount companyBankAccount, BasePaymentBankBillet bankBillet,
        BancoBrasilBilletRegistryRequest billetRegistryRequest) {

        Requisicao requisicao = new Requisicao();

        // Valores fixos
        requisicao.setCodigoModalidadeTitulo((short) 1); // Carteira Simples
        requisicao.setCodigoTipoDesconto((short) 0); // Sem Desconto
        requisicao.setIndicadorPermissaoRecebimentoParcial("N"); // Sem Recebimento Parcial
        requisicao.setCodigoTipoCanalSolicitacao((short) 5); // WebServices

        // Convênio
        requisicao.setNumeroConvenio(billetRegistryRequest.getNumeroConvenio());
        requisicao.setNumeroCarteira(billetRegistryRequest.getNumeroCarteira().shortValue());
        requisicao.setNumeroVariacaoCarteira(billetRegistryRequest.getNumeroVariacaoCarteira().shortValue());
        requisicao.setCodigoChaveUsuario(companyBankAccount.getChaveUsuarioJ());

        // Boleto
        requisicao.setDataEmissaoTitulo(DateTimeHelper.format(billetRegistryRequest.getDataEmissaoTitulo(), SOAP_DATE_FORMAT));
        requisicao.setDataVencimentoTitulo(DateTimeHelper.format(billetRegistryRequest.getDataVencimentoTitulo(),
            SOAP_DATE_FORMAT));
        requisicao.setValorOriginalTitulo(NumberHelper.fromIntegerToBigDecimal(billetRegistryRequest.getValorOriginalTitulo()));
        requisicao.setCodigoTipoJuroMora(billetRegistryRequest.getCodigoTipoJuroMora().shortValue());
        requisicao.setCodigoTipoMulta(billetRegistryRequest.getCodigoTipoMulta().shortValue());
        requisicao.setCodigoTipoTitulo(billetRegistryRequest.getCodigoTipoTitulo().shortValue());
        requisicao.setTextoNumeroTituloBeneficiario(billetRegistryRequest.getTextoNumeroTituloBeneficiario().toString());
        requisicao.setTextoNumeroTituloCliente(billetRegistryRequest.getTextoNumeroTituloCliente());
        requisicao.setCodigoAceiteTitulo((companyBankAccount.getBaseCompanyBankAccount().isAcceptance() ? "S" : "N"));

        // Multa
        requisicao.setCodigoTipoMulta(billetRegistryRequest.getCodigoTipoMulta().shortValue());
        requisicao.setPercentualMultaTitulo(NumberHelper.fromIntegerToBigDecimal(
            billetRegistryRequest.getPercentualMultaTitulo()));
        requisicao.setValorMultaTitulo(new BigDecimal(0));
        requisicao.setDataMultaTitulo(DateTimeHelper.format(billetRegistryRequest.getDataMultaTitulo(), SOAP_DATE_FORMAT));
        // Juros
        requisicao.setCodigoTipoJuroMora(billetRegistryRequest.getCodigoTipoJuroMora().shortValue());
        requisicao.setPercentualJuroMoraTitulo(NumberHelper.fromIntegerToBigDecimal(
            billetRegistryRequest.getPercentualJuroMoraTitulo()));
        requisicao.setValorJuroMoraTitulo(new BigDecimal(0));

        // Pagador
        requisicao.setCodigoTipoInscricaoPagador(billetRegistryRequest.getCodigoTipoInscricaoPagador().shortValue());
        requisicao.setNumeroInscricaoPagador(billetRegistryRequest.getNumeroInscricaoPagador());
        requisicao.setNomePagador(normalize(bankBillet.getPayerName(), 60));
        requisicao.setTextoEnderecoPagador(normalize(bankBillet.getPayerAddressFullStreet(), 60));
        requisicao.setNumeroCepPagador(Integer.valueOf(bankBillet.getPayerAddressZipCode()));
        requisicao.setNomeMunicipioPagador(normalize(bankBillet.getPayerAddressCity(), 20));
        requisicao.setNomeBairroPagador(normalize(bankBillet.getPayerAddressDistrict(), 20));
        requisicao.setSiglaUfPagador(bankBillet.getPayerAddressState());

        return requisicao;
    }

}
