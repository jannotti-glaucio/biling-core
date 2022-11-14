package tech.jannotti.billing.core.banking.santander.exchange.cnab400;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.beanio.BeanWriter;
import org.beanio.StreamFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import tech.jannotti.billing.core.banking.exchange.AbstractBankRemittanceProcessor;
import tech.jannotti.billing.core.banking.santander.SantanderConstants;
import tech.jannotti.billing.core.banking.santander.exchange.records.cnab400.remessa.DetalheRemessa;
import tech.jannotti.billing.core.banking.santander.exchange.records.cnab400.remessa.HeaderRemessa;
import tech.jannotti.billing.core.banking.santander.exchange.records.cnab400.remessa.TraillerRemessa;
import tech.jannotti.billing.core.banking.santander.persistence.model.SantanderBankChannel;
import tech.jannotti.billing.core.banking.santander.persistence.model.SantanderCNAB400RemittanceDetail;
import tech.jannotti.billing.core.banking.santander.persistence.model.SantanderCNAB400RemittanceFile;
import tech.jannotti.billing.core.banking.santander.persistence.model.SantanderCompanyBankAccount;
import tech.jannotti.billing.core.banking.santander.persistence.model.SantanderDocumentType;
import tech.jannotti.billing.core.banking.santander.persistence.repository.BankChannelRepository;
import tech.jannotti.billing.core.banking.santander.persistence.repository.CNAB400RemittanceFileRepository;
import tech.jannotti.billing.core.banking.santander.persistence.repository.CompanyBankAccountRepository;
import tech.jannotti.billing.core.banking.santander.persistence.repository.DocumentTypeRepository;
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

@Component("banking.santander.cnab400RemittanceProcessor")
public class CNAB400RemittanceProcessor extends AbstractBankRemittanceProcessor {

    private static final LogManager logManager = LogFactory.getManager(CNAB400RemittanceProcessor.class);

    private static final Integer CODIGO_DE_OCORRENCIA_BAIXA_DE_TITULO = 2;

    @Autowired
    private CompanyBankAccountRepository companyBankAccountRepository;

    @Autowired
    private BankChannelRepository bankChannelRepository;

    @Autowired
    private DocumentTypeRepository documentTypeRepository;

    @Autowired
    private CNAB400RemittanceFileRepository remittanceFileRepository;

    @Autowired
    private CNAB400ExchangeService cnabExchangeService;

    @Override
    protected String getBankNumber() {
        return SantanderConstants.BANK_NUMBER;
    }

    @Override
    protected void processRemittances(BaseCompanyBankAccount baseCompanyBankAccount, List<BaseBankRemittance> baseRemittances) {

        SantanderCNAB400RemittanceFile remittanceFile = loadRemittanceFile(baseCompanyBankAccount, baseRemittances);

        SantanderBankChannel bankChannel = bankChannelRepository.getByBaseBankChannel(baseCompanyBankAccount.getBankChannel());
        processRemittanceFile(remittanceFile, bankChannel);
    }

