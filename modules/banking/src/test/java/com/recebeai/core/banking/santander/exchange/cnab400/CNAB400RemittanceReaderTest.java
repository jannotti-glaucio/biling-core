package tech.jannotti.billing.core.banking.santander.exchange.cnab400;

import java.io.InputStream;
import java.io.InputStreamReader;

import org.beanio.BeanReader;
import org.beanio.StreamFactory;
import org.junit.Test;

import tech.jannotti.billing.core.banking.santander.SantanderConstants;
import tech.jannotti.billing.core.banking.santander.exchange.records.cnab400.remessa.DetalheRemessa;
import tech.jannotti.billing.core.banking.santander.exchange.records.cnab400.remessa.HeaderRemessa;
import tech.jannotti.billing.core.banking.santander.exchange.records.cnab400.remessa.TraillerRemessa;
import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.commons.beanio.BeanIOHelper;

public class CNAB400RemittanceReaderTest {

    @Test
    public void test() throws Exception {

        String fileName = "REMCNAB4007601522872940.TXT";
        String filePath = "/banking/santander/cnab400/remessa/" + fileName;

        System.out.println("Lendo arquivo CNAB: " + filePath);
        readCNAB(filePath);
    }

    public void readCNAB(String filePath) throws Exception {

        StreamFactory streamFactory = BeanIOHelper.buildStreamFactory(SantanderConstants.CNAB_400_MAPPING_PATH);

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
