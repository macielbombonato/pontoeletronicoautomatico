package org.mtec.pontoeletronico.configuracao.domain;

import java.util.TreeMap;

import org.apache.log4j.Logger;

/**
 * @author Maciel Escudero Bombonato - maciel.bombonato@gmail.com
 *
 */
public class PontoEletronicoConfig {
	
	private static final Logger log = Logger.getLogger(PontoEletronicoConfig.class);
	
	private String nomeUsuario;
	
	private String nomeAprovador;
	
	private double qtdHorasTrabalhoDiario;
	
	private int horaInicioAlmoco;
	
	private int horaFimAlmoco;
	
	private TreeMap<String, FeriadosPontes> feriadosFixos;
	
	private TreeMap<String, FeriadosPontes> feriadosPontesVariaveis;
	
	/**
	 * Gera ou mantem o arquivo de ponto eletronico do usuario corrente.
	 * @param pontoEletronicoConfig
	 */
	public void gerarPontoEletronicoConfig() {
		log.info("Gerando configuracao ponto eletronico.");
		
		this.setNomeUsuario(System.getProperty("user.name"));
		this.setNomeAprovador("_");
		this.setQtdHorasTrabalhoDiario(8D);
		this.setHoraInicioAlmoco(12);
		this.setHoraFimAlmoco(13);
		
		this.setFeriadosFixos(new TreeMap<String, FeriadosPontes>());

		FeriadosPontes anoNovo = new FeriadosPontes();
		anoNovo.setDataFeriado("01/01");
		anoNovo.setNomeFeriado("Ano novo");
		this.getFeriadosFixos().put(anoNovo.getDataFeriado(), anoNovo);
		
		FeriadosPontes natal = new FeriadosPontes();
		natal.setDataFeriado("25/12");
		natal.setNomeFeriado("Natal");
		this.getFeriadosFixos().put(natal.getDataFeriado(), natal);
		
		this.setFeriadosPontesVariaveis(new TreeMap<String, FeriadosPontes>());
	}

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
	 * @return the feriadosFixos
	 */
	public TreeMap<String, FeriadosPontes> getFeriadosFixos() {
		return feriadosFixos;
	}

	/**
	 * @param feriadosFixos the feriadosFixos to set
	 */
	public void setFeriadosFixos(TreeMap<String, FeriadosPontes> feriadosFixos) {
		this.feriadosFixos = feriadosFixos;
	}

	/**
	 * @return the feriadosPontesVariaveis
	 */
	public TreeMap<String, FeriadosPontes> getFeriadosPontesVariaveis() {
		return feriadosPontesVariaveis;
	}

	/**
	 * @param feriadosPontesVariaveis the feriadosPontesVariaveis to set
	 */
	public void setFeriadosPontesVariaveis(
			TreeMap<String, FeriadosPontes> feriadosPontesVariaveis) {
		this.feriadosPontesVariaveis = feriadosPontesVariaveis;
	}

	/**
	 * @return the horaInicioAlmoco
	 */
	public int getHoraInicioAlmoco() {
		return horaInicioAlmoco;
	}

	/**
	 * @param horaInicioAlmoco the horaInicioAlmoco to set
	 */
	public void setHoraInicioAlmoco(int horaInicioAlmoco) {
		this.horaInicioAlmoco = horaInicioAlmoco;
	}

	/**
	 * @return the horaFimAlmoco
	 */
	public int getHoraFimAlmoco() {
		return horaFimAlmoco;
	}

	/**
	 * @param horaFimAlmoco the horaFimAlmoco to set
	 */
	public void setHoraFimAlmoco(int horaFimAlmoco) {
		this.horaFimAlmoco = horaFimAlmoco;
	}
}