    private SantanderCNAB400RemittanceFile loadRemittanceFile(BaseCompanyBankAccount baseCompanyBankAccount,
        List<BaseBankRemittance> baseRemittances) {

        SantanderCompanyBankAccount companyBankAccount = companyBankAccountRepository
            .getByBaseCompanyBankAccount(baseCompanyBankAccount);

        LocalDateTime creationDate = DateTimeHelper.getNowDateTime();

        SantanderDocumentType beneficiaryDocumentType = documentTypeRepository
            .getByBaseDocumentType(baseCompanyBankAccount.getBeneficiaryDocumentType());

        SantanderCNAB400RemittanceFile remittanceFile = new SantanderCNAB400RemittanceFile(companyBankAccount, creationDate);

        remittanceFile.setTipoDeInscricaoDoBeneficiario(beneficiaryDocumentType.getTipoDeDocumento());
        remittanceFile.setNumeroDeInscricaoDoBeneficiario(baseCompanyBankAccount.getBeneficiaryDocumentNumber());
        remittanceFile.setCompanyBankAccount(companyBankAccount);
        remittanceFile.setCreationDate(creationDate);
        remittanceFile.setNomeDoBeneficiario(normalize(baseCompanyBankAccount.getBeneficiaryName(), 30));
        remittanceFile.setCodigoDeTransmissao(companyBankAccount.getCodigoTransmissaoCnab());
        remittanceFile.setDataDeGravacao(creationDate.toLocalDate());

        List<SantanderCNAB400RemittanceDetail> remittanceDetails = new ArrayList<SantanderCNAB400RemittanceDetail>();

        for (BaseBankRemittance baseRemittance : baseRemittances) {

            if (baseRemittance.getPayment().getPaymentMethod().equals(PaymentMethodEnum.BANK_BILLET)) {
                BasePaymentBankBillet bankBillet = baseRemittance.getBankBillet();
                logManager.logDEBUG("Processando remessa de boleto [baseBankRemittanceId=%s]", baseRemittance.getId());

                SantanderDocumentType payerDocumentType = documentTypeRepository
                    .getByBaseDocumentType(bankBillet.getPayerDocumentType());

                // Pega o prefixo do CEP
                String cep = null;
                if (bankBillet.getPayerAddressZipCode().length() >= 5)
                    cep = bankBillet.getPayerAddressZipCode().substring(0, 5);
                else
                    cep = bankBillet.getPayerAddressZipCode();

                // Pega o complemento do CEP
                String complementoDoCep = null;
                if (bankBillet.getPayerAddressZipCode().length() > 5)
                    complementoDoCep = bankBillet.getPayerAddressZipCode().substring(5);

                SantanderCNAB400RemittanceDetail remittanceDetail = new SantanderCNAB400RemittanceDetail(
                    baseRemittance);

                remittanceDetail.setCreationDate(DateTimeHelper.getNowDateTime());
                remittanceDetail.setNossoNumero(bankBillet.getOurNumber());
                remittanceDetail.setCodigoDaCarteira(companyBankAccount.getCodigoCarteiraCnab());
                remittanceDetail.setSeuNumero(bankBillet.getYourNumber());
                remittanceDetail.setDataDeVencimento(bankBillet.getExpirationDate());
                remittanceDetail.setValorDoTitulo(bankBillet.getAmount());
                remittanceDetail.setEspecieDeDocumento(companyBankAccount.getEspecieTituloCnab());
                remittanceDetail.setDataDeEmissao(bankBillet.getIssueDate());
                remittanceDetail.setTipoDeInscricaoDoPagador(payerDocumentType.getTipoDeDocumento());
                remittanceDetail.setNumeroDeInscricaoDoPagador(bankBillet.getPayerDocumentNumber());
                remittanceDetail.setNomeDoPagador(normalize(bankBillet.getPayerName(), 40));
                remittanceDetail.setEnderecoDoPagador(normalize(bankBillet.getPayerAddressFullStreet(), 40));
                remittanceDetail.setBairroDoPagador(normalize(bankBillet.getPayerAddressDistrict(), 12));
                remittanceDetail.setCepDoPagador(cep);
                remittanceDetail.setComplementoDoCepDoPagador(complementoDoCep);
                remittanceDetail.setMunicipioDoPagador(normalize(bankBillet.getPayerAddressCity(), 15));
                remittanceDetail.setEstadoDoPagador(bankBillet.getPayerAddressState());

                if (baseRemittance.getOperation().equals(BankRemittanceOperationEnum.CANCELLATION))
                    remittanceDetail.setCodigoDaOcorrencia(CODIGO_DE_OCORRENCIA_BAIXA_DE_TITULO);
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

    private void processRemittanceFile(SantanderCNAB400RemittanceFile remittanceFile, SantanderBankChannel bankChannel) {

        long remittanceFileId = remittanceFileRepository.getNextRemittanceFileId();

        remittanceFile.setId(remittanceFileId);
        remittanceFile.setCreationDate(DateTimeHelper.getNowDateTime());

        File remittanceFilesDir = new File(remittanceFile.getCompanyBankAccount().getRemittanceFilesDir());
        if (!remittanceFilesDir.exists())
            remittanceFilesDir.mkdirs();

        // Define o nome do arquivo
        String fileName = "COB" + StringUtils.leftPad(String.valueOf(remittanceFileId), 9, "0") + ".REM";
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

    private void writeRemittanceFile(String remittanceFilePath, SantanderCNAB400RemittanceFile remittanceFile,
        SantanderBankChannel bankChannel) {

        logManager.logINFO("Gravando arquivo CNAB [%s]", remittanceFilePath);

        StreamFactory streamFactory = BeanIOHelper.buildStreamFactory(SantanderConstants.CNAB_400_MAPPING_PATH);
        File cnabFile = new File(remittanceFilePath);
        BeanWriter beanWriter = streamFactory.createWriter("cnab400-remessa", cnabFile);

        SantanderCompanyBankAccount companyBankAccount = remittanceFile.getCompanyBankAccount();
        BaseCompanyBankAccount baseCompanyBankAccount = companyBankAccount.getBaseCompanyBankAccount();

        int lineNumber = 1;

        HeaderRemessa header = new HeaderRemessa();
        header.codigoDeTransmissao = remittanceFile.getCodigoDeTransmissao();
        header.nomeDoBeneficiario = remittanceFile.getNomeDoBeneficiario();
        header.dataDeGravacao = DateTimeHelper.toDate(remittanceFile.getDataDeGravacao());
        header.numeroSequencial = lineNumber++;
        beanWriter.write("header", header);

        for (SantanderCNAB400RemittanceDetail remittanceDetail : remittanceFile.getDetails()) {

            DetalheRemessa detalhe = new DetalheRemessa();
            detalhe.tipoDeInscricaoDoBeneficiario = remittanceFile.getTipoDeInscricaoDoBeneficiario();
            detalhe.numeroDeInscricaoDoBeneficiario = remittanceFile.getNumeroDeInscricaoDoBeneficiario();
            detalhe.codigoDeTransmissao = remittanceFile.getCodigoDeTransmissao();
            detalhe.nossoNumero = remittanceDetail.getNossoNumero();
            detalhe.codigoDaCarteira = remittanceDetail.getCodigoDaCarteira();
            detalhe.codigoDeOcorrencia = remittanceDetail.getCodigoDaOcorrencia();
            detalhe.seuNumero = remittanceDetail.getSeuNumero();
            detalhe.dataDeVencimento = DateTimeHelper.toDate(remittanceDetail.getDataDeVencimento());
            detalhe.valorDoTitulo = remittanceDetail.getValorDoTitulo();
            detalhe.agenciaCobradora = baseCompanyBankAccount.getAgencyNumber();
            detalhe.especieDeDocumento = remittanceDetail.getEspecieDeDocumento();
            detalhe.aceite = (baseCompanyBankAccount.isAcceptance() ? "S" : "N");
            detalhe.dataDeEmissao = DateTimeHelper.toDate(remittanceDetail.getDataDeEmissao());
            detalhe.tipoDeInscricaoDoPagador = remittanceDetail.getTipoDeInscricaoDoPagador();
            detalhe.numeroDeInscricaoDoPagador = remittanceDetail.getNumeroDeInscricaoDoPagador();
            detalhe.nomeDoPagador = remittanceDetail.getNomeDoPagador();
            detalhe.enderecoDoPagador = remittanceDetail.getEnderecoDoPagador();
            detalhe.bairroDoPagador = remittanceDetail.getBairroDoPagador();
            detalhe.cepDoPagador = remittanceDetail.getCepDoPagador();
            detalhe.complementoDoCepDoPagador = remittanceDetail.getComplementoDoCepDoPagador();
            detalhe.municipioDoPagador = remittanceDetail.getMunicipioDoPagador();
            detalhe.estadoDoPagador = remittanceDetail.getEstadoDoPagador();

            detalhe.identificadorDoComplementoConta = "I";
            detalhe.complementoConta = getComplementoConta(baseCompanyBankAccount);

            detalhe.numeroSequencial = lineNumber++;
            beanWriter.write("detalhe", detalhe);
        }

        TraillerRemessa trailler = new TraillerRemessa();
        trailler.qtdTotalDeLinhas = remittanceFile.getQtdTotalDeLinhas();
        trailler.valorTotalDosTitulos = remittanceFile.getValorTotalDosTitulos();
        trailler.numeroSequencial = lineNumber;
        beanWriter.write("trailler", trailler);

        beanWriter.flush();
        beanWriter.close();
    }

    private String getComplementoConta(BaseCompanyBankAccount baseCompanyBankAccount) {

        String accountNumber = baseCompanyBankAccount.getAccountNumber();
        String lastAccountNumber = accountNumber.substring(accountNumber.length() - 1, accountNumber.length());
        return lastAccountNumber + baseCompanyBankAccount.getAccountCheckDigit();
    }

}
