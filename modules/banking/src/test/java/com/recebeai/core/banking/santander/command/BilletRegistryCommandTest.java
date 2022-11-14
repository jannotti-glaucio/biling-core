package tech.jannotti.billing.core.banking.santander.command;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import tech.jannotti.billing.core.banking.santander.persistence.model.SantanderBilletRegistryResponse;

public class BilletRegistryCommandTest {

    private BilletRegistryCommand registryCommand = new BilletRegistryCommand();

    @Test
    public void parseDescricaoErro_singleError() {

        String descricaoErro = "00001 - Nosso número inválido / incompatível";

        SantanderBilletRegistryResponse billetRegistryResponse = new SantanderBilletRegistryResponse();
        registryCommand.parseDescricaoErro(descricaoErro, billetRegistryResponse);

        assertEquals("00001", billetRegistryResponse.getDescricaoErroCodigo());
        assertEquals("Nosso número inválido / incompatível", billetRegistryResponse.getDescricaoErroMensagem());
    }

    @Test
    public void parseDescricaoErro_singleError_withoutSpaces() {

        String descricaoErro = "00001-Nosso número inválido / incompatível";

        SantanderBilletRegistryResponse billetRegistryResponse = new SantanderBilletRegistryResponse();
        registryCommand.parseDescricaoErro(descricaoErro, billetRegistryResponse);

        assertEquals("00001", billetRegistryResponse.getDescricaoErroCodigo());
        assertEquals("Nosso número inválido / incompatível", billetRegistryResponse.getDescricaoErroMensagem());
    }

    @Test
    public void parseDescricaoErro_multipleErrors() {

        String descricaoErro = "00102 - Formato do campo TITULO.DT-VENCTO inválido"
            + "\r\n00101 - Formato do campo TITULO.ESPECIE inválido";

        SantanderBilletRegistryResponse billetRegistryResponse = new SantanderBilletRegistryResponse();
        registryCommand.parseDescricaoErro(descricaoErro, billetRegistryResponse);

        assertEquals("00102", billetRegistryResponse.getDescricaoErroCodigo());
        assertEquals("Formato do campo TITULO.DT-VENCTO inválido", billetRegistryResponse.getDescricaoErroMensagem());
    }

}
