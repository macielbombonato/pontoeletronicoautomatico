package org.mtec.pontoeletronico.domain.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.mtec.pontoeletronico.domain.Apontamento;
import org.mtec.pontoeletronico.domain.Mes;
import org.mtec.pontoeletronico.domain.Periodo;
import org.mtec.pontoeletronico.domain.PontoEletronico;
import org.mtec.pontoeletronico.domain.service.interfaces.PontoEletronicoService;

import com.thoughtworks.xstream.XStream;

/**
 * Servico de criacao de ponto eletronico.
 * @author Maciel Escudero Bombonato
 */
public final class PontoEletronicoServiceImpl implements PontoEletronicoService {
	
	private static Logger log = Logger.getLogger(PontoEletronicoServiceImpl.class.getName());
	
	private final String DIRETORIO = System.getProperty("user.home") + "/pontoeletronico";
	private static String NOME_ARQUIVO;
    
    static {
    	try {
			NOME_ARQUIVO = InetAddress.getLocalHost().getHostName() + "_" + System.getProperty("user.name") + ".xml";
		} catch (UnknownHostException e) {
			log.log(Level.SEVERE, "Erro ao montar o nome do arquivo.", e);
			e.printStackTrace();
		}
    }
	
	@Override
	public void marcarPonto(boolean isDaemon) {
		log.info("Inicio do processo de marcacao de ponto.");
        try {
            boolean continuar = true;
            
        	PontoEletronico pontoEletronico = obterArquivoMarcacao();

            int contador = 0;
            
            while (continuar) {
                contador++;
            	
            	if (pontoEletronico != null) {
            		pontoEletronico.gerarPontoEletronico();
                } else {
                    pontoEletronico = new PontoEletronico();
                    
                    pontoEletronico.setNomeUsuario(System.getProperty("user.name"));
                    
                    pontoEletronico.setNomeComputador(InetAddress.getLocalHost().getHostName());

//                    TODO gerarMesApontamento(pontoEletronicoXml);
                    pontoEletronico.gerarPontoEletronico();
                }

//                TODO contador = calcularHorasTrabalhadas(pontoEletronico, contador);
            	
            	salvarArquivoPonto(pontoEletronico);
                
            	if (isDaemon) {
            		Thread.sleep(60000);
            	} else {
            		continuar = false;
            	}
            }
        } catch (InterruptedException ex) {
            log.log(Level.SEVERE, "Erro de manipulacao de arquivo.", ex);
            ex.printStackTrace();
        } catch (IOException ex) {
        	log.log(Level.SEVERE, "Erro de manipulacao de arquivo.", ex);
            ex.printStackTrace();
        } finally {
        	log.info("Fim do processo de marcacao de ponto.");	
        }
	}
	
    /**
     * Recupera arquivo para marcacao de ponto.
     * @return PontoEletronico
     * @throws IOException
     */
    private PontoEletronico obterArquivoMarcacao() throws IOException {
        log.log(Level.INFO, "Obtendo arquivo de marcacao de ponto.");
        
        PontoEletronico arquivoPontoEletronico = null;
        
        XStream xstream = getPontoEletronicoSchema();
        
        File diretorioArquivo = new File(DIRETORIO);

        if (!diretorioArquivo.exists()) {
            diretorioArquivo.mkdir();
        }

        File arquivoMarcacao = new File(DIRETORIO + "/" + NOME_ARQUIVO);
        
        if (!arquivoMarcacao.exists()
        || arquivoMarcacao.length() < 10L) {
        	File diretorio = new File(DIRETORIO);
        	if (!diretorio.exists()) {
        		diretorio.mkdir();
        	}
        	
            arquivoMarcacao.createNewFile();
            
            arquivoPontoEletronico = new PontoEletronico();
        } else {
        	String xml = "";
        	FileReader reader = new FileReader(arquivoMarcacao);
        	BufferedReader leitor = new BufferedReader( reader );  
			String linha = null;  
			while( ( linha = leitor.readLine() ) != null ) {  
				xml += linha;  
			}  

        	arquivoPontoEletronico = (PontoEletronico) xstream.fromXML(xml);	
        }

        return arquivoPontoEletronico;
    }
    
    /**
     * Salva arquivo atualizado.
     * @param pontoEletronico
     * @throws IOException
     * @throws FileNotFoundException
     */
    private void salvarArquivoPonto(PontoEletronico pontoEletronico) throws IOException, FileNotFoundException {
        log.log(Level.INFO, "Salvando arquivo de marcacao de ponto.");

        File arquivoMarcacao = new File(DIRETORIO + "/" + NOME_ARQUIVO);
        
        arquivoMarcacao.delete();
        
        arquivoMarcacao.createNewFile();
        
        FileOutputStream fos = new FileOutputStream(arquivoMarcacao);
        
        XStream xstream = getPontoEletronicoSchema();
        
        fos.write(xstream.toXML(pontoEletronico).getBytes());
        
        fos.close();
    }
    
	/**
	 * Retorna um objeto XStream com as definições do XML do sistema de ponto eletrônico.
	 * @return XStream
	 */
	public XStream getPontoEletronicoSchema() {
		XStream xstream = new XStream();
		
		xstream.alias("PontoEletronico", PontoEletronico.class);
		xstream.aliasField("meses", PontoEletronico.class, "mesesApontamento");
		
		xstream.alias("mes", Mes.class);
		xstream.aliasAttribute(Mes.class, "mesApontamento", "ID");
		
		xstream.alias("apontamento", Apontamento.class);
		xstream.aliasAttribute(Apontamento.class, "dataApontamento", "data");
		
		xstream.alias("periodo", Periodo.class);

		return xstream;
	}

}
