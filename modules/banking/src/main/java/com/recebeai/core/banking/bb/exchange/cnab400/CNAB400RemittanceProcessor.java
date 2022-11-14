package tech.jannotti.billing.core.banking.bb.exchange.cnab400;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.beanio.BeanWriter;
import org.beanio.StreamFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import tech.jannotti.billing.core.banking.bb.BancoBrasilConstants;
import tech.jannotti.billing.core.banking.bb.exchange.records.cnab400.remessa.DetalheRemessa;
import tech.jannotti.billing.core.banking.bb.exchange.records.cnab400.remessa.HeaderRemessa;
import tech.jannotti.billing.core.banking.bb.exchange.records.cnab400.remessa.TraillerRemessa;
import tech.jannotti.billing.core.banking.bb.persistence.model.BancoBrasilBankChannel;
import tech.jannotti.billing.core.banking.bb.persistence.model.BancoBrasilBilletRegistryRequest;
import tech.jannotti.billing.core.banking.bb.persistence.model.BancoBrasilCNAB400RemittanceDetail;
import tech.jannotti.billing.core.banking.bb.persistence.model.BancoBrasilCNAB400RemittanceFile;
import tech.jannotti.billing.core.banking.bb.persistence.model.BancoBrasilCompanyBankAccount;
import tech.jannotti.billing.core.banking.bb.persistence.model.BancoBrasilDocumentType;
import tech.jannotti.billing.core.banking.bb.persistence.repository.BankChannelRepository;
import tech.jannotti.billing.core.banking.bb.persistence.repository.BilletRegistryRequestRepository;
import tech.jannotti.billing.core.banking.bb.persistence.repository.CNAB400RemittanceFileRepository;
import tech.jannotti.billing.core.banking.bb.persistence.repository.CompanyBankAccountRepository;
import tech.jannotti.billing.core.banking.bb.persistence.repository.DocumentTypeRepository;
import tech.jannotti.billing.core.banking.exchange.AbstractBankRemittanceProcessor;
import tech.jannotti.billing.core.commons.beanio.BeanIOHelper;
import tech.jannotti.billing.core.commons.log.LogFactory;
import tech.jannotti.billing.core.commons.log.LogManager;
import tech.jannotti.billing.core.commons.util.DateTimeHelper;
import tech.jannotti.billing.core.connector.banking.exception.BankingExchangeException;
import tech.jannotti.billing.core.persistence.enums.BankRemittanceOperationEnum;
import tech.jannotti.billing.core.persistence.enums.BankRemittanceStatusEnum;
import tech.jannotti.billing.core.persistence.enums.PaymentMethodEnum;
import tech.jannotti.billing.core.persistence.model.base.bank.BaseBankRemittance;
import tech.jannotti.billing.core.persistence.model.base.company.BaseCompanyBankAccount;
import tech.jannotti.billing.core.persistence.model.base.payment.BasePaymentBankBillet;

@Component("banking.bb.cnab400RemittanceProcessor")
public class CNAB400RemittanceProcessor extends AbstractBankRemittanceProcessor {

    private static final LogManager logManager = LogFactory.getManager(CNAB400RemittanceProcessor.class);

    private static final Integer COMANDO_CANCELAMENTO = 2;

    @Autowired
    private CompanyBankAccountRepository companyBankAccountRepository;

    @Autowired
    private BankChannelRepository bankChannelRepository;

    @Autowired
    private DocumentTypeRepository documentTypeRepository;

    @Autowired
    private CNAB400RemittanceFileRepository remittanceFileRepository;

    @Autowired
    private BilletRegistryRequestRepository billetRegistryRequestRepository;

    @Autowired
    private CNAB400ExchangeService cnabExchangeService;

    @Override
    protected String getBankNumber() {
        return BancoBrasilConstants.BANK_NUMBER;
    }

