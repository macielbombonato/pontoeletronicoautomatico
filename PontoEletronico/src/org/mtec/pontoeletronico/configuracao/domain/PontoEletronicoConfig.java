package org.mtec.pontoeletronico.configuracao.domain;

import java.util.HashMap;

/**
 * @author Maciel Escudero Bombonato - maciel.bombonato@gmail.com
 *
 */
public class PontoEletronicoConfig {
	
	private String nomeUsuario;
	
	private String nomeAprovador;
	
	private double qtdHorasAlmoco;
	
	private double qtdHorasTrabalhoDiario;
	
	private HashMap<String, FeriadosPontes> feriadosFixos;
	
	private HashMap<String, FeriadosPontes> feriadosPontesVariaveis;

	/**
	 * @return the qtdHorasTrabalhoDiario
	 */
	public double getQtdHorasTrabalhoDiario() {
		return qtdHorasTrabalhoDiario;
	}

	/**
	 * @param qtdHorasTrabalhoDiario the qtdHorasTrabalhoDiario to set
	 */
	public void setQtdHorasTrabalhoDiario(double qtdHorasTrabalhoDiario) {
		this.qtdHorasTrabalhoDiario = qtdHorasTrabalhoDiario;
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
	 * @return the nomeAprovador
	 */
	public String getNomeAprovador() {
		return nomeAprovador;
	}

	/**
	 * @param nomeAprovador the nomeAprovador to set
	 */
	public void setNomeAprovador(String nomeAprovador) {
		this.nomeAprovador = nomeAprovador;
	}

	/**
	 * @return the qtdHorasAlmoco
	 */
	public double getQtdHorasAlmoco() {
		return qtdHorasAlmoco;
	}

	/**
	 * @param qtdHorasAlmoco the qtdHorasAlmoco to set
	 */
	public void setQtdHorasAlmoco(double qtdHorasAlmoco) {
		this.qtdHorasAlmoco = qtdHorasAlmoco;
	}

	/**
	 * @return the feriadosFixos
	 */
	public HashMap<String, FeriadosPontes> getFeriadosFixos() {
		return feriadosFixos;
	}

	/**
	 * @param feriadosFixos the feriadosFixos to set
	 */
	public void setFeriadosFixos(HashMap<String, FeriadosPontes> feriadosFixos) {
		this.feriadosFixos = feriadosFixos;
	}

	/**
	 * @return the feriadosPontesVariaveis
	 */
	public HashMap<String, FeriadosPontes> getFeriadosPontesVariaveis() {
		return feriadosPontesVariaveis;
	}

	/**
	 * @param feriadosPontesVariaveis the feriadosPontesVariaveis to set
	 */
	public void setFeriadosPontesVariaveis(
			HashMap<String, FeriadosPontes> feriadosPontesVariaveis) {
		this.feriadosPontesVariaveis = feriadosPontesVariaveis;
	}
}
