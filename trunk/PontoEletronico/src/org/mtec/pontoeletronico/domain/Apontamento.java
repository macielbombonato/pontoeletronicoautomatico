package org.mtec.pontoeletronico.domain;

import java.util.Date;

import org.apache.log4j.Logger;

/**
 * 
 * @author Maciel Escudero Bombonato - maciel.bombonato@gmail.com
 *
 */
public final class Apontamento {
	
	private static final Logger log = Logger.getLogger(Apontamento.class);
	
	private Date dataApontamento;
	
	private Double qtdHorasTrabalhadas;
	
	private Double saldoBancoHoras;
	
	private Periodo[] periodos;
	
	/**
	 * Gera ou mantem um apontamento.
	 */
	public void gerarApontamento() {
    	log.info("Gerando apontamento.");
		
		Periodo periodo = null;
		
		if (this.getPeriodos() == null
		|| this.getPeriodos().length == 0) {
			periodo = new Periodo();
			
			periodo.setEntrada(new Date());
			
			this.setPeriodos(new Periodo[]{periodo});
		} else {
			periodo = this.getPeriodos()[this.getPeriodos().length - 1];
		}
		
		periodo.setSaida(new Date());
		
		calcularQuantidadeHorasTrabalhadasApontamento();
	}
	
	/**
	 * Calcula a quantidade de horas trabalhadas em um dia de apontamento.
	 */
	public void calcularQuantidadeHorasTrabalhadasApontamento() {
		this.setQtdHorasTrabalhadas(0D);
		
		if (this.getPeriodos() != null
		&& this.getPeriodos().length > 0) {
			for (int i = 0; i < this.getPeriodos().length; i++) {
				this.setQtdHorasTrabalhadas(
						this.getQtdHorasTrabalhadas() + 
						(this.getPeriodos()[i].getSaida().getTime() - this.getPeriodos()[i].getEntrada().getTime())
					);
			}	
		}
		
		this.setQtdHorasTrabalhadas(((this.getQtdHorasTrabalhadas() / 1000) / 60) / 60);
	}

	/**
	 * @return the dataApontamento
	 */
	public Date getDataApontamento() {
		return dataApontamento;
	}

	/**
	 * @param dataApontamento the dataApontamento to set
	 */
	public void setDataApontamento(Date dataApontamento) {
		this.dataApontamento = dataApontamento;
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
	 * @return the periodo
	 */
	public Periodo[] getPeriodos() {
		return periodos;
	}

	/**
	 * @param periodo the periodo to set
	 */
	public void setPeriodos(Periodo[] periodos) {
		this.periodos = periodos;
	}

}
