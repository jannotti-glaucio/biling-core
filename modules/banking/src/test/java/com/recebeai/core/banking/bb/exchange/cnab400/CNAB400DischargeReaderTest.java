package tech.jannotti.billing.core.banking.bb.exchange.cnab400;

import java.io.InputStream;
import java.io.InputStreamReader;

import org.beanio.BeanReader;
import org.beanio.StreamFactory;
import org.junit.Test;

import tech.jannotti.billing.core.banking.bb.BancoBrasilConstants;
import tech.jannotti.billing.core.banking.bb.exchange.records.cnab400.retorno.DetalheRetorno;
import tech.jannotti.billing.core.banking.bb.exchange.records.cnab400.retorno.HeaderRetorno;
import tech.jannotti.billing.core.banking.bb.exchange.records.cnab400.retorno.TraillerRetorno;
import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.commons.beanio.BeanIOHelper;

public class CNAB400DischargeReaderTest {

    @Test
    public void test() throws Exception {

        String fileName = "BBCOB_3108201801.RET";
        String filePath = "/banking/bb/cnab400/retorno/" + fileName;

        System.out.println("Lendo arquivo CNAB: " + filePath);
        readCNAB(filePath);
    }

    public void readCNAB(String filePath) throws Exception {

        StreamFactory streamFactory = BeanIOHelper.buildStreamFactory(BancoBrasilConstants.CNAB_400_MAPPING_PATH);

        InputStream fileInputStream = this.getClass().getResourceAsStream(filePath);
        if (fileInputStream == null) {
            System.out.println("Arquivo CNAB [" + filePath + "] nao existe");
            return;
        }

        InputStreamReader fileReader = new InputStreamReader(fileInputStream);

        BeanReader beanReader = streamFactory.createReader("cnab400-retorno", fileReader);
        try {
            Object line = null;
            while ((line = beanReader.read()) != null) {

                if (line instanceof HeaderRetorno) {
                    HeaderRetorno registro = (HeaderRetorno) line;
                    System.out.println("Header Retorno: " + ToStringHelper.toString(registro));
                    continue;
                }

                else if (line instanceof DetalheRetorno) {
                    DetalheRetorno registro = (DetalheRetorno) line;
                    System.out.println("Registro Detalhe Retorno: " + ToStringHelper.toString(registro));
                    continue;
                }

                else if (line instanceof TraillerRetorno) {
                    TraillerRetorno registro = (TraillerRetorno) line;
                    System.out.println("Registro Trailer Retorno: " + ToStringHelper.toString(registro));
                    continue;
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
            throw e;
        }
        beanReader.close();
    }

}
