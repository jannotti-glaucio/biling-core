package tech.jannotti.billing.core.banking.santander.exchange.cnab400;

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

import tech.jannotti.billing.core.banking.exchange.AbstractBankDischargeLoader;
import tech.jannotti.billing.core.banking.santander.SantanderConstants;
import tech.jannotti.billing.core.banking.santander.exchange.records.cnab400.RecordToModelConverter;
import tech.jannotti.billing.core.banking.santander.exchange.records.cnab400.retorno.DetalheRetorno;
import tech.jannotti.billing.core.banking.santander.exchange.records.cnab400.retorno.HeaderRetorno;
import tech.jannotti.billing.core.banking.santander.exchange.records.cnab400.retorno.TraillerRetorno;
import tech.jannotti.billing.core.banking.santander.persistence.model.SantanderCNAB400DischargeDetail;
import tech.jannotti.billing.core.banking.santander.persistence.model.SantanderCNAB400DischargeFile;
import tech.jannotti.billing.core.banking.santander.persistence.model.SantanderCompanyBankAccount;
import tech.jannotti.billing.core.banking.santander.persistence.repository.CompanyBankAccountRepository;
import tech.jannotti.billing.core.commons.beanio.BeanIOHelper;
import tech.jannotti.billing.core.commons.log.LogFactory;
import tech.jannotti.billing.core.commons.log.LogManager;
import tech.jannotti.billing.core.commons.util.DateTimeHelper;
import tech.jannotti.billing.core.connector.banking.exception.BankingExchangeException;
import tech.jannotti.billing.core.persistence.model.base.bank.BaseBank;
import tech.jannotti.billing.core.persistence.model.base.company.BaseCompanyBankAccount;

@Component("banking.santander.cnab400DischargeLoader")
public class CNAB400DischargeLoader extends AbstractBankDischargeLoader {

    private static final LogManager logManager = LogFactory.getManager(CNAB400DischargeLoader.class);

    private static final Integer CODIGO_DA_REMESSA_RETORNO = 2;
    private static final String LITERAL_DE_TRANSMISSAO_RETORNO = "RETORNO";
    private static final Integer CODIGO_DO_SERVICO_COBRANCA = 1;
    private static final String LITERAL_DE_SERVICO_COBRANCA = "COBRANCA";

    @Autowired
    private CompanyBankAccountRepository companyBankAccountRepository;

    @Autowired
    private CNAB400ExchangeService cnabExchangeService;

    private StreamFactory streamFactory;

    @PostConstruct
    public void init() {
        streamFactory = BeanIOHelper.buildStreamFactory(SantanderConstants.CNAB_400_MAPPING_PATH);
    }

    public void execute() {

        List<SantanderCompanyBankAccount> companyBankAccounts = companyBankAccountRepository.findAll();
        for (SantanderCompanyBankAccount companyBankAccount : companyBankAccounts) {
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

                SantanderCNAB400DischargeFile dischargeFile = null;
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

    private SantanderCNAB400DischargeFile parseFile(File file, SantanderCompanyBankAccount companyBankAccount) {

        LocalDateTime receiveDate = DateTimeHelper.getNowDateTime();
        SantanderCNAB400DischargeFile dischargeFile = new SantanderCNAB400DischargeFile(companyBankAccount, file.getName(),
            receiveDate);

        List<SantanderCNAB400DischargeDetail> dischargeDetails = new ArrayList<SantanderCNAB400DischargeDetail>();
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
                    SantanderCNAB400DischargeDetail dischargeDetail = RecordToModelConverter
                        .convertDetalheRetornoToDetail(detalheRetorno);
                    dischargeDetails.add(dischargeDetail);
                    continue;

                } else if (line instanceof TraillerRetorno) {
                    TraillerRetorno trailler = (TraillerRetorno) line;
                    RecordToModelConverter.loadTrailerRetornoToDischargeFile(trailler, dischargeFile);
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

    private boolean validateHeader(HeaderRetorno header, SantanderCompanyBankAccount companyBankAccount) {

        BaseCompanyBankAccount baseCompanyBankAccount = bankingService
            .getCompanyBankAccount(companyBankAccount.getBaseCompanyBankAccount().getId());

        if (!header.codigoDaRemessa.equals(CODIGO_DA_REMESSA_RETORNO)) {
            logManager.logWARN("Ignorando arquivo com codigoDaRemessa invalido [" + header.codigoDaRemessa
                + "]");
            return false;
        }

        if (!header.literalDeTransmissao.equals(LITERAL_DE_TRANSMISSAO_RETORNO)) {
            logManager.logWARN("Ignorando arquivo com literalDeTransmissao invalido [" + header.literalDeTransmissao
                + "]");
            return false;
        }

        if (!header.codigoDoServico.equals(CODIGO_DO_SERVICO_COBRANCA)) {
            logManager.logWARN("Ignorando arquivo com codigoDoServico invalido [" + header.codigoDoServico
                + "]");
            return false;
        }

        if (!header.literalDeServico.equals(LITERAL_DE_SERVICO_COBRANCA)) {
            logManager.logWARN("Ignorando arquivo com literalDeServico invalido [" + header.literalDeServico
                + "]");
            return false;
        }

        BaseBank baseBank = bankingService.getBank(header.codigoDoBanco);
        if (baseBank == null) {
            logManager.logWARN("Ignorando arquivo com numero de banco invalido [" + header.codigoDoBanco + "]");
            return false;
        }

        if (baseCompanyBankAccount == null) {
            logManager.logWARN("Ignorando arquivo com numero de agencia e conta invalida [agencia="
                + header.agenciaBeneficiario
                + ", conta=" + header.contaMovimentoBeneficiario + "]");
            return false;
        }

        return true;
    }

}
