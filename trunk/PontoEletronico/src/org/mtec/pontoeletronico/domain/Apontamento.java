package org.mtec.pontoeletronico.domain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.mtec.pontoeletronico.configuracao.domain.PontoEletronicoConfig;
import org.mtec.pontoeletronico.util.PontoEletronicoUtil;

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
	
	private String observacoes;

	private List<Periodo> periodos;
	
	/**
	 * Gera ou mantem um apontamento.
	 */
	public void gerarApontamento(PontoEletronicoConfig pontoEletronicoConfig) {
    	log.info("Gerando apontamento.");
    	
    	Periodo periodo = null;
		
		if (this.getPeriodos() == null
		|| this.getPeriodos().size() == 0) {
			periodo = new Periodo();
			
			periodo.setEntrada(new Date());
			
			this.setPeriodos(new ArrayList<Periodo>());
			
			this.getPeriodos().add(periodo);
		} else {
			periodo = this.getPeriodos().get(this.getPeriodos().size() - 1);
			
			if (periodo != null
			&& periodo.getSaida() != null) {
				
				double qtdHorasAlmoco = pontoEletronicoConfig.getHoraFimAlmoco() - pontoEletronicoConfig.getHoraInicioAlmoco();
				
				double difUltimoApontamento = (((new Date().getTime() - periodo.getSaida().getTime()) / 1000D) / 60D) / 60D;
				
				if (difUltimoApontamento >= qtdHorasAlmoco) {
					periodo = new Periodo();
					
					periodo.setEntrada(new Date());
					
					this.getPeriodos().add(periodo);
				}
			}
		}
		
		periodo.setSaida(new Date());
		
		calcularQuantidadeHorasTrabalhadasApontamento(pontoEletronicoConfig);
	}
	
	/**
	 * Calcula a quantidade de horas trabalhadas em um dia de apontamento.
	 */
	public void calcularQuantidadeHorasTrabalhadasApontamento(PontoEletronicoConfig pontoEletronicoConfig) {
		Calendar data = GregorianCalendar.getInstance();
		data.setTime(this.getDataApontamento());
		
		if (this.getPeriodos() != null
		&& this.getPeriodos().size() > 0) {

			this.setQtdHorasTrabalhadas(0D);
			
			for (int i = 0; i < this.getPeriodos().size(); i++) {
				this.setQtdHorasTrabalhadas(
						this.getQtdHorasTrabalhadas() + 
						(this.getPeriodos().get(i).getSaida().getTime() - this.getPeriodos().get(i).getEntrada().getTime())
					);
			}	
			
			this.setQtdHorasTrabalhadas(((this.getQtdHorasTrabalhadas() / 1000) / 60) / 60);
			
			String key = PontoEletronicoUtil.formatDate(
					this.getDataApontamento(), 
					PontoEletronicoUtil.PATTERN_DD_MM_YYYY
				);
			
			String keyFeriadoFixo = PontoEletronicoUtil.formatDate(
					this.getDataApontamento(),
					PontoEletronicoUtil.PATTERN_DD_MM
				);
			
			String hoje = PontoEletronicoUtil.formatDate(
					new Date(),
					PontoEletronicoUtil.PATTERN_DD_MM_YYYY
				);
			
			if (!hoje.equalsIgnoreCase(key)) {
				if (data.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
					this.setSaldoBancoHoras(this.getQtdHorasTrabalhadas());
				} else if (data.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
					this.setSaldoBancoHoras(this.getQtdHorasTrabalhadas());
				} else if (pontoEletronicoConfig.getFeriadosFixos() != null
				&& pontoEletronicoConfig.getFeriadosPontesVariaveis() != null
				&& pontoEletronicoConfig.getFeriadosFixos().get(keyFeriadoFixo) == null
				&& pontoEletronicoConfig.getFeriadosPontesVariaveis().get(key) == null) {
					this.setSaldoBancoHoras(
							this.getQtdHorasTrabalhadas() - 
							pontoEletronicoConfig.getQtdHorasTrabalhoDiario()
						);
				} else {
					this.setSaldoBancoHoras(this.getQtdHorasTrabalhadas());
				}	
			}
		}
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
	public List<Periodo> getPeriodos() {
		return periodos;
	}

	/**
	 * @param periodo the periodo to set
	 */
	public void setPeriodos(List<Periodo> periodos) {
		this.periodos = periodos;
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