    @Override
    protected void processRemittances(BaseCompanyBankAccount baseCompanyBankAccount, List<BaseBankRemittance> baseRemittances) {

        // TODO Melhorar a estrutura de metodos a chamar
        BancoBrasilCNAB400RemittanceFile remittanceFile = loadRemittanceFile(baseCompanyBankAccount, baseRemittances);

        BancoBrasilBankChannel bankChannel = bankChannelRepository.getByBaseBankChannel(baseCompanyBankAccount.getBankChannel());
        processRemittanceFile(remittanceFile, bankChannel);
    }

    private BancoBrasilCNAB400RemittanceFile loadRemittanceFile(BaseCompanyBankAccount baseCompanyBankAccount,
        List<BaseBankRemittance> baseRemittances) {

        BancoBrasilCompanyBankAccount companyBankAccount = companyBankAccountRepository
            .getByBaseCompanyBankAccount(baseCompanyBankAccount);

        LocalDateTime creationDate = DateTimeHelper.getNowDateTime();

        BancoBrasilDocumentType beneficiaryDocumentType = documentTypeRepository
            .getByBaseDocumentType(baseCompanyBankAccount.getBeneficiaryDocumentType());

        BancoBrasilCNAB400RemittanceFile remittanceFile = new BancoBrasilCNAB400RemittanceFile(companyBankAccount, creationDate);

        remittanceFile.setTipoInscricaoCedente(beneficiaryDocumentType.getCodigoDeInscricao());
        remittanceFile.setNumeroInscricaoCedente(baseCompanyBankAccount.getBeneficiaryDocumentNumber());
        remittanceFile.setCompanyBankAccount(companyBankAccount);
        remittanceFile.setCreationDate(creationDate);
        remittanceFile.setNomeCedente(normalize(baseCompanyBankAccount.getBeneficiaryName(), 30));

        List<BancoBrasilCNAB400RemittanceDetail> remittanceDetails = new ArrayList<BancoBrasilCNAB400RemittanceDetail>();

        for (BaseBankRemittance baseRemittance : baseRemittances) {

            if (baseRemittance.getPayment().getPaymentMethod().equals(PaymentMethodEnum.BANK_BILLET)) {
                BasePaymentBankBillet bankBillet = baseRemittance.getBankBillet();
                logManager.logDEBUG("Processando remessa de boleto [baseBankRemittanceId=%s]", baseRemittance.getId());

                BancoBrasilDocumentType payerDocumentType = documentTypeRepository
                    .getByBaseDocumentType(bankBillet.getPayerDocumentType());

                BancoBrasilBilletRegistryRequest billetRegistryRequest = billetRegistryRequestRepository
                    .getByBankBillet(baseRemittance.getBankBillet());
                if (billetRegistryRequest == null)
                    throw new BankingExchangeException(
                        "Remessa com boleto sem dados do registro [baseBankRemittanceId=%s, bankBilletId=%s]",
                        baseRemittance.getId(), baseRemittance.getBankBillet().getId());

                BancoBrasilCNAB400RemittanceDetail remittanceDetail = new BancoBrasilCNAB400RemittanceDetail(baseRemittance);
                remittanceDetail.setCreationDate(DateTimeHelper.getNowDateTime());
                remittanceDetail.setRemittanceFile(remittanceFile);
                remittanceDetail.setDataEmissaoTitulo(bankBillet.getIssueDate());
                remittanceDetail.setDataVencimentoTitulo(bankBillet.getExpirationDate());
                remittanceDetail.setNumeroConvenioCobrancaCedente(billetRegistryRequest.getNumeroConvenio());
                remittanceDetail.setNumeroCarteira(billetRegistryRequest.getNumeroCarteira());
                remittanceDetail.setVariacaoCarteira(billetRegistryRequest.getNumeroVariacaoCarteira());
                remittanceDetail.setValorTitulo(bankBillet.getAmount());
                remittanceDetail.setEspecieTitulo(companyBankAccount.getEspecieTituloCnab());
                remittanceDetail.setNomeSacado(normalize(bankBillet.getPayerName(), 37));
                remittanceDetail.setEnderecoSacado(normalize(bankBillet.getPayerAddressFullStreet(), 40));
                remittanceDetail.setBairroSacado(normalize(bankBillet.getPayerAddressDistrict(), 12));
                remittanceDetail.setCidadeSacado(normalize(bankBillet.getPayerAddressCity(), 15));
                remittanceDetail.setUfSacado(bankBillet.getPayerAddressState());
                remittanceDetail.setCepSacado(bankBillet.getPayerAddressZipCode());
                remittanceDetail.setNumeroInscricaoSacado(bankBillet.getPayerDocumentNumber());
                remittanceDetail.setTipoInscricaoSacado(payerDocumentType.getCodigoDeInscricao());
                remittanceDetail.setNumeroTituloCedente(bankBillet.getYourNumber());
                remittanceDetail.setNossoNumero(bankBillet.getOurNumber());

                if (baseRemittance.getOperation().equals(BankRemittanceOperationEnum.CANCELLATION))
                    remittanceDetail.setComando(COMANDO_CANCELAMENTO);
                else
                    throw new BankingExchangeException(
                        "Remessa com operacao bancaria nao suportada [baseBankRemittanceId=%s, operation=%s]",
                        baseRemittance.getId(), baseRemittance.getOperation());

                remittanceDetails.add(remittanceDetail);

            } else {
                logManager.logWARN(
                    "Igornando remessa com forma de pagamento nao suportada [baseBankRemittanceId=%s, paymentMethod=%s]",
                    baseRemittance.getId(), baseRemittance.getPayment().getPaymentMethod());
            }
        }
        remittanceFile.setDetails(remittanceDetails);

        return remittanceFile;
    }

