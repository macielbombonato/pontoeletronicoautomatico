package org.mtec.pontoeletronico.domain;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.mtec.pontoeletronico.configuracao.domain.PontoEletronicoConfig;
import org.mtec.pontoeletronico.util.PontoEletronicoUtil;

/**
 * @author Maciel Escudero Bombonato - maciel.bombonato@gmail.com
 */
public final class PontoEletronico {
	
	private static final Logger log = Logger.getLogger(PontoEletronico.class);
	
	private String nomeUsuario;
	
	private String nomeComputador;
	
	private Double qtdHorasTrabalhadas;
	
	private Double saldoBancoHoras;
	
	private TreeMap<String, Mes> mesesApontamento;
	
	/**
	 * Gera ou mantem o arquivo de ponto eletronico do usuario corrente.
	 * @param pontoEletronicoConfig
	 */
	public void gerarPontoEletronico(PontoEletronicoConfig pontoEletronicoConfig) {
		log.info("Gerando ponto eletronico.");
		
		this.setNomeUsuario(pontoEletronicoConfig.getNomeUsuario());
		try {
			this.setNomeComputador(InetAddress.getLocalHost().getHostName());
		} catch (UnknownHostException e) {
			log.error("Erro ao obter o nome do computador.", e);
		}
		
		Mes mes = null;
		
		Calendar mesAtual = GregorianCalendar.getInstance();
		mesAtual.setTime(new Date());
		mesAtual.set(Calendar.DAY_OF_MONTH, 1);
		mesAtual.set(Calendar.HOUR_OF_DAY, 0);
		mesAtual.set(Calendar.MINUTE, 0);
		mesAtual.set(Calendar.SECOND, 0);
		mesAtual.set(Calendar.MILLISECOND, 0);
		
		if (this.getMesesApontamento() == null) {
			this.setMesesApontamento(new TreeMap<String, Mes>());
		}
		
		mes = (Mes) this.getMesesApontamento().get(
				PontoEletronicoUtil.formatDate(
        				mesAtual.getTime(), 
        				PontoEletronicoUtil.PATTERN_MMM_YYYY
        			).toUpperCase()
			);
		
		if (mes == null) {
			mes = new Mes();
			
			mes.montarMesApontamento(pontoEletronicoConfig);
			
			mes.setMesApontamento(
	        		PontoEletronicoUtil.formatDate(
	        				mesAtual.getTime(), 
	        				PontoEletronicoUtil.PATTERN_MMM_YYYY
	        			).toUpperCase()
	        	);
			
			this.getMesesApontamento().put(mes.getMesApontamento().toUpperCase(), mes);
		}
		
		mes.gerarMesApontamento(pontoEletronicoConfig);
	}
	
	/**
	 * @return the nomeUsuario
	 */
	public String getNomeUsuario() {
		return nomeUsuario;
	}

	/**
	 * @param nomeUsuario the nomeUsuario to set
	 */
	public void setNomeUsuario(String nomeUsuario) {
		this.nomeUsuario = nomeUsuario;
	}

	/**
	 * @return the nomeComputador
	 */
	public String getNomeComputador() {
		return nomeComputador;
	}

	/**
	 * @param nomeComputador the nomeComputador to set
	 */
	public void setNomeComputador(String nomeComputador) {
		this.nomeComputador = nomeComputador;
	}

	/**
	 * @return the qtdHorasTrabalhadas
	 */
	public Double getQtdHorasTrabalhadas() {
		return qtdHorasTrabalhadas;
	}

	/**
	 * @param qtdHorasTrabalhadas the qtdHorasTrabalhadas to set
	 */
	public void setQtdHorasTrabalhadas(Double qtdHorasTrabalhadas) {
		this.qtdHorasTrabalhadas = qtdHorasTrabalhadas;
	}

	/**
	 * @return the saldoBancoHoras
	 */
	public Double getSaldoBancoHoras() {
		return saldoBancoHoras;
	}

	/**
	 * @param saldoBancoHoras the saldoBancoHoras to set
	 */
	public void setSaldoBancoHoras(Double saldoBancoHoras) {
		this.saldoBancoHoras = saldoBancoHoras;
	}

	/**
	 * @return the mesApontamento
	 */
	public TreeMap<String, Mes> getMesesApontamento() {
		return mesesApontamento;
	}

	/**
	 * @param mesApontamento the mesApontamento to set
	 */
	public void setMesesApontamento(TreeMap<String, Mes> mesesApontamento) {
		this.mesesApontamento = mesesApontamento;
	}

}
