package org.mtec.pontoeletronico.test.domain.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
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
			
			Periodo[] periodoArray = new Periodo[2];
			periodoArray[0] = new Periodo();
			periodoArray[0].setEntrada(new Date());
			periodoArray[0].setSaida(new Date());
			periodoArray[0].setObservacoes("Observacoes");
			
			periodoArray[1] = new Periodo();
			periodoArray[1].setEntrada(new Date());
			periodoArray[1].setSaida(new Date());
			periodoArray[1].setObservacoes("Observacoes");
			
			apontamento.setPeriodos(periodoArray);
			
			Apontamento apontamento2 = new Apontamento();
			apontamento2.setDataApontamento(new Date());
			apontamento2.setQtdHorasTrabalhadas(new Double(1D));
			apontamento2.setSaldoBancoHoras(new Double(1D));
			
			apontamentosMap.put("apontamento2", apontamento2);
			
			mes.setApontamentos(apontamentosMap);
			
			Periodo[] periodoArray2 = new Periodo[1];
			periodoArray2[0] = new Periodo();
			periodoArray2[0].setEntrada(new Date());
			periodoArray2[0].setSaida(new Date());
			
			apontamento2.setPeriodos(periodoArray2);
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
		pontoEletronicoService.marcarPonto(false);
		
		XStream xstream = pontoEletronicoService.getPontoEletronicoSchema();
		
		String xml = xstream.toXML(pontoEletronico);
		
		assertTrue(pontoEletronico != null);
		assertTrue(pontoEletronico.getQtdHorasTrabalhadas() == 1D);
		assertNotNull(xml);
		assertFalse(xml.equalsIgnoreCase(""));
		
		log.info(xml);
		
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
