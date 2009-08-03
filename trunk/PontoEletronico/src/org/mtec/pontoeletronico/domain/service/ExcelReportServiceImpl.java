package org.mtec.pontoeletronico.domain.service;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map.Entry;

import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.apache.log4j.Logger;
import org.mtec.pontoeletronico.configuracao.domain.PontoEletronicoConfig;
import org.mtec.pontoeletronico.domain.Apontamento;
import org.mtec.pontoeletronico.domain.Mes;
import org.mtec.pontoeletronico.domain.Periodo;
import org.mtec.pontoeletronico.domain.PontoEletronico;
import org.mtec.pontoeletronico.domain.service.interfaces.ExcelReportService;
import org.mtec.pontoeletronico.util.PontoEletronicoUtil;

/**
 * Servico de geração de relatório Excel dos apontamentos registrados
 * pelo sistema de Ponto Eletronico.
 * @author Maciel Escudero Bombonato - maciel.bombonato@gmail.com
 */
public class ExcelReportServiceImpl implements ExcelReportService {

	private static final Logger log = Logger.getLogger(ExcelReportServiceImpl.class);
	
	private PontoEletronico pontoEletronico;
	
	private PontoEletronicoConfig pontoEletronicoConfig;
	
	private WritableWorkbook workbook;

	/* (non-Javadoc)
	 * @see org.mtec.pontoeletronico.domain.service.interfaces.ExcelReportService#gerarRelatorio()
	 */
	@Override
	public void gerarRelatorio() {
		// TODO Auto-generated method stub
		log.info("TODO Implementar.");
		
		WritableFont arial10 = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD, false);
		WritableCellFormat formatoDados = new WritableCellFormat(arial10);
		WritableCellFormat formatoDadosTexto = new WritableCellFormat(arial10);
		try {
			formatoDados.setAlignment(Alignment.CENTRE);
			
			formatoDadosTexto.setAlignment(Alignment.LEFT);
		} catch (WriteException e1) {
			log.error("Erro ao definir formatacao de campo.", e1);
		}
		
		for (Iterator<Entry<String, Mes>> mesIterator = pontoEletronico.getMesesApontamento().entrySet().iterator(); mesIterator.hasNext();) {
			Entry<String, Mes> mesEntry = mesIterator.next();
			
			Mes mes = mesEntry.getValue();
			
			WritableSheet sheet = geraDadosFixosPlanilha(mes);
			
			Label dataQtdTotalHoras = null;
			if (mes.getQtdHorasTrabalhadas() != null) {
				dataQtdTotalHoras = new Label(5, 39, mes.getQtdHorasTrabalhadas()+"", formatoDados);
			}
			
			Label dataSaldoBancoHoras = null;
			if (mes.getSaldoBancoHoras() != null) {
				dataSaldoBancoHoras = new Label(5, 40, mes.getSaldoBancoHoras()+"", formatoDados);
			}
			
			try {
				if (dataQtdTotalHoras != null) {
					sheet.addCell(dataQtdTotalHoras);	
				}
				
				if (dataSaldoBancoHoras != null) {
					sheet.addCell(dataSaldoBancoHoras);	
				}
				
			} catch (RowsExceededException e) {
				log.error("Erro na criacao de linha.", e);
			} catch (WriteException e) {
				log.error("Erro na criacao de linha.", e);
			}
			
			int indexLine = 7;
			for (Iterator<Entry<String, Apontamento>> apontamentoIterator = mes.getApontamentos().entrySet().iterator(); apontamentoIterator.hasNext();) {
				Entry<String, Apontamento> apontamentoEntry = apontamentoIterator.next();
				
				Apontamento apontamento = apontamentoEntry.getValue();
				
				Label dataDia = 
					new Label(
							0, 
							indexLine, 
							PontoEletronicoUtil.formatDate(
									apontamento.getDataApontamento(), 
									PontoEletronicoUtil.PATTERN_DD
								),
							formatoDados
						);
				
				Label dataEntrada = null;
				Label dataIniAlmoco = null;
				Label dataFimAlmoco = null;
				Label dataSaida = null;
				
				if (apontamento.getPeriodos() != null
				&& apontamento.getPeriodos().size() > 0) {
					dataEntrada = 
						new Label(
								1, 
								indexLine, 
								PontoEletronicoUtil.formatDate(
										apontamento.getPeriodos().get(0).getEntrada(), 
										PontoEletronicoUtil.PATTERN_HH_MM
									),
								formatoDados
							);
					
					dataIniAlmoco = 
						new Label(
								2, 
								indexLine, 
								PontoEletronicoUtil.formatDate(
										apontamento.getPeriodos().get(0).getSaida(), 
										PontoEletronicoUtil.PATTERN_HH_MM
									),
								formatoDados
							);
					
					dataFimAlmoco = 
						new Label(
								3, 
								indexLine, 
								PontoEletronicoUtil.formatDate(
										apontamento.getPeriodos().get(apontamento.getPeriodos().size() - 1).getEntrada(), 
										PontoEletronicoUtil.PATTERN_HH_MM
									),
								formatoDados
							);
					
					dataSaida = 
						new Label(
								4, 
								indexLine, 
								PontoEletronicoUtil.formatDate(
										apontamento.getPeriodos().get(apontamento.getPeriodos().size() - 1).getSaida(), 
										PontoEletronicoUtil.PATTERN_HH_MM
									),
								formatoDados
							);
				}
				
				Label dataQtdHoras = null;
				
				if (apontamento.getQtdHorasTrabalhadas() != null) {
					dataQtdHoras = new Label(5, indexLine, apontamento.getQtdHorasTrabalhadas()+"", formatoDados);	
				}
				
				Label dataObs = new Label(6, indexLine, apontamento.getObservacoes(), formatoDadosTexto);
				
				try {
					if (dataDia != null) {
						sheet.addCell(dataDia);	
					}
					
					if (dataEntrada != null) {
						sheet.addCell(dataEntrada);	
					}
					
					if (dataIniAlmoco != null) {
						sheet.addCell(dataIniAlmoco);	
					}
					
					if (dataFimAlmoco != null) {
						sheet.addCell(dataFimAlmoco);	
					}
					
					if (dataSaida != null) {
						sheet.addCell(dataSaida);	
					}
					
					if (dataQtdHoras != null) {
						sheet.addCell(dataQtdHoras);	
					}
					
					if (dataObs != null) {
						sheet.addCell(dataObs);	
					}
				} catch (RowsExceededException e) {
					log.error("Erro na criacao de linha.", e);
				} catch (WriteException e) {
					log.error("Erro na criacao de linha.", e);
				}
				
				indexLine++;
			}
		}
		
