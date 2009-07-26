package org.mtec.pontoeletronico.domain;

import java.util.Date;

public final class Periodo {
	
	private Date entrada;
	
	private Date saida;
	
	private String observacoes;

	/**
	 * @return the observacoes
	 */
	public String getObservacoes() {
		return observacoes;
	}

	/**
	 * @param observacoes the observacoes to set
	 */
	public void setObservacoes(String observacoes) {
		this.observacoes = observacoes;
	}

	/**
	 * @return the entrada
	 */
	public Date getEntrada() {
		return entrada;
	}

	/**
	 * @param entrada the entrada to set
	 */
	public void setEntrada(Date entrada) {
		this.entrada = entrada;
	}

	/**
	 * @return the saida
	 */
	public Date getSaida() {
		return saida;
	}

	/**
	 * @param saida the saida to set
	 */
	public void setSaida(Date saida) {
		this.saida = saida;
	}
	
}
