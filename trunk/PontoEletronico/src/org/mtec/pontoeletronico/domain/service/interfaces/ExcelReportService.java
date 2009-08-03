package org.mtec.pontoeletronico.domain.service.interfaces;

import jxl.write.WritableWorkbook;

import org.mtec.pontoeletronico.configuracao.domain.PontoEletronicoConfig;
import org.mtec.pontoeletronico.domain.PontoEletronico;

/**
 * Interface do serviço de geração de relatório de apontamento 
 * de horas em excel.
 * @author Maciel Escudero Bombonato - maciel.bombonato@gmail.com
 */
public interface ExcelReportService {
	
	/**
	 * Gera relatorio de apontamento do usuario contendo todos os meses
	 * que foram apontados pelo sistema.
	 * @param mesReferencia
	 */
	public void gerarRelatorio();
	
	/**
	 * Gera relatorio de apontamento do usuario contendo o mes informado
	 * pelo usuario.
	 * @param mesReferencia
	 */
	public void gerarRelatorio(String mesReferencia);
	
	/**
	 * @param pontoEletronico the pontoEletronico to set
	 */
	public void setPontoEletronico(PontoEletronico pontoEletronico);

	/**
	 * @param pontoEletronicoConfig the pontoEletronicoConfig to set
	 */
	public void setPontoEletronicoConfig(PontoEletronicoConfig pontoEletronicoConfig);
	
	/**
	 * @param workbook the workbook to set
	 */
	public void setWorkbook(WritableWorkbook workbook);
	
	/**
	 * @return the workbook
	 */
	public WritableWorkbook getWorkbook();
}
