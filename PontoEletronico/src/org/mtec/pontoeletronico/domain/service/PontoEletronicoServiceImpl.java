package org.mtec.pontoeletronico.domain.service;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.log4j.Logger;
import org.mtec.pontoeletronico.configuracao.domain.PontoEletronicoConfig;
import org.mtec.pontoeletronico.domain.PontoEletronico;
import org.mtec.pontoeletronico.domain.service.interfaces.PontoEletronicoService;
import org.mtec.pontoeletronico.util.PontoEletronicoFileUtil;

/**
 * Servico de criacao de ponto eletronico.
 * @author Maciel Escudero Bombonato
 */
public final class PontoEletronicoServiceImpl implements PontoEletronicoService {
	
	private static final Logger log = Logger.getLogger(PontoEletronicoServiceImpl.class);
	
	private PontoEletronico pontoEletronico;
	
	private PontoEletronicoConfig pontoEletronicoConfig;
	
	public void marcarPonto(boolean isDaemon) {
		log.info("Inicio do processo de marcacao de ponto.");
        try {
            boolean continuar = true;
            
        	Calendar agora = GregorianCalendar.getInstance();
        	
            while (continuar) {
                
                agora.setTime(new Date());
            	
            	if (agora.get(Calendar.HOUR_OF_DAY) < pontoEletronicoConfig.getHoraInicioAlmoco()
            	|| agora.get(Calendar.HOUR_OF_DAY) >= pontoEletronicoConfig.getHoraFimAlmoco()) {
                	if (pontoEletronico != null) {
                		pontoEletronico.gerarPontoEletronico(pontoEletronicoConfig);
                    } else {
                        pontoEletronico = new PontoEletronico();
                        
                        pontoEletronico.setNomeUsuario(pontoEletronicoConfig.getNomeUsuario());
                        
                        pontoEletronico.setNomeComputador(InetAddress.getLocalHost().getHostName());

                        pontoEletronico.gerarPontoEletronico(pontoEletronicoConfig);
                    }

                	PontoEletronicoFileUtil.salvarArquivoPonto(pontoEletronico);            		
            	}
                
            	if (isDaemon) {
            		Thread.sleep(60000);
            	} else {
            		continuar = false;
            	}
            }
        } catch (InterruptedException e) {
            log.error("Erro de manipulacao de arquivo.", e);
        } catch (IOException e) {
        	log.error("Erro de manipulacao de arquivo.", e);
        } finally {
        	log.info("Fim do processo de marcacao de ponto.");	
        }
	}
	
	/**
	 * @return the pontoEletronico
	 */
	public PontoEletronico getPontoEletronico() {
		return pontoEletronico;
	}

	/**
	 * @param pontoEletronico the pontoEletronico to set
	 */
	public void setPontoEletronico(PontoEletronico pontoEletronico) {
		this.pontoEletronico = pontoEletronico;
	}

	/**
	 * @return the pontoEletronicoConfig
	 */
	public PontoEletronicoConfig getPontoEletronicoConfig() {
		return pontoEletronicoConfig;
	}

	/**
	 * @param pontoEletronicoConfig the pontoEletronicoConfig to set
	 */
	public void setPontoEletronicoConfig(PontoEletronicoConfig pontoEletronicoConfig) {
		this.pontoEletronicoConfig = pontoEletronicoConfig;
	}
}
