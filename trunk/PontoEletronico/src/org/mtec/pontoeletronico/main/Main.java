package org.mtec.pontoeletronico.main;

import org.mtec.pontoeletronico.domain.service.PontoEletronicoServiceImpl;
import org.mtec.pontoeletronico.domain.service.interfaces.PontoEletronicoService;

/**
 * @author Maciel Escudero Bombonato
 */
public final class Main {

    /**
     * Default constructor.
     */
    public Main() {
    }

    /**
     * @param args the command line arguments
     */
    public static void main(final String[] args) {
        PontoEletronicoService service = new PontoEletronicoServiceImpl();
        service.marcarPonto(true);
    }

}