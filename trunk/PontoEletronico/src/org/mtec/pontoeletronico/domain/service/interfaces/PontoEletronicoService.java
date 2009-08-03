package org.mtec.pontoeletronico.domain.service.interfaces;

import org.mtec.pontoeletronico.configuracao.domain.PontoEletronicoConfig;
import org.mtec.pontoeletronico.domain.PontoEletronico;


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
	 * @param pontoEletronico the pontoEletronico to set
	 */
	public void setPontoEletronico(PontoEletronico pontoEletronico);

	/**
	 * @param pontoEletronicoConfig the pontoEletronicoConfig to set
	 */
	public void setPontoEletronicoConfig(PontoEletronicoConfig pontoEletronicoConfig);
}