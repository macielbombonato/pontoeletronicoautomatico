package org.mtec.pontoeletronico.domain;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.mtec.pontoeletronico.util.PontoEletronicoUtil;

/**
 * @author Maciel Escudero Bombonato - maciel.bombonato@gmail.com
 */
public final class Mes {
	
	private static final Logger log = Logger.getLogger(Mes.class);
	
	private String mesApontamento;
	
	private Double qtdHorasTrabalhadas;
	
	private Double saldoBancoHoras;
	
	private HashMap<String, Apontamento> apontamentos;
	
    /**
     * Gera ou mantem um mes de apontamento.
     */
    public void gerarMesApontamento(PontoEletronico pontoEletronico) {
    	log.info("Gerando mes de apontamento.");
		
		Apontamento apontamento = null;
		
		Calendar hoje = GregorianCalendar.getInstance();
		hoje.setTime(new Date());
		hoje.set(Calendar.HOUR_OF_DAY, 0);
		hoje.set(Calendar.MINUTE, 0);
		hoje.set(Calendar.SECOND, 0);
		hoje.set(Calendar.MILLISECOND, 0);

		String key = PontoEletronicoUtil.formatDate(
				hoje.getTime(), 
				PontoEletronicoUtil.PATTERN_DD_MM_YYYY
			);
		
		if (this.getApontamentos() == null) {
			this.setApontamentos(new HashMap<String, Apontamento>());
		}
		
		if (this.getApontamentos().size() > 0
		&& this.getApontamentos().get(key) != null) {
    		apontamento = (Apontamento) this.getApontamentos().get(key);
   		} else {
			apontamento = new Apontamento();
			
			apontamento.setDataApontamento(hoje.getTime());
			
			this.getApontamentos().put(key, apontamento);
		}
		
		apontamento.gerarApontamento();

		calcularQuantidadeHorasTrabalhadasMes();
		
		reCalcularQuantidadeHorasTrabalhadasApontamentos();
    }
    
	/**
	 * Calcula a quantidade de horas trabalhadas em um mes de apontamento.
	 */
	private void calcularQuantidadeHorasTrabalhadasMes() {
		
		this.setQtdHorasTrabalhadas(0D);
		
		if (this.getApontamentos() != null
		&& this.getApontamentos().size() > 0) {
			for (Iterator<Entry<String, Apontamento>> iterator = this.getApontamentos().entrySet().iterator(); iterator.hasNext();) {
				Entry<String, Apontamento> entry = iterator.next();
				
				Apontamento apontamento = entry.getValue();

				if (apontamento.getPeriodos() != null
				&& apontamento.getPeriodos().length > 0) {
					for (int i = 0; i < apontamento.getPeriodos().length; i++) {
						this.setQtdHorasTrabalhadas(
								this.getQtdHorasTrabalhadas() + 
								(apontamento.getPeriodos()[i].getSaida().getTime() - apontamento.getPeriodos()[i].getEntrada().getTime())
							);
					}	
				}
			}
			
			this.setQtdHorasTrabalhadas(((this.getQtdHorasTrabalhadas() / 1000) / 60) / 60);
		}
	}
	
	/**
	 * Recalcula a quantidade de horas trabalhadas nos apontamentos.
	 */
	private void reCalcularQuantidadeHorasTrabalhadasApontamentos() {
		if (this.getApontamentos() != null
		&& this.getApontamentos().size() > 0) {
			for (Iterator<Entry<String, Apontamento>> iterator = this.getApontamentos().entrySet().iterator(); iterator.hasNext();) {
				Entry<String, Apontamento> entry = iterator.next();
				
				Apontamento apontamento = entry.getValue();
				
				apontamento.calcularQuantidadeHorasTrabalhadasApontamento();
			}
		}
	}
	
	/**
	 * @return the mesApontamento
	 */
	public String getMesApontamento() {
		return mesApontamento;
	}

	/**
	 * @param mesApontamento the mesApontamento to set
	 */
	public void setMesApontamento(String mesApontamento) {
		this.mesApontamento = mesApontamento;
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
	 * @return the apontamento
	 */
	public HashMap<String, Apontamento> getApontamentos() {
		return apontamentos;
	}

	/**
	 * @param apontamento the apontamento to set
	 */
	public void setApontamentos(HashMap<String, Apontamento> apontamentos) {
		this.apontamentos = apontamentos;
	}

}
