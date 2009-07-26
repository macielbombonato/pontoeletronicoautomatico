package org.mtec.pontoeletronico.domain;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.mtec.pontoeletronico.util.PontoEletronicoUtil;

/**
 * @author Maciel Escudero Bombonato
 */
public final class PontoEletronico {
	
	private static Logger log = Logger.getLogger(PontoEletronico.class.getName());
	
	private String nomeUsuario;
	
	private String nomeComputador;
	
	private Double qtdHorasTrabalhadas;
	
	private Double saldoBancoHoras;
	
	private HashMap<String, Mes> mesesApontamento;
	
	/**
	 * Gera ponto eletronico do usuario corrente.
	 */
	public void gerarPontoEletronico() {
		log.log(Level.INFO, "Gerando ponto eletronico.");
		
		Mes mes = null;
		
		Calendar mesAtual = GregorianCalendar.getInstance();
		mesAtual.setTime(new Date());
		mesAtual.set(Calendar.DAY_OF_MONTH, 1);
		mesAtual.set(Calendar.HOUR_OF_DAY, 0);
		mesAtual.set(Calendar.MINUTE, 0);
		mesAtual.set(Calendar.SECOND, 0);
		mesAtual.set(Calendar.MILLISECOND, 0);
		
		if (this.getMesesApontamento() == null) {
			this.setMesesApontamento(new HashMap<String, Mes>());
		}
		
		if (this.getMesesApontamento().size() > 0
		&& this.getMesesApontamento().get(PontoEletronicoUtil.formatDate(mesAtual.getTime(), PontoEletronicoUtil.PATTERN_MMM_YYYY)) != null) {
    		mes = (Mes) this.getMesesApontamento().get(
    				PontoEletronicoUtil.formatDate(
	        				mesAtual.getTime(), 
	        				PontoEletronicoUtil.PATTERN_MMM_YYYY
	        			)
    			);
   		} else {
			mes = new Mes();
			
			mes.setMesApontamento(
	        		PontoEletronicoUtil.formatDate(
	        				mesAtual.getTime(), 
	        				PontoEletronicoUtil.PATTERN_MMM_YYYY
	        			)
	        	);
			
			this.getMesesApontamento().put(mes.getMesApontamento(), mes);
		}
		
		mes.gerarMesApontamento(this);
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
	public HashMap<String, Mes> getMesesApontamento() {
		return mesesApontamento;
	}

	/**
	 * @param mesApontamento the mesApontamento to set
	 */
	public void setMesesApontamento(HashMap<String, Mes> mesesApontamento) {
		this.mesesApontamento = mesesApontamento;
	}

}
