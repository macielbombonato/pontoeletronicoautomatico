package org.mtec.pontoeletronico.main;

import org.apache.log4j.Logger;
import org.mtec.pontoeletronico.domain.service.PontoEletronicoServiceImpl;
import org.mtec.pontoeletronico.domain.service.interfaces.PontoEletronicoService;

/**
 * @author Maciel Escudero Bombonato
 */
public final class Main {

	private static final Logger log = Logger.getLogger(Main.class);
	
	/**
     * Default constructor.
     */
    public Main() {
    }

    /**
     * @param args the command line arguments
     */
    public static void main(final String[] args) {
    	log.info("Iniciando aplicacao Ponto Eletronico.");
    	
    	if (args == null
    	|| args.length == 0
    	|| args[0].equalsIgnoreCase("marcarPonto")) {
            PontoEletronicoService service = new PontoEletronicoServiceImpl();
            service.marcarPonto(true);	
    	} else if (args != null
    	&& args.length == 1
    	&& args[0].equalsIgnoreCase("gerarRelatorio")) {
    		// TODO implementar
    		System.out.println("TODO: Gerar relatorio.");
    	} else if (args != null
    	&& args.length == 2
    	&& args[0].equalsIgnoreCase("gerarRelatorio")) {
    		// TODO implementar
    		System.out.println("TODO: Gerar relatorio mes referencia: " + args[1]);
    	} else {
    		log.info("Aplicacao nao foi inicializada corretamente.\n" +
    				"Utilize: \n" +
    				"marcarPonto --> Inicia o servico de marcacao de ponto. \n" +
    				"gerarRelatorio --> Gera relatorio excel com todos os dados contidos na base do sistema. \n" +
    				"gerarRelatorio [mes de referencia] --> Gera o relatorio de um mes especifico, por exemplo, use gerarRelatorio jun/2009.");
    		System.exit(0);
    	}
    	
    	log.info("Fim da aplicacao Ponto Eletronico.");
    }

}