		try {
			workbook.write();
			workbook.close();
		} catch (WriteException e) {
			log.error("Erro ao escrever no arquivo de relatorio.", e);
		} catch (IOException e) {
			log.error("Erro ao escrever no arquivo de relatorio.", e);
		} 
	}

	/* (non-Javadoc)
	 * @see org.mtec.pontoeletronico.domain.service.interfaces.ExcelReportService#gerarRelatorio(java.lang.String)
	 */
	@Override
	public void gerarRelatorio(String mesReferencia) {
		// TODO Auto-generated method stub
		log.info("TODO Implementar.");
		
		 
	}
	
	/**
	 * Gera os campos com informações fixas e informações de controle.
	 * @param mes
	 * @return WritableSheet
	 */
	private WritableSheet geraDadosFixosPlanilha(Mes mes) {
		WritableSheet sheet = workbook.createSheet(mes.getMesApontamento(), 0);
		
		WritableFont arial14Bold = new WritableFont(WritableFont.ARIAL, 14, WritableFont.BOLD, false);
		WritableFont arial12Bold = new WritableFont(WritableFont.ARIAL, 12, WritableFont.BOLD, false);
		WritableFont arial10Bold = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD, false);
		
		WritableCellFormat formatoTitulo = new WritableCellFormat(arial14Bold);
		WritableCellFormat formatoAtial12Bold = new WritableCellFormat (arial12Bold);
		WritableCellFormat formatoAtial12BoldTexto = new WritableCellFormat (arial12Bold);
		WritableCellFormat formatoAtial10Bold = new WritableCellFormat (arial10Bold);
		WritableCellFormat formatoAtial10BoldTexto = new WritableCellFormat (arial10Bold);
		try {
			formatoTitulo.setAlignment(Alignment.CENTRE);
			
			formatoAtial12Bold.setAlignment(Alignment.CENTRE);
			formatoAtial12Bold.setVerticalAlignment(VerticalAlignment.CENTRE);
			formatoAtial12Bold.setWrap(false);
			formatoAtial12Bold.setShrinkToFit(true);
			formatoAtial12Bold.setBorder(Border.ALL, BorderLineStyle.MEDIUM);
			
			formatoAtial12BoldTexto.setAlignment(Alignment.LEFT);
			formatoAtial12BoldTexto.setVerticalAlignment(VerticalAlignment.CENTRE);
			formatoAtial12BoldTexto.setWrap(false);
			formatoAtial12BoldTexto.setShrinkToFit(true);
			formatoAtial12BoldTexto.setBorder(Border.ALL, BorderLineStyle.MEDIUM);
			
			formatoAtial10Bold.setAlignment(Alignment.CENTRE);
			formatoAtial10Bold.setVerticalAlignment(VerticalAlignment.CENTRE);
			formatoAtial10Bold.setWrap(false);
			formatoAtial10Bold.setShrinkToFit(true);
			formatoAtial10Bold.setBorder(Border.ALL, BorderLineStyle.MEDIUM);
			
			formatoAtial10BoldTexto.setAlignment(Alignment.LEFT);
			formatoAtial10BoldTexto.setVerticalAlignment(VerticalAlignment.CENTRE);
			formatoAtial10BoldTexto.setWrap(false);
			formatoAtial10BoldTexto.setShrinkToFit(true);
			formatoAtial10BoldTexto.setBorder(Border.ALL, BorderLineStyle.MEDIUM);
		} catch (WriteException e1) {
			log.error("Erro ao definir formatacao de campo.", e1);
		}
		
		Label labelTitulo = new Label(0, 0, "Planilha de Horas", formatoTitulo);
		Label labelEmpresa = new Label(0, 2, "Empresa:", formatoAtial12BoldTexto);
		Label labelNome = new Label(0, 3, "Nome:", formatoAtial12BoldTexto);
		Label labelMes = new Label(0, 4, "Mês:", formatoAtial12BoldTexto);
		
		Label dataEmpresa = new Label(1, 2, "Incluir na configuracao.", formatoAtial12BoldTexto);
		Label dataNome = new Label(1, 3, pontoEletronicoConfig.getNomeUsuario(), formatoAtial12BoldTexto);
		Label dataMes = new Label(1, 4, mes.getMesApontamento(), formatoAtial12BoldTexto);
		
		Label labelDia = new Label(0, 6, "Dia", formatoAtial12Bold);
		Label labelEntrada = new Label(1, 6, "Entrada", formatoAtial12Bold);
		Label labelIniAlmoco = new Label(2, 6, "Inicio Almoço", formatoAtial12Bold);
		Label labelFimAlmoco = new Label(3, 6, "Término Almoço", formatoAtial12Bold);
		Label labelSaida = new Label(4, 6, "Saída", formatoAtial12Bold);
		Label labelQtdHoras = new Label(5, 6, "Núm. Horas", formatoAtial12Bold);
		Label labelObs = new Label(6, 6, "Observações", formatoAtial12Bold);
		Label labelVisto = new Label(7, 6, "Visto", formatoAtial12Bold);
		
		Label labelqtdTotalHoras = new Label(2, 39, "Número Total de Horas:", formatoAtial10Bold);
		Label labelSaldoBancoHoras = new Label(2, 40, "Saldo Banco de Horas", formatoAtial10Bold);
		Label labelDataUsuario = new Label(0, 42, "Data:", formatoAtial10BoldTexto);
		Label dataNomeUsuario = new Label(3, 43, pontoEletronicoConfig.getNomeUsuario(), formatoAtial10Bold);
		Label labelDataAprovador = new Label(0, 45, "Data:", formatoAtial10BoldTexto);
		Label dataNomeAprovador = new Label(3, 46, pontoEletronicoConfig.getNomeAprovador(), formatoAtial10Bold);
		
		try {
			sheet.addCell(labelTitulo);
			sheet.addCell(labelEmpresa);
			sheet.addCell(labelNome);
			sheet.addCell(labelMes);
			
			sheet.addCell(dataEmpresa);
			sheet.addCell(dataNome);
			sheet.addCell(dataMes);
			
			sheet.addCell(labelDia);
			sheet.addCell(labelEntrada);
			sheet.addCell(labelIniAlmoco);
			sheet.addCell(labelFimAlmoco);
			sheet.addCell(labelSaida);
			sheet.addCell(labelQtdHoras);
			sheet.addCell(labelObs);
			sheet.addCell(labelVisto);
			
			sheet.addCell(labelqtdTotalHoras);
			sheet.addCell(labelSaldoBancoHoras);
			sheet.addCell(labelDataUsuario);
			sheet.addCell(dataNomeUsuario);
			sheet.addCell(labelDataAprovador);
			sheet.addCell(dataNomeAprovador);
		} catch (RowsExceededException e) {
			log.error("Erro na criacao de linha.", e);
		} catch (WriteException e) {
			log.error("Erro na criacao de linha.", e);
		}
		return sheet;
	}

	/**
	 * @return the pontoEletronico
	 */
	public PontoEletronico getPontoEletronico() {
		return pontoEletronico;
	}

	/**
	 * @param pontoEletronico the pontoEletronico to set
	 */
	public void setPontoEletronico(PontoEletronico pontoEletronico) {
		this.pontoEletronico = pontoEletronico;
	}

	/**
	 * @return the pontoEletronicoConfig
	 */
	public PontoEletronicoConfig getPontoEletronicoConfig() {
		return pontoEletronicoConfig;
	}

	/**
	 * @param pontoEletronicoConfig the pontoEletronicoConfig to set
	 */
	public void setPontoEletronicoConfig(PontoEletronicoConfig pontoEletronicoConfig) {
		this.pontoEletronicoConfig = pontoEletronicoConfig;
	}
	
	/**
	 * @return the workbook
	 */
	public WritableWorkbook getWorkbook() {
		return workbook;
	}

	/**
	 * @param workbook the workbook to set
	 */
	public void setWorkbook(WritableWorkbook workbook) {
		this.workbook = workbook;
	}
}
