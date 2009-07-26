package org.mtec.pontoeletronico.domain.service.interfaces;

import com.thoughtworks.xstream.XStream;

/**
 * Servico de criacao de ponto eletronico.
 * @author Maciel Escudero Bombonato
 */
public interface PontoEletronicoService {
	
	/**
	 * Metodo principal do servico para realizar marcacao de ponto.
	 * @param isDaemon
	 */
    public void marcarPonto(boolean isDaemon);
    
    /**
	 * Retorna um objeto XStream com as definições do XML do sistema de ponto eletrônico.
	 * @return XStream
	 */
	public XStream getPontoEletronicoSchema();

}