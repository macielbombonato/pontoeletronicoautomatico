package org.mtec.pontoeletronico.util;

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

import com.thoughtworks.xstream.XStream;

public final class PontoEletronicoFileUtil {
	
	private static final Logger log = Logger.getLogger(PontoEletronicoFileUtil.class);
	
	private static final String DIRETORIO = System.getProperty("user.home") + "/pontoeletronico";
	private static String NOME_ARQUIVO;
	
	private static final String NOME_ARQUIVO_CONFIG = "pontoEletronicoConfig.xml";
    
    static {
    	try {
			NOME_ARQUIVO = InetAddress.getLocalHost().getHostName() + "_" + System.getProperty("user.name") + ".xml";
		} catch (UnknownHostException e) {
			log.error("Erro ao montar o nome do arquivo.", e);
		}
    }
	
    /**
     * Recupera arquivo para marcacao de ponto.
     * @return PontoEletronico
     * @throws IOException
     */
    public static PontoEletronico obterArquivoMarcacao() throws IOException {
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
    public static void salvarArquivoPonto(PontoEletronico pontoEletronico) throws IOException, FileNotFoundException {
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
    public static PontoEletronicoConfig obterArquivoConfiguracao() throws IOException {
        log.info("Obtendo arquivo de configuracao do sistema de ponto eletronico.");
        
        PontoEletronicoConfig pontoEletronicoConfig = null;
        
        XStream xstream = getPontoEletronicoConfigSchema();
        
        File diretorioArquivo = new File(DIRETORIO);

        if (!diretorioArquivo.exists()) {
            diretorioArquivo.mkdir();
        }

        File arquivoConfiguracao = new File(DIRETORIO + "/" + NOME_ARQUIVO_CONFIG);
        
        if (!arquivoConfiguracao.exists()
        || arquivoConfiguracao.length() < 20L) {
        	File diretorio = new File(DIRETORIO);
        	if (!diretorio.exists()) {
        		diretorio.mkdir();
        	}
        	
        	arquivoConfiguracao.createNewFile();
            
        	pontoEletronicoConfig = new PontoEletronicoConfig();
            
            pontoEletronicoConfig.gerarPontoEletronicoConfig();
            
            salvarArquivoConfiguracao(pontoEletronicoConfig);
        } else {
        	String xml = "";
        	FileReader reader = new FileReader(arquivoConfiguracao);
        	BufferedReader leitor = new BufferedReader( reader );  
			String linha = null;  
			while( ( linha = leitor.readLine() ) != null ) {  
				xml += linha;  
			}  

			pontoEletronicoConfig = (PontoEletronicoConfig) xstream.fromXML(xml);	
        }

        return pontoEletronicoConfig;
    }
    
    /**
     * Salva arquivo de configuracao atualizado.
     * @param pontoEletronicoConfig
     * @throws IOException
     * @throws FileNotFoundException
     */
    public static void salvarArquivoConfiguracao(PontoEletronicoConfig pontoEletronicoConfig) throws IOException, FileNotFoundException {
        log.info("Salvando arquivo de configuracao.");

        File arquivoConfiguracao = new File(DIRETORIO + "/" + NOME_ARQUIVO_CONFIG);
        
        arquivoConfiguracao.delete();
        
        arquivoConfiguracao.createNewFile();
        
        FileOutputStream fos = new FileOutputStream(arquivoConfiguracao);
        
        XStream xstream = getPontoEletronicoConfigSchema();
        
        fos.write(xstream.toXML(pontoEletronicoConfig).getBytes());
        
        fos.close();
    }
    
	/**
	 * Retorna um objeto XStream com as definições do XML do sistema de ponto eletrônico.
	 * @return XStream
	 */
	public static XStream getPontoEletronicoSchema() {
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
	public static XStream getPontoEletronicoConfigSchema() {
		XStream xstream = new XStream();
		
		xstream.alias("PontoEletronicoConfig", PontoEletronicoConfig.class);
		
		return xstream;
	}

}
