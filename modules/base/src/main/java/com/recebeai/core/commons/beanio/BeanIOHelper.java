package tech.jannotti.billing.core.commons.beanio;

import java.io.IOException;
import java.io.InputStream;

import org.beanio.BeanIOConfigurationException;
import org.beanio.StreamFactory;

public class BeanIOHelper {

    public static StreamFactory buildStreamFactory(String filePath) {

        InputStream mappingInputStream = BeanIOHelper.class.getResourceAsStream(filePath);
        StreamFactory streamFactory = StreamFactory.newInstance();

        try {
            streamFactory.load(mappingInputStream);

        } catch (BeanIOConfigurationException e) {
            throw new BeanParserException("Erro no conteudo do arquivo de parser", e);
        } catch (IOException e) {
            throw new BeanParserException("Erro lendo arquivo de parser [" + filePath + "]", e);
        }

        return streamFactory;
    }

}
