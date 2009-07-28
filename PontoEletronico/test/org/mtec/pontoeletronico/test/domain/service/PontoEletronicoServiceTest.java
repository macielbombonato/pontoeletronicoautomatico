package org.mtec.pontoeletronico.test.domain.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mtec.pontoeletronico.configuracao.domain.FeriadosPontes;
import org.mtec.pontoeletronico.configuracao.domain.PontoEletronicoConfig;
import org.mtec.pontoeletronico.domain.Apontamento;
import org.mtec.pontoeletronico.domain.Mes;
import org.mtec.pontoeletronico.domain.Periodo;
import org.mtec.pontoeletronico.domain.PontoEletronico;
import org.mtec.pontoeletronico.domain.service.PontoEletronicoServiceImpl;
import org.mtec.pontoeletronico.domain.service.interfaces.PontoEletronicoService;

import com.thoughtworks.xstream.XStream;

/**
 * @author Maciel Escudero Bombonato - maciel.bombonato@gmail.com
 *
 */
public class PontoEletronicoServiceTest {
	
	private static final Logger log = Logger.getLogger(PontoEletronicoServiceTest.class);

	private PontoEletronico pontoEletronico;
	private PontoEletronicoConfig pontoEletronicoConfig;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		pontoEletronico = new PontoEletronico();
		
		try {
			pontoEletronico.setNomeComputador(java.net.InetAddress.getLocalHost().getHostName());
			pontoEletronico.setNomeUsuario(System.getProperty("user.name"));
			pontoEletronico.setQtdHorasTrabalhadas(new Double(1D));
			pontoEletronico.setSaldoBancoHoras(new Double(1D));
			
			HashMap<String, Mes> mesesMap = new HashMap<String, Mes>();
			Mes mes = new Mes();
			mes.setMesApontamento("2009-01");
			mes.setQtdHorasTrabalhadas(new Double(1D));
			mes.setSaldoBancoHoras(new Double(1D));
			
			mesesMap.put(mes.getMesApontamento(), mes);
			
			pontoEletronico.setMesesApontamento(mesesMap);

			HashMap<String, Apontamento> apontamentosMap = new HashMap<String, Apontamento>();
			
			Apontamento apontamento = new Apontamento();
			apontamento.setDataApontamento(new Date());
			apontamento.setQtdHorasTrabalhadas(new Double(1D));
			apontamento.setSaldoBancoHoras(new Double(1D));
			
			apontamentosMap.put(apontamento.getDataApontamento().toString(), apontamento);
			
			mes.setApontamentos(apontamentosMap);
			
			apontamento.setPeriodos(new ArrayList<Periodo>());
			
			Periodo periodo1 = new Periodo();
			periodo1.setEntrada(new Date());
			periodo1.setSaida(new Date());
			periodo1.setObservacoes("Observacoes");
			apontamento.getPeriodos().add(periodo1);
			
			Periodo periodo2 = new Periodo();
			periodo2.setEntrada(new Date());
			periodo2.setSaida(new Date());
			periodo2.setObservacoes("Observacoes");
			apontamento.getPeriodos().add(periodo2);
			
			Apontamento apontamento2 = new Apontamento();
			apontamento2.setDataApontamento(new Date());
			apontamento2.setQtdHorasTrabalhadas(new Double(1D));
			apontamento2.setSaldoBancoHoras(new Double(1D));
			
			apontamentosMap.put("apontamento2", apontamento2);
			
			mes.setApontamentos(apontamentosMap);
			
			apontamento2.setPeriodos(new ArrayList<Periodo>());
			
			Periodo periodo3 = new Periodo();
			periodo3 = new Periodo();
			periodo3.setEntrada(new Date());
			periodo3.setSaida(new Date());
			
			apontamento2.getPeriodos().add(periodo3);
			
			pontoEletronicoConfig = new PontoEletronicoConfig();
			pontoEletronicoConfig.setNomeUsuario("Maciel Escudero Bombonato");
			pontoEletronicoConfig.setNomeAprovador("Meiry Ane Agnese");
			pontoEletronicoConfig.setQtdHorasTrabalhoDiario(9.5D);
			
			HashMap<String, FeriadosPontes> feriadosMap = new HashMap<String, FeriadosPontes>();
			FeriadosPontes feriadoFixo = new FeriadosPontes();
			feriadoFixo.setDataFeriado("15/07");
			feriadoFixo.setNomeFeriado("TESTE");
			feriadosMap.put(feriadoFixo.getDataFeriado(), feriadoFixo);
			
			HashMap<String, FeriadosPontes> feriadosVariaveisMap = new HashMap<String, FeriadosPontes>();
			FeriadosPontes feriadoVariavel = new FeriadosPontes();
			feriadoVariavel.setDataFeriado("18/07/2009");
			feriadoVariavel.setNomeFeriado("TESTE");
			feriadosVariaveisMap.put(feriadoVariavel.getDataFeriado(), feriadoVariavel);
			
			pontoEletronicoConfig.setFeriadosFixos(feriadosMap);
			
			pontoEletronicoConfig.setFeriadosPontesVariaveis(feriadosVariaveisMap);
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link org.mtec.pontoeletronico.domain.service.PontoEletronicoServiceImpl#marcarPonto()}.
	 */
	@Test
	public void testMarcarPonto() {
		PontoEletronicoService pontoEletronicoService = new PontoEletronicoServiceImpl();
		pontoEletronicoService.setPontoEletronico(pontoEletronico);
		pontoEletronicoService.setPontoEletronicoConfig(pontoEletronicoConfig);
		pontoEletronicoService.marcarPonto(false);
		
		XStream xstream = pontoEletronicoService.getPontoEletronicoSchema();
		
		String xml = xstream.toXML(pontoEletronico);
		
		XStream xstreamConfig = pontoEletronicoService.getPontoEletronicoConfigSchema();
		
		String xmlConfig = xstreamConfig.toXML(pontoEletronicoConfig);
		
		assertTrue(pontoEletronico != null);
		assertTrue(pontoEletronico.getQtdHorasTrabalhadas() == 1D);
		assertNotNull(xml);
		assertFalse(xml.equalsIgnoreCase(""));
		
		assertNotNull(xmlConfig);
		assertFalse(xmlConfig.equalsIgnoreCase(""));
		
		log.info(xml);
		
		log.info(xmlConfig);
		
		PontoEletronico ponto = (PontoEletronico)xstream.fromXML(xml);
		
		assertNotNull(ponto);
		
		log.info(ponto);
	}
	
	/**
	 * Test method for {@link org.mtec.pontoeletronico.domain.service.PontoEletronicoServiceImpl#marcarPonto()}.
	 */
	@Test
	public void testMarcarPontoReal() {
        PontoEletronicoService service = new PontoEletronicoServiceImpl();
        service.marcarPonto(false);
	}

}