    private void processRemittanceFile(BancoBrasilCNAB400RemittanceFile remittanceFile, BancoBrasilBankChannel bankChannel) {

        long remittanceFileId = remittanceFileRepository.getNextRemittanceFileId();

        remittanceFile.setId(remittanceFileId);
        remittanceFile.setNumeroSequencialRemessa(remittanceFileId);
        remittanceFile.setCreationDate(DateTimeHelper.getNowDateTime());

        File remittanceFilesDir = new File(remittanceFile.getCompanyBankAccount().getRemittanceFilesDir());
        if (!remittanceFilesDir.exists())
            remittanceFilesDir.mkdirs();

        // Define o nome do arquivo
        String fileName = "REM" + StringUtils.leftPad(String.valueOf(remittanceFileId), 9, "0") + ".TXT";
        remittanceFile.setFileName(fileName);

        String filePath = remittanceFilesDir.getPath() + File.separator + fileName;

        // Grava o arquivo de remessa
        writeRemittanceFile(filePath, remittanceFile, bankChannel);

        try {
            cnabExchangeService.saveRemittanceFileAndUpdateRemittancesStatus(remittanceFile, BankRemittanceStatusEnum.PROCESSED);
        } catch (Exception e) {
            // Exclui o arquivo de remessa
            discardRemittanceFile(filePath);
            throw e;
        }
    }

