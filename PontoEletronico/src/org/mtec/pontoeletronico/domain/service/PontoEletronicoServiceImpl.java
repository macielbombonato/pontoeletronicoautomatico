package org.mtec.pontoeletronico.domain.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;
import org.mtec.pontoeletronico.configuracao.domain.PontoEletronicoConfig;
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
	
	private static final Logger log = Logger.getLogger(PontoEletronicoServiceImpl.class);
	
	private PontoEletronico pontoEletronico;
	
	private PontoEletronicoConfig pontoEletronicoConfig;
	
	private final String DIRETORIO = System.getProperty("user.home") + "/pontoeletronico";
	private static String NOME_ARQUIVO;
	
	private static final String NOME_ARQUIVO_CONFIG = "pontoEletronicoConfig.xml";
    
    static {
    	try {
			NOME_ARQUIVO = InetAddress.getLocalHost().getHostName() + "_" + System.getProperty("user.name") + ".xml";
		} catch (UnknownHostException e) {
			log.error("Erro ao montar o nome do arquivo.", e);
			e.printStackTrace();
		}
    }
	
	@Override
	public void marcarPonto(boolean isDaemon) {
		log.info("Inicio do processo de marcacao de ponto.");
        try {
            boolean continuar = true;
            
        	PontoEletronico pontoEletronico = obterArquivoMarcacao();
        	
        	PontoEletronicoConfig pontoEletronicoConfig = obterArquivoConfiguracao();

            int contador = 0;
            
            while (continuar) {
                contador++;
            	
            	if (pontoEletronico != null) {
            		pontoEletronico.gerarPontoEletronico(pontoEletronicoConfig);
                } else {
                    pontoEletronico = new PontoEletronico();
                    
                    pontoEletronico.setNomeUsuario(pontoEletronicoConfig.getNomeUsuario());
                    
                    pontoEletronico.setNomeComputador(InetAddress.getLocalHost().getHostName());

                    pontoEletronico.gerarPontoEletronico(pontoEletronicoConfig);
                }

//                TODO contador = calcularHorasTrabalhadas(pontoEletronico, contador);
            	
            	salvarArquivoPonto(pontoEletronico);
                
            	if (isDaemon) {
            		Thread.sleep(60000);
            	} else {
            		continuar = false;
            	}
            }
        } catch (InterruptedException e) {
            log.error("Erro de manipulacao de arquivo.", e);
        } catch (IOException e) {
        	log.error("Erro de manipulacao de arquivo.", e);
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
        log.info("Obtendo arquivo de marcacao de ponto.");
        
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
        log.info("Salvando arquivo de marcacao de ponto.");

        File arquivoMarcacao = new File(DIRETORIO + "/" + NOME_ARQUIVO);
        
        arquivoMarcacao.delete();
        
        arquivoMarcacao.createNewFile();
        
        FileOutputStream fos = new FileOutputStream(arquivoMarcacao);
        
        XStream xstream = getPontoEletronicoSchema();
        
        fos.write(xstream.toXML(pontoEletronico).getBytes());
        
        fos.close();
    }
    
    /**
     * Recupera arquivo de configuracao do sistema de ponto eletronico.
     * @return PontoEletronicoConfig
     * @throws IOException
     */
    private PontoEletronicoConfig obterArquivoConfiguracao() throws IOException {
        log.info("Obtendo arquivo de configuracao do sistema de ponto eletronico.");
        
        PontoEletronicoConfig arquivoPontoEletronicoConfig = null;
        
        XStream xstream = getPontoEletronicoConfigSchema();
        
        File diretorioArquivo = new File(DIRETORIO);

        if (!diretorioArquivo.exists()) {
            diretorioArquivo.mkdir();
        }

        File arquivoConfiguracao = new File(DIRETORIO + "/" + NOME_ARQUIVO_CONFIG);
        
        if (!arquivoConfiguracao.exists()
        || arquivoConfiguracao.length() < 10L) {
        	File diretorio = new File(DIRETORIO);
        	if (!diretorio.exists()) {
        		diretorio.mkdir();
        	}
        	
        	arquivoConfiguracao.createNewFile();
            
            arquivoPontoEletronicoConfig = new PontoEletronicoConfig();
        } else {
        	String xml = "";
        	FileReader reader = new FileReader(arquivoConfiguracao);
        	BufferedReader leitor = new BufferedReader( reader );  
			String linha = null;  
			while( ( linha = leitor.readLine() ) != null ) {  
				xml += linha;  
			}  

        	arquivoPontoEletronicoConfig = (PontoEletronicoConfig) xstream.fromXML(xml);	
        }

        return arquivoPontoEletronicoConfig;
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
	
	/**
	 * Retorna um objeto XStream com as definições do XML da configuracao do sistema de ponto eletrônico.
	 * @return XStream
	 */
	public XStream getPontoEletronicoConfigSchema() {
		XStream xstream = new XStream();
		
		xstream.alias("PontoEletronicoConfig", PontoEletronicoConfig.class);
		xstream.aliasField("meses", PontoEletronico.class, "mesesApontamento");
		
		return xstream;
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
}
