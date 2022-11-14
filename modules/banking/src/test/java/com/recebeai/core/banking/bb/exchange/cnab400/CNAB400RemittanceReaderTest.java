package tech.jannotti.billing.core.banking.bb.exchange.cnab400;

import java.io.InputStream;
import java.io.InputStreamReader;

import org.beanio.BeanReader;
import org.beanio.StreamFactory;
import org.junit.Test;

import tech.jannotti.billing.core.banking.bb.BancoBrasilConstants;
import tech.jannotti.billing.core.banking.bb.exchange.records.cnab400.remessa.DetalheRemessa;
import tech.jannotti.billing.core.banking.bb.exchange.records.cnab400.remessa.HeaderRemessa;
import tech.jannotti.billing.core.banking.bb.exchange.records.cnab400.remessa.TraillerRemessa;
import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.commons.beanio.BeanIOHelper;

public class CNAB400RemittanceReaderTest {

    @Test
    public void test() throws Exception {

        String fileName = "REM000000678.TXT";
        String filePath = "/banking/bb/cnab400/remessa/" + fileName;

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

        BeanReader beanReader = streamFactory.createReader("cnab400-remessa", fileReader);
        try {
            Object line = null;
            while ((line = beanReader.read()) != null) {

                if (line instanceof HeaderRemessa) {
                    HeaderRemessa registro = (HeaderRemessa) line;
                    System.out.println("Header Remessa: " + ToStringHelper.toString(registro));
                    continue;
                }

                else if (line instanceof DetalheRemessa) {
                    DetalheRemessa registro = (DetalheRemessa) line;
                    System.out.println("Registro Detalhe Remessa: " + ToStringHelper.toString(registro));
                    continue;
                }

                else if (line instanceof TraillerRemessa) {
                    TraillerRemessa registro = (TraillerRemessa) line;
                    System.out.println("Registro Trailer Remessa: " + ToStringHelper.toString(registro));
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
