package org.mtec.pontoeletronico.domain.service;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map.Entry;

import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.write.Label;
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
	
	private static WritableFont fonteArial10 = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD, false);
	private static WritableFont fonteArial10Bold = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD, false);
	private static WritableFont fonteArial12Bold = new WritableFont(WritableFont.ARIAL, 12, WritableFont.BOLD, false);
	private static WritableFont fonteArial14Bold = new WritableFont(WritableFont.ARIAL, 14, WritableFont.BOLD, false);
	
	private static WritableCellFormat formatoTitulo = new WritableCellFormat(fonteArial14Bold);
	private static WritableCellFormat formatoCabecalho = new WritableCellFormat(fonteArial12Bold);
	private static WritableCellFormat formatoColunas = new WritableCellFormat(fonteArial10Bold);
	private static WritableCellFormat formatoDados = new WritableCellFormat(fonteArial10);
	private static WritableCellFormat formatoDias = new WritableCellFormat(fonteArial12Bold);
	private static WritableCellFormat formatoObs = new WritableCellFormat(fonteArial10);
	private static WritableCellFormat formatoTotais = new WritableCellFormat(fonteArial10Bold);
	private static WritableCellFormat formatoAssinaturas = new WritableCellFormat(fonteArial10);
	private static WritableCellFormat formatoAssinaturasVazias = new WritableCellFormat(fonteArial10);
	
	/*
	 * Definilções de formatação que devem ficar obrigatóriamente dentro de um bloco de exceção.
	 */
	static {
		try {
			formatoTitulo.setAlignment(Alignment.CENTRE);
			
			formatoCabecalho.setAlignment(Alignment.LEFT);
			
			formatoColunas.setAlignment(Alignment.CENTRE);
			formatoColunas.setBorder(Border.ALL, BorderLineStyle.THIN);
			formatoColunas.setShrinkToFit(true);
			
			formatoDados.setAlignment(Alignment.CENTRE);
			formatoDados.setBorder(Border.ALL, BorderLineStyle.THIN);
			formatoDados.setShrinkToFit(true);
			
			formatoDias.setAlignment(Alignment.RIGHT);
			formatoDias.setBorder(Border.ALL, BorderLineStyle.THIN);
			formatoDias.setShrinkToFit(true);
			
			formatoObs.setAlignment(Alignment.LEFT);
			formatoObs.setBorder(Border.ALL, BorderLineStyle.THIN);
			formatoObs.setShrinkToFit(true);
			
			formatoTotais.setAlignment(Alignment.LEFT);
			
			formatoAssinaturas.setAlignment(Alignment.CENTRE);
			
			formatoAssinaturasVazias.setBorder(Border.BOTTOM, BorderLineStyle.THIN);
		} catch (WriteException e1) {
			log.error("Erro ao definir formatacao de campo.", e1);
		}
	}

	/* (non-Javadoc)
	 * @see org.mtec.pontoeletronico.domain.service.interfaces.ExcelReportService#gerarRelatorio()
	 */
	@Override
	public void gerarRelatorio() {
		log.info("Geracao de relatorio completo de apontamentos.");
		
		for (Iterator<Entry<String, Mes>> mesIterator = pontoEletronico.getMesesApontamento().entrySet().iterator(); mesIterator.hasNext();) {
			Entry<String, Mes> mesEntry = mesIterator.next();
			
			Mes mes = mesEntry.getValue();
			
			gerarRelatorioMesSelecionado(mes);
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
		log.info("Geracao de relatorio de um mes selecionado.");
		
		Mes mes = pontoEletronico.getMesesApontamento().get(mesReferencia);
		
		gerarRelatorioMesSelecionado(mes);
		
		try {
			workbook.write();
			workbook.close();
		} catch (WriteException e) {
			log.error("Erro ao escrever no arquivo de relatorio.", e);
		} catch (IOException e) {
			log.error("Erro ao escrever no arquivo de relatorio.", e);
		} 
	}

	/**
	 * Metodo que gera planilha excel com relatorio de horas
	 * do mes selecionado.
	 * @param formatoDados
	 * @param formatoDadosTexto
	 * @param mes
	 */
	private void gerarRelatorioMesSelecionado(Mes mes) {
		WritableSheet sheet = geraDadosFixosPlanilha(mes);
		
		Label dataQtdTotalHoras = null;
		if (mes.getQtdHorasTrabalhadas() != null) {
			dataQtdTotalHoras = new Label(9, 40, mes.getQtdHorasTrabalhadas()+"", formatoDados);
		} else {
			dataQtdTotalHoras = new Label(9, 40, "", formatoDados);
		}
		
		Label dataSaldoBancoHoras = null;
		if (mes.getSaldoBancoHoras() != null) {
			dataSaldoBancoHoras = new Label(9, 41, mes.getSaldoBancoHoras()+"", formatoDados);
		} else {
			dataSaldoBancoHoras = new Label(9, 41, "", formatoDados);
		}
		
		try {
			sheet.addCell(dataQtdTotalHoras);	
			sheet.addCell(dataSaldoBancoHoras);	
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
						formatoDias
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
							3, 
							indexLine, 
							PontoEletronicoUtil.formatDate(
									apontamento.getPeriodos().get(0).getSaida(), 
									PontoEletronicoUtil.PATTERN_HH_MM
								),
							formatoDados
						);
				
				dataFimAlmoco = 
					new Label(
							5, 
							indexLine, 
							PontoEletronicoUtil.formatDate(
									apontamento.getPeriodos().get(apontamento.getPeriodos().size() - 1).getEntrada(), 
									PontoEletronicoUtil.PATTERN_HH_MM
								),
							formatoDados
						);
				
				dataSaida = 
					new Label(
							7, 
							indexLine, 
							PontoEletronicoUtil.formatDate(
									apontamento.getPeriodos().get(apontamento.getPeriodos().size() - 1).getSaida(), 
									PontoEletronicoUtil.PATTERN_HH_MM
								),
							formatoDados
						);
			} else {
				dataEntrada = new Label(1, indexLine, "", formatoDados);
				
				dataIniAlmoco = new Label(3, indexLine, "", formatoDados);
				
				dataFimAlmoco = new Label(5, indexLine, "", formatoDados);
				
				dataSaida = new Label(7, indexLine, "", formatoDados);
			}
			
			Label dataQtdHoras = null;
			if (apontamento.getQtdHorasTrabalhadas() != null) {
				dataQtdHoras = new Label(9, indexLine, apontamento.getQtdHorasTrabalhadas()+"", formatoDados);	
			} else if (apontamento.getSaldoBancoHoras() != null) {
				dataQtdHoras = new Label(9, indexLine, apontamento.getSaldoBancoHoras()+"", formatoDados);
			} else {
				dataQtdHoras = new Label(9, indexLine, "", formatoDados);
			}
			
			Label dataObs = null;
			if (apontamento.getObservacoes() != null) {
				dataObs = new Label(11, indexLine, apontamento.getObservacoes(), formatoObs);	
			} else {
				dataObs = new Label(11, indexLine, "", formatoObs);
			}
			
			try {
				sheet.addCell(dataDia);	
				sheet.addCell(dataEntrada);	
				sheet.addCell(dataIniAlmoco);	
				sheet.addCell(dataFimAlmoco);	
				sheet.addCell(dataSaida);	
				sheet.addCell(dataQtdHoras);	
				sheet.addCell(dataObs);
			} catch (RowsExceededException e) {
				log.error("Erro na criacao de linha.", e);
			} catch (WriteException e) {
				log.error("Erro na criacao de linha.", e);
			}
			
			indexLine++;
		}
	}

	/**
	 * Gera os campos com informações fixas e informações de controle.
	 * @param mes
	 * @return WritableSheet
	 */
	private WritableSheet geraDadosFixosPlanilha(Mes mes) {
		WritableSheet sheet = workbook.createSheet(mes.getMesApontamento(), 0);
		sheet.getSettings().setDefaultColumnWidth(5);
		sheet.getSettings().setFitToPages(true);
		sheet.getSettings().setBottomMargin(0.5D);
		sheet.getSettings().setTopMargin(0.5D);
		sheet.getSettings().setLeftMargin(0.5D);
		sheet.getSettings().setRightMargin(0.5D);
		sheet.getSettings().setCopies(1);
		sheet.getSettings().setHorizontalCentre(true);
		sheet.getSettings().setVerticalCentre(true);
		sheet.getSettings().setFitWidth(1);
		sheet.getSettings().setFitHeight(1);
		
		Label labelTitulo = new Label(0, 0, "Planilha de Horas", formatoTitulo);
		
		Label labelEmpresa = new Label(0, 2, "Empresa:", formatoCabecalho);
		Label labelNome = new Label(0, 3, "Nome:", formatoCabecalho);
		Label labelMes = new Label(0, 4, "Mês:", formatoCabecalho);
		
		Label dataEmpresa = new Label(2, 2, pontoEletronicoConfig.getNomeEmpresaUsuario(), formatoCabecalho);
		Label dataNome = new Label(2, 3, pontoEletronicoConfig.getNomeUsuario(), formatoCabecalho);
		Label dataMes = new Label(2, 4, mes.getMesApontamento(), formatoCabecalho);
		
		Label labelDia = new Label(0, 6, "Dia", formatoColunas);
		Label labelEntrada = new Label(1, 6, "Entrada", formatoColunas);
		Label labelIniAlmoco = new Label(3, 6, "Inicio Almoço", formatoColunas);
		Label labelFimAlmoco = new Label(5, 6, "Término Almoço", formatoColunas);
		Label labelSaida = new Label(7, 6, "Saída", formatoColunas);
		Label labelQtdHoras = new Label(9, 6, "Núm. Horas", formatoColunas);
		Label labelObs = new Label(11, 6, "Observações", formatoColunas);
		
		Label labelqtdTotalHoras = new Label(5, 40, "Número Total de Horas:", formatoTotais);
		Label labelSaldoBancoHoras = new Label(5, 41, "Saldo Banco de Horas", formatoTotais);
		
		Label labelDataUsuario = new Label(0, 44, "Data:", formatoAssinaturas);
		Label dataAssNomeUsuario = new Label(6, 44, "", formatoAssinaturasVazias);
		Label dataNomeUsuario = new Label(6, 45, pontoEletronicoConfig.getNomeUsuario(), formatoAssinaturas);
		
		Label labelDataAprovador = new Label(0, 48, "Data:", formatoAssinaturas);
		Label dataAssNomeAprovador = new Label(6, 48, "", formatoAssinaturasVazias);
		Label dataNomeAprovador = new Label(6, 49, pontoEletronicoConfig.getNomeAprovador(), formatoAssinaturas);
		
		try {
			mapeiaCelulasUnidas(sheet);
			
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
			
			sheet.addCell(labelqtdTotalHoras);
			sheet.addCell(labelSaldoBancoHoras);
			
			sheet.addCell(labelDataUsuario);
			sheet.addCell(dataAssNomeUsuario);
			sheet.addCell(dataNomeUsuario);
			
			sheet.addCell(labelDataAprovador);
			sheet.addCell(dataAssNomeAprovador);
			sheet.addCell(dataNomeAprovador);
		} catch (RowsExceededException e) {
			log.error("Erro na criacao de linha.", e);
		} catch (WriteException e) {
			log.error("Erro na criacao de linha.", e);
		}
		return sheet;
	}

	/**
	 * Metodo responsavel por realizar o mapeamento das celulas com merge (unidas)
	 * da planilha de relatorio.
	 * @param sheet
	 * @throws WriteException
	 * @throws RowsExceededException
	 */
	private void mapeiaCelulasUnidas(WritableSheet sheet)
			throws WriteException, RowsExceededException {
		// Mapeamento do cabeçalho
		sheet.mergeCells(0, 0, 16, 0);
		sheet.mergeCells(0, 2, 1, 2);
		sheet.mergeCells(2, 2, 16, 2);
		sheet.mergeCells(0, 3, 1, 3);
		sheet.mergeCells(2, 3, 16, 3);
		sheet.mergeCells(0, 4, 1, 4);
		sheet.mergeCells(2, 4, 16, 4);
		
		// Mapeamento da área de dados
		sheet.mergeCells(1, 6, 2, 6);
		sheet.mergeCells(3, 6, 4, 6);
		sheet.mergeCells(5, 6, 6, 6);
		sheet.mergeCells(7, 6, 8, 6);
		sheet.mergeCells(9, 6, 10, 6);
		sheet.mergeCells(11, 6, 16, 6);
		
		for (int i = 1; i < 32; i++) {
			// Mapeamento da área de dados
			sheet.mergeCells(1, 6+i, 2, 6+i);
			sheet.mergeCells(3, 6+i, 4, 6+i);
			sheet.mergeCells(5, 6+i, 6, 6+i);
			sheet.mergeCells(7, 6+i, 8, 6+i);
			sheet.mergeCells(9, 6+i, 10, 6+i);
			sheet.mergeCells(11, 6+i, 16, 6+i);
		}
		
		// Mapeamento final planilha
		sheet.mergeCells(5, 40, 8, 40);
		sheet.mergeCells(5, 41, 8, 41);
		sheet.mergeCells(9, 40, 10, 40);
		sheet.mergeCells(9, 41, 10, 41);
		sheet.mergeCells(6, 44, 16, 44);
		sheet.mergeCells(6, 45, 16, 45);
		sheet.mergeCells(6, 48, 16, 48);
		sheet.mergeCells(6, 49, 16, 49);
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
