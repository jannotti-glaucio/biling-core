package tech.jannotti.billing.core.banking.bb.exchange.cnab400;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.beanio.BeanReader;
import org.beanio.InvalidRecordException;
import org.beanio.StreamFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import tech.jannotti.billing.core.banking.bb.BancoBrasilConstants;
import tech.jannotti.billing.core.banking.bb.exchange.records.cnab400.RecordToModelConverter;
import tech.jannotti.billing.core.banking.bb.exchange.records.cnab400.retorno.DetalheRetorno;
import tech.jannotti.billing.core.banking.bb.exchange.records.cnab400.retorno.HeaderRetorno;
import tech.jannotti.billing.core.banking.bb.exchange.records.cnab400.retorno.TraillerRetorno;
import tech.jannotti.billing.core.banking.bb.persistence.model.BancoBrasilCNAB400DischargeDetail;
import tech.jannotti.billing.core.banking.bb.persistence.model.BancoBrasilCNAB400DischargeFile;
import tech.jannotti.billing.core.banking.bb.persistence.model.BancoBrasilCompanyBankAccount;
import tech.jannotti.billing.core.banking.bb.persistence.repository.CompanyBankAccountRepository;
import tech.jannotti.billing.core.banking.exchange.AbstractBankDischargeLoader;
import tech.jannotti.billing.core.commons.beanio.BeanIOHelper;
import tech.jannotti.billing.core.commons.log.LogFactory;
import tech.jannotti.billing.core.commons.log.LogManager;
import tech.jannotti.billing.core.commons.util.DateTimeHelper;
import tech.jannotti.billing.core.connector.banking.exception.BankingExchangeException;
import tech.jannotti.billing.core.persistence.model.base.bank.BaseBank;
import tech.jannotti.billing.core.persistence.model.base.company.BaseCompanyBankAccount;

@Component("banking.bb.cnab400DischargeLoader")
public class CNAB400DischargeLoader extends AbstractBankDischargeLoader {

    private static final LogManager logManager = LogFactory.getManager(CNAB400DischargeLoader.class);

    private static final String IDENTIFICACAO_TIPO_DE_OPERACAO_RETORNO = "RETORNO";
    private static final int TIPO_DE_OPERACAO_RETORNO = 2;
    private static final int IDENTIFICACAO_TIPO_SERVICO = 1;
    private static final String IDENTIFICACAO_EXTENSO_TIPO_SERVICO = "COBRANCA";

    @Autowired
    private CompanyBankAccountRepository companyBankAccountRepository;

    @Autowired
    private CNAB400ExchangeService cnabExchangeService;

    private StreamFactory streamFactory;

    @PostConstruct
    public void init() {
        streamFactory = BeanIOHelper.buildStreamFactory(BancoBrasilConstants.CNAB_400_MAPPING_PATH);
    }

    public void execute() {

        // TODO Ver como jogar parte desse metodo para a classe abstrata

        List<BancoBrasilCompanyBankAccount> companyBankAccounts = companyBankAccountRepository.findAll();
        for (BancoBrasilCompanyBankAccount companyBankAccount : companyBankAccounts) {
            logManager.logINFO("Processando Conta [id=" + companyBankAccount.getId() + "]");

            File sourceDir = new File(companyBankAccount.getDischargeFilesSourceDir());
            if (!sourceDir.exists())
                sourceDir.mkdirs();

            File processedDir = new File(companyBankAccount.getDischargeFilesProcessedDir());
            File rejectedDir = new File(companyBankAccount.getDischargeFilesRejectedDir());

            Collection<File> files = FileUtils.listFiles(sourceDir, new String[] { "ret", "RET" }, false);
            if (CollectionUtils.isEmpty(files)) {
                logManager.logDEBUG("Nao ha arquivos para processar");
                return;
            }

            for (File file : files) {
                logManager.logINFO("Processando arquivo [" + file.getPath() + "]");

                if (file.isFile()) {

                    BancoBrasilCNAB400DischargeFile dischargeFile = null;
                    try {
                        dischargeFile = parseFile(file, companyBankAccount);
                    } catch (BankingExchangeException e) {
                        logManager.logERROR("Erro lendo conteudo do arquivo", e);
                    }

                    if (dischargeFile == null) {
                        moveRejectedDischargeFile(file, rejectedDir);
                        continue;
                    }

                    moveProcessedDischargeFile(file, processedDir);

                    cnabExchangeService.saveDischargeFile(dischargeFile);
                }
            }
        }
    }