    private void writeRemittanceFile(String remittanceFilePath, BancoBrasilCNAB400RemittanceFile remittanceFile,
        BancoBrasilBankChannel bankChannel) {

        logManager.logINFO("Gravando arquivo CNAB [%s]", remittanceFilePath);

        StreamFactory streamFactory = BeanIOHelper.buildStreamFactory(BancoBrasilConstants.CNAB_400_MAPPING_PATH);
        File cnabFile = new File(remittanceFilePath);
        BeanWriter beanWriter = streamFactory.createWriter("cnab400-remessa", cnabFile);

        BancoBrasilCompanyBankAccount companyBankAccount = remittanceFile.getCompanyBankAccount();
        BaseCompanyBankAccount baseCompanyBankAccount = companyBankAccount.getBaseCompanyBankAccount();

        int lineNumber = 1;

        HeaderRemessa header = new HeaderRemessa();
        header.identificacaoExtensoTipoOperacao = bankChannel.getTipoOperacaoRemessa();
        header.prefixoAgencia = baseCompanyBankAccount.getAgencyNumber();
        header.dvPrefixoAgencia = baseCompanyBankAccount.getAgencyCheckDigit();
        header.numeroContaCorrenteCedente = baseCompanyBankAccount.getAccountNumber();
        header.dvNumeroContaCorrenteCedente = baseCompanyBankAccount.getAccountCheckDigit();
        header.nomeCedente = remittanceFile.getNomeCedente();
        header.dataGravacao = DateTimeHelper.toDate(remittanceFile.getCreationDate());
        header.sequenciaRemessa = remittanceFile.getNumeroSequencialRemessa();
        header.sequencialRegistro = lineNumber++;
        header.numeroConvenioLider = companyBankAccount.getNumeroConvenioLider();
        beanWriter.write("header", header);

        for (BancoBrasilCNAB400RemittanceDetail remittanceDetail : remittanceFile.getDetails()) {
            BancoBrasilDocumentType documentType = documentTypeRepository
                .getByBaseDocumentType(baseCompanyBankAccount.getBeneficiaryDocumentType());

            DetalheRemessa detalheRemessa = new DetalheRemessa();
            detalheRemessa.tipoInscricaoCedente = documentType.getCodigoDeInscricao();
            detalheRemessa.numeroCpfCnpjCedente = baseCompanyBankAccount.getBeneficiaryDocumentNumber();
            detalheRemessa.prefixoAgencia = baseCompanyBankAccount.getAgencyNumber();
            detalheRemessa.dvPrefixoAgencia = baseCompanyBankAccount.getAgencyCheckDigit();
            detalheRemessa.numeroContaCorrenteCedente = baseCompanyBankAccount.getAccountNumber();
            detalheRemessa.dvNumContaCorCedente = baseCompanyBankAccount.getAccountCheckDigit();
            detalheRemessa.numeroConvenioCobrancaCedente = remittanceDetail.getNumeroConvenioCobrancaCedente();
            detalheRemessa.dataVencimento = DateTimeHelper.toDate(remittanceDetail.getDataVencimentoTitulo());
            detalheRemessa.carteiraCobranca = remittanceDetail.getNumeroCarteira();
            detalheRemessa.variacaoCarteira = remittanceDetail.getVariacaoCarteira();
            detalheRemessa.numeroTituloAtribuidoCedente = remittanceDetail.getNumeroTituloCedente().toString();
            detalheRemessa.nossoNumero = remittanceDetail.getNossoNumero();
            detalheRemessa.valorTitulo = remittanceDetail.getValorTitulo();
            detalheRemessa.especieTitulo = remittanceDetail.getEspecieTitulo();
            detalheRemessa.aceiteTitulo = (baseCompanyBankAccount.isAcceptance() ? "S" : "N");
            detalheRemessa.dataEmissao = DateTimeHelper.toDate(remittanceDetail.getDataEmissaoTitulo());
            detalheRemessa.tipoInscricaoSacado = remittanceDetail.getTipoInscricaoSacado();
            detalheRemessa.numeroCnpjCpfSacado = remittanceDetail.getNumeroInscricaoSacado();
            detalheRemessa.nomeSacado = remittanceDetail.getNomeSacado();
            detalheRemessa.enderecoSacado = remittanceDetail.getEnderecoSacado();
            detalheRemessa.bairroSacado = remittanceDetail.getBairroSacado();
            detalheRemessa.cepSacado = remittanceDetail.getCepSacado();
            detalheRemessa.cidadeSacado = remittanceDetail.getCidadeSacado();
            detalheRemessa.ufSacado = remittanceDetail.getUfSacado();
            detalheRemessa.sequencialRegistro = lineNumber++;
            detalheRemessa.comando = remittanceDetail.getComando();

            beanWriter.write("detalhe", detalheRemessa);
        }

        TraillerRemessa trailer = new TraillerRemessa();
        trailer.sequencialRegistro = lineNumber++;
        beanWriter.write("trailler", trailer);

        beanWriter.flush();
        beanWriter.close();
    }

}
