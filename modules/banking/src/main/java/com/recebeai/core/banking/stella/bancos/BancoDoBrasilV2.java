package tech.jannotti.billing.core.banking.stella.bancos;

import br.com.caelum.stella.boleto.Boleto;
import br.com.caelum.stella.boleto.bancos.BancoDoBrasil;

public class BancoDoBrasilV2 extends BancoDoBrasil {

    private static final long serialVersionUID = 1L;

    @Override
    public String getNossoNumeroECodigoDocumento(Boleto boleto) {
        return getNumeroConvenioFormatado(boleto.getBeneficiario()) + getNossoNumeroFormatado(boleto.getBeneficiario());
    }

}