    private BancoBrasilCNAB400DischargeFile parseFile(File file, BancoBrasilCompanyBankAccount companyBankAccount) {

        LocalDateTime receiveDate = DateTimeHelper.getNowDateTime();
        BancoBrasilCNAB400DischargeFile dischargeFile = new BancoBrasilCNAB400DischargeFile(companyBankAccount, file.getName(),
            receiveDate);

        List<BancoBrasilCNAB400DischargeDetail> dischargeDetails = new ArrayList<BancoBrasilCNAB400DischargeDetail>();
        BeanReader beanReader = streamFactory.createReader("cnab400-retorno", file);
        try {
            Object line = null;
            while ((line = beanReader.read()) != null) {

                if (line instanceof HeaderRetorno) {
                    HeaderRetorno header = (HeaderRetorno) line;

                    if (!validateHeader(header, companyBankAccount))
                        return null;

                    RecordToModelConverter.loadHeaderRetornoToDischargeFile(header, dischargeFile);
                    continue;

                } else if (line instanceof DetalheRetorno) {
                    DetalheRetorno detalheRetorno = (DetalheRetorno) line;
                    BancoBrasilCNAB400DischargeDetail dischargeDetail = RecordToModelConverter
                        .convertDetalheRetornoToDischargeDetail(detalheRetorno);
                    dischargeDetails.add(dischargeDetail);
                    continue;

                } else if (line instanceof TraillerRetorno) {
                    TraillerRetorno trailler = (TraillerRetorno) line;
                    RecordToModelConverter.loadTraillerRetornoToDischargeFile(trailler, dischargeFile);
                    continue;
                }
            }
        } catch (InvalidRecordException e) {
            throw new BankingExchangeException("Erro lendo registro do arquivo", e);
        }
        beanReader.close();

        dischargeFile.setDetails(dischargeDetails);
        return dischargeFile;
    }

    private boolean validateHeader(HeaderRetorno header, BancoBrasilCompanyBankAccount companyBankAccount) {

        BaseCompanyBankAccount baseCompanyBankAccount = bankingService
            .getCompanyBankAccount(companyBankAccount.getBaseCompanyBankAccount().getId());

        if (!header.prefixoAgencia.equals(baseCompanyBankAccount.getAgencyNumber())) {
            logManager.logWARN(
                "Ignorando arquivo com prefixoAgencia invalido [" + header.prefixoAgencia + "]");
            return false;
        }

        if (!header.dvPrefixoAgencia.equals(baseCompanyBankAccount.getAgencyCheckDigit().toString())) {
            logManager.logWARN(
                "Ignorando arquivo com dvPrefixoAgencia invalido [" + header.dvPrefixoAgencia + "]");
            return false;
        }

        header.numeroContaCorrente = header.numeroContaCorrente.replaceFirst("0+", "");

        if (!header.numeroContaCorrente.equals(baseCompanyBankAccount.getAccountNumber())) {
            logManager.logWARN(
                "Ignorando arquivo com numeroContaCorrente invalido [" + header.numeroContaCorrente + "]");
            return false;
        }

        if (!header.dvNumeroContaCorrenteCedente.equals(baseCompanyBankAccount.getAccountCheckDigit().toString())) {
            logManager.logWARN(
                "Ignorando arquivo com dvNumeroContaCorrenteCedente invalido [" + header.dvNumeroContaCorrenteCedente
                    + "]");
            return false;
        }

        if (!header.tipoOperacao.equals(TIPO_DE_OPERACAO_RETORNO)) {
            logManager.logWARN(
                "Ignorando arquivo com tipoOperacao invalido [" + header.tipoOperacao + "]");
            return false;
        }

        if (!header.identificacaoTipoOperacao.equals(IDENTIFICACAO_TIPO_DE_OPERACAO_RETORNO)) {
            logManager.logWARN(
                "Ignorando arquivo com identificacaoTipoOperacao invalido [" + header.identificacaoTipoOperacao + "]");
            return false;
        }

        if (!header.identificacaoTipoServico.equals(IDENTIFICACAO_TIPO_SERVICO)) {
            logManager.logWARN(
                "Ignorando arquivo com identificacaoTipoServico invalido [" + header.identificacaoTipoServico + "]");
            return false;
        }

        if (!header.identificacaoExtensoTipoServico.equals(IDENTIFICACAO_EXTENSO_TIPO_SERVICO)) {
            logManager.logWARN("Ignorando arquivo com identificacaoExtensoTipoServico invalido ["
                + header.identificacaoExtensoTipoServico + "]");
            return false;
        }

        String bankNumber = header.codigoNomeBanco.substring(0, 3);
        BaseBank baseBank = bankingService.getBank(bankNumber);
        if (baseBank == null) {
            logManager.logWARN("Ignorando arquivo com numero de banco invalido [" + bankNumber + "]");
            return false;
        }

        return true;
    }

}
