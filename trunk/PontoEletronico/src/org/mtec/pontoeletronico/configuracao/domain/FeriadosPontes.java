package org.mtec.pontoeletronico.configuracao.domain;


/**
 * @author Maciel Escudero Bombonato - maciel.bombonato@gmail.com
 *
 */
public final class FeriadosPontes {
	
	private String dataFeriado;
	
	private String nomeFeriado;
	
	private String observacoes;

	/**
	 * @return the dataFeriado
	 */
	public String getDataFeriado() {
		return dataFeriado;
	}

	/**
	 * @param dataFeriado the dataFeriado to set
	 */
	public void setDataFeriado(String dataFeriado) {
		this.dataFeriado = dataFeriado;
	}

	/**
	 * @return the nomeFeriado
	 */
	public String getNomeFeriado() {
		return nomeFeriado;
	}

	/**
	 * @param nomeFeriado the nomeFeriado to set
	 */
	public void setNomeFeriado(String nomeFeriado) {
		this.nomeFeriado = nomeFeriado;
	}

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
}
