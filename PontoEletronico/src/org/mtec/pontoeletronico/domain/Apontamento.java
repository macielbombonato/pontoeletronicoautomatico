package org.mtec.pontoeletronico.domain;

import java.util.Date;

public final class Apontamento {
	
	private Date dataApontamento;
	
	private Double qtdHorasTrabalhadas;
	
	private Double saldoBancoHoras;
	
	private Periodo[] periodos;

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
