package org.mtec.pontoeletronico.domain;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.mtec.pontoeletronico.configuracao.domain.FeriadosPontes;
import org.mtec.pontoeletronico.configuracao.domain.PontoEletronicoConfig;
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
    public void gerarMesApontamento(PontoEletronicoConfig pontoEletronicoConfig) {
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
		
		apontamento.gerarApontamento(pontoEletronicoConfig);

		reCalcularQuantidadeHorasTrabalhadasApontamentos(pontoEletronicoConfig);
		
		calcularQuantidadeHorasTrabalhadasMes();
    }
    
	/**
	 * Calcula a quantidade de horas trabalhadas em um mes de apontamento.
	 */
	private void calcularQuantidadeHorasTrabalhadasMes() {

		if (this.getApontamentos() != null
		&& this.getApontamentos().size() > 0) {
			this.setQtdHorasTrabalhadas(0D);
			this.setSaldoBancoHoras(0D);
			
			for (Iterator<Entry<String, Apontamento>> iterator = this.getApontamentos().entrySet().iterator(); iterator.hasNext();) {
				Entry<String, Apontamento> entry = iterator.next();
				
				Apontamento apontamento = entry.getValue();
				
				if (apontamento.getSaldoBancoHoras() != null) {
					this.setSaldoBancoHoras(
							this.getSaldoBancoHoras() + 
							apontamento.getSaldoBancoHoras()
						);	
				}

				if (apontamento.getPeriodos() != null
				&& apontamento.getPeriodos().size() > 0) {
					for (int i = 0; i < apontamento.getPeriodos().size(); i++) {
						this.setQtdHorasTrabalhadas(
								this.getQtdHorasTrabalhadas() + 
								(apontamento.getPeriodos().get(i).getSaida().getTime() - apontamento.getPeriodos().get(i).getEntrada().getTime())
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
	private void reCalcularQuantidadeHorasTrabalhadasApontamentos(PontoEletronicoConfig pontoEletronicoConfig) {
		if (this.getApontamentos() != null
		&& this.getApontamentos().size() > 0) {
			for (Iterator<Entry<String, Apontamento>> iterator = this.getApontamentos().entrySet().iterator(); iterator.hasNext();) {
				Entry<String, Apontamento> entry = iterator.next();
				
				Apontamento apontamento = entry.getValue();
				
				apontamento.calcularQuantidadeHorasTrabalhadasApontamento(pontoEletronicoConfig);
			}
		}
	}
	
    /**
     * Montar um mes de apontamento considerando apenas os dias uteis.
     */
    public void montarMesApontamento(PontoEletronicoConfig pontoEletronicoConfig) {
    	log.info("Montando um mes de apontamento.");
		
		Calendar hoje = GregorianCalendar.getInstance();
		hoje.setTime(new Date());
		hoje.set(Calendar.HOUR_OF_DAY, 0);
		hoje.set(Calendar.MINUTE, 0);
		hoje.set(Calendar.SECOND, 0);
		hoje.set(Calendar.MILLISECOND, 0);
    	
    	Calendar ultimoDiaCalendar = GregorianCalendar.getInstance();
		ultimoDiaCalendar.setTime(new Date());
		ultimoDiaCalendar.set(Calendar.DAY_OF_MONTH, 1);
		ultimoDiaCalendar.set(Calendar.HOUR_OF_DAY, 0);
		ultimoDiaCalendar.set(Calendar.MINUTE, 0);
		ultimoDiaCalendar.set(Calendar.SECOND, 0);
		ultimoDiaCalendar.set(Calendar.MILLISECOND, 0);
		ultimoDiaCalendar.add(Calendar.MONTH, 1);
		ultimoDiaCalendar.add(Calendar.DAY_OF_MONTH, -1);
		
    	Calendar data = GregorianCalendar.getInstance();
    	data.setTime(new Date());
    	data.set(Calendar.HOUR_OF_DAY, 0);
    	data.set(Calendar.MINUTE, 0);
    	data.set(Calendar.SECOND, 0);
    	data.set(Calendar.MILLISECOND, 0);

		String key = "";
		String keyFeriadoFixo = "";
		FeriadosPontes feriadoFixo = null;
		FeriadosPontes feriadoPonteVariavel = null;
		
		if (this.getApontamentos() == null) {
			this.setApontamentos(new HashMap<String, Apontamento>());
		}
		
		for (int i = 1; i < ultimoDiaCalendar.get(Calendar.DAY_OF_MONTH); i++) {
			data.set(Calendar.DAY_OF_MONTH, i);
			
			key = PontoEletronicoUtil.formatDate(
					data.getTime(), 
					PontoEletronicoUtil.PATTERN_DD_MM_YYYY
				);
			
			keyFeriadoFixo = PontoEletronicoUtil.formatDate(
					data.getTime(), 
					PontoEletronicoUtil.PATTERN_DD_MM
				);
			
			if (pontoEletronicoConfig != null) {
				if (pontoEletronicoConfig.getFeriadosFixos() != null) {
					feriadoFixo = pontoEletronicoConfig.getFeriadosFixos().get(keyFeriadoFixo);	
				}
				if (pontoEletronicoConfig.getFeriadosPontesVariaveis() != null) {
					feriadoPonteVariavel = pontoEletronicoConfig.getFeriadosPontesVariaveis().get(key);	
				}
			}
			
			if (data.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
				Apontamento apontamento = new Apontamento();
				
				apontamento.setDataApontamento(data.getTime());
				
				apontamento.setObservacoes("Sabado");
				
				this.getApontamentos().put(key, apontamento);
				
			} else if (data.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
				Apontamento apontamento = new Apontamento();
				
				apontamento.setDataApontamento(data.getTime());
				
				apontamento.setObservacoes("Domingo");
				
				this.getApontamentos().put(key, apontamento);
				
			} else if (feriadoFixo != null) {
				Apontamento apontamento = new Apontamento();
				
				apontamento.setDataApontamento(data.getTime());
				
				apontamento.setObservacoes(feriadoFixo.getNomeFeriado());
				
				this.getApontamentos().put(key, apontamento);
				
			} else if (feriadoPonteVariavel != null) {
				Apontamento apontamento = new Apontamento();
				
				apontamento.setDataApontamento(data.getTime());
				
				apontamento.setObservacoes(feriadoPonteVariavel.getNomeFeriado());
				
				this.getApontamentos().put(key, apontamento);
								
			} else {
				Apontamento apontamento = new Apontamento();
				
				if (data.get(Calendar.DAY_OF_MONTH) < hoje.get(Calendar.DAY_OF_MONTH)) {
					apontamento.setSaldoBancoHoras(pontoEletronicoConfig.getQtdHorasTrabalhoDiario() * -1);
				}
				
				apontamento.setDataApontamento(data.getTime());
				
				this.getApontamentos().put(key, apontamento);				
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
