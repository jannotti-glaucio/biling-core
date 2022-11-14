package tech.jannotti.billing.core.banking.stella.gerador;

import br.com.caelum.stella.DigitoPara;
import br.com.caelum.stella.boleto.bancos.gerador.GeradorDeDigitoPadrao;

public class GeradorDeDigitoBradesco extends GeradorDeDigitoPadrao {

    private static final long serialVersionUID = 1L;

    @Override
    public int geraDigitoMod11(String codigoDeBarras) {
        return Integer.valueOf(new DigitoPara(codigoDeBarras)
            .comMultiplicadoresDeAte(2, 7)
            .complementarAoModulo()
            .trocandoPorSeEncontrar("0", 11)
            .mod(11)
            .calcula());
    }

}
